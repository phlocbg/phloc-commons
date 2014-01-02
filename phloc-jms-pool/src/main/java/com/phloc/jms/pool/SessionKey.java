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
package com.phloc.jms.pool;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;

/**
 * A cache key for the session details used to locate PooledSession instances.
 */
@Immutable
public final class SessionKey
{
  private final boolean m_bTransacted;
  private final int m_nAckMode;
  private final int m_nHashCode;

  public SessionKey (final boolean bTransacted, final int nAckMode)
  {
    m_bTransacted = bTransacted;
    m_nAckMode = nAckMode;
    m_nHashCode = new HashCodeGenerator (this).append (bTransacted).append (nAckMode).getHashCode ();
  }

  public boolean isTransacted ()
  {
    return m_bTransacted;
  }

  public int getAckMode ()
  {
    return m_nAckMode;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof SessionKey))
      return false;
    final SessionKey rhs = (SessionKey) o;
    return m_bTransacted == rhs.m_bTransacted && m_nAckMode == rhs.m_nAckMode;
  }

  @Override
  public int hashCode ()
  {
    return m_nHashCode;
  }
}
