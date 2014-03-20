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
package com.phloc.commons.url;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.encode.IDecoder;
import com.phloc.commons.encode.IEncoder;
import com.phloc.commons.encode.IdentityDecoder;
import com.phloc.commons.encode.IdentityEncoder;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.reader.XMLMapHandler;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mutable.IWrapper;
import com.phloc.commons.mutable.Wrapper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.encode.URLParameterEncoder;

/**
 * URL utilities.
 *
 * @author Philip Helger
 */
@Immutable
public final class URLUtils
{
  /** Default URL charset is UTF-8 */
  public static final Charset CHARSET_URL_OBJ = CCharset.CHARSET_UTF_8_OBJ;

  /** Separator before first param: ? */
  public static final char QUESTIONMARK = '?';
  public static final String QUESTIONMARK_STR = Character.toString (QUESTIONMARK);

  /** Separator between params: &amp; */
  public static final char AMPERSAND = '&';
  public static final String AMPERSAND_STR = Character.toString (AMPERSAND);

  /** Separator between param name and param value: = */
  public static final char EQUALS = '=';
  public static final String EQUALS_STR = Character.toString (EQUALS);

  /** Separator between URL path and anchor name: # */
  public static final char HASH = '#';
  public static final String HASH_STR = Character.toString (HASH);

  private static final Logger s_aLogger = LoggerFactory.getLogger (URLUtils.class);

  private static char [] s_aCleanURLOld;
  private static char [][] s_aCleanURLNew;

  private static BitSet dontNeedEncoding;
  private static final int caseDiff = ('a' - 'A');

  static
  {

    /*
     * The list of characters that are not encoded has been determined as
     * follows: RFC 2396 states: ----- Data characters that are allowed in a URI
     * but do not have a reserved purpose are called unreserved. These include
     * upper and lower case letters, decimal digits, and a limited set of
     * punctuation marks and symbols. unreserved = alphanum | mark mark = "-" |
     * "_" | "." | "!" | "~" | "*" | "'" | "(" | ")" Unreserved characters can
     * be escaped without changing the semantics of the URI, but this should not
     * be done unless the URI is being used in a context that does not allow the
     * unescaped character to appear. ----- It appears that both Netscape and
     * Internet Explorer escape all special characters from this list with the
     * exception of "-", "_", ".", "*". While it is not clear why they are
     * escaping the other characters, perhaps it is safest to assume that there
     * might be contexts in which the others are unsafe if not escaped.
     * Therefore, we will use the same list. It is also noteworthy that this is
     * consistent with O'Reilly's "HTML: The Definitive Guide" (page 164). As a
     * last note, Intenet Explorer does not encode the "@" character which is
     * clearly not unreserved according to the RFC. We are being consistent with
     * the RFC in this matter, as is Netscape.
     */
    dontNeedEncoding = new BitSet (256);
    int i;
    for (i = 'a'; i <= 'z'; i++)
      dontNeedEncoding.set (i);
    for (i = 'A'; i <= 'Z'; i++)
      dontNeedEncoding.set (i);
    for (i = '0'; i <= '9'; i++)
      dontNeedEncoding.set (i);
    /*
     * encoding a space to a + is done in the encode() method
     */
    dontNeedEncoding.set (' ');
    dontNeedEncoding.set ('-');
    dontNeedEncoding.set ('_');
    dontNeedEncoding.set ('.');
    dontNeedEncoding.set ('*');
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final URLUtils s_aInstance = new URLUtils ();

  private URLUtils ()
  {}

  /**
   * URL-decode the passed value automatically handling charset issues. The used
   * char set is determined by {@link #CHARSET_URL_OBJ}.
   *
   * @param sValue
   *        The value to be decoded. May not be <code>null</code>.
   * @return The decoded value.
   */
  @Nonnull
  public static String urlDecode (@Nonnull final String sValue)
  {
    return urlDecode (sValue, CHARSET_URL_OBJ);
  }

  /**
   * URL-decode the passed value automatically handling charset issues.
   *
   * @param sValue
   *        The value to be decoded. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use. May not be <code>null</code>.
   * @return The decoded value.
   */
  @Nonnull
  public static String urlDecode (@Nonnull final String sValue, @Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (sValue, "Value");
    ValueEnforcer.notNull (aCharset, "Charset");

    boolean needToChange = false;
    final int numChars = sValue.length ();
    final StringBuilder sb = new StringBuilder (numChars > 500 ? numChars / 2 : numChars);
    int i = 0;

    char c;
    byte [] bytes = null;
    while (i < numChars)
    {
      c = sValue.charAt (i);
      switch (c)
      {
        case '+':
          sb.append (' ');
          i++;
          needToChange = true;
          break;
        case '%':
          /*
           * Starting with this instance of %, process all consecutive
           * substrings of the form %xy. Each substring %xy will yield a byte.
           * Convert all consecutive bytes obtained this way to whatever
           * character(s) they represent in the provided encoding.
           */
          try
          {

            // (numChars-i)/3 is an upper bound for the number
            // of remaining bytes
            if (bytes == null)
              bytes = new byte [(numChars - i) / 3];
            int pos = 0;

            while (((i + 2) < numChars) && (c == '%'))
            {
              final int v = Integer.parseInt (sValue.substring (i + 1, i + 3), 16);
              if (v < 0)
                throw new IllegalArgumentException ("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
              bytes[pos++] = (byte) v;
              i += 3;
              if (i < numChars)
                c = sValue.charAt (i);
            }

            // A trailing, incomplete byte encoding such as
            // "%x" will cause an exception to be thrown

            if ((i < numChars) && (c == '%'))
              throw new IllegalArgumentException ("URLDecoder: Incomplete trailing escape (%) pattern");

            sb.append (new String (bytes, 0, pos, aCharset));
          }
          catch (final NumberFormatException e)
          {
            throw new IllegalArgumentException ("URLDecoder: Illegal hex characters in escape (%) pattern - " +
                e.getMessage ());
          }
          needToChange = true;
          break;
        default:
          sb.append (c);
          i++;
          break;
      }
    }

    return needToChange ? sb.toString () : sValue;
  }

  /**
   * URL-encode the passed value automatically handling charset issues. The used
   * char set is determined by {@link #CHARSET_URL_OBJ}.
   *
   * @param sValue
   *        The value to be encoded. May not be <code>null</code>.
   * @return The encoded value.
   */
  @Nonnull
  public static String urlEncode (@Nonnull final String sValue)
  {
    return urlEncode (sValue, CHARSET_URL_OBJ);
  }

  /**
   * URL-encode the passed value automatically handling charset issues.
   *
   * @param sValue
   *        The value to be encoded. May not be <code>null</code>.
   * @param aCharset
   *        The charset to use. May not be <code>null</code>.
   * @return The encoded value.
   */
  @Nonnull
  public static String urlEncode (@Nonnull final String sValue, @Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (sValue, "Value");
    ValueEnforcer.notNull (aCharset, "Charset");

    boolean needToChange = false;
    final StringBuilder out = new StringBuilder (sValue.length ());
    final CharArrayWriter charArrayWriter = new CharArrayWriter ();

    for (int i = 0; i < sValue.length ();)
    {
      int c = sValue.charAt (i);
      // System.out.println("Examining character: " + c);
      if (dontNeedEncoding.get (c))
      {
        if (c == ' ')
        {
          c = '+';
          needToChange = true;
        }
        // System.out.println("Storing: " + c);
        out.append ((char) c);
        i++;
      }
      else
      {
        // convert to external encoding before hex conversion
        do
        {
          charArrayWriter.write (c);
          /*
           * If this character represents the start of a Unicode surrogate pair,
           * then pass in two characters. It's not clear what should be done if
           * a bytes reserved in the surrogate pairs range occurs outside of a
           * legal surrogate pair. For now, just treat it as if it were any
           * other character.
           */
          if (c >= 0xD800 && c <= 0xDBFF)
          {
            /*
             * System.out.println(Integer.toHexString(c) +
             * " is high surrogate");
             */
            if ((i + 1) < sValue.length ())
            {
              final int d = sValue.charAt (i + 1);
              /*
               * System.out.println("\tExamining " + Integer.toHexString(d));
               */
              if (d >= 0xDC00 && d <= 0xDFFF)
              {
                /*
                 * System.out.println("\t" + Integer.toHexString(d) +
                 * " is low surrogate");
                 */
                charArrayWriter.write (d);
                i++;
              }
            }
          }
          i++;
        } while (i < sValue.length () && !dontNeedEncoding.get ((c = sValue.charAt (i))));

        charArrayWriter.flush ();
        final String str = new String (charArrayWriter.toCharArray ());
        final byte [] ba = str.getBytes (aCharset);
        for (final byte element : ba)
        {
          out.append ('%');
          char ch = Character.forDigit ((element >> 4) & 0xF, 16);
          // converting to use uppercase letter as part of
          // the hex value if ch is a letter.
          if (Character.isLetter (ch))
          {
            ch -= caseDiff;
          }
          out.append (ch);
          ch = Character.forDigit (element & 0xF, 16);
          if (Character.isLetter (ch))
          {
            ch -= caseDiff;
          }
          out.append (ch);
        }
        charArrayWriter.reset ();
        needToChange = true;
      }
    }

    return needToChange ? out.toString () : sValue;
  }

  private static void _initCleanURL ()
  {
    // This one cannot be in the static initializer of the class, because
    // ClassPathResource internally uses
    // URLUtils.getInputStream and this static initialization code of this class
    // can therefore not use ClasspathResource because it would create a
    // recursive
    // dependency!
    // Ever trickier is the when running multiple threads for reading XML (e.g.
    // in the unit test) this code would wait forever in the static initializer
    // because XMLMapHandler internally also acquires an XML reader....
    final Map <String, String> aCleanURLMap = new HashMap <String, String> ();
    if (XMLMapHandler.readMap (new ClassPathResource ("codelists/cleanurl-data.xml"), aCleanURLMap).isFailure ())
      throw new InitializationException ("Failed to init CleanURL data!");

    s_aCleanURLOld = new char [aCleanURLMap.size ()];
    s_aCleanURLNew = new char [aCleanURLMap.size ()] [];

    // Convert to char array
    int i = 0;
    for (final Map.Entry <String, String> aEntry : aCleanURLMap.entrySet ())
    {
      final String sKey = aEntry.getKey ();
      if (sKey.length () != 1)
        throw new IllegalStateException ("Clean URL source character has an invalid length: " + sKey.length ());
      s_aCleanURLOld[i] = sKey.charAt (0);
      s_aCleanURLNew[i] = aEntry.getValue ().toCharArray ();
      ++i;
    }
  }

  /**
   * Clean an URL part from nasty Umlauts. This mapping needs extension!
   *
   * @param sURLPart
   *        The original URL part. May be <code>null</code>.
   * @return The cleaned version or <code>null</code> if the input was
   *         <code>null</code>.
   */
  @Nullable
  public static String getCleanURLPartWithoutUmlauts (@Nullable final String sURLPart)
  {
    if (s_aCleanURLOld == null)
      _initCleanURL ();
    final char [] ret = StringHelper.replaceMultiple (sURLPart, s_aCleanURLOld, s_aCleanURLNew);
    return new String (ret);
  }

  @Nonnull
  public static IURLData getAsURLData (@Nonnull final String sHref)
  {
    return getAsURLData (sHref, IdentityDecoder.<String> create ());
  }

  /**
   * Parses the passed URL into a structured form
   *
   * @param sHref
   *        The URL to be parsed
   * @param aParameterDecoder
   *        The parameter decoder to use. May not be <code>null</code>.
   * @return the corresponding {@link IURLData} representation of the passed URL
   */
  @Nonnull
  public static IURLData getAsURLData (@Nonnull final String sHref, @Nonnull final IDecoder <String> aParameterDecoder)
  {
    ValueEnforcer.notNull (sHref, "Href");
    ValueEnforcer.notNull (aParameterDecoder, "ParameterDecoder");

    final String sRealHref = sHref.trim ();

    // Is it a protocol that does not allow for query parameters?
    final IURLProtocol eProtocol = URLProtocolRegistry.getProtocol (sRealHref);
    if (eProtocol != null && !eProtocol.allowsForQueryParameters ())
      return new URLData (sRealHref, null, null);

    if (GlobalDebug.isDebugMode ())
      if (eProtocol != null)
        try
        {
          new URL (sRealHref);
        }
        catch (final MalformedURLException ex)
        {
          s_aLogger.warn ("java.net.URL claims URL '" + sRealHref + "' to be invalid: " + ex.getMessage ());
        }

    String sPath;
    Map <String, String> aParams = null;
    String sAnchor;

    // First get the anchor out
    String sRemainingHref = sRealHref;
    final int nIndexAnchor = sRemainingHref.indexOf (HASH);
    if (nIndexAnchor >= 0)
    {
      // Extract anchor
      sAnchor = sRemainingHref.substring (nIndexAnchor + 1).trim ();
      sRemainingHref = sRemainingHref.substring (0, nIndexAnchor).trim ();
    }
    else
      sAnchor = null;

    // Find parameters
    final int nQuestionIndex = sRemainingHref.indexOf (QUESTIONMARK);
    if (nQuestionIndex >= 0)
    {
      // Use everything after the '?'
      final String sQueryString = sRemainingHref.substring (nQuestionIndex + 1).trim ();

      // Maybe empty, if the URL ends with a '?'
      if (StringHelper.hasText (sQueryString))
        aParams = _getQueryStringAsMap (sQueryString, aParameterDecoder);

      sPath = sRemainingHref.substring (0, nQuestionIndex).trim ();
    }
    else
      sPath = sRemainingHref;

    return new URLData (sPath, aParams, sAnchor);
  }

  @Nonnull
  @ReturnsMutableCopy
  private static Map <String, String> _getQueryStringAsMap (@Nullable final String sQueryString,
                                                            @Nonnull final IDecoder <String> aParameterDecoder)
  {
    final Map <String, String> aMap = new LinkedHashMap <String, String> ();
    if (StringHelper.hasText (sQueryString))
    {
      for (final String sKeyValuePair : StringHelper.getExploded (AMPERSAND, sQueryString))
        if (sKeyValuePair.length () > 0)
        {
          final List <String> aParts = StringHelper.getExploded (EQUALS, sKeyValuePair, 2);
          final String sKey = aParts.get (0);
          // Maybe empty when passing something like "url?=value"
          if (StringHelper.hasText (sKey))
          {
            final String sValue = aParts.size () == 2 ? aParts.get (1) : "";
            if (sValue == null)
              throw new NullPointerException ("parameter value may not be null");
            // Now decode the name and the value
            aMap.put (aParameterDecoder.decode (sKey), aParameterDecoder.decode (sValue));
          }
        }
    }
    return aMap;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, String> getQueryStringAsMap (@Nullable final String sQueryString)
  {
    return _getQueryStringAsMap (sQueryString, IdentityDecoder.<String> create ());
  }

  @Nonnull
  public static String getURLString (@Nonnull final IURLData aURL, @Nullable final Charset aParameterCharset)
  {
    return getURLString (aURL.getPath (), aURL.getAllParams (), aURL.getAnchor (), aParameterCharset);
  }

  /**
   * Get the final representation of the URL using the specified elements.
   *
   * @param sPath
   *        The main path. May be <code>null</code>.
   * @param aParams
   *        The set of parameters to be appended. May be <code>null</code>.
   * @param sAnchor
   *        An optional anchor to be added. May be <code>null</code>.
   * @param aParameterEncoder
   *        The parameters encoding to be used. May not be <code>null</code>.
   * @return May be <code>null</code> if path, anchor and parameters are
   *         <code>null</code>.
   */
  @SuppressWarnings ("null")
  @Nullable
  public static String getURLString (@Nullable final String sPath,
                                     @Nullable final Map <String, String> aParams,
                                     @Nullable final String sAnchor,
                                     @Nonnull final IEncoder <String> aParameterEncoder)
  {
    ValueEnforcer.notNull (aParameterEncoder, "ParameterEncoder");

    final boolean bHasParams = ContainerHelper.isNotEmpty (aParams);
    final boolean bHasAnchor = StringHelper.hasText (sAnchor);

    // return URL as is?
    if (!bHasParams && !bHasAnchor)
    {
      // Return URL as is
      return sPath;
    }

    final StringBuilder aSB = new StringBuilder ();
    if (StringHelper.hasText (sPath))
      aSB.append (sPath);

    if (bHasParams)
    {
      final boolean bHasQuestionMark = aSB.indexOf (QUESTIONMARK_STR) >= 0;

      if (bHasQuestionMark)
      {
        // Only if the "?" is not the last char otherwise the base href already
        // contains a parameter!
        final char cLast = StringHelper.getLastChar (aSB);
        if (cLast != QUESTIONMARK && cLast != AMPERSAND)
          aSB.append (AMPERSAND);
      }
      else
      {
        // First parameter
        aSB.append (QUESTIONMARK);
      }

      // ESCA-JAVA0285:
      // add all values
      for (final Map.Entry <String, String> aEntry : aParams.entrySet ())
      {
        // Key
        final String sKey = aEntry.getKey ();
        aSB.append (aParameterEncoder.encode (sKey));

        // Value
        final String sValue = aEntry.getValue ();
        if (StringHelper.hasText (sValue))
          aSB.append (EQUALS).append (aParameterEncoder.encode (sValue));

        // Separator
        aSB.append (AMPERSAND);
      }

      // delete the last "&"
      aSB.deleteCharAt (aSB.length () - 1);
    }

    // Append anchor
    if (bHasAnchor)
    {
      if (StringHelper.getLastChar (aSB) != HASH)
        aSB.append (HASH);
      aSB.append (sAnchor);
    }

    // Avoid empty URLs
    if (aSB.length () == 0)
      return QUESTIONMARK_STR;

    return aSB.toString ();
  }

  /**
   * Get the final representation of the URL using the specified elements.
   *
   * @param sPath
   *        The main path. May be <code>null</code>.
   * @param aParams
   *        The set of parameters to be appended. May be <code>null</code>.
   * @param sAnchor
   *        An optional anchor to be added. May be <code>null</code>.
   * @param aParameterCharset
   *        If not <code>null</code> the parameters are encoded using this
   *        charset.
   * @return May be <code>null</code> if all parameters are <code>null</code>.
   */
  @Nullable
  public static String getURLString (@Nullable final String sPath,
                                     @Nullable final Map <String, String> aParams,
                                     @Nullable final String sAnchor,
                                     @Nullable final Charset aParameterCharset)
  {
    IEncoder <String> aParameterEncoder;
    if (aParameterCharset == null)
      aParameterEncoder = IdentityEncoder.create ();
    else
      aParameterEncoder = new URLParameterEncoder (aParameterCharset);
    return getURLString (sPath, aParams, sAnchor, aParameterEncoder);
  }

  /**
   * Get the passed String as an URL. If the string is empty or not an URL
   * <code>null</code> is returned.
   *
   * @param sURL
   *        Source URL. May be <code>null</code>.
   * @return <code>null</code> if the passed URL is empty or invalid.
   */
  @Nullable
  public static URL getAsURL (@Nullable final String sURL)
  {
    if (StringHelper.hasText (sURL))
      try
      {
        return new URL (sURL);
      }
      catch (final MalformedURLException ex)
      {
        // fall-through
      }
    return null;
  }

  /**
   * Get the passed URI as an URL. If the URI is null or cannot be converted to
   * an URL <code>null</code> is returned.
   *
   * @param aURI
   *        Source URI. May be <code>null</code>.
   * @return <code>null</code> if the passed URI is null or cannot be converted
   *         to an URL.
   */
  @Nullable
  public static URL getAsURL (@Nullable final URI aURI)
  {
    if (aURI != null)
      try
      {
        return aURI.toURL ();
      }
      catch (final MalformedURLException ex)
      {
        // fall-through
      }
    return null;
  }

  /**
   * Get the passed String as an URI. If the string is empty or not an URI
   * <code>null</code> is returned.
   *
   * @param sURI
   *        Source URI. May be <code>null</code>.
   * @return <code>null</code> if the passed URI is empty or invalid.
   */
  @Nullable
  public static URI getAsURI (@Nullable final String sURI)
  {
    if (StringHelper.hasText (sURI))
      try
      {
        return new URI (sURI);
      }
      catch (final URISyntaxException ex)
      {
        // fall-through
      }
    return null;
  }

  /**
   * Get the passed URL as an URI. If the URL is null or not an URI
   * <code>null</code> is returned.
   *
   * @param aURL
   *        Source URL. May be <code>null</code>.
   * @return <code>null</code> if the passed URL is empty or invalid.
   */
  @Nullable
  public static URI getAsURI (@Nullable final URL aURL)
  {
    if (aURL != null)
      try
      {
        return aURL.toURI ();
      }
      catch (final URISyntaxException ex)
      {
        // fall-through
      }
    return null;
  }

  /**
   * Get an input stream from the specified URL. By default caching is disabled.
   * This method only handles GET requests - POST requests are not possible.
   *
   * @param aURL
   *        The URL to use. May not be <code>null</code>.
   * @param nConnectTimeoutMS
   *        Connect timeout milliseconds. 0 == infinite. &lt; 0: ignored.
   * @param nReadTimeoutMS
   *        Read timeout milliseconds. 0 == infinite. &lt; 0: ignored.
   * @param aConnectionModifier
   *        An optional callback object to modify the URLConnection before it is
   *        opened.
   * @param aExceptionHolder
   *        An optional exception holder for further outside investigation.
   * @return <code>null</code> if the input stream could not be opened.
   */
  @Nullable
  public static InputStream getInputStream (@Nonnull final URL aURL,
                                            final int nConnectTimeoutMS,
                                            final int nReadTimeoutMS,
                                            @Nullable final INonThrowingRunnableWithParameter <URLConnection> aConnectionModifier,
                                            @Nullable final IWrapper <IOException> aExceptionHolder)
  {
    ValueEnforcer.notNull (aURL, "URL");

    URLConnection aConnection;
    HttpURLConnection aHTTPConnection = null;
    try
    {
      aConnection = aURL.openConnection ();
      if (nConnectTimeoutMS >= 0)
        aConnection.setConnectTimeout (nConnectTimeoutMS);
      if (nReadTimeoutMS >= 0)
        aConnection.setReadTimeout (nReadTimeoutMS);
      if (aConnection instanceof HttpURLConnection)
        aHTTPConnection = (HttpURLConnection) aConnection;

      // Disable caching
      aConnection.setUseCaches (false);

      // Apply optional callback
      if (aConnectionModifier != null)
        aConnectionModifier.run (aConnection);

      if (aConnection instanceof JarURLConnection)
      {
        final JarEntry aJarEntry = ((JarURLConnection) aConnection).getJarEntry ();
        if (aJarEntry != null)
        {
          // Directories are identified by ending with a "/"
          if (aJarEntry.isDirectory ())
          {
            // Cannot open an InputStream on a directory
            return null;
          }

          // Heuristics for directories not ending with a "/"
          if (aJarEntry.getSize () == 0 && aJarEntry.getCrc () == 0)
          {
            // Cannot open an InputStream on a directory
            s_aLogger.warn ("Heuristically determined " + aURL + " to be a directory!");
            return null;
          }
        }
      }

      // by default follow-redirects is true for HTTPUrlConnections
      final InputStream ret = aConnection.getInputStream ();

      return ret;
    }
    catch (final IOException ex)
    {
      if (aExceptionHolder != null)
      {
        // Remember the exception
        aExceptionHolder.set (ex);
      }
      else
      {
        if (ex instanceof SocketTimeoutException)
          s_aLogger.warn ("Timeout to open input stream for '" +
                          aURL +
                          "': " +
                          ex.getClass ().getName () +
                          " - " +
                          ex.getMessage ());
        else
          s_aLogger.warn ("Failed to open input stream for '" +
                          aURL +
                          "': " +
                          ex.getClass ().getName () +
                          " - " +
                          ex.getMessage ());
      }

      if (aHTTPConnection != null)
      {
        // Read error completely for keep-alive (see
        // http://docs.oracle.com/javase/6/docs/technotes/guides/net/http-keepalive.html)
        InputStream aErrorIS = null;
        try
        {
          aErrorIS = aHTTPConnection.getErrorStream ();
          if (aErrorIS != null)
          {
            final byte [] aBuf = new byte [1024];
            // read the response body
            while (aErrorIS.read (aBuf) > 0)
            {
              // Read next
            }
          }
        }
        catch (final IOException ex2)
        {
          // deal with the exception
          s_aLogger.warn ("Failed to consume error stream for '" +
                          aURL +
                          "': " +
                          ex2.getClass ().getName () +
                          " - " +
                          ex2.getMessage ());
        }
        finally
        {
          StreamUtils.close (aErrorIS);
        }
      }
    }
    return null;
  }

  /**
   * POST something to a URL.
   *
   * @param aURL
   *        The destination URL. May not be <code>null</code>.
   * @param nConnectTimeoutMS
   *        Connect timeout milliseconds. 0 == infinite. &lt; 0: ignored.
   * @param nReadTimeoutMS
   *        Read timeout milliseconds. 0 == infinite. &lt; 0: ignored.
   * @param aContentType
   *        The MIME type to be send as the <code>Content-Type</code> header.
   *        May be <code>null</code>.
   * @param aContentBytes
   *        The main content to be send via POST. May not be <code>null</code>
   *        but maybe empty. The <code>Content-Length</code> HTTP header is
   *        automatically filled with the specified byte length.
   * @param aAdditionalHTTPHeaders
   *        An optional map of HTTP headers to be send. This map should not
   *        contain the <code>Content-Type</code> and the
   *        <code>Content-Length</code> headers. May be <code>null</code>.
   * @param aConnectionModifier
   *        An optional callback object to modify the URLConnection before it is
   *        opened.
   * @param aExceptionHolder
   *        An optional exception holder for further outside investigation.
   * @return <code>null</code> if the input stream could not be opened.
   */
  @Nullable
  public static InputStream postAndGetInputStream (@Nonnull final URL aURL,
                                                   final int nConnectTimeoutMS,
                                                   final int nReadTimeoutMS,
                                                   @Nullable final IMimeType aContentType,
                                                   @Nonnull final byte [] aContentBytes,
                                                   @Nullable final Map <String, String> aAdditionalHTTPHeaders,
                                                   @Nullable final INonThrowingRunnableWithParameter <URLConnection> aConnectionModifier,
                                                   @Nullable final IWrapper <IOException> aExceptionHolder)
  {
    ValueEnforcer.notNull (aURL, "URL");
    ValueEnforcer.notNull (aContentBytes, "ContentBytes");

    final Wrapper <OutputStream> aOpenedOS = new Wrapper <OutputStream> ();
    final INonThrowingRunnableWithParameter <URLConnection> aPOSTModifier = new INonThrowingRunnableWithParameter <URLConnection> ()
    {
      public void run (@Nonnull final URLConnection aURLConnection)
      {
        final HttpURLConnection aHTTPURLConnection = (HttpURLConnection) aURLConnection;
        try
        {
          aHTTPURLConnection.setRequestMethod ("POST");
          aHTTPURLConnection.setDoInput (true);
          aHTTPURLConnection.setDoOutput (true);
          if (aContentType != null)
            aHTTPURLConnection.setRequestProperty ("Content-Type", aContentType.getAsString ());
          aHTTPURLConnection.setRequestProperty ("Content-Length", Integer.toString (aContentBytes.length));
          if (aAdditionalHTTPHeaders != null)
            for (final Map.Entry <String, String> aEntry : aAdditionalHTTPHeaders.entrySet ())
              aHTTPURLConnection.setRequestProperty (aEntry.getKey (), aEntry.getValue ());

          final OutputStream aOS = aHTTPURLConnection.getOutputStream ();
          aOpenedOS.set (aOS);
          aOS.write (aContentBytes);
          aOS.flush ();
        }
        catch (final IOException ex)
        {
          throw new IllegalStateException ("Failed to POST data to " + aURL.toExternalForm (), ex);
        }

        // Run provided modifier (if any)
        if (aConnectionModifier != null)
          aConnectionModifier.run (aURLConnection);
      }
    };

    try
    {
      return getInputStream (aURL, nConnectTimeoutMS, nReadTimeoutMS, aPOSTModifier, aExceptionHolder);
    }
    finally
    {
      // Close the OutputStream opened for POSTing
      StreamUtils.close (aOpenedOS.get ());
    }
  }

  /**
   * Create a parameter string suitable for POST body (e.g. for web form
   * submission).
   *
   * @param aParams
   *        Parameter map. May be <code>null</code> or empty.
   * @param aParameterEncoder
   *        The encoder to be used to encode parameter names and parameter
   *        values. May not be <code>null</code>. This may be e.g. a
   *        {@link URLParameterEncoder}.
   * @return A non-<code>null</code> string
   */
  @Nonnull
  public static String getApplicationFormEncoded (@Nullable final Map <String, String> aParams,
                                                  @Nonnull final IEncoder <String> aParameterEncoder)
  {
    ValueEnforcer.notNull (aParameterEncoder, "ParameterEncoder");

    if (ContainerHelper.isEmpty (aParams))
      return "";

    final StringBuilder aSB = new StringBuilder ();
    if (aParams != null)
      for (final Map.Entry <String, String> aEntry : aParams.entrySet ())
      {
        // Separator
        if (aSB.length () > 0)
          aSB.append (AMPERSAND);

        // Key
        final String sKey = aEntry.getKey ();
        aSB.append (aParameterEncoder.encode (sKey));

        // Value
        final String sValue = aEntry.getValue ();
        if (StringHelper.hasText (sValue))
          aSB.append (EQUALS).append (aParameterEncoder.encode (sValue));
      }
    return aSB.toString ();
  }
}
