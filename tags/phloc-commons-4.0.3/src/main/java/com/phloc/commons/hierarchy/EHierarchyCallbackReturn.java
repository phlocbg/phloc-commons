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
package com.phloc.commons.hierarchy;

/**
 * Return value for hierarchy iteration.
 * 
 * @author philip
 */
public enum EHierarchyCallbackReturn
{
  /**
   * Continue with the next element. This may either be the first child element
   * or the next sibling.
   */
  CONTINUE,

  /**
   * Skip the child elements of the current element and go to the next sibling.
   */
  USE_NEXT_SIBLING,

  /**
   * Skip the child elements and all siblings of the current content element and
   * go to the parent element's sibling.
   */
  USE_PARENTS_NEXT_SIBLING,

  /**
   * Stop the iteration completely.
   */
  STOP_ITERATION;
}
