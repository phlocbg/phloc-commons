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
package com.phloc.jms.pool;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.transaction.TransactionManager;

/**
 * A pooled connection factory that automatically enlists sessions in the
 * current active XA transaction if any.
 */
public class XaPooledConnectionFactory extends PooledConnectionFactory
{
  private TransactionManager m_aTransactionManager;

  public XaPooledConnectionFactory (final ConnectionFactory connectionFactory)
  {
    super (connectionFactory);
  }

  public TransactionManager getTransactionManager ()
  {
    return m_aTransactionManager;
  }

  public void setTransactionManager (final TransactionManager transactionManager)
  {
    m_aTransactionManager = transactionManager;
  }

  @Override
  protected ConnectionPool createConnectionPool (final Connection connection) throws JMSException
  {
    return new XaConnectionPool (connection, getTransactionManager ());
  }
}
