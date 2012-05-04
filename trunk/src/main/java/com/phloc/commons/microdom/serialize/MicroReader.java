/**
 * Copyright (C) 2006-2012 phloc systems
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
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.xml.serialize.XMLReader;

/**
 * Utility class to read an XML stream into an {@link IMicroDocument}.
 * 
 * @author philip
 */
@Immutable
public final class MicroReader
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MicroReader s_aInstance = new MicroReader ();

  private MicroReader ()
  {}

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final InputSource aIS)
  {
    return readMicroXML (aIS, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final InputSource aIS,
                                             @Nullable final EntityResolver aSpecialEntityResolver)
  {
    return readMicroXML (aIS, aSpecialEntityResolver, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final InputSource aIS,
                                             @Nullable final EntityResolver aSpecialEntityResolver,
                                             @Nullable final ErrorHandler aSpecialErrorHdl)
  {
    return readMicroXML (aIS, aSpecialEntityResolver, aSpecialErrorHdl, false, false);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final InputSource aIS,
                                             @Nullable final EntityResolver aSpecialEntityResolver,
                                             @Nullable final ErrorHandler aSpecialErrorHdl,
                                             final boolean bDTDValidation,
                                             final boolean bSchemaValidation)
  {
    if (aIS == null)
      return null;

    final MicroSAXHandler aHdl = new MicroSAXHandler (false, aSpecialEntityResolver);
    if (XMLReader.readXMLSAX (aIS,
                              aHdl,
                              aHdl,
                              aHdl,
                              aSpecialErrorHdl != null ? aSpecialErrorHdl : aHdl,
                              aHdl,
                              bDTDValidation,
                              bSchemaValidation).isFailure ())
      return null;
    return aHdl.getDocument ();
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final InputStream aIS)
  {
    return readMicroXML (aIS, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final InputStream aIS,
                                             @Nullable final EntityResolver aSpecialEntityResolver)
  {
    if (aIS == null)
      return null;
    return readMicroXML (new InputSource (aIS), aSpecialEntityResolver);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@Nullable final File aFile)
  {
    return readMicroXML (aFile, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@Nullable final File aFile,
                                             @Nullable final EntityResolver aSpecialEntityResolver)
  {
    if (aFile == null)
      return null;
    return readMicroXML (FileUtils.getInputStream (aFile), aSpecialEntityResolver);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@Nullable final IInputStreamProvider aISP)
  {
    return readMicroXML (aISP, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@Nullable final IInputStreamProvider aISP,
                                             @Nullable final EntityResolver aSpecialEntityResolver)
  {
    if (aISP == null)
      return null;
    return readMicroXML (aISP.getInputStream (), aSpecialEntityResolver);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final Reader aReader)
  {
    return readMicroXML (aReader, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@WillClose @Nullable final Reader aReader,
                                             @Nullable final EntityResolver aSpecialEntityResolver)
  {
    if (aReader == null)
      return null;
    return readMicroXML (new InputSource (aReader), aSpecialEntityResolver);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@Nullable final String sXML)
  {
    return readMicroXML (sXML, null);
  }

  @Nullable
  public static IMicroDocument readMicroXML (@Nullable final String sXML,
                                             @Nullable final EntityResolver aSpecialEntityResolver)
  {
    if (sXML == null)
      return null;
    return readMicroXML (new StringReader (sXML), aSpecialEntityResolver);
  }
}
