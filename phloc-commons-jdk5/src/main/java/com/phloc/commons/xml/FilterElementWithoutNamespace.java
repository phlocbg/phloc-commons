/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.xml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.w3c.dom.Element;

import com.phloc.commons.filter.IFilter;
import com.phloc.commons.filter.ISerializableFilter;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An implementation of {@link IFilter} on {@link Element} objects that will
 * only return elements without a namespace URI.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class FilterElementWithoutNamespace implements ISerializableFilter <Element>
{
  private static final FilterElementWithoutNamespace s_aInstance = new FilterElementWithoutNamespace ();

  private FilterElementWithoutNamespace ()
  {}

  @Nonnull
  public static FilterElementWithoutNamespace getInstance ()
  {
    return s_aInstance;
  }

  public boolean matchesFilter (@Nullable final Element aElement)
  {
    return aElement != null && aElement.getNamespaceURI () == null;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FilterElementWithoutNamespace))
      return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
