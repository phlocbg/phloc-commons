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

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.Connection;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Misc JMS utilities.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class JMSUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSUtils.class);

  private JMSUtils ()
  {}

  /**
   * Close the passed JMS connection
   * 
   * @param aConnection
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final Connection aConnection)
  {
    if (aConnection != null)
      try
      {
        // There is no need to close the sessions, producers, and consumers of a
        // closed connection.
        aConnection.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS connection", ex);
      }
  }
}
