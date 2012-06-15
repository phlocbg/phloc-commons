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
package com.phloc.commons.collections.attrs;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IReadonlyAttributeContainer} based on a
 * hash map. This implementation may carry <code>null</code> values but that is
 * not recommended.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MapBasedReadonlyAttributeContainer extends AbstractReadonlyAttributeContainer
{
  private final Map <String, Object> m_aAttrs;

  public MapBasedReadonlyAttributeContainer (@Nonnull final Map <String, Object> aMap)
  {
    if (aMap == null)
      throw new NullPointerException ("map");
    m_aAttrs = new HashMap <String, Object> (aMap);
  }

  public boolean containsAttribute (final String name)
  {
    return m_aAttrs.containsKey (name);
  }

  @Nonnull
  @ReturnsImmutableObject
  public Map <String, Object> getAllAttributes ()
  {
    return ContainerHelper.makeUnmodifiable (m_aAttrs);
  }

  @Nonnull
  public Enumeration <String> getAttributeNames ()
  {
    return ContainerHelper.getEnumeration (m_aAttrs.keySet ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public Set <String> getAllAttributeNames ()
  {
    return ContainerHelper.makeUnmodifiable (m_aAttrs.keySet ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public Collection <Object> getAllAttributeValues ()
  {
    return ContainerHelper.makeUnmodifiable (m_aAttrs.values ());
  }

  @Nullable
  public Object getAttributeObject (final String name)
  {
    return m_aAttrs.get (name);
  }

  @Nonnegative
  public int size ()
  {
    return m_aAttrs.size ();
  }

  public boolean isEmpty ()
  {
    return m_aAttrs.isEmpty ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MapBasedReadonlyAttributeContainer))
      return false;
    final MapBasedReadonlyAttributeContainer rhs = (MapBasedReadonlyAttributeContainer) o;
    return m_aAttrs.equals (rhs.m_aAttrs);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aAttrs).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aAttrs).toString ();
  }
}
