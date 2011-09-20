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
package com.phloc.commons.microdom.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.commons.xml.CXML;
import com.phloc.commons.xml.CXMLRegEx;

/**
 * Default implementation of the {@link IMicroElement} interface.
 * 
 * @author philip
 */
public final class MicroElement extends AbstractMicroNodeWithChildren implements IMicroElement
{
  private String m_sNamespaceURI;
  private final String m_sTagName;
  private Map <String, String> m_aAttrs;

  MicroElement (@Nullable final String sNamespaceURI, @Nonnull @Nonempty final String sTagName)
  {
    if (StringHelper.hasNoText (sTagName))
      throw new IllegalArgumentException ("No valid tag name specified");
    m_sNamespaceURI = sNamespaceURI;

    // Store only the local name (cut the prefix) if a namespace is present
    final int nPrefixEnd = sNamespaceURI != null ? sTagName.indexOf (CXML.XML_PREFIX_NAMESPACE_SEP) : -1;
    m_sTagName = nPrefixEnd == -1 ? sTagName : sTagName.substring (nPrefixEnd + 1);

    // Only for the dev version
    if (GlobalDebug.isDebugMode ())
      if (!CXMLRegEx.PATTERN_NAME.matcher (m_sTagName).matches ())
        throw new IllegalArgumentException ("The passed element name '" + m_sTagName + "' is not a valid element name!");
  }

  @Nonnull
  public String getNodeName ()
  {
    return m_sTagName;
  }

  public boolean hasAttributes ()
  {
    return m_aAttrs != null && !m_aAttrs.isEmpty ();
  }

  @Nullable
  @ReturnsImmutableObject
  public Map <String, String> getAttributes ()
  {
    return hasAttributes () ? ContainerHelper.makeUnmodifiable (m_aAttrs) : null;
  }

  @Nullable
  public String getAttribute (@Nullable final String sAttrName)
  {
    return m_aAttrs == null ? null : m_aAttrs.get (sAttrName);
  }

  @Nullable
  public <DSTTYPE> DSTTYPE getAttributeWithConversion (@Nullable final String sAttrName,
                                                       @Nonnull final Class <DSTTYPE> aDstClass)
  {
    final String sAttrVal = getAttribute (sAttrName);
    // Avoid having a conversion issue with empty strings!
    if (StringHelper.hasNoText (sAttrVal))
      return null;
    // throws IllegalArgumentException if nothing can be converted
    final DSTTYPE ret = TypeConverter.convertIfNecessary (sAttrVal, aDstClass);
    return ret;
  }

  public boolean hasAttribute (@Nullable final String sAttrName)
  {
    return m_aAttrs != null && m_aAttrs.containsKey (sAttrName);
  }

  @Nonnull
  public EChange removeAttribute (@Nullable final String sAttrName)
  {
    return EChange.valueOf (m_aAttrs != null && m_aAttrs.remove (sAttrName) != null);
  }

  @Nonnull
  public IMicroElement setAttribute (final String sAttrName, final String sAttrValue)
  {
    if (StringHelper.hasNoText (sAttrName))
      throw new IllegalArgumentException ("No valid attribute name passed");

    // Only for the dev version
    if (GlobalDebug.isDebugMode ())
    {
      if (!CXMLRegEx.PATTERN_NAME.matcher (sAttrName).matches ())
        throw new IllegalArgumentException ("The passed attribute name '" +
                                            sAttrName +
                                            "' is not a valid attribute name!");
      if (false)
        if (!CXMLRegEx.PATTERN_ATTVALUE.matcher (sAttrValue).matches ())
          throw new IllegalArgumentException ("The passed attribute value '" +
                                              sAttrValue +
                                              "' is not a valid attribute value!");
      // multi line attributes are valid in XHTML 1.0 Transitional!
      if (false)
        if (sAttrValue != null && sAttrValue.indexOf ('\n') != -1)
          throw new IllegalArgumentException ("The passed attribute value '" +
                                              sAttrValue +
                                              "' contains new line characters!");
    }

    if (sAttrValue != null)
    {
      if (m_aAttrs == null)
        m_aAttrs = new LinkedHashMap <String, String> ();
      m_aAttrs.put (sAttrName, sAttrValue);
    }
    else
      removeAttribute (sAttrName);
    return this;
  }

  @Nonnull
  public IMicroElement setAttribute (@Nonnull final String sAttrName, @Nullable final int nAttrValue)
  {
    return setAttribute (sAttrName, Integer.toString (nAttrValue));
  }

  @Nonnull
  public IMicroElement setAttribute (@Nonnull final String sAttrName, @Nullable final long nAttrValue)
  {
    return setAttribute (sAttrName, Long.toString (nAttrValue));
  }

  @Nonnull
  public IMicroElement setAttributeWithConversion (@Nonnull final String sAttrName, @Nullable final Object aAttrValue)
  {
    final String sValue = TypeConverter.convertIfNecessary (aAttrValue, String.class);
    return setAttribute (sAttrName, sValue);
  }

  @Nonnull
  public EChange removeAllAttributes ()
  {
    if (ContainerHelper.isEmpty (m_aAttrs))
      return EChange.UNCHANGED;
    m_aAttrs.clear ();
    return EChange.CHANGED;
  }

  @Nullable
  public String getNamespaceURI ()
  {
    return m_sNamespaceURI;
  }

  public boolean hasNamespaceURI (@Nullable final String sNamespaceURI)
  {
    return CompareUtils.nullSafeEquals (m_sNamespaceURI, sNamespaceURI);
  }

  @Nonnull
  public EChange setNamespaceURI (@Nullable final String sNamespaceURI)
  {
    if (CompareUtils.nullSafeEquals (m_sNamespaceURI, sNamespaceURI))
      return EChange.UNCHANGED;
    m_sNamespaceURI = sNamespaceURI;
    return EChange.CHANGED;
  }

  @Nullable
  public String getLocalName ()
  {
    return m_sNamespaceURI == null ? null : m_sTagName;
  }

  @Nonnull
  public String getTagName ()
  {
    return m_sTagName;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getChildElements ()
  {
    final List <IMicroElement> ret = new ArrayList <IMicroElement> ();
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
      {
        if (aChild.isElement ())
        {
          ret.add ((IMicroElement) aChild);
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
                ret.add ((IMicroElement) aContChild);
          }
      }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getChildElements (@Nullable final String sTagName)
  {
    final List <IMicroElement> ret = new ArrayList <IMicroElement> ();
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          final IMicroElement aChildElement = (IMicroElement) aChild;
          if (aChildElement.getTagName ().equals (sTagName))
            ret.add (aChildElement);
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                final IMicroElement aContChildElement = (IMicroElement) aContChild;
                if (aContChildElement.getTagName ().equals (sTagName))
                  ret.add (aContChildElement);
              }
          }

    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getChildElements (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
  {
    if (StringHelper.hasNoText (sNamespaceURI))
      return getChildElements (sLocalName);

    final List <IMicroElement> ret = new ArrayList <IMicroElement> ();
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          final IMicroElement aChildElement = (IMicroElement) aChild;
          if (aChildElement.hasNamespaceURI (sNamespaceURI) && aChildElement.getLocalName ().equals (sLocalName))
            ret.add (aChildElement);
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                final IMicroElement aContChildElement = (IMicroElement) aContChild;
                if (aContChildElement.hasNamespaceURI (sNamespaceURI) &&
                    aContChildElement.getLocalName ().equals (sLocalName))
                  ret.add (aContChildElement);
              }
          }
    return ret;
  }

  public boolean hasChildElements ()
  {
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          return true;
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
                return true;
          }
    return false;
  }

  public boolean hasChildElements (@Nullable final String sTagName)
  {
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          if (((IMicroElement) aChild).getTagName ().equals (sTagName))
            return true;
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                if (((IMicroElement) aContChild).getTagName ().equals (sTagName))
                  return true;
              }
          }
    return false;
  }

  public boolean hasChildElements (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
  {
    if (StringHelper.hasNoText (sNamespaceURI))
      return hasChildElements (sLocalName);

    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          final IMicroElement aChildElement = (IMicroElement) aChild;
          if (aChildElement.hasNamespaceURI (sNamespaceURI) && aChildElement.getLocalName ().equals (sLocalName))
            return true;
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                final IMicroElement aContChildElement = (IMicroElement) aContChild;
                if (aContChildElement.hasNamespaceURI (sNamespaceURI) &&
                    aContChildElement.getLocalName ().equals (sLocalName))
                  return true;
              }
          }
    return false;
  }

  @Nullable
  public IMicroElement getFirstChildElement ()
  {
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
          return (IMicroElement) aChild;
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
                return (IMicroElement) aContChild;
          }
    return null;
  }

  @Nullable
  public IMicroElement getFirstChildElement (@Nullable final String sTagName)
  {
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          final IMicroElement aChildElement = (IMicroElement) aChild;
          if (aChildElement.getTagName ().equals (sTagName))
            return aChildElement;
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                final IMicroElement aContChildElement = (IMicroElement) aContChild;
                if (aContChildElement.getTagName ().equals (sTagName))
                  return aContChildElement;
              }
          }
    return null;
  }

  @Nullable
  public IMicroElement getFirstChildElement (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
  {
    if (StringHelper.hasNoText (sNamespaceURI))
      return getFirstChildElement (sLocalName);

    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          final IMicroElement aChildElement = (IMicroElement) aChild;
          if (aChildElement.hasNamespaceURI (sNamespaceURI) && aChildElement.getLocalName ().equals (sLocalName))
            return aChildElement;
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                final IMicroElement aContChildElement = (IMicroElement) aContChild;
                if (aContChildElement.hasNamespaceURI (sNamespaceURI) &&
                    aContChildElement.getLocalName ().equals (sLocalName))
                  return aContChildElement;
              }
          }
    return null;
  }

  @Nullable
  public String getTextContent ()
  {
    if (!hasChildren ())
      return null;

    final StringBuilder aSB = new StringBuilder ();
    for (final IMicroNode aChild : directGetChildren ())
      if (aChild.isText ())
      {
        // ignore whitespace-only content
        if (!((IMicroText) aChild).isElementContentWhitespace ())
          aSB.append (aChild.getNodeValue ());
      }
      else
        if (aChild.isCDATA ())
        {
          aSB.append (aChild.getNodeValue ());
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isText ())
              {
                // ignore whitespace-only content
                if (!((IMicroText) aContChild).isElementContentWhitespace ())
                  aSB.append (aContChild.getNodeValue ());
              }
              else
                if (aContChild.isCDATA ())
                {
                  aSB.append (aContChild.getNodeValue ());
                }
          }
    return aSB.toString ();
  }

  @Nullable
  public <DSTTYPE> DSTTYPE getTextContentWithConversion (@Nonnull final Class <DSTTYPE> aDstClass)
  {
    // Get the regular content
    final String sTextContent = getTextContent ();

    // Avoid having a conversion issue with empty strings!
    if (StringHelper.hasNoText (sTextContent))
      return null;
    final DSTTYPE ret = TypeConverter.convertIfNecessary (sTextContent, aDstClass);
    return ret;
  }

  @Nonnull
  public IMicroElement getClone ()
  {
    final MicroElement ret = new MicroElement (m_sNamespaceURI, m_sTagName);

    // Copy attributes
    if (m_aAttrs != null)
      ret.m_aAttrs = new LinkedHashMap <String, String> (m_aAttrs);

    // Deep clone all child nodes
    if (hasChildren ())
      for (final IMicroNode aChildNode : directGetChildren ())
        ret.appendChild (aChildNode.getClone ());
    return ret;
  }

  @Override
  public boolean isEqualContent (final IMicroNode o)
  {
    if (o == this)
      return true;
    if (!super.isEqualContent (o))
      return false;
    final MicroElement rhs = (MicroElement) o;
    return CompareUtils.nullSafeEquals (m_sNamespaceURI, rhs.m_sNamespaceURI) &&
           m_sTagName.equals (rhs.m_sTagName) &&
           CompareUtils.nullSafeEquals (m_aAttrs, rhs.m_aAttrs);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .appendIfNotNull ("namespace", m_sNamespaceURI)
                            .append ("tagname", m_sTagName)
                            .appendIfNotNull ("attrs", m_aAttrs)
                            .toString ();
  }
}
