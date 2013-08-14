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
package com.phloc.jms.wrapper;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

public class QueueSessionWrapper extends SessionWrapper implements QueueSession
{
  public QueueSessionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final QueueSession aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  @Override
  @Nonnull
  protected QueueSession getWrapped ()
  {
    return (QueueSession) super.getWrapped ();
  }

  @Nonnull
  public QueueReceiver createReceiver (final Queue queue) throws JMSException
  {
    final QueueReceiver aReceiver = getWrapped ().createReceiver (queue);
    return getWrapper ().wrap (aReceiver);
  }

  @Nonnull
  public QueueReceiver createReceiver (final Queue queue, final String messageSelector) throws JMSException
  {
    final QueueReceiver aReceiver = getWrapped ().createReceiver (queue, messageSelector);
    return getWrapper ().wrap (aReceiver);
  }

  @Nonnull
  public QueueSender createSender (final Queue queue) throws JMSException
  {
    final QueueSender aSender = getWrapped ().createSender (queue);
    return getWrapper ().wrap (aSender);
  }
}
