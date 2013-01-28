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
package com.phloc.commons.collections.pair;

import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * This is a helper class that wraps a list of pair objects.
 * 
 * @author philip
 * @param <DATA1TYPE>
 *        First type of the pair type.
 * @param <DATA2TYPE>
 *        Second type of the pair type.
 */
@NotThreadSafe
public class KeyValuePairList <DATA1TYPE, DATA2TYPE> extends ArrayList <IReadonlyPair <DATA1TYPE, DATA2TYPE>>
{
  public void add (@Nullable final DATA1TYPE aKey, @Nullable final DATA2TYPE aValue)
  {
    super.add (ReadonlyPair.create (aKey, aValue));
  }

  public void addNonNullValue (@Nullable final DATA1TYPE aKey, @Nullable final DATA2TYPE aValue)
  {
    if (aValue != null)
      add (aKey, aValue);
  }
}