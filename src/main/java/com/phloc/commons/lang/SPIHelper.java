package com.phloc.commons.lang;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

public class SPIHelper
{
  private SPIHelper ()
  {
    // private
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPI
   *        The SPI interface class
   * @return A collection of all currently available plugins
   */
  @Nonnull
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPI)
  {
    return getAllSPIImplementations (aSPI, ClassHelper.getDefaultClassLoader ());
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPI
   *        The SPI interface class
   * @param aClassLoader
   *        The class loader to use for the SPI loader
   * @return A collection of all currently available plugins
   */
  @Nonnull
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPI,
                                                             @Nonnull final ClassLoader aClassLoader)
  {
    return getAllSPIImplementations (aSPI, aClassLoader, null);
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPI
   *        The SPI interface class
   * @param aLogger
   *        An optional logger to use (As this class cannot create it's own
   *        logger)
   * @return A collection of all currently available plugins
   */
  @Nonnull
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPI,
                                                             @Nullable final Logger aLogger)
  {
    return getAllSPIImplementations (aSPI, ClassHelper.getDefaultClassLoader (), aLogger);
  }

  /**
   * Uses the {@link ServiceLoader} to load all SPI implementations of the
   * passed class
   * 
   * @param aSPI
   *        The SPI interface class
   * @param aClassLoader
   *        The class loader to use for the SPI loader
   * @param aLogger
   *        An optional logger to use (As this class cannot create it's own
   *        logger)
   * @return A collection of all currently available plugins
   */
  @Nonnull
  public static <T> Collection <T> getAllSPIImplementations (@Nonnull final Class <T> aSPI,
                                                             @Nonnull final ClassLoader aClassLoader,
                                                             @Nullable final Logger aLogger)
  {
    final ServiceLoader <T> aServiceLoader = ServiceLoader.<T> load (aSPI, aClassLoader);
    final Collection <T> aImplementations = new HashSet <T> ();
    final Iterator <T> aIterator = aServiceLoader.iterator ();

    // ESCA-JAVA0254: We use the iterator to be able to catch exceptions thrown
    // when loading SPI
    // implementations (e.g. the SPI implementation class does not exist)
    while (aIterator.hasNext ())
    {
      // ESCA-JAVA0170:
      try
      {
        aImplementations.add (aIterator.next ());
      }
      catch (final ServiceConfigurationError e)
      {
        // For now just ignore the exceptions
        if (aLogger == null)
        {
          // ESCA-JAVA0267: If we cannot use a logger, we have to use system!
          System.err.println ("Unable to load SPI implementation: " + e.getMessage ()); //$NON-NLS-1$
        }
        else
        {
          aLogger.error ("Unable to load SPI implementation", e); //$NON-NLS-1$
        }
      }
      catch (final Exception e)
      {
        if (aLogger == null)
        {
          // ESCA-JAVA0267: If we cannot use a logger, we have to use system!
          System.err.println ("Unable to load SPI implementation: " + e.getMessage ()); //$NON-NLS-1$
        }
        else
        {
          aLogger.error ("Unable to load SPI implementation", e); //$NON-NLS-1$
        }
      }
    }
    if (aLogger != null)
    {
      aLogger.debug ("Finished identifying all SPI implementations --> returning"); //$NON-NLS-1$
    }
    return aImplementations;
  }
}
