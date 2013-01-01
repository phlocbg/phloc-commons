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
package com.phloc.commons.io.resourceprovider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResourceProvider;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.io.IWritableResourceProvider;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public class WritableResourceProviderChain extends ReadableResourceProviderChain implements IWritableResourceProvider
{
  protected final List <IWritableResourceProvider> m_aWritableResourceProviders = new ArrayList <IWritableResourceProvider> ();

  public WritableResourceProviderChain (@Nonnull final IReadableResourceProvider... aResProviders)
  {
    super (aResProviders);

    for (final IReadableResourceProvider aResProvider : aResProviders)
      if (aResProvider instanceof IWritableResourceProvider)
        m_aWritableResourceProviders.add ((IWritableResourceProvider) aResProvider);
    if (m_aWritableResourceProviders.isEmpty ())
      throw new IllegalArgumentException ("No writable resource provider passed - use a ReadableResourceProviderChain");
  }

  public final boolean supportsWriting (@Nullable final String sName)
  {
    // Check if any provider can handle this resource
    for (final IWritableResourceProvider aResProvider : m_aWritableResourceProviders)
      if (aResProvider.supportsWriting (sName))
        return true;
    return false;
  }

  @Nonnull
  @OverrideOnDemand
  public IWritableResource getWritableResource (final String sName)
  {
    // Use the first resource provider that supports the name
    for (final IWritableResourceProvider aResProvider : m_aWritableResourceProviders)
      if (aResProvider.supportsWriting (sName))
        return aResProvider.getWritableResource (sName);
    throw new IllegalArgumentException ("Cannot handle writing '" + sName + "' by " + m_aReadingResourceProviders);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final WritableResourceProviderChain rhs = (WritableResourceProviderChain) o;
    return m_aWritableResourceProviders.equals (rhs.m_aWritableResourceProviders);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aWritableResourceProviders).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("writableResProviders", m_aWritableResourceProviders)
                            .toString ();
  }
}
