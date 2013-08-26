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

import javax.annotation.concurrent.ThreadSafe;
import javax.jms.BytesMessage;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Misc JMS utilities concerning the handling of messages.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class JMSMessageUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSMessageUtils.class);

  private JMSMessageUtils ()
  {}

  public static byte [] getAllBytes (final BytesMessage aBytesMessage) throws JMSException
  {
    final long nResponseBodyLength = aBytesMessage.getBodyLength ();
    if (nResponseBodyLength > Integer.MAX_VALUE)
      throw new IllegalStateException ("Response message is too large: " + nResponseBodyLength);

    final byte [] aBytes = new byte [(int) nResponseBodyLength];
    aBytesMessage.readBytes (aBytes);
    return aBytes;
  }
}
