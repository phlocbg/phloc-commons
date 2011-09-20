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
package com.phloc.commons.tree.withid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.MockHasIDString;
import com.phloc.commons.parent.IParentProvider;
import com.phloc.commons.parent.MockChildrenProvider;
import com.phloc.commons.parent.MockHasChildren;
import com.phloc.commons.parent.MockHasParent;
import com.phloc.commons.parent.impl.ParentProviderHasParent;
import com.phloc.commons.tree.withid.utils.TreeWithIDBuilder;

/**
 * Test class for class {@link TreeWithIDBuilder}.
 * 
 * @author philip
 */
public final class TreeWithIDBuilderTest
{
  public static final class ParentProviderMockHasIDString implements IParentProvider <MockHasIDString>
  {
    public MockHasIDString getParent (final MockHasIDString aCurrent)
    {
      return null;
    }
  }

  @Test
  public void testBuildFromParent ()
  {
    final DefaultTreeWithID <String, MockHasParent> aTree = TreeWithIDBuilder.buildTree (ContainerHelper.newList (new MockHasParent ("a"),
                                                                                                           new MockHasParent ("ab"),
                                                                                                           new MockHasParent ("abc"),
                                                                                                           new MockHasParent ("abd")));
    assertNotNull (aTree.getRootItem ());
    assertNull (aTree.getRootItem ().getID ());
    assertEquals (1, aTree.getRootItem ().getChildCount ());
    assertEquals ("a", aTree.getRootItem ().getChildAtIndex (0).getID ());
    assertEquals ("ab", aTree.getRootItem ().getChildAtIndex (0).getChildAtIndex (0).getID ());
    assertEquals ("abc", aTree.getRootItem ().getChildAtIndex (0).getChildAtIndex (0).getChildAtIndex (0).getID ());
    assertEquals ("abd", aTree.getRootItem ().getChildAtIndex (0).getChildAtIndex (0).getChildAtIndex (1).getID ());

    assertNotNull (TreeWithIDBuilder.buildTree (ContainerHelper.newList (new MockHasParent ("abc"),
                                                                         new MockHasParent ("abd"),
                                                                         new MockHasParent ("a"),
                                                                         new MockHasParent ("ab"))));

    try
    {
      // parent "ab" and "a" missing
      TreeWithIDBuilder.buildTree (ContainerHelper.newList (new MockHasParent ("abc")));
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    try
    {
      TreeWithIDBuilder.buildTree ((Collection <MockHasParent>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      TreeWithIDBuilder.buildTree (ContainerHelper.newList (new MockHasParent ("abc")), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      TreeWithIDBuilder.buildTree ((Collection <MockHasParent>) null, new ParentProviderHasParent <MockHasParent> ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testBuildFromChildren ()
  {
    final DefaultTreeWithID <String, MockHasChildren> aTree = TreeWithIDBuilder.buildTree (new MockChildrenProvider (new MockHasChildren ("",
                                                                                                                                   new MockHasChildren ("a",
                                                                                                                                                        new MockHasChildren ("b",
                                                                                                                                                                             new MockHasChildren ("c"),
                                                                                                                                                                             new MockHasChildren ("d"))))));
    assertNotNull (aTree.getRootItem ());
    assertNull (aTree.getRootItem ().getID ());
    assertEquals (1, aTree.getRootItem ().getChildCount ());
    assertEquals ("a", aTree.getRootItem ().getChildAtIndex (0).getID ());
    assertEquals ("b", aTree.getRootItem ().getChildAtIndex (0).getChildAtIndex (0).getID ());
    assertEquals ("c", aTree.getRootItem ().getChildAtIndex (0).getChildAtIndex (0).getChildAtIndex (0).getID ());
    assertEquals ("d", aTree.getRootItem ().getChildAtIndex (0).getChildAtIndex (0).getChildAtIndex (1).getID ());

    try
    {
      TreeWithIDBuilder.buildTree ((MockChildrenProvider) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testBuildTreeFromIHasID ()
  {
    final MockHasIDString [] x = new MockHasIDString [0];
    final IParentProvider <MockHasIDString> pp = new ParentProviderMockHasIDString ();
    final DefaultTreeWithID <String, MockHasIDString> aTree = TreeWithIDBuilder.buildTree (x, pp);
    assertNotNull (aTree);

    try
    {
      TreeWithIDBuilder.buildTree ((MockHasIDString []) null, pp);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      TreeWithIDBuilder.buildTree (x, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
