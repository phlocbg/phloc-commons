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
package com.phloc.commons.text.resource;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PropertyKey;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.system.EJavaVersion;

/**
 * The key of a resource bundle.
 * 
 * @author philip
 */
@Immutable
public final class ResourceBundleKey implements Serializable
{
  private final String m_sBundleName;
  private final String m_sKey;

  public ResourceBundleKey (@Nonnull @Nonempty final String sBundleName,
                            @Nonnull @Nonempty @PropertyKey final String sKey)
  {
    if (StringHelper.hasNoText (sBundleName))
      throw new IllegalArgumentException ("bundleName");
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key");

    m_sBundleName = sBundleName;
    m_sKey = sKey;
  }

  @Nonnull
  @Nonempty
  public String getBundleName ()
  {
    return m_sBundleName;
  }

  @Nonnull
  @Nonempty
  @PropertyKey
  public String getKey ()
  {
    return m_sKey;
  }

  @Nullable
  public String getString (@Nonnull final Locale aContentLocale)
  {
    return getString (m_sBundleName, aContentLocale, m_sKey);
  }

  @Nullable
  public String getString (@Nonnull final Locale aContentLocale, @Nonnull final ClassLoader aClassLoader)
  {
    return getString (m_sBundleName, aContentLocale, m_sKey, aClassLoader);
  }

  @Nullable
  public String getUtf8String (@Nonnull final Locale aContentLocale)
  {
    return getUtf8String (m_sBundleName, aContentLocale, m_sKey);
  }

  @Nullable
  public String getUtf8String (@Nonnull final Locale aContentLocale, @Nonnull final ClassLoader aClassLoader)
  {
    return getUtf8String (m_sBundleName, aContentLocale, m_sKey, aClassLoader);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ResourceBundleKey))
      return false;
    final ResourceBundleKey rhs = (ResourceBundleKey) o;
    return m_sBundleName.equals (rhs.m_sBundleName) && m_sKey.equals (rhs.m_sKey);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sBundleName).append (m_sKey).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("bundleName", m_sBundleName).append ("key", m_sKey).toString ();
  }

  @Nullable
  public static String getString (@Nonnull final String sBundleName,
                                  @Nonnull final Locale aContentLocale,
                                  @Nonnull @PropertyKey final String sKey)
  {
    return getString (sBundleName, aContentLocale, sKey, ClassHelper.getDefaultClassLoader ());
  }

  @Nullable
  public static String getString (@Nonnull final String sBundleName,
                                  @Nonnull final Locale aContentLocale,
                                  @Nonnull @PropertyKey final String sKey,
                                  @Nonnull final ClassLoader aClassLoader)
  {
    try
    {
      return ResourceBundle.getBundle (sBundleName, aContentLocale, aClassLoader).getString (sKey);
    }
    catch (final MissingResourceException ex)
    {
      return null;
    }
  }

  @Nullable
  public static String getUtf8String (@Nonnull final String sBundleName,
                                      @Nonnull final Locale aContentLocale,
                                      @Nonnull @PropertyKey final String sKey)
  {
    return getUtf8String (sBundleName, aContentLocale, sKey, ClassHelper.getDefaultClassLoader ());
  }

  @Nullable
  public static String getUtf8String (@Nonnull final String sBundleName,
                                      @Nonnull final Locale aContentLocale,
                                      @Nonnull @PropertyKey final String sKey,
                                      @Nonnull final ClassLoader aClassLoader)
  {
    try
    {
      return Utf8ResourceBundle.getBundle (sBundleName, aContentLocale, aClassLoader).getString (sKey);
    }
    catch (final MissingResourceException ex)
    {
      return null;
    }
  }

  /**
   * Clear the complete resource bundle cache using the default class loader!
   */
  public static void clearCache ()
  {
    clearCache (ClassHelper.getDefaultClassLoader ());
  }

  public static void clearCache (@Nonnull final ClassLoader aClassLoader)
  {
    // ResourceBundle.clearCache () is only available from Java >= 1.6
    if (EJavaVersion.JDK_16.isSupportedVersion ())
    {
      try
      {
        // Use the same classloader as in retrieval!
        GenericReflection.invokeStaticMethod (ResourceBundle.class, "clearCache", aClassLoader);
      }
      catch (final NoSuchMethodException ex)
      {
        // Failed
      }
      catch (final IllegalAccessException e)
      {
        // Failed
      }
      catch (final InvocationTargetException e)
      {
        // Failed
      }
    }
  }
}
