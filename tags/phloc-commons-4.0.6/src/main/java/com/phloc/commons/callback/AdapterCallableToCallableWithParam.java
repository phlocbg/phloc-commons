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
package com.phloc.commons.callback;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.ToStringGenerator;

/**
 * A helper class that converts a {@link Callable} into an
 * {@link IThrowingCallableWithParameter}.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The callable result type.
 * @param <PARAMTYPE>
 *        The parameter type.
 */
@Immutable
public class AdapterCallableToCallableWithParam <DATATYPE, PARAMTYPE> implements IThrowingCallableWithParameter <DATATYPE, PARAMTYPE>
{
  private final Callable <DATATYPE> m_aCallable;

  public AdapterCallableToCallableWithParam (@Nonnull final Callable <DATATYPE> aCallable)
  {
    if (aCallable == null)
      throw new NullPointerException ("Callable");
    m_aCallable = aCallable;
  }

  @Nonnull
  public DATATYPE call (@Nonnull final PARAMTYPE aParam) throws Exception
  {
    return m_aCallable.call ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("callabale", m_aCallable).toString ();
  }
}
