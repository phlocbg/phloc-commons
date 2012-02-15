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
package com.phloc.commons.name;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ComparatorHasDisplayText}.
 * 
 * @author philip
 */
public final class ComparatorHasDisplayTextTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final List <MockHasDisplayText> aList = ContainerHelper.newList (MockHasDisplayText.createDE_EN ("de1", "en3"),
                                                                     MockHasDisplayText.createDE_EN ("de2", "en2"),
                                                                     MockHasDisplayText.createDE_EN ("de3", "en1"));
    List <MockHasDisplayText> l2 = ContainerHelper.getSorted (aList,
                                                              new ComparatorHasDisplayText <IHasDisplayText> (L_DE,
                                                                                                              L_DE));
    assertEquals (3, l2.size ());
    assertEquals ("de1", l2.get (0).getDisplayText (L_DE));
    assertEquals ("de2", l2.get (1).getDisplayText (L_DE));
    assertEquals ("de3", l2.get (2).getDisplayText (L_DE));
    assertEquals ("en3", l2.get (0).getDisplayText (L_EN));
    assertEquals ("en2", l2.get (1).getDisplayText (L_EN));
    assertEquals ("en1", l2.get (2).getDisplayText (L_EN));

    l2 = ContainerHelper.getSorted (aList, new ComparatorHasDisplayText <IHasDisplayText> (L_DE, L_EN));
    assertEquals (3, l2.size ());
    assertEquals ("de3", l2.get (0).getDisplayText (L_DE));
    assertEquals ("de2", l2.get (1).getDisplayText (L_DE));
    assertEquals ("de1", l2.get (2).getDisplayText (L_DE));
    assertEquals ("en1", l2.get (0).getDisplayText (L_EN));
    assertEquals ("en2", l2.get (1).getDisplayText (L_EN));
    assertEquals ("en3", l2.get (2).getDisplayText (L_EN));

    l2 = ContainerHelper.getSorted (aList, new ComparatorHasDisplayText <IHasDisplayText> (L_DE,
                                                                                           L_DE,
                                                                                           ESortOrder.DESCENDING));
    assertEquals (3, l2.size ());
    assertEquals ("de3", l2.get (0).getDisplayText (L_DE));
    assertEquals ("de2", l2.get (1).getDisplayText (L_DE));
    assertEquals ("de1", l2.get (2).getDisplayText (L_DE));
    assertEquals ("en1", l2.get (0).getDisplayText (L_EN));
    assertEquals ("en2", l2.get (1).getDisplayText (L_EN));
    assertEquals ("en3", l2.get (2).getDisplayText (L_EN));

    l2 = ContainerHelper.getSorted (aList, new ComparatorHasDisplayText <IHasDisplayText> (L_DE,
                                                                                           L_EN,
                                                                                           ESortOrder.DESCENDING));
    assertEquals (3, l2.size ());
    assertEquals ("de1", l2.get (0).getDisplayText (L_DE));
    assertEquals ("de2", l2.get (1).getDisplayText (L_DE));
    assertEquals ("de3", l2.get (2).getDisplayText (L_DE));
    assertEquals ("en3", l2.get (0).getDisplayText (L_EN));
    assertEquals ("en2", l2.get (1).getDisplayText (L_EN));
    assertEquals ("en1", l2.get (2).getDisplayText (L_EN));

    try
    {
      new ComparatorHasDisplayText <IHasDisplayText> (L_DE, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new ComparatorHasDisplayText <IHasDisplayText> (L_DE, null, ESortOrder.ASCENDING);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new ComparatorHasDisplayText <IHasDisplayText> (L_DE, L_EN, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
