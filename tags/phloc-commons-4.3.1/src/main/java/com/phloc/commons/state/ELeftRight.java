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
 * Small enum for left and right
 * 
 * @author Philip Helger
 */
public enum ELeftRight implements ILeftRightIndicator
{
  LEFT,
  RIGHT;

  public boolean isLeft ()
  {
    return this == LEFT;
  }

  public boolean isRight ()
  {
    return this == RIGHT;
  }

  @Nonnull
  public static ELeftRight valueOf (@Nonnull final ILeftRightIndicator aLeftRightIndicator)
  {
    return aLeftRightIndicator.isLeft () ? LEFT : RIGHT;
  }
}
