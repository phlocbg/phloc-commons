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
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 480463 $ $Date: 2006-11-29 09:15:23 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class UnmodifiableByteListIterator extends ProxyByteListIterator
{
  private ByteListIterator m_aProxied;

  UnmodifiableByteListIterator (@Nonnull final ByteListIterator iterator)
  {
    m_aProxied = iterator;
  }

  public void remove ()
  {
    throw new UnsupportedOperationException ("This ByteListIterator is not modifiable.");
  }

  public void add (final byte value)
  {
    throw new UnsupportedOperationException ("This ByteListIterator is not modifiable.");
  }

  public void set (final byte value)
  {
    throw new UnsupportedOperationException ("This ByteListIterator is not modifiable.");
  }

  @Override
  protected ByteListIterator getListIterator ()
  {
    return m_aProxied;
  }

  @Nullable
  public static final ByteListIterator wrap (@Nullable final ByteListIterator iterator)
  {
    if (null == iterator)
      return null;
    if (iterator instanceof UnmodifiableByteListIterator)
      return iterator;
    return new UnmodifiableByteListIterator (iterator);
  }
}
