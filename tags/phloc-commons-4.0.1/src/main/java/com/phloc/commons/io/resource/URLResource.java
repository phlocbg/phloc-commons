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
package com.phloc.commons.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mutable.IWrapper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.URLUtils;

/**
 * Implementation of the {@link IReadableResource} interface for URL objects.
 * 
 * @author philip
 */
@Immutable
public final class URLResource implements IReadableResource
{
  /** The protocol for file resources */
  public static final String PROTOCOL_FILE = "file";
  private static final Logger s_aLogger = LoggerFactory.getLogger (URLResource.class);
  private final URL m_aURL;

  public URLResource (@Nonnull final ISimpleURL aURL) throws MalformedURLException
  {
    this (aURL.getAsString ());
  }

  public URLResource (@Nonnull final String sURL) throws MalformedURLException
  {
    this (new URL (sURL));
  }

  public URLResource (@Nonnull final URI aURI) throws MalformedURLException
  {
    this (aURI.toURL ());
  }

  public URLResource (@Nonnull final URL aURL)
  {
    if (aURL == null)
      throw new NullPointerException ("URL");
    m_aURL = aURL;
  }

  /**
   * Check if the passed resource name is an explicit URL resource.
   * 
   * @param sName
   *        The name to check. May be <code>null</code>.
   * @return <code>true</code> if the passed name is an explicit URL resource.
   */
  public static boolean isExplicitURLResource (@Nullable final String sName)
  {
    return URLUtils.getAsURL (sName) != null;
  }

  @Nonnull
  public String getResourceID ()
  {
    return getPath ();
  }

  @Nonnull
  public String getPath ()
  {
    return m_aURL.toExternalForm ();
  }

  @Nullable
  public static InputStream getInputStream (@Nonnull final URL aURL)
  {
    return getInputStream (aURL, (Map <String, String>) null, (IWrapper <IOException>) null);
  }

  @Nullable
  public static InputStream getInputStream (@Nonnull final URL aURL,
                                            @Nullable final Map <String, String> aRequestProperties,
                                            @Nullable final IWrapper <IOException> aExceptionHolder)
  {
    if (aURL == null)
      throw new NullPointerException ("URL");

    URLConnection aConnection = null;
    try
    {
      aConnection = aURL.openConnection ();
      aConnection.setUseCaches (false);

      // Apply all request properties
      if (aRequestProperties != null)
        for (final Map.Entry <String, String> aEntry : aRequestProperties.entrySet ())
          aConnection.setRequestProperty (aEntry.getKey (), aEntry.getValue ());

      // by default follow-redirects is true for HTTPUrlConnections
      return aConnection.getInputStream ();
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
        s_aLogger.warn ("Failed to open input stream for '" +
                        aURL +
                        "': " +
                        ex.getClass ().getName () +
                        " - " +
                        ex.getMessage ());
      }

      if (aConnection instanceof HttpURLConnection)
      {
        // Read error completely for keep-alive (see
        // http://docs.oracle.com/javase/6/docs/technotes/guides/net/http-keepalive.html)
        InputStream aErrorIS = null;
        try
        {
          aErrorIS = ((HttpURLConnection) aConnection).getErrorStream ();
          if (aErrorIS != null)
          {
            final byte [] aBuf = new byte [1024];
            // read the response body
            while (aErrorIS.read (aBuf) > 0)
            {}
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
      return null;
    }
  }

  @Nullable
  public InputStream getInputStream ()
  {
    return getInputStream (m_aURL);
  }

  @Nullable
  public InputStream getInputStream (@Nullable final Map <String, String> aRequestProperties)
  {
    return getInputStream (m_aURL, aRequestProperties, (IWrapper <IOException>) null);
  }

  @Nullable
  public InputStream getInputStream (@Nullable final IWrapper <IOException> aExceptionHolder)
  {
    return getInputStream (m_aURL, (Map <String, String>) null, aExceptionHolder);
  }

  @Nullable
  public InputStream getInputStream (@Nullable final Map <String, String> aRequestProperties,
                                     @Nullable final IWrapper <IOException> aExceptionHolder)
  {
    return getInputStream (m_aURL, aRequestProperties, aExceptionHolder);
  }

  @Nullable
  @Deprecated
  public Reader getReader (@Nonnull final String sCharset)
  {
    return StreamUtils.createReader (getInputStream (), sCharset);
  }

  @Nullable
  public Reader getReader (@Nonnull final Charset aCharset)
  {
    return StreamUtils.createReader (getInputStream (), aCharset);
  }

  public boolean exists ()
  {
    // 1. as file
    if (PROTOCOL_FILE.equals (m_aURL.getProtocol ()))
      return getAsFile ().exists ();

    // Not a file URL
    InputStream aIS = null;
    try
    {
      // 2. as stream
      aIS = getInputStream ();
      return aIS != null;
    }
    catch (final Exception ex)
    {
      // 3. no
      return false;
    }
    finally
    {
      StreamUtils.close (aIS);
    }
  }

  @Nonnull
  public URL getAsURL ()
  {
    return m_aURL;
  }

  @Nullable
  public URI getAsURI ()
  {
    return URLUtils.getAsURI (m_aURL);
  }

  @Nonnull
  public static File getAsFile (@Nonnull final URL aURL)
  {
    if (aURL == null)
      throw new NullPointerException ("URL");
    if (!PROTOCOL_FILE.equals (aURL.getProtocol ()))
      throw new IllegalArgumentException ("Not a file URL: " + aURL);

    File aFile;
    try
    {
      aFile = new File (aURL.toURI ().getSchemeSpecificPart ());
    }
    catch (final URISyntaxException ex)
    {
      // Fallback for URLs that are not valid URIs
      aFile = new File (aURL.getPath ());
    }

    // This file may be non-existing
    return aFile;
  }

  @Nonnull
  public File getAsFile ()
  {
    return getAsFile (m_aURL);
  }

  @Nonnull
  public URLResource getReadableCloneForPath (@Nonnull final URL aURL)
  {
    return new URLResource (aURL);
  }

  @Nonnull
  public URLResource getReadableCloneForPath (@Nonnull final String sPath)
  {
    try
    {
      return new URLResource (sPath);
    }
    catch (final MalformedURLException ex)
    {
      throw new IllegalArgumentException ("Cannot convert to an URL: " + sPath, ex);
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof URLResource))
      return false;
    final URLResource rhs = (URLResource) o;
    return EqualsUtils.equals (m_aURL, rhs.m_aURL);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aURL).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("url", m_aURL).toString ();
  }
}
