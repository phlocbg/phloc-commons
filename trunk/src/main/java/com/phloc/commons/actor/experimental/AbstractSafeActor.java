/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.actor.experimental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An actor with exception trapping.
 * 
 * @author BFEIGENB
 */
public abstract class AbstractSafeActor extends AbstractActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractSafeActor.class);

  @Override
  protected void loopBody (final IActorMessage aMessage)
  {
    try
    {
      if (s_aLogger.isTraceEnabled ())
        s_aLogger.trace ("SafeActor loopBody: " + aMessage);
      doBody ((DefaultActorMessage) aMessage);
    }
    catch (final Exception e)
    {
      s_aLogger.error ("SafeActor: exception", e);
    }
  }

  @Override
  protected void runBody ()
  {
    // by default, nothing to do
  }

  /**
   * Override to define message reception behaviour.
   * 
   * @param aMessage
   * @throws Exception
   */
  protected abstract void doBody (DefaultActorMessage aMessage) throws Exception;
}
