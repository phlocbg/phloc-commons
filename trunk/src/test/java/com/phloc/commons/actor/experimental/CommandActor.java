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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.actor.experimental.DefaultActorMessage;
import com.phloc.commons.actor.experimental.IActorMessage;

/**
 * An actor that can execute a command as a method on a supplied class.
 *
 * @author BFEIGENB
 */
public class CommandActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CommandActor.class);

  @Override
  protected void loopBody (final IActorMessage m)
  {
    final String subject = m.getSubject ();
    if ("execute".equals (subject))
    {
      excuteMethod (m, false);
    }
    else
      if ("executeStatic".equals (subject))
      {
        excuteMethod (m, true);
      }
      else
        if ("init".equals (subject))
        {
          // nothing to do
        }
        else
        {
          s_aLogger.warn ("CommandActor:%s loopBody unknown subject: %s", getName (), subject);
        }
  }

  private void excuteMethod (final IActorMessage m, final boolean fstatic)
  {
    Object res = null;
    Object id = null;
    try
    {
      Object [] params = (Object []) m.getData ();
      id = params[0];
      final String className = (String) params[1];
      params = params.length > 2 ? (Object []) params[2] : null;
      final Class <?> clazz = Class.forName (className);
      final Method method = clazz.getMethod (fstatic ? "executeStatic" : "execute", new Class [] { Object.class });
      if (Modifier.isStatic (method.getModifiers ()) == fstatic)
      {
        final Object target = fstatic ? null : clazz.newInstance ();
        res = method.invoke (target, params);
      }
    }
    catch (final Exception e)
    {
      res = e;
    }

    final DefaultActorMessage dm = new DefaultActorMessage ("executeComplete", new Object [] { id, res });
    getManager ().send (dm, this, m.getSource ());
  }
}
