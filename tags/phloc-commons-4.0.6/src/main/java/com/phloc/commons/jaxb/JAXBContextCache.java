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
package com.phloc.commons.jaxb;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.IsLocked;
import com.phloc.commons.annotations.IsLocked.ELockType;
import com.phloc.commons.cache.AbstractNotifyingCache;

/**
 * Specific cache class for JAXB context elements. This is helpful, as the JAXB
 * context creation is a very time consuming task.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class JAXBContextCache extends AbstractNotifyingCache <Package, JAXBContext>
{
  private static final class SingletonHolder
  {
    static final JAXBContextCache s_aInstance = new JAXBContextCache ();
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (JAXBContextCache.class);

  private static boolean s_bDefaultInstantiated = false;

  private JAXBContextCache ()

  {
    super (JAXBContextCache.class.getName ());
  }

  public static boolean isInstantiated ()
  {
    return s_bDefaultInstantiated;
  }

  @Nonnull
  public static JAXBContextCache getInstance ()
  {
    s_bDefaultInstantiated = true;
    return SingletonHolder.s_aInstance;
  }

  @Override
  @Nullable
  @IsLocked (ELockType.WRITE)
  public JAXBContext getValueToCache (@Nullable final Package aPackage)
  {
    if (aPackage == null)
      return null;

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Creating JAXB context for package " + aPackage.getName ());

    try
    {
      if (aPackage.getAnnotation (XmlSchema.class) == null)
        s_aLogger.warn ("The package " + aPackage.getName () + " does not seem to be JAXB generated!");
      return JAXBContext.newInstance (aPackage.getName ());
    }
    catch (final JAXBException ex)
    {
      final String sMsg = "Failed to create JAXB context for package " + aPackage.getName ();
      s_aLogger.error (sMsg);
      throw new IllegalArgumentException (sMsg, ex);
    }
  }

  @Nullable
  public JAXBContext getFromCache (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    final Package aPackage = aClass.getPackage ();
    if (aPackage.getAnnotation (XmlSchema.class) == null)
    {
      // E.g. an internal class - try anyway!
      if (GlobalDebug.isDebugMode ())
        s_aLogger.info ("Creating JAXB context for class " + aClass.getName ());

      try
      {
        return JAXBContext.newInstance (aClass);
      }
      catch (final JAXBException ex)
      {
        final String sMsg = "Failed to create JAXB context for class " + aClass.getName ();
        s_aLogger.error (sMsg);
        throw new IllegalArgumentException (sMsg, ex);
      }
    }

    return getFromCache (aPackage);
  }
}
