/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.jms.pool;

import javax.annotation.Nullable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;

/**
 * A cache key for the connection details
 */
public final class ConnectionKey
{
  private final String m_sUserName;
  private final String m_sPassword;
  private final int m_nHashCode;

  public ConnectionKey (@Nullable final String sUserName, @Nullable final String sPassword)
  {
    m_sUserName = sUserName;
    m_sPassword = sPassword;
    m_nHashCode = new HashCodeGenerator (this).append (sUserName).append (sPassword).getHashCode ();
  }

  @Nullable
  public String getPassword ()
  {
    return m_sPassword;
  }

  @Nullable
  public String getUserName ()
  {
    return m_sUserName;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ConnectionKey))
      return false;
    final ConnectionKey rhs = (ConnectionKey) o;
    return EqualsUtils.equals (m_sUserName, rhs.m_sUserName) && EqualsUtils.equals (m_sPassword, rhs.m_sPassword);
  }

  @Override
  public int hashCode ()
  {
    return m_nHashCode;
  }
}
