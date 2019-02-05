/**
 * Copyright (C) 2006-2015 phloc systems
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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A helper class that converts a {@link IThrowingRunnable} into an
 * {@link IThrowingCallable}.
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        The callable result type.
 */
@Immutable
public class AdapterThrowingRunnableToCallable <DATATYPE> implements IThrowingCallable <DATATYPE>
{
  private final IThrowingRunnable m_aRunnable;
  private final DATATYPE m_aResult;

  public AdapterThrowingRunnableToCallable (@Nonnull final IThrowingRunnable aRunnable)
  {
    this (aRunnable, null);
  }

  public AdapterThrowingRunnableToCallable (@Nonnull final IThrowingRunnable aRunnable,
                                            @Nullable final DATATYPE aResult)
  {
    this.m_aRunnable = ValueEnforcer.notNull (aRunnable, "Runnable");
    this.m_aResult = aResult;
  }

  @Override
  @Nullable
  public DATATYPE call () throws Exception
  {
    this.m_aRunnable.run ();
    return this.m_aResult;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("runnable", this.m_aRunnable)
                                       .append ("result", this.m_aResult)
                                       .toString ();
  }

  /**
   * Create a callable that always returns <code>null</code>.
   * 
   * @param aRunnable
   *        The runnable to be executed.
   * @return The created {@link AdapterThrowingRunnableToCallable} object.
   */
  @Nonnull
  public static AdapterThrowingRunnableToCallable <Object> createAdapter (@Nonnull final IThrowingRunnable aRunnable)
  {
    return new AdapterThrowingRunnableToCallable <Object> (aRunnable);
  }

  /**
   * Create a callable that always returns the passed value.
   * 
   * @param aRunnable
   *        The runnable to be executed.
   * @param aResult
   *        The expected result from calling {@link IThrowingCallable#call()} .
   *        May be <code>null</code>.
   * @return The created {@link AdapterThrowingRunnableToCallable} object.
   * @param <DATATYPE>
   *        The return data type
   */
  @Nonnull
  public static <DATATYPE> AdapterThrowingRunnableToCallable <DATATYPE> createAdapter (@Nonnull final IThrowingRunnable aRunnable,
                                                                                       @Nullable final DATATYPE aResult)
  {
    return new AdapterThrowingRunnableToCallable <DATATYPE> (aRunnable, aResult);
  }
}
