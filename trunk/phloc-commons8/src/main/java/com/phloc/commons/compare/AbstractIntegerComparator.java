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
package com.phloc.commons.compare;

import javax.annotation.Nonnull;

/**
 * Abstract comparator that handles values that can be represented as long
 * values.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type to be compared. Must somehow have a value that can be
 *        compared as a long value.
 */
public abstract class AbstractIntegerComparator <DATATYPE> extends AbstractComparator <DATATYPE>
{
  public AbstractIntegerComparator ()
  {
    super ();
  }

  /**
   * Compare with a special order.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public AbstractIntegerComparator (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  protected abstract long asLong (DATATYPE aObject);

  @Override
  protected final int mainCompare (final DATATYPE aElement1, final DATATYPE aElement2)
  {
    final long n1 = asLong (aElement1);
    final long n2 = asLong (aElement2);
    return CompareUtils.compare (n1, n2);
  }
}
