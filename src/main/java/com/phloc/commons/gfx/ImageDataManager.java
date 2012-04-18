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
package com.phloc.commons.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.LRUCache;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.stats.IStatisticsHandlerCache;
import com.phloc.commons.stats.StatisticsManager;

/**
 * This service class is used to cache information about images. It is used to
 * set the HTML attributes width and height for images. It has an internal cache
 * to avoid querying the data every time.
 * 
 * @author philip
 */
@ThreadSafe
public final class ImageDataManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ImageDataManager.class);
  private static final IStatisticsHandlerCache s_aStatsHdl = StatisticsManager.getCacheHandler (ImageDataManager.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <IReadableResource, ScalableSize> s_aImageData = new LRUCache <IReadableResource, ScalableSize> (1000);
  private static final Set <IReadableResource> s_aNonExistingResources = new HashSet <IReadableResource> ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ImageDataManager s_aInstance = new ImageDataManager ();

  private ImageDataManager ()
  {}

  @Nullable
  private static ScalableSize _readImageData (@Nonnull final IInputStreamProvider aRes)
  {
    ScalableSize aData = null;
    InputStream aIS = null;
    try
    {
      aIS = aRes.getInputStream ();
      if (aIS != null)
      {
        // Returned image may be null in case it is not a valid image!
        // Happens e.g. if a file with the extension ".png" is not a PNG image
        final BufferedImage aImage = ImageIO.read (aIS);
        if (aImage != null)
          aData = new ScalableSize (aImage.getWidth (), aImage.getHeight ());
        else
          s_aLogger.warn ("Does not seem to be an image resource: " + aRes);
      }
      else
        s_aLogger.warn ("Failed to resolve image resource: " + aRes);
    }
    catch (final UnsatisfiedLinkError ex)
    {
      // Happened on Ubuntu where library libmawt.so is not present
      s_aLogger.error ("Seems like no AWT binding is present", ex);
    }
    catch (final NoClassDefFoundError ex)
    {
      // Happened on Ubuntu where library libmawt.so is not present in a
      // follow-up call after the java.lang.UnsatisfiedLinkError error
      // occurred
      s_aLogger.error ("Seems like no AWT binding is present", ex);
    }
    catch (final IIOException ex)
    {
      s_aLogger.error ("Failed to interprete image data from resource " + aRes + ": " + ex.getMessage ());
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Failed to read image data from resource " + aRes, ex);
    }
    catch (final AccessControlException ex)
    {
      // can be thrown by f.exist!
      s_aLogger.error ("Whatsoever on " + aRes, ex);
    }
    catch (final IllegalArgumentException ex)
    {
      // can be thrown by the BMP reader :)
      s_aLogger.error ("Failed to read image data from resource " + aRes + ": " + ex.getMessage ());
    }
    finally
    {
      // ImageIO.read doesn't close the stream!
      StreamUtils.close (aIS);
    }
    return aData;
  }

  @Nullable
  public static ScalableSize getImageSize (@Nullable final IReadableResource aRes)
  {
    if (aRes == null)
      return null;

    /*
     * Use containsKey here instead of "get () != null" in case an image is
     * queried over and over but is not existing. The implementation inserts
     * null values for all elements that are invalid
     */
    s_aRWLock.readLock ().lock ();
    try
    {
      // Valid image data?
      final ScalableSize aData = s_aImageData.get (aRes);
      if (aData != null)
      {
        s_aStatsHdl.cacheHit ();
        return aData;
      }

      // Known non-existing image data?
      if (s_aNonExistingResources.contains (aRes))
      {
        s_aStatsHdl.cacheHit ();
        return null;
      }
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    // Main read data
    final ScalableSize aData = _readImageData (aRes);

    s_aRWLock.writeLock ().lock ();
    try
    {
      // In case the image is invalid (why-so-ever), remember a null value
      if (aData == null)
        s_aNonExistingResources.add (aRes);
      else
        s_aImageData.put (aRes, aData);
      s_aStatsHdl.cacheMiss ();
      return aData;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Remove a single resource from the cache.
   * 
   * @param aRes
   *        The resource to be removed. May be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static EChange clearCachedSize (@Nullable final IReadableResource aRes)
  {
    if (aRes != null)
    {
      s_aRWLock.writeLock ().lock ();
      try
      {
        // Existing resource?
        if (s_aImageData.remove (aRes) != null)
          return EChange.CHANGED;

        // Non-existing resource?
        if (s_aNonExistingResources.remove (aRes))
          return EChange.CHANGED;
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return EChange.UNCHANGED;
  }

  /**
   * Remove all cached elements
   * 
   * @return {@link EChange} - never null
   */
  @Nonnull
  public static EChange clearCache ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_aImageData.isEmpty () && s_aNonExistingResources.isEmpty ())
        return EChange.UNCHANGED;

      s_aImageData.clear ();
      s_aNonExistingResources.clear ();
      s_aLogger.info ("Cache was cleared: " + ImageDataManager.class.getName ());
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
