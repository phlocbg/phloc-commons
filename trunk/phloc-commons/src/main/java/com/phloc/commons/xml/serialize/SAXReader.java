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

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import javax.annotation.concurrent.ThreadSafe;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.io.IInputStreamProvider;
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
import com.phloc.commons.xml.sax.InputSourceFactory;

/**
 * Helper class to read XML documents via SAX
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class SAXReader
{
  public static final class SAXReaderFactory implements IFactory <org.xml.sax.XMLReader>
  {
    @Nonnull
    public org.xml.sax.XMLReader create ()
    {
      try
      {
        final org.xml.sax.XMLReader ret = XMLReaderFactory.createXMLReader ();
        return ret;
      }
      catch (final SAXException ex)
      {
        throw new InitializationException ("Failed to instantiate XML reader!", ex);
      }
    }
  }

  private static final IStatisticsHandlerTimer s_aSaxTimerHdl = StatisticsManager.getTimerHandler (SAXReader.class.getName ());
  private static final IStatisticsHandlerCounter s_aSaxSuccessCounterHdl = StatisticsManager.getCounterHandler (SAXReader.class.getName () +
                                                                                                                "$success");
  private static final IStatisticsHandlerCounter s_aSaxErrorCounterHdl = StatisticsManager.getCounterHandler (SAXReader.class.getName () +
                                                                                                              "$error");

  // In practice no more than 5 readers are required (even 3 would be enough)
  private static final IObjectPool <org.xml.sax.XMLReader> s_aSAXPool = new ObjectPool <org.xml.sax.XMLReader> (5,
                                                                                                                new SAXReaderFactory ());

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final SAXReader s_aInstance = new SAXReader ();

  private SAXReader ()
  {}

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final File aFile, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aFile), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final URI aURI, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aURI), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final URL aURL, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aURL), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final IInputStreamProvider aISP,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aISP), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final IReadableResource aResource,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aResource), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final CharSequence aXML, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aXML), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final String sXML, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (sXML), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final char [] aXML, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aXML), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final char [] aXML,
                                     @Nonnegative final int nOfs,
                                     @Nonnegative final int nLen,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aXML, nOfs, nLen), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final byte [] aXML, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aXML), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final byte [] aXML,
                                     @Nonnegative final int nOfs,
                                     @Nonnegative final int nLen,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aXML, nOfs, nLen), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull final ByteBuffer aXML, @Nonnull final ISAXReaderSettings aSettings)
  {
    return readXMLSAX (InputSourceFactory.create (aXML), aSettings);
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull @WillClose final InputStream aIS,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    if (aIS == null)
      throw new NullPointerException ("IS");

    try
    {
      return readXMLSAX (InputSourceFactory.create (aIS), aSettings);
    }
    finally
    {
      StreamUtils.close (aIS);
    }
  }

  @Nonnull
  public static ESuccess readXMLSAX (@Nonnull @WillClose final Reader aReader,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    if (aReader == null)
      throw new NullPointerException ("Reader");

    try
    {
      return readXMLSAX (InputSourceFactory.create (aReader), aSettings);
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }

  /**
   * Read an XML document via a SAX handler. The streams are closed after
   * reading.
   * 
   * @param aIS
   *        The input source to read from. Automatically closed upon success or
   *        error. May not be <code>null</code>.
   *        {@link com.phloc.commons.xml.sax.InputSourceFactory} may be used to
   *        create {@link InputSource} objects from different input types.
   * @param aSettings
   *        Reader settings. At least a content handler should be set. May be
   *        <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if reading succeeded,
   *         {@link ESuccess#FAILURE} otherwise
   */
  @Nonnull
  public static ESuccess readXMLSAX (@WillClose @Nonnull final InputSource aIS,
                                     @Nonnull final ISAXReaderSettings aSettings)
  {
    if (aIS == null)
      throw new NullPointerException ("InputSource");
    if (aSettings == null)
      throw new NullPointerException ("SAXReaderSettings");

    try
    {
      // use parser from pool
      final org.xml.sax.XMLReader aParser = s_aSAXPool.borrowObject ();
      try
      {
        final StopWatch aSW = new StopWatch (true);
        aParser.setContentHandler (aSettings.getContentHandler ());
        aParser.setDTDHandler (aSettings.getDTDHandler ());
        aParser.setEntityResolver (aSettings.getEntityResolver ());
        aParser.setErrorHandler (aSettings.getErrorHandler ());

        // Set all features
        if (aSettings.hasAnyFeature ())
          for (final Map.Entry <EXMLParserFeature, Boolean> aEntry : aSettings.getAllFeatures ().entrySet ())
            aEntry.getKey ().applyTo (aParser, aEntry.getValue ().booleanValue ());

        // Set optional properties
        if (aSettings.getLexicalHandler () != null)
          EXMLParserProperty.SAX_LEXICAL_HANDLER.applyTo (aParser, aSettings.getLexicalHandler ());

        // Start parsing
        aParser.parse (aIS);

        // Stats
        s_aSaxSuccessCounterHdl.increment ();
        s_aSaxTimerHdl.addTime (aSW.stopAndGetMillis ());
        return ESuccess.SUCCESS;
      }
      finally
      {
        // Return parser to pool
        s_aSAXPool.returnObject (aParser);
      }
    }
    catch (final SAXParseException ex)
    {
      boolean bHandled = false;
      if (aSettings.getErrorHandler () != null)
        try
        {
          aSettings.getErrorHandler ().fatalError (ex);
          bHandled = true;
        }
        catch (final SAXException ex2)
        {
          // fall-through
        }

      if (!bHandled)
        aSettings.getExceptionHandler ().onException (ex);
    }
    catch (final Throwable t)
    {
      aSettings.getExceptionHandler ().onException (t);
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
}
