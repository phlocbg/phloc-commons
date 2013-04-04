/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.xml.serialize.IXMLSerializer;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

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
   * Write a Micro Node to a file using the default settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aFile
   *        The file to write to. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeToFile (@Nonnull final IMicroNode aNode, @Nonnull final File aFile)
  {
    return writeToFile (aNode, aFile, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  /**
   * Write a Micro Node to a file.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aFile
   *        The file to write to. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for the creation. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeToFile (@Nonnull final IMicroNode aNode,
                                      @Nonnull final File aFile,
                                      @Nonnull final IXMLWriterSettings aSettings)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    final OutputStream aOS = FileUtils.getOutputStream (aFile);
    if (aOS == null)
      return ESuccess.FAILURE;

    // No need to wrap the OS in a BufferedOutputStream as inside, it is later
    // on wrapped in a BufferedWriter
    return writeToStream (aNode, aOS, aSettings);
  }

  /**
   * Write a Micro Node to an output stream using the default settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. The
   *        output stream is closed anyway directly after the operation finishes
   *        (on success and on error).
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeToStream (@Nonnull final IMicroNode aNode, @Nonnull @WillClose final OutputStream aOS)
  {
    return writeToStream (aNode, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  /**
   * Write a Micro Node to an {@link OutputStream}.
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
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeToStream (@Nonnull final IMicroNode aNode,
                                        @Nonnull @WillClose final OutputStream aOS,
                                        @Nonnull final IXMLWriterSettings aSettings)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aOS == null)
      throw new NullPointerException ("outputStream");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    try
    {
      final IXMLSerializer <IMicroNode> aSerializer = new MicroSerializer (aSettings);
      aSerializer.write (aNode, aOS);
      return ESuccess.SUCCESS;
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  /**
   * Write a Micro Node to a {@link Writer}.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aWriter
   *        The writer to write to. May not be <code>null</code>. The writer is
   *        closed anyway directly after the operation finishes (on success and
   *        on error).
   * @param aSettings
   *        The settings to be used for the creation. May not be
   *        <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeToWriter (@Nonnull final IMicroNode aNode,
                                        @Nonnull @WillClose final Writer aWriter,
                                        @Nonnull final IXMLWriterSettings aSettings)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aWriter == null)
      throw new NullPointerException ("writer");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    try
    {
      final IXMLSerializer <IMicroNode> aSerializer = new MicroSerializer (aSettings);
      aSerializer.write (aNode, aWriter);
      return ESuccess.SUCCESS;
    }
    finally
    {
      StreamUtils.close (aWriter);
    }
  }

  @Nullable
  public static String getNodeAsString (@Nonnull final IMicroNode aNode, @Nonnull final IXMLWriterSettings aSettings)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    NonBlockingStringWriter aWriter = null;
    try
    {
      // start serializing
      aWriter = new NonBlockingStringWriter (50 * CGlobal.BYTES_PER_KILOBYTE);
      if (writeToWriter (aNode, aWriter, aSettings).isSuccess ())
      {
        s_aSizeHdl.addSize (aWriter.size ());
        return aWriter.getAsString ();
      }
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Error serializing MicroDOM with settings " + aSettings.toString (), t);
    }
    finally
    {
      StreamUtils.close (aWriter);
    }
    return null;
  }

  /**
   * Convert the passed micro node to an XML string using
   * {@link XMLWriterSettings#DEFAULT_XML_SETTINGS}. This is a specialized
   * version of {@link #getNodeAsString(IMicroNode, IXMLWriterSettings)}.
   * 
   * @param aNode
   *        The node to be converted to a string. May not be <code>null</code> .
   * @return The string representation of the passed node.
   */
  @Nullable
  public static String getXMLString (@Nonnull final IMicroNode aNode)
  {
    return getNodeAsString (aNode, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }
}
