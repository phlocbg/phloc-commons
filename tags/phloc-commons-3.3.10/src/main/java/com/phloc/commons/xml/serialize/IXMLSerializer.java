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
package com.phloc.commons.xml.serialize;

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

/**
 * Base interface for XML like serializers. Works both for IMicroNode and
 * org.w3c.dom.Node objects.
 * 
 * @author philip
 * @param <NODETYPE>
 *        The node type to be serialized.
 */
public interface IXMLSerializer <NODETYPE>
{
  /**
   * Write the specified node to the specified {@link OutputStream}.
   * 
   * @param aNode
   *        The node to write. May not be <code>null</code>.
   * @param aOS
   *        The stream to serialize onto. May not be <code>null</code>.
   */
  void write (@Nonnull NODETYPE aNode, @Nonnull @WillNotClose OutputStream aOS);
}
