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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IReadableResourceProvider;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Simple resource provider that only uses files.
 * 
 * @author Philip Helger
 */
@Immutable
public final class ClassPathResourceProvider implements IReadableResourceProvider
{
  private final String m_sPrefix;

  public ClassPathResourceProvider ()
  {
    this (null);
  }

  public ClassPathResourceProvider (@Nullable final String sPrefix)
  {
    m_sPrefix = sPrefix;
  }

  public boolean supportsReading (@Nullable final String sName)
  {
    // Class path resource supports all paths
    return StringHelper.hasText (sName);
  }

  @Nonnull
  public IReadableResource getReadableResource (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("name");

    return new ClassPathResource (m_sPrefix == null ? sName : m_sPrefix + sName);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ClassPathResourceProvider))
      return false;
    final ClassPathResourceProvider rhs = (ClassPathResourceProvider) o;
    return EqualsUtils.equals (m_sPrefix, rhs.m_sPrefix);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPrefix).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("prefix", m_sPrefix).toString ();
  }
}
