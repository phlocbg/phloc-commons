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
package com.phloc.commons.tree.utils.xml;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.convert.IUnidirectionalConverter;
import com.phloc.commons.convert.UnidirectionalConverterIdentity;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.id.ComparatorHasIDString;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.ChildrenProviderElementWithName;
import com.phloc.commons.microdom.utils.MicroWalker;
import com.phloc.commons.parent.impl.ChildrenProviderHasChildrenSorting;
import com.phloc.commons.tree.IBasicTree;
import com.phloc.commons.tree.simple.ITreeItem;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.BasicTreeWithID;
import com.phloc.commons.tree.withid.DefaultTreeWithID;
import com.phloc.commons.tree.withid.ITreeItemWithID;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;

/**
 * Convert a tree to XML
 * 
 * @author philip
 */
@Immutable
public final class TreeXMLConverter
{
  private static final String ELEMENT_ROOT = "root";
  private static final String ELEMENT_ITEM = "item";
  private static final String ATTR_ID = "id";
  private static final String ELEMENT_DATA = "data";

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TreeXMLConverter s_aInstance = new TreeXMLConverter ();

  private TreeXMLConverter ()
  {}

  /**
   * Specialized conversion method for converting a tree with ID to a
   * standardized XML tree.
   * 
   * @param <VALUETYPE>
   *        tree item value type
   * @param <ITEMTYPE>
   *        tree item type
   * @param aTree
   *        The tree to be converted
   * @param aConverter
   *        The main data converter that converts the tree item values into XML
   * @return The created document.
   */
  @Nonnull
  public static <VALUETYPE, ITEMTYPE extends ITreeItemWithID <String, VALUETYPE, ITEMTYPE>> IMicroElement getTreeWithStringIDAsXML (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                                    @Nonnull final IConverterTreeItemToMicroNode <? super VALUETYPE> aConverter)
  {
    return getTreeWithIDAsXML (aTree,
                               new ComparatorHasIDString <ITEMTYPE> (),
                               UnidirectionalConverterIdentity.<String> create (),
                               aConverter);
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> IMicroElement getTreeWithIDAsXML (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                                        @Nonnull final Comparator <? super ITEMTYPE> aItemComparator,
                                                                                                                                        @Nonnull final IUnidirectionalConverter <KEYTYPE, String> aIDConverter,
                                                                                                                                        @Nonnull final IConverterTreeItemToMicroNode <? super VALUETYPE> aDataConverter)
  {
    final IMicroElement eRoot = new MicroElement (ELEMENT_ROOT);
    final NonBlockingStack <IMicroElement> aParents = new NonBlockingStack <IMicroElement> ();
    aParents.push (eRoot);
    TreeWalker.walkTree (aTree,
                         new ChildrenProviderHasChildrenSorting <ITEMTYPE> (aItemComparator),
                         new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
                         {
                           @Override
                           public void onItemBeforeChildren (@Nullable final ITEMTYPE aItem)
                           {
                             if (aItem != null)
                             {
                               // create item element
                               final IMicroElement eItem = aParents.peek ().appendElement (ELEMENT_ITEM);
                               eItem.setAttribute (ATTR_ID, aIDConverter.convert (aItem.getID ()));

                               // append data
                               final IMicroElement eData = eItem.appendElement (ELEMENT_DATA);
                               aDataConverter.appendDataValue (eData, aItem.getData ());

                               aParents.push (eItem);
                             }
                           }

                           @Override
                           public void onItemAfterChildren (@Nullable final ITEMTYPE aItem)
                           {
                             if (aItem != null)
                               aParents.pop ();
                           }
                         });
    return eRoot;
  }

  @Nonnull
  public static <VALUETYPE, ITEMTYPE extends ITreeItem <VALUETYPE, ITEMTYPE>> IMicroElement getTreeAsXML (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                          @Nonnull final Comparator <? super ITEMTYPE> aItemComparator,
                                                                                                          @Nonnull final IConverterTreeItemToMicroNode <? super VALUETYPE> aDataConverter)
  {
    final IMicroElement eRoot = new MicroElement (ELEMENT_ROOT);
    final NonBlockingStack <IMicroElement> aParents = new NonBlockingStack <IMicroElement> ();
    aParents.push (eRoot);
    TreeWalker.walkTree (aTree,
                         new ChildrenProviderHasChildrenSorting <ITEMTYPE> (aItemComparator),
                         new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
                         {
                           @Override
                           public void onItemBeforeChildren (@Nullable final ITEMTYPE aItem)
                           {
                             if (aItem != null)
                             {
                               // create item element
                               final IMicroElement eItem = aParents.peek ().appendElement (ELEMENT_ITEM);

                               // append data
                               final IMicroElement eData = eItem.appendElement (ELEMENT_DATA);
                               aDataConverter.appendDataValue (eData, aItem.getData ());

                               aParents.push (eItem);
                             }
                           }

                           @Override
                           public void onItemAfterChildren (@Nullable final ITEMTYPE aItem)
                           {
                             if (aItem != null)
                               aParents.pop ();
                           }
                         });
    return eRoot;
  }

  private static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void _getXMLAsTreeWithID (@Nonnull final IMicroElement aElement,
                                                                                                                                 @Nonnull final IUnidirectionalConverter <String, KEYTYPE> aIDConverter,
                                                                                                                                 @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter,
                                                                                                                                 @Nonnull final BasicTreeWithID <KEYTYPE, VALUETYPE, ITEMTYPE> aTree)
  {
    final NonBlockingStack <ITEMTYPE> aParents = new NonBlockingStack <ITEMTYPE> ();
    aParents.push (aTree.getRootItem ());
    MicroWalker.walkNode (aElement,
                          new ChildrenProviderElementWithName (ELEMENT_ITEM),
                          new DefaultHierarchyWalkerCallback <IMicroElement> ()
                          {
                            @Override
                            public void onItemBeforeChildren (@Nullable final IMicroElement eItem)
                            {
                              if (eItem != null)
                              {
                                final KEYTYPE aTreeItemID = aIDConverter.convert (eItem.getAttribute (ATTR_ID));

                                final IMicroElement eData = eItem.getFirstChildElement (ELEMENT_DATA);
                                final VALUETYPE aTreeItemValue = aDataConverter.getAsDataValue (eData);

                                final ITEMTYPE aTreeItem = aParents.peek ().createChildItem (aTreeItemID,
                                                                                             aTreeItemValue);
                                aParents.push (aTreeItem);
                              }
                            }

                            @Override
                            public void onItemAfterChildren (@Nullable final IMicroElement aItem)
                            {
                              if (aItem != null)
                                aParents.pop ();
                            }
                          });
  }

  @Nonnull
  public static <VALUETYPE> DefaultTreeWithGlobalUniqueID <String, VALUETYPE> getXMLAsTreeWithUniqueStringID (@Nonnull final IMicroDocument aDoc,
                                                                                                              @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter)
  {
    return getXMLAsTreeWithUniqueStringID (aDoc.getDocumentElement (), aDataConverter);
  }

  @Nonnull
  public static <VALUETYPE> DefaultTreeWithGlobalUniqueID <String, VALUETYPE> getXMLAsTreeWithUniqueStringID (@Nonnull final IMicroElement aElement,
                                                                                                              @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter)
  {
    return TreeXMLConverter.<String, VALUETYPE> getXMLAsTreeWithUniqueID (aElement,
                                                                          UnidirectionalConverterIdentity.<String> create (),
                                                                          aDataConverter);
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> DefaultTreeWithGlobalUniqueID <KEYTYPE, VALUETYPE> getXMLAsTreeWithUniqueID (@Nonnull final IMicroDocument aDoc,
                                                                                                                  @Nonnull final IUnidirectionalConverter <String, KEYTYPE> aIDConverter,
                                                                                                                  @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter)
  {
    return getXMLAsTreeWithUniqueID (aDoc.getDocumentElement (), aIDConverter, aDataConverter);
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> DefaultTreeWithGlobalUniqueID <KEYTYPE, VALUETYPE> getXMLAsTreeWithUniqueID (@Nonnull final IMicroElement aElement,
                                                                                                                  @Nonnull final IUnidirectionalConverter <String, KEYTYPE> aIDConverter,
                                                                                                                  @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter)
  {
    final DefaultTreeWithGlobalUniqueID <KEYTYPE, VALUETYPE> aTree = new DefaultTreeWithGlobalUniqueID <KEYTYPE, VALUETYPE> ();
    _getXMLAsTreeWithID (aElement, aIDConverter, aDataConverter, aTree);
    return aTree;
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> DefaultTreeWithID <KEYTYPE, VALUETYPE> getXMLAsTreeWithID (@Nonnull final IMicroDocument aDoc,
                                                                                                @Nonnull final IUnidirectionalConverter <String, KEYTYPE> aIDConverter,
                                                                                                @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter)
  {
    return getXMLAsTreeWithID (aDoc.getDocumentElement (), aIDConverter, aDataConverter);
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> DefaultTreeWithID <KEYTYPE, VALUETYPE> getXMLAsTreeWithID (@Nonnull final IMicroElement aElement,
                                                                                                @Nonnull final IUnidirectionalConverter <String, KEYTYPE> aIDConverter,
                                                                                                @Nonnull final IConverterMicroNodeToTreeItem <? extends VALUETYPE> aDataConverter)
  {
    final DefaultTreeWithID <KEYTYPE, VALUETYPE> aTree = new DefaultTreeWithID <KEYTYPE, VALUETYPE> ();
    _getXMLAsTreeWithID (aElement, aIDConverter, aDataConverter, aTree);
    return aTree;
  }
}
