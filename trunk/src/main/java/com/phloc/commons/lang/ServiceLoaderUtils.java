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
package com.phloc.commons.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * {@link ServiceLoader} helper class.
 * 
 * @author boris
 * @author philip
 */
@Immutable
public final class ServiceLoaderUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ServiceLoaderUtils.class);

  private ServiceLoaderUtils ()
  {}

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPIClass
   *        The SPI interface class
   * @return A collection of all currently available plugins
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPIClass)
  {
    return getAllSPIImplementations (aSPIClass, ClassHelper.getDefaultClassLoader (), null);
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPIClass
   *        The SPI interface class
   * @param aClassLoader
   *        The class loader to use for the SPI loader
   * @return A collection of all currently available plugins
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPIClass,
                                                             @Nonnull final ClassLoader aClassLoader)
  {
    return getAllSPIImplementations (aSPIClass, aClassLoader, null);
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPIClass
   *        The SPI interface class
   * @param aLogger
   *        An optional logger to use (As this class cannot create it's own
   *        logger)
   * @return A collection of all currently available plugins
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPIClass,
                                                             @Nullable final Logger aLogger)
  {
    return getAllSPIImplementations (aSPIClass, ClassHelper.getDefaultClassLoader (), aLogger);
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPIClass
   *        The SPI interface class
   * @param aClassLoader
   *        The class loader to use for the SPI loader
   * @param aLogger
   *        An optional logger to use (As this class cannot create it's own
   *        logger)
   * @return A collection of all currently available plugins. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPIClass,
                                                             @Nonnull final ClassLoader aClassLoader,
                                                             @Nullable final Logger aLogger)
  {
    if (aSPIClass == null)
      throw new NullPointerException ("SPIClass");
    if (aClassLoader == null)
      throw new NullPointerException ("ClassLoader");

    final Logger aRealLogger = aLogger != null ? aLogger : s_aLogger;
    final ServiceLoader <T> aServiceLoader = ServiceLoader.<T> load (aSPIClass, aClassLoader);
    final List <T> ret = new ArrayList <T> ();

    // We use the iterator to be able to catch exceptions thrown
    // when loading SPI implementations (e.g. the SPI implementation class does
    // not exist)
    final Iterator <T> aIterator = aServiceLoader.iterator ();
    while (aIterator.hasNext ())
    {
      try
      {
        ret.add (aIterator.next ());
      }
      catch (final Throwable t)
      {
        aRealLogger.error ("Unable to load an SPI implementation of " + aSPIClass, t);
      }
    }

    if (aRealLogger.isDebugEnabled ())
      aRealLogger.debug ("Finished identifying all SPI implementations of " +
                         aSPIClass +
                         " --> returning " +
                         ret.size () +
                         " instances");
    return ret;
  }
}
