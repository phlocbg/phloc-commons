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
package com.phloc.commons.url.protocol;

import java.util.Set;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIInterface;

/**
 * Interface for a registrar providing custom URL protocols
 * 
 * @author Boris Gregorcic
 * @author philip
 */
@IsSPIInterface
public interface IURLProtocolRegistrarSPI
{
  /**
   * @return The set of protocols to be registered for this registrar. The
   *         returned set may be <code>null</code> but may not contain
   *         <code>null</code> elements!
   */
  @Nullable
  Set <? extends IURLProtocol> getProtocols ();
}
