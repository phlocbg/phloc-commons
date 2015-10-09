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
package com.phloc.commons.compare;

import static org.junit.Assert.assertEquals;

import java.text.Collator;

import org.junit.Test;

import com.phloc.commons.locale.ComparatorLocale;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link CompareUtils}.
 * 
 * @author Philip Helger
 */
public final class CompareUtilsTest extends AbstractPhlocTestCase
{
  @Test
  public void testNullSafeCompare ()
  {
    final String s1 = "A";
    final String s2 = "B";
    assertEquals (-1, CompareUtils.nullSafeCompare (s1, s2));
    assertEquals (+1, CompareUtils.nullSafeCompare (s2, s1));
    assertEquals (0, CompareUtils.nullSafeCompare (s1, s1));
    assertEquals (0, CompareUtils.nullSafeCompare (s2, s2));

    assertEquals (+1, CompareUtils.nullSafeCompare (s1, null));
    assertEquals (+1, CompareUtils.nullSafeCompare (s2, null));
    assertEquals (-1, CompareUtils.nullSafeCompare (null, s1));
    assertEquals (-1, CompareUtils.nullSafeCompare (null, s2));
    assertEquals (0, CompareUtils.nullSafeCompare ((String) null, null));

    // Using our collator
    assertEquals (-1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", L_DE));
    assertEquals (-1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", CollatorUtils.getCollatorSpaceBeforeDot (L_DE)));
    // Using the system collaor
    assertEquals (+1, CompareUtils.nullSafeCompare ("1.1 a", "1.1.1 a", Collator.getInstance (L_DE)));

    assertEquals (-1, CompareUtils.nullSafeCompare (L_DE, L_EN, new ComparatorLocale ()));
    assertEquals (+1, CompareUtils.nullSafeCompare (L_DE, null, new ComparatorLocale ()));
    assertEquals (-1, CompareUtils.nullSafeCompare (null, L_EN, new ComparatorLocale ()));
  }
}
