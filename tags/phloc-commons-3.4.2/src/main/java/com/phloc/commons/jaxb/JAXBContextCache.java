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
import com.phloc.commons.cache.AbstractNotifyingCache;

/**
 * Specific cache class for JAXB context elements. This is helpful, as the JAXB
 * context creation is a very time consuming task.
 * 
 * @author philip
 */
@ThreadSafe
public final class JAXBContextCache extends AbstractNotifyingCache <Package, JAXBContext>
{
  private static final JAXBContextCache s_aInstance = new JAXBContextCache ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (JAXBContextCache.class);

  private JAXBContextCache ()
  {
    super (JAXBContextCache.class.getName ());
  }

  @Nonnull
  public static JAXBContextCache getInstance ()
  {
    return s_aInstance;
  }

  @Override
  @Nonnull
  protected JAXBContext getValueToCache (@Nonnull final Package aPackage)
  {
    if (aPackage == null)
      throw new NullPointerException ("package");

    if (GlobalDebug.isDebugMode () && s_aLogger.isInfoEnabled ())
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

    return getFromCache (aClass.getPackage ());
  }
}
