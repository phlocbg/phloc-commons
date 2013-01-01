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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.microdom.EMicroNodeType;
import com.phloc.commons.microdom.IMicroEntityReference;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroProcessingInstruction;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMicroEntityReference} interface.
 * 
 * @author philip
 */
@Immutable
public final class MicroProcessingInstruction extends AbstractMicroNode implements IMicroProcessingInstruction
{
  private final String m_sTarget;
  private final String m_sData;

  public MicroProcessingInstruction (@Nonnull @Nonempty final String sTarget)
  {
    this (sTarget, null);
  }

  public MicroProcessingInstruction (@Nonnull @Nonempty final String sTarget, @Nullable final String sData)
  {
    if (StringHelper.hasNoText (sTarget))
      throw new IllegalArgumentException ("The passed target is valid");
    m_sTarget = sTarget;
    m_sData = sData;
  }

  @Nonnull
  public EMicroNodeType getType ()
  {
    return EMicroNodeType.PROCESSING_INSTRUCTION;
  }

  @Nonnull
  @Nonempty
  public String getNodeName ()
  {
    return "#pi";
  }

  @Override
  @Nonnull
  @Nonempty
  public String getNodeValue ()
  {
    return m_sTarget;
  }

  public String getTarget ()
  {
    return m_sTarget;
  }

  public String getData ()
  {
    return m_sData;
  }

  @Nonnull
  public IMicroProcessingInstruction getClone ()
  {
    return new MicroProcessingInstruction (m_sTarget, m_sData);
  }

  public boolean isEqualContent (@Nullable final IMicroNode o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MicroProcessingInstruction))
      return false;
    final MicroProcessingInstruction rhs = (MicroProcessingInstruction) o;
    return m_sTarget.equals (rhs.m_sTarget) && EqualsUtils.equals (m_sData, rhs.m_sData);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("target", m_sTarget)
                            .append ("data", m_sData)
                            .toString ();
  }
}
