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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * A helper class that converts a {@link IThrowingRunnable} into an
 * {@link IThrowingCallable}.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The callable result type.
 * @param <PARAMTYPE>
 *        The parameter type
 */
@Immutable
public final class AdapterThrowingRunnableToCallableWithParameter <DATATYPE, PARAMTYPE> implements
                                                                                        IThrowingCallableWithParameter <DATATYPE, PARAMTYPE>
{
  private final IThrowingRunnableWithParameter <PARAMTYPE> m_aRunnable;
  private final DATATYPE m_aResult;

  public AdapterThrowingRunnableToCallableWithParameter (@Nonnull final IThrowingRunnableWithParameter <PARAMTYPE> aRunnable)
  {
    this (aRunnable, null);
  }

  public AdapterThrowingRunnableToCallableWithParameter (@Nonnull final IThrowingRunnableWithParameter <PARAMTYPE> aRunnable,
                                                         @Nullable final DATATYPE aResult)
  {
    if (aRunnable == null)
      throw new NullPointerException ("runnable");
    m_aRunnable = aRunnable;
    m_aResult = aResult;
  }

  @Nullable
  public DATATYPE call (final PARAMTYPE aParam) throws Exception
  {
    m_aRunnable.run (aParam);
    return m_aResult;
  }

  /**
   * Create a callable that always returns <code>null</code>.
   * 
   * @param aRunnable
   *        The runnable to be executed.
   * @return The created {@link AdapterThrowingRunnableToCallableWithParameter}
   *         object.
   */
  @Nonnull
  public static <PARAMTYPE> AdapterThrowingRunnableToCallableWithParameter <Object, PARAMTYPE> createAdapter (@Nonnull final IThrowingRunnableWithParameter <PARAMTYPE> aRunnable)
  {
    return new AdapterThrowingRunnableToCallableWithParameter <Object, PARAMTYPE> (aRunnable);
  }

  /**
   * Create a callable that always returns the passed value.
   * 
   * @param aRunnable
   *        The runnable to be executed.
   * @param aResult
   *        The expected result from calling {@link IThrowingCallable#call()} .
   *        May be <code>null</code>.
   * @return The created {@link AdapterThrowingRunnableToCallableWithParameter}
   *         object.
   */
  @Nonnull
  public static <DATATYPE, PARAMTYPE> AdapterThrowingRunnableToCallableWithParameter <DATATYPE, PARAMTYPE> createAdapter (@Nonnull final IThrowingRunnableWithParameter <PARAMTYPE> aRunnable,
                                                                                                                          @Nullable final DATATYPE aResult)
  {
    return new AdapterThrowingRunnableToCallableWithParameter <DATATYPE, PARAMTYPE> (aRunnable, aResult);
  }
}
