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
package com.phloc.commons.xml.serialize;

import java.io.OutputStream;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;
import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.xml.EXMLVersion;

/**
 * This is a helper class to serialize DOM nodes to a String.
 * 
 * @author philip
 */
@Immutable
public final class XMLWriter
{
  public static final EXMLVersion DEFAULT_XML_VERSION = EXMLVersion.DEFAULT;
  public static final NamespaceContext DEFAULT_NAMESPACE_CTX = null;

  private static final Logger s_aLogger = LoggerFactory.getLogger (XMLWriter.class);
  private static final IStatisticsHandlerSize s_aSizeHdl = StatisticsManager.getSizeHandler (XMLWriter.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLWriter s_aInstance = new XMLWriter ();

  private XMLWriter ()
  {}

  /**
   * Write a node to an {@link OutputStream} using the default settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aOS
   *        The {@link OutputStream} to write to. May not be <code>null</code>.
   *        The {@link OutputStream} is closed anyway directly after the
   *        operation finishes (on success and on error).
   */
  @Nonnull
  public static ESuccess writeToStream (@Nonnull final Node aNode, @Nonnull @WillClose final OutputStream aOS)
  {
    return writeToStream (aNode, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  /**
   * Write a node to an {@link OutputStream} using custom settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aOS
   *        The {@link OutputStream} to write to. May not be <code>null</code>.
   *        The {@link OutputStream} is closed anyway directly after the
   *        operation finishes (on success and on error).
   * @param aSettings
   *        The serialization settings to be used. May not be <code>null</code>.
   */
  @Nonnull
  public static ESuccess writeToStream (@Nonnull final Node aNode,
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
      final IXMLSerializer <Node> aSerializer = new XMLSerializerPhloc (aSettings);
      aSerializer.write (aNode, aOS);
      return ESuccess.SUCCESS;
    }
    catch (final RuntimeException ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
      throw ex;
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
    }
    finally
    {
      StreamUtils.close (aOS);
    }
    return ESuccess.FAILURE;
  }

  /**
   * Write a node to a {@link Writer} using the default settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aWriter
   *        The {@link Writer} to write to. May not be <code>null</code>. The
   *        {@link Writer} is closed anyway directly after the operation
   *        finishes (on success and on error).
   */
  @Nonnull
  public static ESuccess writeToWriter (@Nonnull final Node aNode, @Nonnull @WillClose final Writer aWriter)
  {
    return writeToWriter (aNode, aWriter, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }

  /**
   * Write a node to a {@link Writer} using the default settings.
   * 
   * @param aNode
   *        The node to be serialized. May be any kind of node (incl.
   *        documents). May not be <code>null</code>.
   * @param aWriter
   *        The {@link Writer} to write to. May not be <code>null</code>. The
   *        {@link Writer} is closed anyway directly after the operation
   *        finishes (on success and on error).
   * @param aSettings
   *        The serialization settings to be used. May not be <code>null</code>.
   */
  @Nonnull
  public static ESuccess writeToWriter (@Nonnull final Node aNode,
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
      final IXMLSerializer <Node> aSerializer = new XMLSerializerPhloc (aSettings);
      aSerializer.write (aNode, aWriter);
      return ESuccess.SUCCESS;
    }
    catch (final RuntimeException ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
      throw ex;
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Error in XML serialization", ex);
    }
    finally
    {
      StreamUtils.close (aWriter);
    }
    return ESuccess.FAILURE;
  }

  @Nullable
  public static String getNodeAsString (@Nonnull final Node aNode, @Nonnull final IXMLWriterSettings aSettings)
  {
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
      s_aLogger.error ("Error serializing DOM node with settings " + aSettings.toString (), t);
    }
    finally
    {
      // don't forget to close the stream!
      StreamUtils.close (aWriter);
    }
    return null;
  }

  /**
   * Convert the passed micro node to an XML string using
   * {@link XMLWriterSettings#DEFAULT_XML_SETTINGS}. This is a specialized
   * version of {@link #getNodeAsString(Node, IXMLWriterSettings)}.
   * 
   * @param aNode
   *        The node to be converted to a string. May not be <code>null</code> .
   * @return The string representation of the passed node.
   */
  @Nullable
  public static String getXMLString (@Nonnull final Node aNode)
  {
    return getNodeAsString (aNode, XMLWriterSettings.DEFAULT_XML_SETTINGS);
  }
}
