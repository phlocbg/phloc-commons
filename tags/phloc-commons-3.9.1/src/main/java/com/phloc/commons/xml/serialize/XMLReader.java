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
package com.phloc.commons.xml.serialize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.pool.IObjectPool;
import com.phloc.commons.pool.ObjectPool;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.timing.StopWatch;
import com.phloc.commons.xml.EXMLParserFeature;
import com.phloc.commons.xml.EXMLParserProperty;
import com.phloc.commons.xml.XMLFactory;
import com.phloc.commons.xml.sax.CollectingSAXErrorHandler;
import com.phloc.commons.xml.sax.InputSourceFactory;
import com.phloc.commons.xml.sax.LoggingSAXErrorHandler;

/**
 * Helper class to read XML documents via SAX or DOM
 * 
 * @author philip
 */
public final class XMLReader
{
  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (XMLReader.class);
  private static final IStatisticsHandlerTimer s_aSaxTimerHdl = StatisticsManager.getTimerHandler (XMLReader.class.getName () +
                                                                                                   "$SAX");
  private static final IStatisticsHandlerCounter s_aSaxErrorCounterHdl = StatisticsManager.getCounterHandler (XMLReader.class.getName () +
                                                                                                              "$SAXERRORS");
  private static final IStatisticsHandlerTimer s_aDomTimerHdl = StatisticsManager.getTimerHandler (XMLReader.class.getName () +
                                                                                                   "$DOM");
  private static final IStatisticsHandlerTimer s_aDomSchemaTimerHdl = StatisticsManager.getTimerHandler (XMLReader.class.getName () +
                                                                                                         "$DOMwithSchema");
  private static final IStatisticsHandlerCounter s_aDomErrorCounterHdl = StatisticsManager.getCounterHandler (XMLReader.class.getName () +
                                                                                                              "$DOMERRORS");

  private static final IFactory <org.xml.sax.XMLReader> s_aXMLReaderFactry = new IFactory <org.xml.sax.XMLReader> ()
  {
    public org.xml.sax.XMLReader create ()
    {
      try
      {
        final org.xml.sax.XMLReader ret = XMLReaderFactory.createXMLReader ();
        ret.setErrorHandler (LoggingSAXErrorHandler.getInstance ());
        return ret;
      }
      catch (final SAXException ex)
      {
        throw new InitializationException ("Failed to instantiate XML reader!", ex);
      }
    }
  };

  // In practice no more than 5 readers are required (even 3 would be enough)
  private static final IObjectPool <org.xml.sax.XMLReader> s_aPool = new ObjectPool <org.xml.sax.XMLReader> (5,
                                                                                                             s_aXMLReaderFactry);

  // Default SAX parser features
  private static final Map <EXMLParserFeature, Boolean> s_aDefaultSaxParserFeatures = new EnumMap <EXMLParserFeature, Boolean> (EXMLParserFeature.class)
  {
    {
      put (EXMLParserFeature.NAMESPACES, Boolean.TRUE);
      put (EXMLParserFeature.SAX_NAMESPACE_PREFIXES, Boolean.TRUE);
      put (EXMLParserFeature.AUGMENT_PSVI, Boolean.FALSE);
    }
  };

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLReader s_aInstance = new XMLReader ();

  private XMLReader ()
  {}

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputSource aIS) throws SAXException
  {
    return readXMLDOM (aIS, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull @WillClose final InputStream aIS) throws SAXException
  {
    return readXMLDOM (aIS, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final File aFile) throws SAXException
  {
    return readXMLDOM (aFile, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final IReadableResource aIIS) throws SAXException
  {
    return readXMLDOM (aIIS, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final Reader aReader) throws SAXException
  {
    return readXMLDOM (aReader, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final String sXML) throws SAXException
  {
    return readXMLDOM (sXML, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final byte [] aXML) throws SAXException
  {
    return readXMLDOM (aXML, (Schema) null, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputSource aIS, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (aIS, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputStream aIS, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (aIS, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final File aFile, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (aFile, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final IReadableResource aISP, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (aISP, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final Reader aReader, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (aReader, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final String sXML, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (sXML, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final byte [] aXML, @Nullable final Schema aSchema) throws SAXException
  {
    return readXMLDOM (aXML, aSchema, (ErrorHandler) null, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputSource aIS,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aIS, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputStream aIS,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aIS, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final File aFile, @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aFile, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final IReadableResource aISP,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aISP, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final Reader aReader,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aReader, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final String sXML, @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (sXML, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final byte [] aXML, @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aXML, (Schema) null, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputSource aInputSource,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aInputSource, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull @WillClose final InputStream aIS,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aIS, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final File aFile,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aFile, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final IReadableResource aResource,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aResource, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull @WillClose final Reader aReader,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aReader, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final String sXML,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (sXML, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final byte [] aXML,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler) throws SAXException
  {
    return readXMLDOM (aXML, aSchema, aCustomErrorHandler, (EntityResolver) null);
  }

  @Nullable
  public static Document readXMLDOM (@WillClose @Nonnull final InputSource aInputSource,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (aInputSource == null)
      throw new NullPointerException ("inputSource");

    Document doc = null;
    try
    {
      final StopWatch aSW = new StopWatch (true);
      final DocumentBuilder aDocumentBuilder = aSchema == null ? XMLFactory.getDocumentBuilder ()
                                                              : XMLFactory.createDocumentBuilder (aSchema);

      // Ensure a collecting error handler is present
      CollectingSAXErrorHandler aCEH;
      if (aCustomErrorHandler instanceof CollectingSAXErrorHandler)
        aCEH = (CollectingSAXErrorHandler) aCustomErrorHandler;
      else
        aCEH = new CollectingSAXErrorHandler (aCustomErrorHandler != null ? aCustomErrorHandler
                                                                         : LoggingSAXErrorHandler.getInstance ());
      aDocumentBuilder.setErrorHandler (aCEH);

      // Set optional entity resolver
      aDocumentBuilder.setEntityResolver (aEntityResolver);

      // Main parsing
      doc = aDocumentBuilder.parse (aInputSource);

      // Statistics update
      if (aSchema == null)
        s_aDomTimerHdl.addTime (aSW.stopAndGetMillis ());
      else
        s_aDomSchemaTimerHdl.addTime (aSW.stopAndGetMillis ());

      // By default, a document is returned, even if does not match the schema
      // (if errors occurred), so I'm handling this manually by check for
      // collected errors
      if (aCEH.getResourceErrors ().containsAtLeastOneError ())
        return null;
    }
    catch (final SAXException ex)
    {
      s_aLogger.error ("Invalid XML document", ex);
      s_aDomErrorCounterHdl.increment ();
      throw ex;
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Error reading XML document", ex);
      s_aDomErrorCounterHdl.increment ();
    }
    finally
    {
      // Close both byte stream and character stream, as we don't know which one
      // was used
      StreamUtils.close (aInputSource.getByteStream ());
      StreamUtils.close (aInputSource.getCharacterStream ());
    }
    return doc;
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull @WillClose final InputStream aIS,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    try
    {
      return readXMLDOM (InputSourceFactory.create (aIS), aSchema, aCustomErrorHandler, aEntityResolver);
    }
    finally
    {
      StreamUtils.close (aIS);
    }
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final File aFile,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    return readXMLDOM (InputSourceFactory.create (aFile), aSchema, aCustomErrorHandler, aEntityResolver);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final IReadableResource aResource,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (aResource == null)
      throw new NullPointerException ("resource");

    return readXMLDOM (InputSourceFactory.create (aResource), aSchema, aCustomErrorHandler, aEntityResolver);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull @WillClose final Reader aReader,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (aReader == null)
      throw new NullPointerException ("reader");

    try
    {
      return readXMLDOM (InputSourceFactory.create (aReader), aSchema, aCustomErrorHandler, aEntityResolver);
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final String sXML,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (sXML == null)
      throw new NullPointerException ("xml");

    return readXMLDOM (InputSourceFactory.create (sXML), aSchema, aCustomErrorHandler, aEntityResolver);
  }

  @Nullable
  public static Document readXMLDOM (@Nonnull final byte [] aXML,
                                     @Nullable final Schema aSchema,
                                     @Nullable final ErrorHandler aCustomErrorHandler,
                                     @Nullable final EntityResolver aEntityResolver) throws SAXException
  {
    if (aXML == null)
      throw new NullPointerException ("xml");

    return readXMLDOM (InputSourceFactory.create (aXML), aSchema, aCustomErrorHandler, aEntityResolver);
  }

  /**
   * Read an XML document via a SAX handler. The stream is closed after reading.
   * 
   * @param aIS
   *        The input stream to read from. After parsing - no matter whether
   *        successful or not - the stream is closed!
   * @param aEntityResolver
   * @param aDTDHdl
   * @param aContentHdl
   * @param aErrorHdl
   * @param aLexicalHdl
   *        The optional lexical SAX handler to use. Makes only sense if DTD
   *        validation is enabled. May be <code>null</code>.
   * @param bDTDValidating
   *        If true, validation against a DTD is enabled.
   * @param bSchemaValidating
   *        If true, validation against an XML schema is enabled.
   * @return {@link ESuccess#SUCCESS} if reading succeeded,
   *         {@link ESuccess#FAILURE} otherwise
   */
  @Nonnull
  public static ESuccess readXMLSAX (@WillClose @Nonnull final InputStream aIS,
                                     final EntityResolver aEntityResolver,
                                     final DTDHandler aDTDHdl,
                                     final ContentHandler aContentHdl,
                                     final ErrorHandler aErrorHdl,
                                     final LexicalHandler aLexicalHdl,
                                     final boolean bDTDValidating,
                                     final boolean bSchemaValidating)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    return readXMLSAX (InputSourceFactory.create (aIS),
                       aEntityResolver,
                       aDTDHdl,
                       aContentHdl,
                       aErrorHdl,
                       aLexicalHdl,
                       bDTDValidating,
                       bSchemaValidating);
  }

  /**
   * Read an XML document via a SAX handler. The stream is closed after reading.
   * By default namespace and namespace prefix handling is enabled.
   * 
   * @param aIS
   *        The input source to read from. Automatically closed upon success or
   *        error.
   * @param aEntityResolver
   * @param aDTDHdl
   * @param aContentHdl
   * @param aErrorHdl
   *        The optional error handler to use. In case you want to collect
   *        errors etc.
   * @param aLexicalHdl
   *        The optional lexical SAX handler to use. Makes only sense if DTD
   *        validation is enabled. May be <code>null</code>.
   * @param bDTDValidating
   *        If <code>true</code>, validation against a DTD is enabled.
   * @param bSchemaValidating
   *        If <code>true</code>, validation against an XML schema is enabled.
   * @return {@link ESuccess#SUCCESS} if reading succeeded,
   *         {@link ESuccess#FAILURE} otherwise
   */
  @Nonnull
  public static ESuccess readXMLSAX (@WillClose @Nonnull final InputSource aIS,
                                     final EntityResolver aEntityResolver,
                                     final DTDHandler aDTDHdl,
                                     final ContentHandler aContentHdl,
                                     final ErrorHandler aErrorHdl,
                                     @Nullable final LexicalHandler aLexicalHdl,
                                     final boolean bDTDValidating,
                                     final boolean bSchemaValidating)
  {
    final Map <EXMLParserFeature, Boolean> aFeatures = new EnumMap <EXMLParserFeature, Boolean> (s_aDefaultSaxParserFeatures);
    aFeatures.put (EXMLParserFeature.VALIDATION, Boolean.valueOf (bDTDValidating));
    aFeatures.put (EXMLParserFeature.SCHEMA, Boolean.valueOf (bSchemaValidating));
    if (false)
      aFeatures.put (EXMLParserFeature.SCHEMA_FULL_CHECKING, Boolean.valueOf (bSchemaValidating));
    aFeatures.put (EXMLParserFeature.WARN_ON_DUPLICATE_ATTDEF, Boolean.valueOf (bDTDValidating || bSchemaValidating));
    // WARN_ON_UNDECLARED_ELEMDEF is not supported by JDK 1.6.0_35
    aFeatures.put (EXMLParserFeature.WARN_ON_DUPLICATE_ENTITYDEF, Boolean.valueOf (bDTDValidating || bSchemaValidating));
    return readXMLSAX (aIS, aEntityResolver, aDTDHdl, aContentHdl, aErrorHdl, aLexicalHdl, aFeatures);
  }

  /**
   * Read an XML document via a SAX handler. The stream is closed after reading.
   * 
   * @param aIS
   *        The input source to read from. Automatically closed upon success or
   *        error.
   * @param aEntityResolver
   * @param aDTDHdl
   * @param aContentHdl
   * @param aErrorHdl
   *        The optional error handler to use. In case you want to collect
   *        errors etc.
   * @param aLexicalHdl
   *        The optional lexical SAX handler to use. Makes only sense if DTD
   *        validation is enabled. May be <code>null</code>.
   * @param aFeatures
   *        The feature map to be applied. May be <code>null</code>. All
   *        contained values must be non-<code>null</code>.
   * @return {@link ESuccess#SUCCESS} if reading succeeded,
   *         {@link ESuccess#FAILURE} otherwise
   */
  @Nonnull
  public static ESuccess readXMLSAX (@WillClose @Nonnull final InputSource aIS,
                                     final EntityResolver aEntityResolver,
                                     final DTDHandler aDTDHdl,
                                     final ContentHandler aContentHdl,
                                     final ErrorHandler aErrorHdl,
                                     @Nullable final LexicalHandler aLexicalHdl,
                                     @Nullable final Map <EXMLParserFeature, Boolean> aFeatures)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    try
    {
      // use parser from pool
      final org.xml.sax.XMLReader aParser = s_aPool.borrowObject ();
      if (aParser == null)
      {
        s_aLogger.error ("Failed to get parser from pool!");
        return ESuccess.FAILURE;
      }

      try
      {
        final StopWatch aSW = new StopWatch (true);
        aParser.setContentHandler (aContentHdl);
        aParser.setDTDHandler (aDTDHdl);
        aParser.setEntityResolver (aEntityResolver);
        aParser.setErrorHandler (aErrorHdl);

        // Set all features
        if (aFeatures != null)
          for (final Map.Entry <EXMLParserFeature, Boolean> aEntry : aFeatures.entrySet ())
            aEntry.getKey ().applyTo (aParser, aEntry.getValue ().booleanValue ());

        // Set optional properties
        if (aLexicalHdl != null)
          EXMLParserProperty.SAX_FEATURE_LEXICAL_HANDLER.applyTo (aParser, aLexicalHdl);

        // Start parsing
        aParser.parse (aIS);
        s_aSaxTimerHdl.addTime (aSW.stopAndGetMillis ());
        return ESuccess.SUCCESS;
      }
      finally
      {
        // Return parser to pool
        s_aPool.returnObject (aParser);
      }
    }
    catch (final SAXParseException ex)
    {
      s_aLogger.error ("Error parsing XML at position (" + ex.getLineNumber () + "," + ex.getColumnNumber () + ")", ex);
    }
    catch (final SAXException ex)
    {
      s_aLogger.error ("Error parsing XML", ex);
    }
    catch (final UnknownHostException ex)
    {
      // Caught if entity resolver failed
      s_aLogger.error ("Failed to resolve entity host: " + ex.getMessage ());
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Error reading XML from input", ex);
    }
    finally
    {
      // Close both byte stream and character stream, as we don't know which one
      // was used
      StreamUtils.close (aIS.getByteStream ());
      StreamUtils.close (aIS.getCharacterStream ());
    }
    s_aSaxErrorCounterHdl.increment ();
    return ESuccess.FAILURE;
  }

  @Nullable
  public static Boolean getDefaultSaxParserFeatureValue (@Nullable final EXMLParserFeature eFeature)
  {
    return s_aDefaultSaxParserFeatures.get (eFeature);
  }

  public static void setDefaultSaxParserFeatureValue (@Nonnull final EXMLParserFeature eFeature,
                                                      @Nullable final Boolean aValue)
  {
    if (eFeature == null)
      throw new NullPointerException ("feature");

    if (aValue == null)
      s_aDefaultSaxParserFeatures.remove (eFeature);
    else
      s_aDefaultSaxParserFeatures.put (eFeature, aValue);
  }
}