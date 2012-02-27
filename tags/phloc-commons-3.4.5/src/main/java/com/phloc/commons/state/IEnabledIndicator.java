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
package com.phloc.commons.state;

/**
 * Simple interface for all objects that can be enabled and disabled.
 * 
 * @author philip
 */
public interface IEnabledIndicator
{
  /**
   * @return <code>true</code> if the object is currently enabled,
   *         <code>false</code> otherwise.
   */
  boolean isEnabled ();

  /**
   * @return <code>true</code> if the object is currently disabled,
   *         <code>false</code> otherwise.
   */
  boolean isDisabled ();
}
