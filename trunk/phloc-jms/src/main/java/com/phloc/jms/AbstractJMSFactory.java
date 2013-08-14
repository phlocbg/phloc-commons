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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.jms.wrapper.JMSWrapper;

/**
 * Abstract generic JMS factory that allows for JMS object wrapping. To make
 * this class work, {@link #createConnectionFactory()} must be implemented.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractJMSFactory
{
  protected static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static ExceptionListener s_aExceptionListener = new LoggingJMSExceptionListener ();

  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private ConnectionFactory m_aConnectionFactory;
  @GuardedBy ("m_aRWLock")
  private JMSWrapper m_aWrapper;

  public AbstractJMSFactory ()
  {}

  @Nullable
  public static ExceptionListener getDefaultExceptionListener ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aExceptionListener;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setDefaultExceptionListener (@Nullable final ExceptionListener aExceptionListener)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aExceptionListener = aExceptionListener;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  protected abstract ConnectionFactory createConnectionFactory ();

  @Nonnull
  private ConnectionFactory _getOrCreateConnectionFactory ()
  {
    // First try with read lock
    m_aRWLock.readLock ().lock ();
    try
    {
      if (m_aConnectionFactory != null)
        return m_aConnectionFactory;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    // Try again with write lock
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aConnectionFactory == null)
        m_aConnectionFactory = createConnectionFactory ();
      return m_aConnectionFactory;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @OverrideOnDemand
  protected JMSWrapper createJMSWrapper ()
  {
    return new JMSWrapper ();
  }

  @Nonnull
  private JMSWrapper _getOrCreateWrapper ()
  {
    // First try with read lock
    m_aRWLock.readLock ().lock ();
    try
    {
      if (m_aWrapper != null)
        return m_aWrapper;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    // Try again with write lock
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aWrapper == null)
        m_aWrapper = createJMSWrapper ();
      return m_aWrapper;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public Connection createConnection () throws JMSException
  {
    return createConnection (true);
  }

  @Nonnull
  public Connection createConnection (final boolean bStartConnection) throws JMSException
  {
    final Connection ret = _getOrCreateConnectionFactory ().createConnection ();
    ret.setExceptionListener (getDefaultExceptionListener ());
    if (bStartConnection)
      ret.start ();

    // Wrap the Connection object
    return _getOrCreateWrapper ().wrap (ret);
  }

  public void shutdown ()
  {}

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("connectionFactory", m_aConnectionFactory)
                                       .append ("wrapper", m_aWrapper)
                                       .toString ();
  }
}
