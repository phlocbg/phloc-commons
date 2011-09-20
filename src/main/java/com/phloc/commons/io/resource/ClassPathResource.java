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
package com.phloc.commons.io.resource;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Wraps a class path object as a readable resource.
 * 
 * @author philip
 */
@Immutable
public final class ClassPathResource implements IReadableResource
{
  /** Use this prefix to uniquely identify classpath resources */
  public static final String CLASSPATH_PREFIX_LONG = "classpath:";
  /** Use this prefix to uniquely identify classpath resources - alternative */
  public static final String CLASSPATH_PREFIX_SHORT = "cp:";

  private static final Logger s_aLogger = LoggerFactory.getLogger (ClassPathResource.class);

  private final String m_sPath;
  private boolean m_bURLResolved = false;
  private URL m_aURL;

  public ClassPathResource (@Nonnull @Nonempty final String sPath)
  {
    if (StringHelper.hasNoText (sPath))
      throw new IllegalArgumentException ("No path specified");

    if (StringHelper.startsWith (sPath, CLASSPATH_PREFIX_LONG))
      m_sPath = sPath.substring (CLASSPATH_PREFIX_LONG.length ());
    else
      if (StringHelper.startsWith (sPath, CLASSPATH_PREFIX_SHORT))
        m_sPath = sPath.substring (CLASSPATH_PREFIX_SHORT.length ());
      else
        m_sPath = sPath;

    // In case something was cut...
    if (StringHelper.hasNoText (m_sPath))
      throw new IllegalArgumentException ("No path specified after prefix: " + sPath);
  }

  /**
   * Check if the passed resource name is an explicit classpath resource. This
   * is the case, if the name starts either with {@link #CLASSPATH_PREFIX_LONG}
   * or {@link #CLASSPATH_PREFIX_SHORT}.
   * 
   * @param sName
   *        The name to check. May be <code>null</code>.
   * @return <code>true</code> if the passed name is not <code>null</code> and
   *         an explicit classpath resource.
   */
  public static boolean isExplicitClassPathResource (@Nullable final String sName)
  {
    return StringHelper.startsWith (sName, CLASSPATH_PREFIX_LONG) ||
           StringHelper.startsWith (sName, CLASSPATH_PREFIX_SHORT);
  }

  @Nonnull
  public String getResourceID ()
  {
    final URL aURL = getAsURL ();
    return aURL == null ? m_sPath : aURL.toExternalForm ();
  }

  @Nonnull
  @Nonempty
  public String getPath ()
  {
    return m_sPath;
  }

  @Nullable
  public static InputStream getInputStream (@Nonnull @Nonempty final String sPath)
  {
    if (StringHelper.hasNoText (sPath))
      throw new IllegalArgumentException ("No path specified");

    final String sRealPath = sPath.startsWith ("/") ? sPath : '/' + sPath;

    // returns null if not found
    InputStream ret = ClassHelper.getDefaultClassLoader ().getResourceAsStream (sRealPath);
    if (ret == null)
    {
      // This is essential if we're running as a web application!!!
      ret = ClassPathResource.class.getResourceAsStream (sRealPath);
      if (ret == null)
      {
        // this is a fix for a user that needed to have the application
        // loaded by the bootstrap classloader
        ret = ClassLoader.getSystemClassLoader ().getResourceAsStream (sRealPath);

        // Do NOT emit a warning here, because this class is used in logger
        // initialization
      }
    }
    return ret;
  }

  @Nullable
  public InputStream getInputStream ()
  {
    return getInputStream (m_sPath);
  }

  @Nullable
  public Reader getReader (@Nonnull final String sCharset)
  {
    return StreamUtils.createReader (getInputStream (), sCharset);
  }

  public boolean exists ()
  {
    // Uses a cached already resolved URL
    return getAsURL () != null;
  }

  public boolean existsNoCacheUsage ()
  {
    // Resolve the URL again
    return getAsURL (m_sPath) != null;
  }

  @Nullable
  public static URL getAsURL (@Nonnull @Nonempty final String sPath)
  {
    if (StringHelper.hasNoText (sPath))
      throw new IllegalArgumentException ("No path specified");

    final String sRealPath = sPath.startsWith ("/") ? sPath : '/' + sPath;

    // returns null if not found
    URL ret = ClassHelper.getDefaultClassLoader ().getResource (sRealPath);
    if (ret == null)
    {
      // This is essential if we're running as a web application!!!
      ret = ClassPathResource.class.getResource (sRealPath);
      if (ret == null)
      {
        // this is a fix for a user that needed to have the application
        // loaded by the bootstrap classloader
        ret = ClassLoader.getSystemClassLoader ().getResource (sRealPath);
      }
    }
    return ret;
  }

  @Nullable
  public URL getAsURL ()
  {
    if (!m_bURLResolved)
    {
      m_aURL = getAsURL (m_sPath);
      m_bURLResolved = true;
    }
    return m_aURL;
  }

  @Nullable
  private static File _asFile (@Nullable final URL aURL, final String sPath)
  {
    File aFile = aURL == null ? null : URLResource.getAsFile (aURL);
    if (aFile == null)
    {
      // Happens when reading CRM customers from a CSV file without the original
      // files existing
      s_aLogger.warn ("Failed to convert '" + sPath + "' to an URL - using direct File access");
      aFile = new File (sPath);
    }
    return aFile;
  }

  @Nullable
  public static File getAsFile (@Nonnull @Nonempty final String sPath)
  {
    final URL aURL = getAsURL (sPath);
    return _asFile (aURL, sPath);
  }

  @Nonnull
  public File getAsFile ()
  {
    // Try to use the cached URL here - avoid double resolution
    final URL aURL = getAsURL ();
    return _asFile (aURL, m_sPath);
  }

  public static boolean canRead (@Nonnull @Nonempty final String sPath)
  {
    return getAsURL (sPath) != null;
  }

  public boolean canRead ()
  {
    return getAsURL () != null;
  }

  @Nonnull
  public ClassPathResource getReadableCloneForPath (@Nonnull final String sPath)
  {
    return new ClassPathResource (sPath);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ClassPathResource))
      return false;
    final ClassPathResource rhs = (ClassPathResource) o;
    return m_sPath.equals (rhs.m_sPath);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPath).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("cpPath", m_sPath)
                                       .append ("urlResolved", m_bURLResolved)
                                       .appendIfNotNull ("URL", m_aURL)
                                       .toString ();
  }
}
