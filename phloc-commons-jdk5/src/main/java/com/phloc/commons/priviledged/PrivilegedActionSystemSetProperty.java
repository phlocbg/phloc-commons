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
package com.phloc.commons.priviledged;

import java.security.PrivilegedAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;

/**
 * A special privileged object, that calls <code>System.setProperty</code>
 * 
 * @author Philip Helger
 */
public final class PrivilegedActionSystemSetProperty implements PrivilegedAction <String>
{
  private final String m_sKey;
  private final String m_sValue;

  public PrivilegedActionSystemSetProperty (@Nonnull final String sKey, @Nonnull final String sValue)
  {
    m_sKey = ValueEnforcer.notNull (sKey, "Key");
    m_sValue = ValueEnforcer.notNull (sValue, "Value");
  }

  @Nullable
  public String run ()
  {
    return System.setProperty (m_sKey, m_sValue);
  }
}
