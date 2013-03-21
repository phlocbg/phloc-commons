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
package com.phloc.commons.xml.schema;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.phloc.commons.annotations.IsLocked;
import com.phloc.commons.annotations.IsLocked.ELockType;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.cache.AbstractNotifyingCache;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.sax.LoggingSAXErrorHandler;
import com.phloc.commons.xml.transform.TransformSourceFactory;

/**
 * Abstract base class for caching JAXP validation scheme elements.
 * 
 * @author philip
 */
@ThreadSafe
public abstract class DefaultSchemaCache extends AbstractNotifyingCache <List <? extends IReadableResource>, Schema>
{
  private final String m_sSchemaTypeName;
  private final SchemaFactory m_aSchemaFactory;

  public DefaultSchemaCache (@Nonnull final String sSchemaTypeName, @Nonnull final SchemaFactory aSchemaFactory)
  {
    super (DefaultSchemaCache.class.getName () + "$" + sSchemaTypeName);
    if (aSchemaFactory == null)
      throw new NullPointerException ("SchemaFactory");
    m_sSchemaTypeName = sSchemaTypeName;
    m_aSchemaFactory = aSchemaFactory;
  }

  @Override
  @Nonnull
  @IsLocked (ELockType.WRITE)
  protected Schema getValueToCache (@Nullable final List <? extends IReadableResource> aKey)
  {
    if (ContainerHelper.isEmpty (aKey))
      throw new IllegalArgumentException ("No resources provided!");

    // Collect all sources
    final Source [] aSources = new Source [aKey.size ()];
    for (int i = 0; i < aKey.size (); ++i)
      aSources[i] = TransformSourceFactory.create (aKey.get (i));

    try
    {
      final Schema ret = m_aSchemaFactory.newSchema (aSources);
      if (ret == null)
        throw new IllegalStateException ("Failed to create " +
                                         m_sSchemaTypeName +
                                         " schema from " +
                                         Arrays.toString (aSources));
      return ret;
    }
    catch (final SAXException ex)
    {
      throw new IllegalArgumentException ("Failed to parse " +
                                          m_sSchemaTypeName +
                                          " from " +
                                          Arrays.toString (aSources), ex);
    }
  }

  @Nonnull
  public final Schema getSchema (@Nonnull final IReadableResource aResource)
  {
    if (aResource == null)
      throw new NullPointerException ("resources");

    return getValueToCache (ContainerHelper.newList (aResource));
  }

  @Nonnull
  public final Schema getSchema (@Nonnull @Nonempty final IReadableResource... aResources)
  {
    if (ArrayHelper.isEmpty (aResources))
      throw new IllegalArgumentException ("no resources provided!");
    if (ArrayHelper.containsAnyNullElement (aResources))
      throw new IllegalArgumentException ("At leaste one resource is null!");

    return getValueToCache (ContainerHelper.newList (aResources));
  }

  @Nonnull
  public final Schema getSchema (@Nonnull @Nonempty final List <? extends IReadableResource> aResources)
  {
    if (ContainerHelper.isEmpty (aResources))
      throw new IllegalArgumentException ("no resources provided!");
    if (ContainerHelper.containsAnyNullElement (aResources))
      throw new IllegalArgumentException ("At leaste one resource is null!");

    return getValueToCache (ContainerHelper.newList (aResources));
  }

  @Nonnull
  public EChange removeFromCache (@Nullable final IReadableResource aKey)
  {
    return removeFromCache (ContainerHelper.newList (aKey));
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("schemaTypeName", m_sSchemaTypeName).toString ();
  }

  /**
   * Utility method to get the validator for a given schema using the standard
   * logging error handler.
   * 
   * @param aSchema
   *        The schema for which the validator is to be retrieved. May not be
   *        <code>null</code>.
   * @return The validator and never <code>null</code>.
   */
  @Nonnull
  public static final Validator getValidatorFromSchema (@Nonnull final Schema aSchema)
  {
    if (aSchema == null)
      throw new NullPointerException ("schema");

    final Validator aValidator = aSchema.newValidator ();
    aValidator.setErrorHandler (LoggingSAXErrorHandler.getInstance ());
    return aValidator;
  }
}
