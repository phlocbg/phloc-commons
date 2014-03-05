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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Interface for object that have a representation as {@link Node}
 * 
 * @author Philip Helger
 */
public interface IHasXMLRepresentation
{
  /**
   * @param aDoc
   *        The owning DOM {@link Document}. May not be <code>null</code>.
   * @return this as an {@link Node}. May not be <code>null</code>.
   */
  @Nonnull
  Node getAsXMLNode (@Nonnull Document aDoc);
}
