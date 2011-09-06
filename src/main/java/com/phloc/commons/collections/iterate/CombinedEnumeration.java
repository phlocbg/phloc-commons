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
package com.phloc.commons.collections.iterate;

import java.util.Enumeration;

import javax.annotation.Nullable;

public final class CombinedEnumeration <ELEMENTTYPE> implements Enumeration <ELEMENTTYPE>
{
  private final Enumeration <ELEMENTTYPE> m_aEnum1;
  private final Enumeration <ELEMENTTYPE> m_aEnum2;
  private boolean m_bFirstEnum = true;

  public CombinedEnumeration (@Nullable final Enumeration <ELEMENTTYPE> aEnum1,
                              @Nullable final Enumeration <ELEMENTTYPE> aEnum2)
  {
    m_aEnum1 = aEnum1;
    m_aEnum2 = aEnum2;
  }

  public boolean hasMoreElements ()
  {
    boolean ret = false;
    if (m_bFirstEnum)
    {
      ret = m_aEnum1 != null && m_aEnum1.hasMoreElements ();
      if (!ret)
        m_bFirstEnum = false;
    }
    if (!m_bFirstEnum)
      ret = m_aEnum2 != null && m_aEnum2.hasMoreElements ();
    return ret;
  }

  @Nullable
  public ELEMENTTYPE nextElement ()
  {
    return m_bFirstEnum ? m_aEnum1.nextElement () : m_aEnum2.nextElement ();
  }
}