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
package com.phloc.commons.collections.multimap;

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Utility class implementing some common functionality con multi maps
 * 
 * @author Philip Helger
 */
@Immutable
public final class MultiMapHelper
{
  private MultiMapHelper ()
  {}

  @Nonnegative
  public static long getTotalValueCount (@Nonnull final IMultiMap <?, ?, ?> aMultiMap)
  {
    long ret = 0;
    for (final Collection <?> aChild : aMultiMap.values ())
      ret += aChild.size ();
    return ret;
  }
}
