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
package com.phloc.jms.pool;

import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;

interface IPooledSessionEventListener
{
  /**
   * Called on successful creation of a new TemporaryQueue.
   * 
   * @param tempQueue
   *        The TemporaryQueue just created.
   */
  void onTemporaryQueueCreate (TemporaryQueue tempQueue);

  /**
   * Called on successful creation of a new TemporaryTopic.
   * 
   * @param tempTopic
   *        The TemporaryTopic just created.
   */
  void onTemporaryTopicCreate (TemporaryTopic tempTopic);

  /**
   * Called when the PooledSession is closed.
   * 
   * @param session
   *        The PooledSession that has been closed.
   */
  void onSessionClosed (PooledSession session);

}
