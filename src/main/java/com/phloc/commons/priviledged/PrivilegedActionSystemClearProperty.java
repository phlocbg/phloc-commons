/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.priviledged;

import java.security.PrivilegedAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A special privileged object, that calls <code>System.clearProperty</code>
 * 
 * @author philip
 */
public final class PrivilegedActionSystemClearProperty implements PrivilegedAction <String>
{
  private final String m_sKey;

  public PrivilegedActionSystemClearProperty (@Nonnull final String sKey)
  {
    if (sKey == null)
      throw new NullPointerException ("key");
    m_sKey = sKey;
  }

  @Nullable
  public String run ()
  {
    return System.clearProperty (m_sKey);
  }
}
