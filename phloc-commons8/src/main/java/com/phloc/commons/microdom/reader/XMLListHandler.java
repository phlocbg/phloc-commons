/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.microdom.reader;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IOutputStreamProvider;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Simple class that reads a list from an XML input stream.<br>
 * The XML file needs to look as follows:
 * 
 * <pre>
 * &lt;list>
 *   &lt;item value="..."/>
 *   &lt;item value="..."/>
 *   ...
 * &lt;/list>
 * </pre>
 * 
 * @author Philip Helger
 */
@Immutable
public final class XMLListHandler
{
  /** Name of the root element */
  public static final String ELEMENT_LIST = "list";
  /** Element name of a single item */
  public static final String ELEMENT_ITEM = "item";
  /** Attribute name for the item value */
  public static final String ATTR_VALUE = "value";

  private static final Logger s_aLogger = LoggerFactory.getLogger (XMLListHandler.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLListHandler s_aInstance = new XMLListHandler ();

  private XMLListHandler ()
  {}

  @Nullable
  @ReturnsMutableCopy
  public static List <String> readList (@Nonnull final IInputStreamProvider aISP)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    return readList (aISP.getInputStream ());
  }

  @Nonnull
  public static ESuccess readList (@Nonnull final IInputStreamProvider aISP,
                                   @Nonnull final Collection <String> aTargetList)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    return readList (aISP.getInputStream (), aTargetList);
  }

  /**
   * Read a predefined XML file that contains list items.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   *        Automatically closed no matter whether reading succeeded or not.
   * @return <code>null</code> if reading fails - all list items otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  public static List <String> readList (@Nonnull @WillClose final InputStream aIS)
  {
    final List <String> ret = new ArrayList <String> ();
    if (readList (aIS, ret).isFailure ())
      return null;
    return ret;
  }

  /**
   * Read a predefined XML file that contains list items.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   *        Automatically closed no matter whether reading succeeded or not.
   * @param aTargetList
   *        The target collection to be filled. May not be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if reading succeeded,
   *         {@link ESuccess#FAILURE} if the input stream is no valid XML or any
   *         other error occurred.
   */
  @Nonnull
  public static ESuccess readList (@Nonnull @WillClose final InputStream aIS,
                                   @Nonnull final Collection <String> aTargetList)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aTargetList, "TargetList");

    try
    {
      // open file
      final IMicroDocument aDoc = MicroReader.readMicroXML (aIS);
      if (aDoc != null)
      {
        readList (aDoc.getDocumentElement (), aTargetList);
        return ESuccess.SUCCESS;
      }
    }
    catch (final Throwable t)
    {
      s_aLogger.warn ("Failed to read list resource '" + aIS + "'", t);
    }
    finally
    {
      StreamUtils.close (aIS);
    }

    return ESuccess.FAILURE;
  }

  @Nonnull
  public static ESuccess readList (@Nonnull final IMicroElement aParentElement,
                                   @Nonnull final Collection <String> aTargetList)
  {
    ValueEnforcer.notNull (aParentElement, "ParentElement");
    ValueEnforcer.notNull (aTargetList, "TargetList");

    try
    {
      // and insert all elements
      for (final IMicroElement eItem : aParentElement.getAllChildElements (ELEMENT_ITEM))
      {
        final String sValue = eItem.getAttribute (ATTR_VALUE);
        if (sValue == null)
          s_aLogger.warn ("Ignoring list item because value is null");
        else
          if (!aTargetList.add (sValue))
            s_aLogger.warn ("Ignoring list item '" + sValue + "' because value is already contained");
      }
      return ESuccess.SUCCESS;
    }
    catch (final Throwable t)
    {
      s_aLogger.warn ("Failed to read list document", t);
    }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public static IMicroDocument createListDocument (@Nonnull final Collection <String> aCollection)
  {
    ValueEnforcer.notNull (aCollection, "Collection");

    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_LIST);
    for (final String sItem : aCollection)
    {
      final IMicroElement eItem = eRoot.appendElement (ELEMENT_ITEM);
      eItem.setAttribute (ATTR_VALUE, sItem);
    }
    return aDoc;
  }

  @Nonnull
  public static ESuccess writeList (@Nonnull final Collection <String> aCollection,
                                    @Nonnull final IOutputStreamProvider aOSP)
  {
    ValueEnforcer.notNull (aOSP, "OutputStreamProvider");

    return writeList (aCollection, aOSP.getOutputStream (EAppend.DEFAULT));
  }

  /**
   * Write the passed collection to the passed output stream using the
   * predefined XML layout.
   * 
   * @param aCollection
   *        The map to be written. May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. The stream is closed independent of
   *        success or failure. May not be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} when everything went well,
   *         {@link ESuccess#FAILURE} otherwise.
   */
  @Nonnull
  public static ESuccess writeList (@Nonnull final Collection <String> aCollection,
                                    @Nonnull @WillClose final OutputStream aOS)
  {
    ValueEnforcer.notNull (aCollection, "Collection");
    ValueEnforcer.notNull (aOS, "OutputStream");

    try
    {
      final IMicroDocument aDoc = createListDocument (aCollection);
      return MicroWriter.writeToStream (aDoc, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS);
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }
}
