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
package com.phloc.commons.changelog;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.resource.URLResource;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.commons.version.Version;
import com.phloc.commons.xml.CXML;

/**
 * This class handles the reading and writing of changelog objects.
 * 
 * @author philip
 */
@Immutable
public final class ChangeLogSerializer
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ChangeLogSerializer.class);
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String ELEMENT_CHANGELOG = "changelog";
  private static final String ATTR_VERSION = "version";
  private static final String ATTR_COMPONENT = "component";
  private static final String ELEMENT_ENTRY = "entry";
  private static final String ATTR_DATE = "date";
  private static final String ATTR_ACTION = "action";
  private static final String ATTR_CATEGORY = "category";
  private static final String ATTR_INCOMPATIBLE = "incompatible";
  private static final String ELEMENT_CHANGE = "change";
  private static final String ELEMENT_ISSUE = "issue";
  private static final String ELEMENT_RELEASE = "release";

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ChangeLogSerializer s_aInstance = new ChangeLogSerializer ();

  private static final INonThrowingRunnableWithParameter <String> s_aLoggingCallback = new INonThrowingRunnableWithParameter <String> ()
  {
    public void run (final String sError)
    {
      s_aLogger.error (sError);
    }
  };

  private ChangeLogSerializer ()
  {}

  /**
   * Read the change log resource specified by the input stream provider using
   * the default logging error handler.
   * 
   * @param aISP
   *        The ISP to read from. Maybe <code>null</code> resulting in a
   *        <code>null</code> return.
   * @return <code>null</code> if the passed ISP was <code>null</code>.
   */
  @Nullable
  public static ChangeLog readChangeLog (@Nullable final IInputStreamProvider aISP)
  {
    return readChangeLog (aISP, s_aLoggingCallback);
  }

  /**
   * Read the change log resource specified by the input stream provider.
   * 
   * @param aISP
   *        The ISP to read from. Maybe <code>null</code> resulting in a
   *        <code>null</code> return.
   * @param aErrorCallback
   *        The callback that handles potential errors.
   * @return <code>null</code> if the passed ISP was <code>null</code>.
   */
  @Nullable
  public static ChangeLog readChangeLog (@Nullable final IInputStreamProvider aISP,
                                         @Nonnull final INonThrowingRunnableWithParameter <String> aErrorCallback)
  {
    if (aErrorCallback == null)
      throw new NullPointerException ("errorCallback");

    final IMicroDocument aDoc = MicroReader.readMicroXML (aISP);
    if (aDoc == null)
      return null;

    final DateFormat aDF = new SimpleDateFormat (DATE_FORMAT);
    final IMicroElement eRoot = aDoc.getDocumentElement ();
    final ChangeLog ret = new ChangeLog (new Version (eRoot.getAttribute (ATTR_VERSION)),
                                         eRoot.getAttribute (ATTR_COMPONENT));

    // Add all entries
    for (final IMicroElement eEntry : eRoot.getChildElements (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_ENTRY))
    {
      final String sDate = eEntry.getAttribute (ATTR_DATE);
      final String sAction = eEntry.getAttribute (ATTR_ACTION);
      final String sCategory = eEntry.getAttribute (ATTR_CATEGORY);
      final String sIncompatible = eEntry.getAttribute (ATTR_INCOMPATIBLE);

      Date aDate = null;
      try
      {
        aDate = aDF.parse (sDate);
      }
      catch (final ParseException ex)
      {
        aErrorCallback.run ("Failed to parse entry date '" + sDate + "'");
        continue;
      }
      final EChangeLogAction eAction = EChangeLogAction.getFromIDOrNull (sAction);
      if (eAction == null)
      {
        aErrorCallback.run ("Failed to parse change log action '" + sAction + "'");
        continue;
      }
      final EChangeLogCategory eCategory = EChangeLogCategory.getFromIDOrNull (sCategory);
      if (eCategory == null)
      {
        aErrorCallback.run ("Failed to parse change log category '" + sCategory + "'");
        continue;
      }
      final boolean bIsIncompatible = StringHelper.hasText (sIncompatible) ? StringHelper.parseBool (sIncompatible)
                                                                          : false;

      final ChangeLogEntry aEntry = new ChangeLogEntry (ret, aDate, eAction, eCategory, bIsIncompatible);
      ret.addEntry (aEntry);

      final IMicroElement eChange = eEntry.getFirstChildElement (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_CHANGE);
      if (eChange == null)
      {
        aErrorCallback.run ("No change element present!");
        continue;
      }
      final MultiLingualText aMLT = MicroTypeConverter.convertToNative (eChange, MultiLingualText.class);
      if (aMLT == null)
      {
        aErrorCallback.run ("Failed to read multi lingual text in change element!");
        continue;
      }
      aEntry.setText (aMLT);
      for (final IMicroElement eIssue : eEntry.getChildElements (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_ISSUE))
        aEntry.addIssue (eIssue.getTextContent ());
    }

    // Add all releases
    for (final IMicroElement eRelease : eRoot.getChildElements (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_RELEASE))
    {
      final String sDate = eRelease.getAttribute (ATTR_DATE);
      final String sVersion = eRelease.getAttribute (ATTR_VERSION);
      Date aDate = null;
      try
      {
        aDate = aDF.parse (sDate);
      }
      catch (final ParseException ex)
      {
        s_aLogger.warn ("Failed to parse release date '" + sDate + "'");
        continue;
      }
      ret.addRelease (new ChangeLogRelease (aDate, new Version (sVersion)));
    }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <URI, ChangeLog> readAllChangeLogs ()
  {
    return readAllChangeLogs (s_aLoggingCallback);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <URI, ChangeLog> readAllChangeLogs (@Nonnull final ClassLoader aClassLoader)
  {
    return readAllChangeLogs (s_aLoggingCallback, aClassLoader);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <URI, ChangeLog> readAllChangeLogs (@Nonnull final INonThrowingRunnableWithParameter <String> aErrorCallback)
  {
    return readAllChangeLogs (aErrorCallback, ClassHelper.getDefaultClassLoader ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <URI, ChangeLog> readAllChangeLogs (@Nonnull final INonThrowingRunnableWithParameter <String> aErrorCallback,
                                                        @Nonnull final ClassLoader aClassLoader)
  {
    if (aErrorCallback == null)
      throw new NullPointerException ("errorCallback");
    if (aClassLoader == null)
      throw new NullPointerException ("classLoader");

    try
    {
      final Map <URI, ChangeLog> ret = new HashMap <URI, ChangeLog> ();
      // Find all change log XML files in the classpath
      for (final URL aURL : ContainerHelper.newList (aClassLoader.getResources (CChangeLog.CHANGELOG_XML_FILENAME)))
      {
        final ChangeLog aChangeLog = readChangeLog (new URLResource (aURL), aErrorCallback);
        if (aChangeLog != null)
          ret.put (aURL.toURI (), aChangeLog);
        else
          s_aLogger.warn ("Failed to read changelog from URL " + aURL.toExternalForm ());
      }
      return ret;
    }
    catch (final IOException ex)
    {
      // Can be thrown by getResources
      throw new IllegalStateException ("Failed to resolved changelogs", ex);
    }
    catch (final URISyntaxException ex)
    {
      // Can be thrown by toURI
      throw new IllegalStateException ("Failed to convert URL to URI", ex);
    }
  }

  @Nonnull
  public static IMicroDocument writeChangeLog (@Nonnull final ChangeLog aChangeLog)
  {
    if (aChangeLog == null)
      throw new NullPointerException ("changeLog");

    final DateFormat aDF = new SimpleDateFormat (DATE_FORMAT);
    final IMicroDocument ret = new MicroDocument ();
    final IMicroElement eRoot = ret.appendElement (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_CHANGELOG);
    eRoot.setAttribute (CXML.XML_ATTR_XMLNS_WITH_SEP + CXML.XML_NS_PREFIX_XSI, CXML.XML_NS_XSI);
    eRoot.setAttribute (CXML.XML_NS_PREFIX_XSI + ":schemaLocation", CChangeLog.CHANGELOG_SCHEMALOCATION_10);
    eRoot.setAttribute (ATTR_VERSION, aChangeLog.getVersion ().getAsString ());
    if (StringHelper.hasText (aChangeLog.getComponent ()))
      eRoot.setAttribute (ATTR_COMPONENT, aChangeLog.getComponent ());

    for (final ChangeLogEntry aEntry : aChangeLog.getAllEntries ())
    {
      final IMicroElement eEntry = eRoot.appendElement (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_ENTRY);
      eEntry.setAttribute (ATTR_DATE, aDF.format (aEntry.getDate ()));
      eEntry.setAttribute (ATTR_ACTION, aEntry.getAction ().getID ());
      eEntry.setAttribute (ATTR_CATEGORY, aEntry.getCategory ().getID ());
      if (aEntry.isIncompatible ())
        eEntry.setAttribute (ATTR_INCOMPATIBLE, Boolean.TRUE.toString ());
      eEntry.appendChild (MicroTypeConverter.convertToMicroElement (aEntry.getAllTexts (),
                                                                    CChangeLog.CHANGELOG_NAMESPACE_10,
                                                                    ELEMENT_CHANGE));
      for (final String sIssue : aEntry.getAllIssues ())
        eEntry.appendElement (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_ISSUE).appendText (sIssue);
    }

    for (final ChangeLogRelease aRelease : aChangeLog.getAllReleases ())
    {
      final IMicroElement eRelease = eRoot.appendElement (CChangeLog.CHANGELOG_NAMESPACE_10, ELEMENT_RELEASE);
      eRelease.setAttribute (ATTR_DATE, aDF.format (aRelease.getDate ()));
      eRelease.setAttribute (ATTR_VERSION, aRelease.getVersion ().getAsString ());
    }

    return ret;
  }
}
