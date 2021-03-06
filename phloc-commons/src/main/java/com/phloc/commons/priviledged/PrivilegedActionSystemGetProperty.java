/**
 * Copyright (C) 2006-2015 phloc systems
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
import com.phloc.commons.annotations.Nonempty;

/**
 * A special privileged object, that calls <code>System.getProperty</code>
 * 
 * @author Philip Helger
 */
public final class PrivilegedActionSystemGetProperty implements PrivilegedAction <String>
{
  private final String m_sKey;

  public PrivilegedActionSystemGetProperty (@Nonnull @Nonempty final String sKey)
  {
    m_sKey = ValueEnforcer.notEmpty (sKey, "Key");
  }

  @Nullable
  public String run ()
  {
    return System.getProperty (m_sKey);
  }
}
