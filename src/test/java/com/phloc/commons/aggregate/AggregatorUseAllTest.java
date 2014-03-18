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
package com.phloc.commons.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;

/**
 * Test class for class {@link AggregatorUseAll}.
 * 
 * @author Philip Helger
 */
public final class AggregatorUseAllTest
{
  @Test
  public void testAll ()
  {
    final AggregatorUseAll <String> a1 = new AggregatorUseAll <String> ();
    final AggregatorUseAll <String> a2 = new AggregatorUseAll <String> ();
    assertEquals (a1, a1);
    assertEquals (a1, a2);
    assertFalse (a1.equals (null));
    assertFalse (a1.equals ("any other"));
    assertEquals (a1.hashCode (), a1.hashCode ());
    assertEquals (a1.hashCode (), a2.hashCode ());
    assertFalse (a1.hashCode () == 0);
    assertFalse (a1.hashCode () == "any other".hashCode ());
    assertNotNull (a1.toString ());
    assertFalse (a1.toString ().equals (a2.toString ()));
    final List <String> l = ContainerHelper.newList ("a", null, "b", "", "c");
    assertEquals (l, a1.aggregate (l));
    assertEquals (l, a2.aggregate (l));
  }
}
