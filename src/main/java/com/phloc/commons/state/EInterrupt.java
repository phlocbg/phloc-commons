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
package com.phloc.commons.state;

import javax.annotation.Nonnull;

/**
 * Small enum for setter method to identify whether a value has changed or not.
 * 
 * @author Philip Helger
 */
public enum EInterrupt implements IInterruptIndicator
{
  INTERRUPTED,
  NOT_INTERRUPTED;

  public boolean isInterrupted ()
  {
    return this == INTERRUPTED;
  }

  public boolean isNotInterrupted ()
  {
    return this == NOT_INTERRUPTED;
  }

  @Nonnull
  public EInterrupt or (@Nonnull final IInterruptIndicator aInterrupt)
  {
    return valueOf (isInterrupted () || aInterrupt.isInterrupted ());
  }

  @Nonnull
  public EInterrupt and (@Nonnull final IInterruptIndicator aInterrupt)
  {
    return valueOf (isInterrupted () && aInterrupt.isInterrupted ());
  }

  @Nonnull
  public static EInterrupt valueOf (final boolean bInterrupted)
  {
    return bInterrupted ? INTERRUPTED : NOT_INTERRUPTED;
  }

  @Nonnull
  public static EInterrupt valueOf (@Nonnull final IInterruptIndicator aInterruptIndicator)
  {
    return valueOf (aInterruptIndicator.isInterrupted ());
  }
}
