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

// TODO: add this to all others
/*
 * Copyright (C) IBM Corportation, 2102.  All rights reserved.
 * Copyright (C) Barry Feigenbaum, 2102.  All rights reserved.
 */

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.StringHelper;

/**
 * A partial implementation of an Actor for running in a DefaultActorManager.
 * 
 * @author BFEIGENB
 */
public abstract class AbstractActor implements IActor
{
  public static final int DEFAULT_MAX_MESSAGES = 100;
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractActor.class);

  private DefaultActorManager m_aManager;
  private String m_sCategory = DEFAULT_CATEGORY;
  private String m_sName;
  private boolean m_bIsActive;
  private boolean m_bThreadAssigned;
  private boolean m_bShutdown;
  private boolean m_bSuspended;
  protected final List <DefaultActorMessage> m_aMessages = new LinkedList <DefaultActorMessage> ();

  @Nullable
  public DefaultActorManager getManager ()
  {
    return m_aManager;
  }

  public void setManager (@Nullable final DefaultActorManager aManager)
  {
    if (m_aManager != null && aManager != null)
      throw new IllegalStateException ("cannot change manager of attached actor");
    m_aManager = aManager;
  }

  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  public void setName (@Nullable final String name)
  {
    if (m_aManager != null)
      throw new IllegalStateException ("cannot change name if manager set");
    m_sName = name;
  }

  @Nullable
  public String getCategory ()
  {
    return m_sCategory;
  }

  public void setCategory (@Nullable final String sCategory)
  {
    m_sCategory = sCategory;
  }

  /**
   * Process a message conditionally. If testMessage() returns null no message
   * will be consumed.
   * 
   * @see AbstractActor#testMessage()
   */
  public boolean receive ()
  {
    final IActorMessage aMessage = testMessage ();
    final boolean bResult = aMessage != null;
    if (aMessage != null)
    {
      if (!remove (aMessage))
        s_aLogger.warn ("receive message not removed: " + aMessage);
      final DefaultActorMessage aDefActorMsg = aMessage instanceof DefaultActorMessage ? (DefaultActorMessage) aMessage
                                                                                      : null;
      try
      {
        if (aDefActorMsg != null)
          aDefActorMsg.fireMessageListeners (new ActorMessageEvent (this, aDefActorMsg, EMessageStatus.DELIVERED));

        // Main processing
        if (s_aLogger.isTraceEnabled ())
          s_aLogger.trace ("receive " + getName () + " processing " + aMessage);
        loopBody (aMessage);

        if (aDefActorMsg != null)
          aDefActorMsg.fireMessageListeners (new ActorMessageEvent (this, aDefActorMsg, EMessageStatus.COMPLETED));
      }
      catch (final Exception e)
      {
        if (aDefActorMsg != null)
          aDefActorMsg.fireMessageListeners (new ActorMessageEvent (this, aDefActorMsg, EMessageStatus.FAILED, e));
        s_aLogger.error ("loop exception", e);
      }
    }
    m_aManager.awaitMessage (this);
    return bResult;
  }

  /**
   * Test to see if a message should be processed. Subclasses should override
   */
  @OverrideOnDemand
  public boolean willReceive (final String sSubject)
  {
    return StringHelper.hasText (sSubject);
  }

  /**
   * Test the current message. Default action is to accept all.
   */
  @Nullable
  protected IActorMessage testMessage ()
  {
    return getMatch (null, false);
  }

  /**
   * Process the accepted subject.
   */
  abstract protected void loopBody (IActorMessage m);

  /**
   * Test a message against a defined subject pattern.
   */
  @Nullable
  protected DefaultActorMessage getMatch (final String subject, final boolean isRegExpr)
  {
    DefaultActorMessage res = null;
    synchronized (m_aMessages)
    {
      res = (DefaultActorMessage) peekNext (subject, isRegExpr);
    }
    return res;
  }

  @Nullable
  public DefaultActorMessage [] getMessages ()
  {
    return m_aMessages.toArray (new DefaultActorMessage [m_aMessages.size ()]);
  }

  @Nonnegative
  public int getMessageCount ()
  {
    synchronized (m_aMessages)
    {
      return m_aMessages.size ();
    }
  }

  /**
   * Limit the number of messages that can be received. Subclasses should
   * override.
   */
  @Nonnegative
  public int getMaxMessageCount ()
  {
    return DEFAULT_MAX_MESSAGES;
  }

  /**
   * Queue a messaged to be processed later.
   */
  public void addMessage (final DefaultActorMessage message)
  {
    if (message != null)
    {
      synchronized (m_aMessages)
      {
        if (m_aMessages.size () < getMaxMessageCount ())
        {
          m_aMessages.add (message);
          // messages.notifyAll();
        }
        else
        {
          throw new IllegalStateException ("too many messages, cannot add");
        }
      }
    }
  }

  @Nullable
  public IActorMessage peekNext ()
  {
    return peekNext (null, false);
  }

  @Nullable
  public IActorMessage peekNext (final String subject)
  {
    return peekNext (subject, false);
  }

  /**
   * See if a message exists that meets the selection criteria.
   **/
  @Nullable
  public IActorMessage peekNext (final String sSubject, final boolean bIsRegExpr)
  {
    IActorMessage aResultMessage = null;
    if (m_bIsActive)
    {
      final long nNow = new Date ().getTime ();
      synchronized (m_aMessages)
      {
        for (final DefaultActorMessage aMessage : m_aMessages)
        {
          if (aMessage.getDelayUntil () <= nNow)
            if (sSubject == null || aMessage.subjectMatches (sSubject, bIsRegExpr))
            {
              aResultMessage = aMessage;
              break;
            }
        }
      }
    }
    if (false)
      s_aLogger.trace ("peekNext " + sSubject + "," + bIsRegExpr + ": " + aResultMessage);
    return aResultMessage;
  }

  public boolean remove (@Nullable final IActorMessage aMessage)
  {
    synchronized (m_aMessages)
    {
      return m_aMessages.remove (aMessage);
    }
  }

  public boolean isActive ()
  {
    return m_bIsActive;
  }

  public void activate ()
  {
    m_bIsActive = true;
  }

  public void deactivate ()
  {
    m_bIsActive = false;
  }

  /**
   * Do startup processing.
   */
  protected void runBody ()
  {
    // Send ourselves an init message
    getManager ().send (new DefaultActorMessage ("init"), null, this);
  }

  public void run ()
  {
    runBody ();
    getManager ().awaitMessage (this);
  }

  public boolean isThreadAssigned ()
  {
    return m_bThreadAssigned;
  }

  protected void setThreadAssigned (final boolean bThreadAssigned)
  {
    m_bThreadAssigned = bThreadAssigned;
  }

  public boolean isShutdown ()
  {
    return m_bShutdown;
  }

  public void shutdown ()
  {
    m_bShutdown = true;
  }

  public void resetShutdown ()
  {
    m_bShutdown = false;
  }

  public void setSuspended (final boolean f)
  {
    m_bSuspended = f;
  }

  public boolean isSuspended ()
  {
    return m_bSuspended;
  }

  @Override
  public String toString ()
  {
    return getClass ().getSimpleName () +
           "[" +
           "name=" +
           m_sName +
           ", category=" +
           m_sCategory +
           ", messages=" +
           m_aMessages.size () +
           "]";
  }
}
