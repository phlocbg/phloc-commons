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
import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

/**
 * Wrapped class for a JMS {@link TemporaryQueue}.
 * 
 * @author Philip Helger
 */
public class TemporaryQueueWrapper extends QueueWrapper implements TemporaryQueue
{
  public TemporaryQueueWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final TemporaryQueue aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected TemporaryQueue getWrapped ()
  {
    return (TemporaryQueue) super.getWrapped ();
  }

  public void delete () throws JMSException
  {
    getWrapped ().delete ();
  }
}
