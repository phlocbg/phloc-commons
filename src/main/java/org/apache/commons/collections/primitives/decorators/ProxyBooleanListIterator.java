/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.primitives.decorators;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.BooleanIterator;
import org.apache.commons.collections.primitives.BooleanListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
abstract class ProxyBooleanListIterator extends ProxyBooleanIterator implements BooleanListIterator
{
  ProxyBooleanListIterator ()
  {}

  public boolean hasPrevious ()
  {
    return getListIterator ().hasPrevious ();
  }

  public int nextIndex ()
  {
    return getListIterator ().nextIndex ();
  }

  public boolean previous ()
  {
    return getListIterator ().previous ();
  }

  public int previousIndex ()
  {
    return getListIterator ().previousIndex ();
  }

  @Override
  @Nonnull
  protected final BooleanIterator getIterator ()
  {
    return getListIterator ();
  }
  
  @Nonnull
  protected abstract BooleanListIterator getListIterator ();
}
