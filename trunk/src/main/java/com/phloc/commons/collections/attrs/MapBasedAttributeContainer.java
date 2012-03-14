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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Base class for all kind of string-object mapping container. This
 * implementation is not thread-safe!
 * 
 * @author philip
 */
@NotThreadSafe
public class MapBasedAttributeContainer extends AbstractReadonlyAttributeContainer implements
                                                                                  IAttributeContainer,
                                                                                  IHasSize
{
  /**
   * attribute storage.
   */
  protected final Map <String, Object> m_aAttrs = new ConcurrentHashMap <String, Object> ();

  public MapBasedAttributeContainer ()
  {}

  public MapBasedAttributeContainer (@Nonnull final Map <String, Object> aMap)
  {
    if (aMap == null)
      throw new NullPointerException ("map");
    m_aAttrs.putAll (aMap);
  }

  public MapBasedAttributeContainer (@Nonnull final IReadonlyAttributeContainer aCont)
  {
    if (aCont == null)
      throw new NullPointerException ("cont");
    m_aAttrs.putAll (aCont.getAllAttributes ());
  }

  public boolean containsAttribute (@Nullable final String sName)
  {
    // ConcurrentHashMap cannot handle null keys
    return sName != null && m_aAttrs.containsKey (sName);
  }

  @Nonnull
  @ReturnsImmutableObject
  public Map <String, Object> getAllAttributes ()
  {
    return ContainerHelper.makeUnmodifiable (m_aAttrs);
  }

  @Nullable
  public Object getAttributeObject (@Nullable final String sName)
  {
    // ConcurrentHashMap cannot handle null keys
    return sName == null ? null : m_aAttrs.get (sName);
  }

  @Nonnull
  public EChange setAttribute (@Nonnull final String sName, @Nonnull final Object aValue)
  {
    if (sName == null)
      throw new NullPointerException ("name");
    if (aValue == null)
      throw new NullPointerException ("value");
    final Object aOldValue = m_aAttrs.put (sName, aValue);
    return EChange.valueOf (!EqualsUtils.nullSafeEquals (aOldValue, aValue));
  }

  @Nonnull
  public EChange removeAttribute (@Nullable final String sName)
  {
    return EChange.valueOf (sName != null && m_aAttrs.remove (sName) != null);
  }

  @Nonnull
  public Enumeration <String> getAttributeNames ()
  {
    // Build an enumerator on top of the set
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

  @Nonnegative
  public int size ()
  {
    return m_aAttrs.size ();
  }

  public boolean isEmpty ()
  {
    return m_aAttrs.isEmpty ();
  }

  @Nonnull
  public EChange clear ()
  {
    if (m_aAttrs.isEmpty ())
      return EChange.UNCHANGED;
    m_aAttrs.clear ();
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final MapBasedAttributeContainer rhs = (MapBasedAttributeContainer) o;
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
    return new ToStringGenerator (this).append ("attrs", m_aAttrs).toString ();
  }
}
