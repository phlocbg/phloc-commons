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
package com.phloc.test.nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class CallNullableTest
{
  private CallNullableTest ()
  {}

  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public static void testCorrect ()
  {
    final NullableTestCorrect n = new NullableTestCorrect ();
    n.paramUndefined (null);
    // FindBugs complains here:
    n.paramNonnull (null);
    n.paramNonnullAlways (null);
    n.paramNonnullMaybe (null);
    n.paramNonnullNever (null);
    n.paramNonnullUnknown (null);
  }

  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public static void testCorrect2 ()
  {
    final INullableTest n = new NullableTestCorrect ();
    n.paramUndefined (null);
    // FindBugs complains here:
    n.paramNonnull (null);
    n.paramNonnullAlways (null);
    n.paramNonnullMaybe (null);
    n.paramNonnullNever (null);
    n.paramNonnullUnknown (null);
  }

  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public static void testNonNull ()
  {
    final NullableTestNonNull n = new NullableTestNonNull ();
    n.paramUndefined (null);
    // FindBugs complains here:
    n.paramNonnull (null);
    n.paramNonnullAlways (null);
    n.paramNonnullMaybe (null);
    n.paramNonnullNever (null);
    n.paramNonnullUnknown (null);
  }

  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public static void testNonNull2 ()
  {
    final INullableTest n = new NullableTestNonNull ();
    n.paramUndefined (null);
    // FindBugs complains here:
    n.paramNonnull (null);
    n.paramNonnullAlways (null);
    n.paramNonnullMaybe (null);
    n.paramNonnullNever (null);
    n.paramNonnullUnknown (null);
  }

  public static void testNullable ()
  {
    final NullableTestNullable n = new NullableTestNullable ();
    n.paramUndefined (null);
    n.paramNonnull (null);
    n.paramNonnullAlways (null);
    n.paramNonnullMaybe (null);
    n.paramNonnullNever (null);
    n.paramNonnullUnknown (null);
  }

  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public static void testNullable2 ()
  {
    final INullableTest n = new NullableTestNullable ();
    n.paramUndefined (null);
    // FindBugs complains here:
    n.paramNonnull (null);
    n.paramNonnullAlways (null);
    n.paramNonnullMaybe (null);
    n.paramNonnullNever (null);
    n.paramNonnullUnknown (null);
  }
}
