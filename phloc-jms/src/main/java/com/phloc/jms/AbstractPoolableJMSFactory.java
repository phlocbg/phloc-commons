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

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.ConnectionFactory;

import com.phloc.jms.pool.PooledConnectionFactory;

/**
 * An enhancement of {@link AbstractJMSFactory} that uses a
 * {@link PooledConnectionFactory} to pool JMS connections.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractPoolableJMSFactory extends AbstractJMSFactory
{
  @Nonnull
  protected abstract ConnectionFactory createUnpooledConnectionFactory ();

  @Override
  @Nonnull
  protected final ConnectionFactory createConnectionFactory ()
  {
    final ConnectionFactory aConnectionFactory = createUnpooledConnectionFactory ();

    final PooledConnectionFactory ret = new PooledConnectionFactory (aConnectionFactory);
    // Start the pooling
    ret.start ();
    return ret;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public void shutdown ()
  {
    // Stop the pooling
    ((PooledConnectionFactory) getConnectionFactory ()).stop ();
    super.shutdown ();
  }
}
