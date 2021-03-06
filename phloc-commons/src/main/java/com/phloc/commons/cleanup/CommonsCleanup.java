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
package com.phloc.commons.cleanup;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.compare.CollatorUtils;
import com.phloc.commons.equals.EqualsImplementationRegistry;
import com.phloc.commons.gfx.ImageDataManager;
import com.phloc.commons.hash.HashCodeImplementationRegistry;
import com.phloc.commons.jaxb.JAXBContextCache;
import com.phloc.commons.lang.ClassHierarchyCache;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.locale.LocaleUtils;
import com.phloc.commons.locale.country.CountryCache;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.regex.RegExPool;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.text.resource.ResourceBundleUtils;
import com.phloc.commons.url.URLProtocolRegistry;
import com.phloc.commons.xml.schema.XMLSchemaCache;

/**
 * The sole purpose of this class to clear all caches, that reside in this
 * library.
 * 
 * @author Philip Helger
 */
@Immutable
public final class CommonsCleanup
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final CommonsCleanup s_aInstance = new CommonsCleanup ();

  private CommonsCleanup ()
  {}

  /**
   * Cleanup all custom caches contained in phloc-commons. Loaded SPI
   * implementations are not affected by this method!
   */
  public static void cleanup ()
  {
    // Reset caches to the default values
    LocaleCache.resetCache ();
    CountryCache.resetCache ();
    MimeTypeDeterminator.resetCache ();
    URLProtocolRegistry.reinitialize ();

    // Clear caches
    ImageDataManager.clearCache ();
    DefaultTextResolver.clearCache ();
    EnumHelper.clearCache ();
    ResourceBundleUtils.clearCache ();
    RegExPool.clearPatternCache ();
    CollatorUtils.clearCache ();
    LocaleUtils.clearCache ();
    if (JAXBContextCache.isInstantiated ())
      JAXBContextCache.getInstance ().clearCache ();
    if (XMLSchemaCache.isInstantiated ())
      XMLSchemaCache.getInstance ().clearCache ();
    StatisticsManager.clearCache ();
    EqualsImplementationRegistry.clearCache ();
    HashCodeImplementationRegistry.clearCache ();

    // Clean this one last as it is used in equals and hashCode implementations!
    ClassHierarchyCache.clearCache ();
  }
}
