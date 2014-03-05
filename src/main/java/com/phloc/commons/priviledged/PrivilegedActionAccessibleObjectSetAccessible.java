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

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A special privileged object, that calls "setAccessible(true)" on an object.
 * 
 * @author Philip Helger
 */
public final class PrivilegedActionAccessibleObjectSetAccessible implements PrivilegedAction <Object>
{
  private final AccessibleObject m_aObject;
  private final boolean m_bAccessible;

  public PrivilegedActionAccessibleObjectSetAccessible (@Nonnull final AccessibleObject aObject)
  {
    this (aObject, true);
  }

  public PrivilegedActionAccessibleObjectSetAccessible (@Nonnull final AccessibleObject aObject,
                                                        final boolean bAccessible)
  {
    if (aObject == null)
      throw new NullPointerException ("method");
    m_aObject = aObject;
    m_bAccessible = bAccessible;
  }

  @Nullable
  public Object run ()
  {
    // A value of true indicates that the reflected object should suppress
    // Java language access checking when it is used.
    m_aObject.setAccessible (m_bAccessible);
    return null;
  }
}
