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
package com.phloc.commons.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;

/**
 * Test class for class {@link AggregatorAlwaysNull}.
 * 
 * @author philip
 */
public final class AggregatorAlwaysNullTest
{
  @Test
  public void testAll ()
  {
    final AggregatorAlwaysNull <String, String> a1 = new AggregatorAlwaysNull <String, String> ();
    final AggregatorAlwaysNull <String, String> a2 = new AggregatorAlwaysNull <String, String> ();
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
    assertNull (a1.aggregate (ContainerHelper.newList ("a", "b")));
    assertNull (a1.aggregate (new ArrayList <String> ()));
  }
}
