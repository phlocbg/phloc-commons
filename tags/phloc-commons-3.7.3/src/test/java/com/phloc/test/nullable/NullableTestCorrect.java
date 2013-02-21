/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.test.nullable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;

public class NullableTestCorrect implements INullableTest
{
  public void paramUndefined (final String s)
  {}

  public void paramNonnull (@Nonnull final String s)
  {}

  public void paramNonnullAlways (@Nonnull (when = When.ALWAYS) final String s)
  {}

  public void paramNonnullMaybe (@Nonnull (when = When.MAYBE) final String s)
  {}

  public void paramNonnullNever (@Nonnull (when = When.NEVER) final String s)
  {}

  public void paramNonnullUnknown (@Nonnull (when = When.UNKNOWN) final String s)
  {}

  public void paramNullable (@Nullable final String s)
  {}
}