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
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

/**
 * Wrapped class for a JMS {@link TopicConnection}.
 * 
 * @author Philip Helger
 */
public class TopicConnectionWrapper extends ConnectionWrapper implements TopicConnection
{
  public TopicConnectionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final TopicConnection aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected TopicConnection getWrapped ()
  {
    return (TopicConnection) super.getWrapped ();
  }

  @Nonnull
  public TopicSession createTopicSession (final boolean transacted, final int acknowledgeMode) throws JMSException
  {
    final TopicSession aTopicSession = getWrapped ().createTopicSession (transacted, acknowledgeMode);
    return getWrapper ().wrapTopicSession (aTopicSession);
  }

  @Nonnull
  public ConnectionConsumer createConnectionConsumer (final Topic topic,
                                                      final String messageSelector,
                                                      final ServerSessionPool sessionPool,
                                                      final int maxMessages) throws JMSException
  {
    final ConnectionConsumer aConnectionConsumer = getWrapped ().createConnectionConsumer (topic,
                                                                                           messageSelector,
                                                                                           sessionPool,
                                                                                           maxMessages);
    return getWrapper ().wrapConnectionConsumer (aConnectionConsumer);
  }
}
