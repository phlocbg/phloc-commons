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
package com.phloc.commons.text.resource;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PropertyKey;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.system.EJavaVersion;

/**
 * Resource bundle utility methods
 * 
 * @author philip
 */
@Immutable
public final class ResourceBundleUtils
{
  private ResourceBundleUtils ()
  {}

  @Nullable
  public static ResourceBundle getResourceBundle (@Nonnull final String sBundleName,
                                                  @Nonnull final Locale aContentLocale)
  {
    return getResourceBundle (sBundleName, aContentLocale, ClassHelper.getDefaultClassLoader ());
  }

  @Nullable
  public static ResourceBundle getResourceBundle (@Nonnull final String sBundleName,
                                                  @Nonnull final Locale aContentLocale,
                                                  @Nonnull final ClassLoader aClassLoader)
  {
    try
    {
      return ResourceBundle.getBundle (sBundleName, aContentLocale, aClassLoader);
    }
    catch (final MissingResourceException ex)
    {
      return null;
    }
  }

  @Nullable
  public static ResourceBundle getUtf8ResourceBundle (@Nonnull final String sBundleName,
                                                      @Nonnull final Locale aContentLocale)
  {
    return getUtf8ResourceBundle (sBundleName, aContentLocale, ClassHelper.getDefaultClassLoader ());
  }

  @Nullable
  public static ResourceBundle getUtf8ResourceBundle (@Nonnull final String sBundleName,
                                                      @Nonnull final Locale aContentLocale,
                                                      @Nonnull final ClassLoader aClassLoader)
  {
    try
    {
      return Utf8ResourceBundle.getBundle (sBundleName, aContentLocale, aClassLoader);
    }
    catch (final MissingResourceException ex)
    {
      return null;
    }
  }

  @Nullable
  public static String getString (@Nullable final ResourceBundle aResourceBundle,
                                  @Nonnull @PropertyKey final String sKey)
  {
    if (aResourceBundle != null)
      try
      {
        return aResourceBundle.getString (sKey);
      }
      catch (final MissingResourceException ex)
      {
        // Fall through
      }
    return null;
  }

  @Nullable
  public static String getString (@Nonnull final String sBundleName,
                                  @Nonnull final Locale aContentLocale,
                                  @Nonnull @PropertyKey final String sKey)
  {
    return getString (getResourceBundle (sBundleName, aContentLocale), sKey);
  }

  @Nullable
  public static String getString (@Nonnull final String sBundleName,
                                  @Nonnull final Locale aContentLocale,
                                  @Nonnull @PropertyKey final String sKey,
                                  @Nonnull final ClassLoader aClassLoader)
  {
    return getString (getResourceBundle (sBundleName, aContentLocale, aClassLoader), sKey);
  }

  @Nullable
  public static String getUtf8String (@Nonnull final String sBundleName,
                                      @Nonnull final Locale aContentLocale,
                                      @Nonnull @PropertyKey final String sKey)
  {
    return getString (getUtf8ResourceBundle (sBundleName, aContentLocale), sKey);
  }

  @Nullable
  public static String getUtf8String (@Nonnull final String sBundleName,
                                      @Nonnull final Locale aContentLocale,
                                      @Nonnull @PropertyKey final String sKey,
                                      @Nonnull final ClassLoader aClassLoader)
  {
    return getString (getUtf8ResourceBundle (sBundleName, aContentLocale, aClassLoader), sKey);
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
