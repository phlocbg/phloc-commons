/**
 * Copyright (C) 2006-2011 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.commons.microdom.serialize;

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.xml.serialize.IXMLSerializer;

/**
 * Utility class for serializing micro document objects.
 * 
 * @author philip
 */
@Immutable
public final class MicroWriter
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MicroWriter.class);
  private static final IStatisticsHandlerSize s_aSizeHdl = StatisticsManager.getSizeHandler (MicroWriter.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MicroWriter s_aInstance = new MicroWriter ();

  private MicroWriter ()
  {}

  /**
   * Write a Micro Node to an output stream.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. The
   *        output stream is closed anyway directly after the operation finishes
   *        (on success and on error).
   * @param aSettings
   *        The settings to be used for the creation. May not be
   *        <code>null</code>.
   */
  public static void saveToStream (@Nonnull final IMicroNode aNode,
                                   @Nonnull @WillClose final OutputStream aOS,
                                   @Nonnull final IMicroWriterSettings aSettings)
  {
    if (aOS == null)
      throw new NullPointerException ("outputStream");
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    try
    {
      final IXMLSerializer <IMicroNode> aSerializer = new MicroSerializer (aSettings.getXMLVersion (),
                                                                           aSettings.getCharset ());
      aSerializer.setFormat (aSettings.getFormat ());
      aSerializer.setSerializeDocType (aSettings.getSerializeDocType ());
      aSerializer.setSerializeComments (aSettings.getSerializeComments ());
      aSerializer.setIndent (aSettings.getIndent ());
      aSerializer.write (aNode, aOS);
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  @Nullable
  public static String getNodeAsString (@Nonnull final IMicroNode aNode, @Nonnull final IMicroWriterSettings aSettings)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    try
    {
      // start serializing
      // For the pDAF3 Starkl view application this should be approx. 20KB
      // Largest in ConfigTK so far was 640KB
      final NonBlockingByteArrayOutputStream aOS = new NonBlockingByteArrayOutputStream (50 * CGlobal.BYTES_PER_KILOBYTE);
      saveToStream (aNode, aOS, aSettings);
      s_aSizeHdl.addSize (aOS.size ());
      return aOS.getAsString (aSettings.getCharset ());
    }
    catch (final RuntimeException ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Error serializing with settings " + aSettings, ex);
    }
    return null;
  }

  /**
   * Convert the passed micro node to an XML string using
   * {@link MicroWriterSettings#DEFAULT_XML_SETTINGS}. This is a specialized
   * version of {@link #getNodeAsString(IMicroNode, IMicroWriterSettings)}.
   * 
   * @param aNode
   *        The node to be converted to a string. May not be <code>null</code> .
   * @return The string representation of the passed node.
   */
  @Nullable
  public static String getXMLString (@Nonnull final IMicroNode aNode)
  {
    return getNodeAsString (aNode, MicroWriterSettings.DEFAULT_XML_SETTINGS);
  }
}
