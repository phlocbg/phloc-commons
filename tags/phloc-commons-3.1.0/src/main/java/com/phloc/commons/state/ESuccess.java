/**
 * Copyright (C) 2006-2011 phloc systems
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
 * Small enum for manager actions to indicate success or failure.
 * 
 * @author philip
 */
public enum ESuccess implements ISuccessIndicator
{
  SUCCESS,
  FAILURE;

  public boolean isSuccess ()
  {
    return this == SUCCESS;
  }

  public boolean isFailure ()
  {
    return this == FAILURE;
  }

  @Nonnull
  public ESuccess or (@Nonnull final ISuccessIndicator aSuccess)
  {
    return valueOf (isSuccess () || aSuccess.isSuccess ());
  }

  @Nonnull
  public ESuccess and (@Nonnull final ISuccessIndicator aSuccess)
  {
    return valueOf (isSuccess () && aSuccess.isSuccess ());
  }

  @Nonnull
  public static ESuccess valueOf (final boolean bSuccess)
  {
    return bSuccess ? SUCCESS : FAILURE;
  }

  @Nonnull
  public static ESuccess valueOfChange (@Nonnull final IChangeIndicator aChange)
  {
    return valueOf (aChange.isChanged ());
  }
}