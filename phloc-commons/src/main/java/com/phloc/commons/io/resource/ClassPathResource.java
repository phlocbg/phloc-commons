/**
 * Copyright (C) 2006-2015 phloc systems
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Wraps a class path object as a readable resource.
 * 
 * @author Philip Helger
 */
@Immutable
public class ClassPathResource implements IReadableResource
{
  /** Use this prefix to uniquely identify classpath resources */
  public static final String CLASSPATH_PREFIX_LONG = "classpath:";
  /** Use this prefix to uniquely identify classpath resources - alternative */
  public static final String CLASSPATH_PREFIX_SHORT = "cp:";

  private String m_sPath;
  private final WeakReference <ClassLoader> m_aClassLoader;
  private boolean m_bURLResolved = false;
  private URL m_aURL;

  /**
   * Create a new class path resource, using the specified URL. Class loader
   * handling is automatic.
   * 
   * @param aURL
   *        The URL to be used. May not be <code>null</code>.
   */
  public ClassPathResource (@Nonnull final URL aURL)
  {
    this (aURL, null);
  }

  /**
   * Create a new class path resource using the specified URL and class loader
   * (optional).
   * 
   * @param aURL
   *        The URL to be used. May not be <code>null</code>.
   * @param aClassLoader
   *        The class loader to use. May be <code>null</code> indicating that
   *        automatic class loader handling should be applied.
   */
  public ClassPathResource (@Nonnull final URL aURL, @Nullable final ClassLoader aClassLoader)
  {
    this (aURL.toExternalForm (), aClassLoader);
  }

  /**
   * Create a new class path resource, using the specified path. Class loader
   * handling is automatic.
   * 
   * @param sPath
   *        The path to be used. May neither be <code>null</code> nor empty.
   */
  public ClassPathResource (@Nonnull @Nonempty final String sPath)
  {
    this (sPath, null);
  }

  /**
   * Create a new class path resource using the specified path and class loader
   * (optional).
   * 
   * @param sPath
   *        The path to be used. May neither be <code>null</code> nor empty.
   * @param aClassLoader
   *        The class loader to use. May be <code>null</code> indicating that
   *        automatic class loader handling should be applied.
   */
  public ClassPathResource (@Nonnull @Nonempty final String sPath, @Nullable final ClassLoader aClassLoader)
  {
    ValueEnforcer.notEmpty (sPath, "Path");

    m_sPath = getWithoutClassPathPrefix (sPath);

    // In case something was cut...
    if (StringHelper.hasNoText (m_sPath))
      throw new IllegalArgumentException ("No path specified after prefix: " + sPath);

    // Ensure the ClassLoader can be garbage collected if necessary
    m_aClassLoader = aClassLoader == null ? null : new WeakReference <ClassLoader> (aClassLoader);
  }

  private void writeObject (@Nonnull final ObjectOutputStream aOOS) throws IOException
  {
    if (m_aClassLoader != null)
      throw new IOException ("Cannot serialize a ClassPathResource that has a specific ClassLoader!");
    aOOS.writeUTF (m_sPath);
    // Don't write the rest! After serialization the URL must be resolved again!
  }

  private void readObject (@Nonnull final ObjectInputStream aOIS) throws IOException
  {
    m_sPath = aOIS.readUTF ();
  }

  @Nullable
  public static String getWithoutClassPathPrefix (@Nullable final String sPath)
  {
    if (StringHelper.startsWith (sPath, CLASSPATH_PREFIX_LONG))
      return sPath.substring (CLASSPATH_PREFIX_LONG.length ());
    if (StringHelper.startsWith (sPath, CLASSPATH_PREFIX_SHORT))
      return sPath.substring (CLASSPATH_PREFIX_SHORT.length ());
    return sPath;
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

  @Nullable
  private ClassLoader _getSpecifiedClassLoader ()
  {
    return m_aClassLoader == null ? null : m_aClassLoader.get ();
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
  private static InputStream _asInputStream (@Nullable final URL aURL)
  {
    return aURL == null ? null : URLResource.getInputStream (aURL);
  }

  /**
   * Get the input stream for the specified path using automatic class loader
   * handling. The class loaders are iterated in the following order:
   * <ol>
   * <li>Default class loader (usually the context class loader)</li>
   * <li>The class loader of this class</li>
   * <li>The system class loader</li>
   * </ol>
   * 
   * @param sPath
   *        The path to be resolved. May neither be <code>null</code> nor empty.
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  public static InputStream getInputStream (@Nonnull @Nonempty final String sPath)
  {
    final URL aURL = getAsURL (sPath);
    return _asInputStream (aURL);
  }

  /**
   * Get the input stream of the passed resource using the specified class
   * loader only.
   * 
   * @param sPath
   *        The path to be resolved. May neither be <code>null</code> nor empty.
   * @param aClassLoader
   *        The class loader to be used. May not be <code>null</code>.
   * @return <code>null</code> if the path could not be resolved using the
   *         specified class loader.
   */
  @Nullable
  public static InputStream getInputStream (@Nonnull @Nonempty final String sPath,
                                            @Nonnull final ClassLoader aClassLoader)
  {
    final URL aURL = getAsURL (sPath, aClassLoader);
    return _asInputStream (aURL);
  }

  /**
   * Get the input stream for the specified path using automatic class loader
   * handling. If no class loader was specified in the constructor, the class
   * loaders are iterated in the following order:
   * <ol>
   * <li>Default class loader (usually the context class loader)</li>
   * <li>The class loader of this class</li>
   * <li>The system class loader</li>
   * </ol>
   * 
   * @return <code>null</code> if no such resource exists.
   */
  @Nullable
  public InputStream getInputStream ()
  {
    final URL aURL = getAsURL ();
    return _asInputStream (aURL);
  }

  /**
   * Get the input stream to the this resource, using the passed class loader
   * only.
   * 
   * @param aClassLoader
   *        The class loader to be used. May not be <code>null</code>.
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  public InputStream getInputStreamNoCache (@Nonnull final ClassLoader aClassLoader)
  {
    final URL aURL = getAsURLNoCache (aClassLoader);
    return _asInputStream (aURL);
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

  /**
   * Create a {@link Reader} of this resource, using the specified class loader
   * only.
   * 
   * @param aClassLoader
   *        The class loader to be used. May not be <code>null</code>.
   * @param sCharset
   *        The charset to be used for the {@link Reader}. May not be
   *        <code>null</code>.
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  @Deprecated
  public Reader getReaderNoCache (@Nonnull final ClassLoader aClassLoader, @Nonnull final String sCharset)
  {
    return StreamUtils.createReader (getInputStreamNoCache (aClassLoader), sCharset);
  }

  /**
   * Create a {@link Reader} of this resource, using the specified class loader
   * only.
   * 
   * @param aClassLoader
   *        The class loader to be used. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used for the {@link Reader}. May not be
   *        <code>null</code>.
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  public Reader getReaderNoCache (@Nonnull final ClassLoader aClassLoader, @Nonnull final Charset aCharset)
  {
    return StreamUtils.createReader (getInputStreamNoCache (aClassLoader), aCharset);
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

  /**
   * Get the URL for the specified path using automatic class loader handling.
   * The class loaders are iterated in the following order:
   * <ol>
   * <li>Default class loader (usually the context class loader)</li>
   * <li>The class loader of this class</li>
   * <li>The system class loader</li>
   * </ol>
   * 
   * @param sPath
   *        The path to be resolved. May neither be <code>null</code> nor empty.
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  public static URL getAsURL (@Nonnull @Nonempty final String sPath)
  {
    ValueEnforcer.notEmpty (sPath, "Path");

    // Ensure the path starts with a "/"
    final String sRealPath = sPath.startsWith ("/") ? sPath : '/' + sPath;

    // Use the default class loader. Returns null if not found
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

  /**
   * Get the input stream of the passed resource using the specified class
   * loader only.
   * 
   * @param sPath
   *        The path to be resolved. May neither be <code>null</code> nor empty.
   * @param aClassLoader
   *        The class loader to be used. May not be <code>null</code>.
   * @return <code>null</code> if the path could not be resolved using the
   *         specified class loader.
   */
  @Nullable
  public static URL getAsURL (@Nonnull @Nonempty final String sPath, @Nonnull final ClassLoader aClassLoader)
  {
    ValueEnforcer.notNull (aClassLoader, "ClassLoader");
    ValueEnforcer.notEmpty (sPath, "Path");

    // Ensure the path starts with a "/"
    final String sRealPath = sPath.startsWith ("/") ? sPath : '/' + sPath;

    // returns null if not found
    return aClassLoader.getResource (sRealPath);
  }

  @Nullable
  public URL getAsURL ()
  {
    if (!m_bURLResolved)
    {
      // Is a specific class loader defined?
      final ClassLoader aClassLoader = _getSpecifiedClassLoader ();
      m_aURL = aClassLoader == null ? getAsURL (m_sPath) : getAsURL (m_sPath, aClassLoader);

      // Remember that we tried to resolve the URL - avoid retry
      m_bURLResolved = true;
    }
    return m_aURL;
  }

  /**
   * Convert the path to a URL without using caching. Otherwise the resolution
   * of {@link #getAsURL()} using the constructor supplied class loader would
   * possibly contradict with this resolution.
   * 
   * @param aClassLoader
   *        The class loader to be used. May not be <code>null</code>.
   * @return <code>null</code> if the path could not be resolved to a URL
   */
  @Nullable
  public URL getAsURLNoCache (@Nonnull final ClassLoader aClassLoader)
  {
    return getAsURL (m_sPath, aClassLoader);
  }

  @Nullable
  private static File _getAsFile (@Nullable final URL aURL)
  {
    if (aURL != null)
      try
      {
        return URLResource.getAsFile (aURL);
      }
      catch (final IllegalArgumentException ex)
      {
        // Happens for non-file URLs
      }
    return null;
  }

  /**
   * Get the file for the specified path using automatic class loader handling.
   * The class loaders are iterated in the following order:
   * <ol>
   * <li>Default class loader (usually the context class loader)</li>
   * <li>The class loader of this class</li>
   * <li>The system class loader</li>
   * </ol>
   * 
   * @param sPath
   *        The path to be resolved. May neither be <code>null</code> nor empty.
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  public static File getAsFile (@Nonnull @Nonempty final String sPath)
  {
    final URL aURL = getAsURL (sPath);
    return _getAsFile (aURL);
  }

  @Nullable
  public static File getAsFile (@Nonnull @Nonempty final String sPath, @Nonnull final ClassLoader aClassLoader)
  {
    final URL aURL = getAsURL (sPath, aClassLoader);
    return _getAsFile (aURL);
  }

  @Nullable
  public File getAsFile ()
  {
    // Try to use the cached URL here - avoid double resolution
    final URL aURL = getAsURL ();
    return _getAsFile (aURL);
  }

  @Nullable
  public File getAsFileNoCache (@Nonnull final ClassLoader aClassLoader)
  {
    final URL aURL = getAsURLNoCache (aClassLoader);
    return _getAsFile (aURL);
  }

  public static boolean canRead (@Nonnull @Nonempty final String sPath)
  {
    return getAsURL (sPath) != null;
  }

  public static boolean canRead (@Nonnull @Nonempty final String sPath, @Nonnull final ClassLoader aClassLoader)
  {
    return getAsURL (sPath, aClassLoader) != null;
  }

  public boolean canRead ()
  {
    return getAsURL () != null;
  }

  public boolean canReadNoCache (@Nonnull final ClassLoader aClassLoader)
  {
    return getAsURLNoCache (aClassLoader) != null;
  }

  @Nonnull
  public ClassPathResource getReadableCloneForPath (@Nonnull final String sPath)
  {
    return new ClassPathResource (sPath, _getSpecifiedClassLoader ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ClassPathResource))
      return false;
    final ClassPathResource rhs = (ClassPathResource) o;
    // URL and URLresolved are state variables
    return EqualsUtils.equals (m_sPath, rhs.m_sPath) &&
           EqualsUtils.equals (_getSpecifiedClassLoader (), rhs._getSpecifiedClassLoader ());
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPath).append (m_aClassLoader).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("cpPath", m_sPath)
                                       .appendIfNotNull ("classLoader", _getSpecifiedClassLoader ())
                                       .append ("urlResolved", m_bURLResolved)
                                       .append ("URL", m_aURL)
                                       .toString ();
  }
}
