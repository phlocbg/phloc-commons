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
package com.phloc.commons.tree.utils.sort;

import com.phloc.commons.id.ComparatorHasIDComparable;
import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * Comparator for sorting {@link ITreeItemWithID} items by their comparable ID.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        tree item key type
 * @param <VALUETYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 */
public class ComparatorTreeItemIDComparable <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> extends
                                                                                                                                                                       ComparatorHasIDComparable <KEYTYPE, ITEMTYPE>
{
  public ComparatorTreeItemIDComparable ()
  {
    super ();
  }
}
