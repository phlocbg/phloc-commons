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
package com.phloc.commons.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.string.StringHelper;

/**
 * This class contains multiple XML utility methods.
 *
 * @author philip
 */
@Immutable
public final class XMLHelper
{
  // Order is important!
  // Note: for performance reasons they are all char arrays!
  private static final char [] MASK_PATTERNS = new char [] { '&', '<', '>', '"', '\'',
                                                            // Control
                                                            // characters
                                                            0,
                                                            1,
                                                            2,
                                                            3,
                                                            4,
                                                            5,
                                                            6,
                                                            7,
                                                            8,
                                                            // 9,
                                                            // 10
                                                            11,
                                                            12,
                                                            // 13
                                                            14,
                                                            15,
                                                            16,
                                                            17,
                                                            18,
                                                            19,
                                                            20,
                                                            21,
                                                            22,
                                                            23,
                                                            24,
                                                            25,
                                                            26,
                                                            27,
                                                            28,
                                                            29,
                                                            30,
                                                            31 };

  /*
   * IE8 emits an error when using &apos; - that's why the work around with
   * &#39; is used!
   */
  private static final char [][] MASK_REPLACE = new char [] [] { "&amp;".toCharArray (),
                                                                "&lt;".toCharArray (),
                                                                "&gt;".toCharArray (),
                                                                "&quot;".toCharArray (),

                                                                "&#39;".toCharArray (),
                                                                // Control
                                                                // character
                                                                // replacements
                                                                "&#0;".toCharArray (),
                                                                "&#1;".toCharArray (),
                                                                "&#2;".toCharArray (),
                                                                "&#3;".toCharArray (),
                                                                "&#4;".toCharArray (),
                                                                "&#5;".toCharArray (),
                                                                "&#6;".toCharArray (),
                                                                "&#7;".toCharArray (),
                                                                "&#8;".toCharArray (),
                                                                // 9 (\t)
                                                                // 10 (\n)
                                                                "&#11;".toCharArray (),
                                                                "&#12;".toCharArray (),
                                                                // 13 (\r)
                                                                "&#14;".toCharArray (),
                                                                "&#15;".toCharArray (),
                                                                "&#16;".toCharArray (),
                                                                "&#17;".toCharArray (),
                                                                "&#18;".toCharArray (),
                                                                "&#19;".toCharArray (),
                                                                "&#20;".toCharArray (),
                                                                "&#21;".toCharArray (),
                                                                "&#22;".toCharArray (),
                                                                "&#23;".toCharArray (),
                                                                "&#24;".toCharArray (),
                                                                "&#25;".toCharArray (),
                                                                "&#26;".toCharArray (),
                                                                "&#27;".toCharArray (),
                                                                "&#28;".toCharArray (),
                                                                "&#29;".toCharArray (),
                                                                "&#30;".toCharArray (),
                                                                "&#31;".toCharArray () };

  static
  {
    // Check integrity
    if (MASK_PATTERNS.length != MASK_REPLACE.length)
      throw new IllegalStateException ("Arrays have different length!");
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLHelper s_aInstance = new XMLHelper ();

  private XMLHelper ()
  {}

  /**
   * Get the first direct child element of the passed element.
   *
   * @param aStartNode
   *        The element to start searching.
   * @return <code>null</code> if the passed element does not have any direct
   *         child element.
   */
  @Nullable
  public static Element getFirstChildElement (@Nonnull final Node aStartNode)
  {
    final NodeList aNodeList = aStartNode.getChildNodes ();
    final int nLen = aNodeList.getLength ();
    for (int i = 0; i < nLen; ++i)
    {
      final Node aNode = aNodeList.item (i);
      if (aNode.getNodeType () == Node.ELEMENT_NODE)
        return (Element) aNode;
    }
    return null;
  }

  /**
   * Check if the passed node has at least one direct child element or not.
   *
   * @param aStartNode
   *        The parent element to be searched. May not be <code>null</code>.
   * @return <code>true</code> if the passed node has at least one child
   *         element, <code>false</code> otherwise.
   */
  public static boolean hasChildElementNodes (@Nonnull final Node aStartNode)
  {
    return getFirstChildElement (aStartNode) != null;
  }

  /**
   * Search all child nodes of the given for the first element that has the
   * specified tag name.
   *
   * @param aStartNode
   *        The parent element to be searched. May not be <code>null</code>.
   * @param sName
   *        The tag name to search.
   * @return <code>null</code> if the parent element has no such child element.
   */
  @Nullable
  public static Element getFirstChildElementOfName (@Nonnull final Node aStartNode, @Nullable final String sName)
  {
    final NodeList aNodeList = aStartNode.getChildNodes ();
    final int nLen = aNodeList.getLength ();
    for (int i = 0; i < nLen; ++i)
    {
      final Node aNode = aNodeList.item (i);
      if (aNode.getNodeType () == Node.ELEMENT_NODE)
      {
        final Element aElement = (Element) aNode;
        if (aElement.getTagName ().equals (sName))
          return aElement;
      }
    }
    return null;
  }

  /**
   * Get the owner document of the passed node. If the node itself is a
   * document, only a cast is performed.
   *
   * @param aNode
   *        The node to get the document from. May be <code>null</code>.
   * @return <code>null</code> if the passed node was <code>null</code>.
   */
  @Nullable
  public static Document getOwnerDocument (@Nullable final Node aNode)
  {
    return aNode == null ? null : aNode instanceof Document ? (Document) aNode : aNode.getOwnerDocument ();
  }

  @Nonnull
  public static Node append (@Nonnull final Node aParentNode, @Nullable final Object aChild)
  {
    if (aParentNode == null)
      throw new NullPointerException ("parentNode");

    if (aChild != null)
      if (aChild instanceof Document)
      {
        // Special handling for Document comes first, as this is a special case
        // of "Node"

        // Cannot add complete documents!
        append (aParentNode, ((Document) aChild).getDocumentElement ());
      }
      else
        if (aChild instanceof Node)
        {
          // directly append Node
          final Node aChildNode = (Node) aChild;
          final Document aParentDoc = getOwnerDocument (aParentNode);
          if (getOwnerDocument (aChildNode).equals (aParentDoc))
          {
            // Nodes have the same parent
            aParentNode.appendChild (aChildNode);
          }
          else
          {
            // Node to be added belongs to a different document
            aParentNode.appendChild (aParentDoc.adoptNode (aChildNode.cloneNode (true)));
          }
        }
        else
          if (aChild instanceof String)
          {
            // append a string node
            aParentNode.appendChild (getOwnerDocument (aParentNode).createTextNode ((String) aChild));
          }
          else
            if (aChild instanceof Iterable <?>)
            {
              // it's a nested collection -> recursion
              for (final Object aSubChild : (Iterable <?>) aChild)
                append (aParentNode, aSubChild);
            }
            else
              if (ArrayHelper.isArray (aChild))
              {
                // it's a nested collection -> recursion
                for (final Object aSubChild : (Object []) aChild)
                  append (aParentNode, aSubChild);
              }
              else
              {
                // unsupported type
                throw new IllegalArgumentException ("Passed object cannot be appended to a DOMNode (type=" +
                                                    aChild.getClass ().getName () +
                                                    ".");
              }
    return aParentNode;
  }

  public static void append (@Nonnull final Node aSrcNode, @Nonnull final Collection <?> aNodesToAppend)
  {
    for (final Object aNode : aNodesToAppend)
      append (aSrcNode, aNode);
  }

  @Nonnegative
  public static int getDirectChildElementCountNoNS (@Nullable final Element aParent)
  {
    return aParent == null ? 0 : ContainerHelper.getSize (getChildElementIteratorNoNS (aParent));
  }

  @Nonnegative
  public static int getDirectChildElementCountNoNS (@Nullable final Element aParent,
                                                    @Nonnull @Nonempty final String sTagName)
  {
    return aParent == null ? 0 : ContainerHelper.getSize (getChildElementIteratorNoNS (aParent, sTagName));
  }

  @Nonnegative
  public static int getDirectChildElementCountNS (@Nullable final Element aParent, @Nullable final String sNamespaceURI)
  {
    return aParent == null ? 0 : ContainerHelper.getSize (getChildElementIteratorNS (aParent, sNamespaceURI));
  }

  @Nonnegative
  public static int getDirectChildElementCountNS (@Nullable final Element aParent,
                                                  @Nullable final String sNamespaceURI,
                                                  @Nonnull @Nonempty final String sLocalName)
  {
    return aParent == null ? 0
                          : ContainerHelper.getSize (getChildElementIteratorNS (aParent, sNamespaceURI, sLocalName));
  }

  /**
   * Get an iterator over all child elements that have no namespace.
   *
   * @param aStartNode
   *        the parent element
   * @return a non-null Iterator
   */
  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNoNS (@Nonnull final Node aStartNode)
  {
    return new ChildElementIterator (aStartNode, new IFilter <Element> ()
    {
      public boolean matchesFilter (final Element aElement)
      {
        return aElement.getNamespaceURI () == null;
      }
    });
  }

  /**
   * Get an iterator over all child elements that have no namespace and the
   * desired tag name.
   *
   * @param aStartNode
   *        the parent element
   * @param sTagName
   *        the name of the tag that is desired
   * @return a non-null Iterator
   * @throws IllegalArgumentException
   *         if the passed tag name is null or empty
   */
  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNoNS (@Nonnull final Node aStartNode,
                                                                         @Nonnull @Nonempty final String sTagName)
  {
    if (StringHelper.hasNoText (sTagName))
      throw new IllegalArgumentException ("Passed tag name is illegal");

    return new ChildElementIterator (aStartNode, new IFilter <Element> ()
    {
      public boolean matchesFilter (final Element aElement)
      {
        return aElement.getNamespaceURI () == null && aElement.getTagName ().equals (sTagName);
      }
    });
  }

  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNS (@Nonnull final Node aStartNode,
                                                                       @Nullable final String sNamespaceURI)
  {
    return new ChildElementIterator (aStartNode, new IFilter <Element> ()
    {
      public boolean matchesFilter (final Element aElement)
      {
        return hasNamespaceURI (aElement, sNamespaceURI);
      }
    });
  }

  @Nonnull
  public static IIterableIterator <Element> getChildElementIteratorNS (@Nonnull final Node aStartNode,
                                                                       @Nullable final String sNamespaceURI,
                                                                       @Nonnull @Nonempty final String sLocalName)
  {
    if (StringHelper.hasNoText (sLocalName))
      throw new IllegalArgumentException ("Passed local name is illegal");

    return new ChildElementIterator (aStartNode, new IFilter <Element> ()
    {
      public boolean matchesFilter (final Element aElement)
      {
        // check namespace before checking local name
        return hasNamespaceURI (aElement, sNamespaceURI) &&
               EqualsUtils.nullSafeEquals (aElement.getLocalName (), sLocalName);
      }
    });
  }

  public static boolean hasNamespaceURI (@Nonnull final Node aNode, @Nullable final String sNamespaceURI)
  {
    final String sNSURI = aNode.getNamespaceURI ();
    return sNSURI != null && sNSURI.equals (sNamespaceURI);
  }

  /**
   * Shortcut for {@link #getPathToNode(Node, String)} using "/" as the
   * separator.
   *
   * @param aNode
   *        The node to check.
   * @return A non-<code>null</code> path.
   */
  @Nonnull
  public static String getPathToNode (@Nonnull final Node aNode)
  {
    return getPathToNode (aNode, "/");
  }

  /**
   * Get the path from root node to the passed node.
   *
   * @param aNode
   *        The node to start. May not be <code>null</code>.
   * @param sSep
   *        The separator string to use. May not be <code>null</code>.
   * @return The path to the node.
   */
  @Nonnull
  public static String getPathToNode (@Nonnull final Node aNode, @Nonnull final String sSep)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (sSep == null)
      throw new NullPointerException ("separator");

    final StringBuilder aRet = new StringBuilder ();
    Node aCurNode = aNode;
    while (aCurNode != null)
    {
      final StringBuilder aName = new StringBuilder (aCurNode.getNodeName ());
      if (aCurNode.getNodeType () == Node.ELEMENT_NODE && aCurNode.getParentNode () != null)
      {
        // get index of my node
        int nIndex = 0;

        final Iterator <Element> it = getChildElementIteratorNoNS (aCurNode.getParentNode ());
        while (it.hasNext ())
        {
          final Element x = it.next ();
          if (x == aCurNode)// NOPMD
            break;
          if (x.getTagName ().equals (((Element) aCurNode).getTagName ()))
            ++nIndex;
        }
        aName.append ('[').append (nIndex).append (']');
      }

      aRet.insert (0, sSep).insert (0, aName);

      // goto parent
      aCurNode = aCurNode.getParentNode ();
    }
    return aRet.toString ();
  }

  /**
   * Remove all child nodes of the given node.
   *
   * @param aElement
   *        The element whose children are to be removed.
   */
  public static void removeAllChildElements (@Nonnull final Element aElement)
  {
    while (aElement.getChildNodes ().getLength () > 0)
      aElement.removeChild (aElement.getChildNodes ().item (0));
  }

  @Nonnull
  public static char [] getMaskedXMLText (@Nullable final String s)
  {
    return StringHelper.replaceMultiple (s, MASK_PATTERNS, MASK_REPLACE);
  }

  @Nonnull
  public static int getMaskedXMLTextLength (@Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return 0;
    final int nResLen = StringHelper.getReplaceMultipleResultLength (s.toCharArray (), MASK_PATTERNS, MASK_REPLACE);
    return nResLen == CGlobal.ILLEGAL_UINT ? s.length () : nResLen;
  }

  public static void maskXMLTextTo (@Nullable final String s, @Nonnull final Writer aWriter) throws IOException
  {
    StringHelper.replaceMultipleTo (s, MASK_PATTERNS, MASK_REPLACE, aWriter);
  }

  /**
   * Check if the passed node is a text node. This includes all nodes derived
   * from {@link CharacterData} which are not {@link Comment} nodes as well as
   * {@link EntityReference} nodes.
   *
   * @param aNode
   *        The node to be checked.
   * @return <code>true</code> if the passed node is a text node,
   *         <code>false</code> otherwise.
   */
  public static boolean isTextNode (@Nullable final Node aNode)
  {
    return (aNode instanceof CharacterData && !(aNode instanceof Comment)) || aNode instanceof EntityReference;
  }

  /**
   * Get the content of the first Text child element of the passed element.
   *
   * @param aStartNode
   *        the element to scan for a TextNode child
   * @return <code>null</code> if the element contains no text node as child
   */
  @Nullable
  public static String getFirstChildText (@Nullable final Node aStartNode)
  {
    if (aStartNode != null)
    {
      final NodeList aNodeList = aStartNode.getChildNodes ();
      final int nLen = aNodeList.getLength ();
      for (int i = 0; i < nLen; ++i)
      {
        final Node aNode = aNodeList.item (i);
        if (aNode instanceof Text)
        {
          final Text aText = (Text) aNode;

          // ignore whitespace-only content
          if (!aText.isElementContentWhitespace ())
            return aText.getData ();
        }
      }
    }
    return null;
  }

  /**
   * The latest version of XercesJ 2.9 returns an empty string for non existing
   * attributes. To differentiate between empty attributes and non-existing
   * attributes, this method returns null for non existing attributes.
   *
   * @param aElement
   *        the source element to get the attribute from
   * @param sAttrName
   *        the name of the attribute to query
   * @return <code>null</code> if the attribute does not exists, the string
   *         value otherwise
   */
  @Nullable
  public static String getAttributeValue (@Nonnull final Element aElement, @Nonnull final String sAttrName)
  {
    return getAttributeValue (aElement, sAttrName, null);
  }

  /**
   * The latest version of XercesJ 2.9 returns an empty string for non existing
   * attributes. To differentiate between empty attributes and non-existing
   * attributes, this method returns a default value for non existing
   * attributes.
   *
   * @param aElement
   *        the source element to get the attribute from. May not be
   *        <code>null</code>.
   * @param sAttrName
   *        the name of the attribute to query. May not be <code>null</code>.
   * @param sDefault
   *        the value to be returned if the attribute is not present.
   * @return the default value if the attribute does not exists, the string
   *         value otherwise
   */
  @Nullable
  public static String getAttributeValue (@Nonnull final Element aElement,
                                          @Nonnull final String sAttrName,
                                          @Nullable final String sDefault)
  {
    final Attr aAttr = aElement.getAttributeNode (sAttrName);
    return aAttr == null ? sDefault : aAttr.getValue ();
  }

  @Nullable
  public static Map <String, String> getAllAttributesAsMap (@Nullable final Element aSrcNode)
  {
    if (aSrcNode != null)
    {
      final NamedNodeMap aNNM = aSrcNode.getAttributes ();
      if (aNNM != null)
      {
        final Map <String, String> aMap = new LinkedHashMap <String, String> (aNNM.getLength ());
        final int nMax = aNNM.getLength ();
        for (int i = 0; i < nMax; ++i)
        {
          final Attr aAttr = (Attr) aNNM.item (i);
          aMap.put (aAttr.getName (), aAttr.getValue ());
        }
        return aMap;
      }
    }
    return null;
  }

  @Nonnull
  public static String getXMLNSPrefix (@Nonnull final String sPrefix)
  {
    if (StringHelper.hasNoText (sPrefix) || sPrefix.contains (CXML.XML_PREFIX_NAMESPACE_SEP_STR))
      throw new IllegalArgumentException ("prefix is invalid: " + sPrefix);
    return CXML.XML_ATTR_XMLNS_WITH_SEP + sPrefix;
  }

  @Nullable
  public static String getNamespaceURI (@Nullable final Node aNode)
  {
    if (aNode instanceof Document)
      return getNamespaceURI (((Document) aNode).getDocumentElement ());
    if (aNode != null)
      return aNode.getNamespaceURI ();
    return null;
  }
}