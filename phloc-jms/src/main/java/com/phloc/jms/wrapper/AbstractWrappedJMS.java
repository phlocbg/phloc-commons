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
package com.phloc.jms.wrapper;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Base class for all JMS wrapper classes
 * 
 * @author Philip Helger
 */
public abstract class AbstractWrappedJMS implements IJMSWrapper
{
  private final JMSWrapper m_aWrapper;

  public AbstractWrappedJMS (@Nonnull final JMSWrapper aWrapper)
  {
    if (aWrapper == null)
      throw new NullPointerException ("wrapper");
    m_aWrapper = aWrapper;
  }

  /**
   * @return The {@link JMSWrapper} instance to be used. Never <code>null</code>
   *         .
   */
  @Nonnull
  protected final JMSWrapper getWrapper ()
  {
    return m_aWrapper;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("wrapper", m_aWrapper).toString ();
  }
}
