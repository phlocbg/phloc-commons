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
package com.phloc.commons.compare;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract comparator that handles values that can be represented as double
 * values.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The data type to be compared. Must somehow have a value that can be
 *        compared as a double value.
 */
public abstract class AbstractNumericComparator <DATATYPE> extends AbstractComparatorNullAware <DATATYPE>
{
  public AbstractNumericComparator ()
  {
    super ();
  }

  /**
   * Compare with a special order.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public AbstractNumericComparator (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  protected abstract double asDouble (@Nullable DATATYPE aObject);

  @Override
  protected final int mainCompare (@Nullable final DATATYPE aElement1, @Nullable final DATATYPE aElement2)
  {
    final double d1 = asDouble (aElement1);
    final double d2 = asDouble (aElement2);
    return Double.compare (d1, d2);
  }
}
