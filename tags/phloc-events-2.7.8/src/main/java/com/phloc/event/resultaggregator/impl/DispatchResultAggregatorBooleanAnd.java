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
package com.phloc.event.resultaggregator.impl;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.phloc.commons.aggregate.IAggregator;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.event.impl.EventObservingExceptionWrapper;

/**
 * Aggregate a list of Boolean result values by combining them with a logical
 * AND.
 * 
 * @author philip
 */
public final class DispatchResultAggregatorBooleanAnd implements IAggregator <Object, Object>
{
  public DispatchResultAggregatorBooleanAnd ()
  {}

  @Nonnull
  public Boolean aggregate (@Nonnull final Collection <Object> aResults)
  {
    if (aResults == null)
      throw new NullPointerException ("results");

    boolean bResult = true;
    for (final Object aResult : aResults)
      if (!(aResult instanceof EventObservingExceptionWrapper))
      {
        bResult = bResult && ((Boolean) aResult).booleanValue ();

        // No need to continue calculation :)
        if (!bResult)
          break;
      }
    return Boolean.valueOf (bResult);
  }

  @Override
  public boolean equals (final Object o)
  {
    return o == this || o instanceof DispatchResultAggregatorBooleanAnd;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }
}
