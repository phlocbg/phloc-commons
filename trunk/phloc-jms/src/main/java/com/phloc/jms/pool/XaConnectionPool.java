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

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import com.phloc.jms.JMSUtils;

/**
 * An XA-aware connection pool. When a session is created and an xa transaction
 * is active, the session will automatically be enlisted in the current
 * transaction.
 * 
 * @author gnodet
 */
public class XaConnectionPool extends ConnectionPool
{
  private final TransactionManager m_aTransactionManager;

  public XaConnectionPool (final Connection connection, final TransactionManager transactionManager) throws JMSException
  {
    super (connection);
    m_aTransactionManager = transactionManager;
  }

  @Override
  public Session createSession (final boolean bTransacted, final int nAckMode) throws JMSException
  {
    try
    {
      final boolean bIsXA = m_aTransactionManager != null &&
                            m_aTransactionManager.getStatus () != Status.STATUS_NO_TRANSACTION;
      final PooledSession session = (PooledSession) super.createSession (bIsXA ? true : bTransacted,
                                                                         bIsXA ? Session.SESSION_TRANSACTED : nAckMode);
      if (bIsXA)
      {
        session.setIgnoreClose (true);
        session.setIsXa (true);
        m_aTransactionManager.getTransaction ().registerSynchronization (new MySynchronization (session));
        incrementReferenceCount ();
        m_aTransactionManager.getTransaction ().enlistResource (createXaResource (session));
      }
      else
      {
        session.setIgnoreClose (false);
      }
      return session;
    }
    catch (final RollbackException e)
    {
      throw JMSUtils.createException ("Rollback Exception", e);
    }
    catch (final SystemException e)
    {
      throw JMSUtils.createException ("System Exception", e);
    }
  }

  /**
   * @param session
   * @return The XA resource
   * @throws JMSException
   */
  protected XAResource createXaResource (final PooledSession session) throws JMSException
  {
    return session.getXAResource ();
  }

  protected class MySynchronization implements Synchronization
  {
    private final PooledSession m_aSession;

    private MySynchronization (final PooledSession session)
    {
      m_aSession = session;
    }

    public void beforeCompletion ()
    {}

    public void afterCompletion (final int status)
    {
      try
      {
        // This will return session to the pool.
        m_aSession.setIgnoreClose (false);
        m_aSession.close ();
        m_aSession.setIgnoreClose (true);
        m_aSession.setIsXa (false);
        XaConnectionPool.this.decrementReferenceCount ();
      }
      catch (final JMSException e)
      {
        throw new RuntimeException (e);
      }
    }
  }
}
