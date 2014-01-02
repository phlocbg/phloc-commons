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

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.ConnectionFactory;

import com.phloc.commons.factory.IFactory;
import com.phloc.jms.JMSFactory;

/**
 * An enhancement of {@link JMSFactory} that uses a
 * {@link PooledConnectionFactory} to pool JMS connections.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class PoolableJMSFactory extends JMSFactory
{
  public PoolableJMSFactory (@Nonnull final IFactory <ConnectionFactory> aFactory)
  {
    super (aFactory);
  }

  @Nonnull
  @Override
  @OverridingMethodsMustInvokeSuper
  protected ConnectionFactory createConnectionFactory ()
  {
    final ConnectionFactory aConnectionFactory = super.createConnectionFactory ();

    final PooledConnectionFactory ret = new PooledConnectionFactory (aConnectionFactory);
    // Start the pooling
    ret.start ();
    return ret;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void shutdown ()
  {
    final PooledConnectionFactory aConnectionFactory = (PooledConnectionFactory) getConnectionFactory ();
    if (aConnectionFactory != null)
    {
      // Stop the pooling
      aConnectionFactory.stop ();
    }
    super.shutdown ();
  }
}
