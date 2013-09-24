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
package com.phloc.jms;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base interface for a JMS connection factory.
 * 
 * @author Philip Helger
 */
public interface IJMSFactory
{
  /**
   * Create a new connection start it
   * 
   * @return Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  @Nonnull
  Connection createConnection () throws JMSException;

  /**
   * Create a new connection start it or not
   * 
   * @param bStartConnection
   *        <code>true</code> if the new connection should be started
   * @return Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  @Nonnull
  Connection createConnection (boolean bStartConnection) throws JMSException;

  /**
   * Shutdown the whole JMS factory. Afterwards no connection can be created
   * anymore.
   */
  void shutdown ();
}
