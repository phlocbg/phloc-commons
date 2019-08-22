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
package com.phloc.commons.microdom.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.microdom.EMicroNodeType;
import com.phloc.commons.microdom.IHasAttributeValue;
import com.phloc.commons.microdom.IHasElementName;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.commons.xml.CXML;
import com.phloc.commons.xml.CXMLRegEx;

/**
 * Default implementation of the {@link IMicroElement} interface.
 *
 * @author Boris Gregorcic
 */
public final class MicroElement extends AbstractMicroNodeWithChildren implements IMicroElement
{
  private static final long serialVersionUID = 1301312611126433950L;

  private static final Logger s_aLogger = LoggerFactory.getLogger (MicroElement.class);

  private String m_sNamespaceURI;
  private final String m_sTagName;
  private Map <String, String> m_aAttrs;

  public MicroElement (@Nonnull final IHasElementName aElementNameProvider)
  {
    this (null, aElementNameProvider.getElementName ());
  }

  public MicroElement (@Nullable final String sNamespaceURI, @Nonnull final IHasElementName aElementNameProvider)
  {
    this (sNamespaceURI, aElementNameProvider.getElementName ());
  }

  public MicroElement (@Nonnull @Nonempty final String sTagName)
  {
    this (null, sTagName);
  }

  public MicroElement (@Nullable final String sNamespaceURI, @Nonnull @Nonempty final String sTagName)
  {
    ValueEnforcer.notEmpty (sTagName, "TagName"); //$NON-NLS-1$
    this.m_sNamespaceURI = sNamespaceURI;

    // Store only the local name (cut the prefix) if a namespace is present
    final int nPrefixEnd = sNamespaceURI != null ? sTagName.indexOf (CXML.XML_PREFIX_NAMESPACE_SEP) : -1;
    if (nPrefixEnd == -1)
      this.m_sTagName = sTagName;
    else
    {
      // Cut the prefix
      s_aLogger.warn ("Removing micro element namespace prefix '" + //$NON-NLS-1$
                      sTagName.substring (0, nPrefixEnd) +
                      "' from tag name '" + //$NON-NLS-1$
                      sTagName +
                      "'"); //$NON-NLS-1$
      this.m_sTagName = sTagName.substring (nPrefixEnd + 1);
    }

    // Only for the debug version, as this slows things down heavily
    // BG 30.07.2019: deactivated this check all together. Was anyway only in
    // debug mode and showed false positives (XML standard allows umlauts and
    // this did not accept tags called 'blüte'
    // if (GlobalDebug.isDebugMode ())
    // if (!CXMLRegEx.PATTERN_NAME_QUICK.matcher (this.m_sTagName).matches ())
    // if (!CXMLRegEx.PATTERN_NAME.matcher (this.m_sTagName).matches ())
    // throw new IllegalArgumentException ("The micro element tag name '" +
    // //$NON-NLS-1$
    // this.m_sTagName +
    // "' is not a valid element name!"); //$NON-NLS-1$
  }

  @Override
  @Nonnull
  public EMicroNodeType getType ()
  {
    return EMicroNodeType.ELEMENT;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getNodeName ()
  {
    return this.m_sTagName;
  }

  @Override
  public boolean hasAttributes ()
  {
    return this.m_aAttrs != null && !this.m_aAttrs.isEmpty ();
  }

  @Override
  @Nonnegative
  public int getAttributeCount ()
  {
    return this.m_aAttrs == null ? 0 : this.m_aAttrs.size ();
  }

  @Override
  @Nullable
  @ReturnsMutableCopy
  public Map <String, String> getAllAttributes ()
  {
    return hasAttributes () ? ContainerHelper.newOrderedMap (this.m_aAttrs) : null;
  }

  @Override
  @Nullable
  @ReturnsMutableCopy
  public Set <String> getAllAttributeNames ()
  {
    return hasAttributes () ? ContainerHelper.newOrderedSet (this.m_aAttrs.keySet ()) : null;
  }

  @Override
  @Nullable
  @ReturnsMutableCopy
  public List <String> getAllAttributeValues ()
  {
    return hasAttributes () ? ContainerHelper.newList (this.m_aAttrs.values ()) : null;
  }

  @Override
  @Nullable
  public String getAttribute (@Nullable final String sAttrName)
  {
    return this.m_aAttrs == null ? null : this.m_aAttrs.get (sAttrName);
  }

  @Override
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

  @Override
  @Nullable
  public Integer getAttributeAsInteger (final String sAttrName)
  {
    return StringParser.parseIntObj (getAttribute (sAttrName));
  }

  @Override
  @Nullable
  public Boolean getAttributeAsBoolean (final String sAttrName)
  {
    final String sValue = getAttribute (sAttrName);
    if (StringHelper.hasText (sValue))
    {
      return StringParser.parseBoolObj (sValue);
    }
    return null;
  }

  @Override
  public boolean hasAttribute (@Nullable final String sAttrName)
  {
    return this.m_aAttrs != null && this.m_aAttrs.containsKey (sAttrName);
  }

  @Override
  @Nonnull
  public EChange removeAttribute (@Nullable final String sAttrName)
  {
    return EChange.valueOf (this.m_aAttrs != null && this.m_aAttrs.remove (sAttrName) != null);
  }

  @Override
  @Nonnull
  public MicroElement setAttribute (@Nonnull @Nonempty final String sAttrName, @Nullable final String sAttrValue)
  {
    ValueEnforcer.notEmpty (sAttrName, "AttrName"); //$NON-NLS-1$

    // Only for the dev version
    if (GlobalDebug.isDebugMode ())
    {
      if (!CXMLRegEx.PATTERN_NAME_QUICK.matcher (sAttrName).matches ())
        if (!CXMLRegEx.PATTERN_NAME.matcher (sAttrName).matches ())
          throw new IllegalArgumentException ("The passed attribute name '" + //$NON-NLS-1$
                                              sAttrName +
                                              "' is not a valid attribute name!"); //$NON-NLS-1$
    }

    if (sAttrValue != null)
    {
      if (this.m_aAttrs == null)
        this.m_aAttrs = new LinkedHashMap <String, String> ();
      this.m_aAttrs.put (sAttrName, sAttrValue);
    }
    else
      removeAttribute (sAttrName);
    return this;
  }

  @Override
  @Nonnull
  public MicroElement setAttribute (@Nonnull final String sAttrName,
                                    @Nonnull final IHasAttributeValue aAttrValueProvider)
  {
    ValueEnforcer.notNull (aAttrValueProvider, "AttrValueProvider"); //$NON-NLS-1$

    return setAttribute (sAttrName, aAttrValueProvider.getAttrValue ());
  }

  @Override
  @Nonnull
  public IMicroElement setAttribute (@Nonnull final String sAttrName, final int nAttrValue)
  {
    return setAttribute (sAttrName, Integer.toString (nAttrValue));
  }

  @Override
  @Nonnull
  public IMicroElement setAttribute (@Nonnull final String sAttrName, final long nAttrValue)
  {
    return setAttribute (sAttrName, Long.toString (nAttrValue));
  }

  @Override
  @Nonnull
  public IMicroElement setAttributeWithConversion (@Nonnull final String sAttrName, @Nullable final Object aAttrValue)
  {
    final String sValue = TypeConverter.convertIfNecessary (aAttrValue, String.class);
    return setAttribute (sAttrName, sValue);
  }

  @Override
  @Nonnull
  public EChange removeAllAttributes ()
  {
    if (ContainerHelper.isEmpty (this.m_aAttrs))
      return EChange.UNCHANGED;
    this.m_aAttrs.clear ();
    return EChange.CHANGED;
  }

  @Override
  @Nullable
  public String getNamespaceURI ()
  {
    return this.m_sNamespaceURI;
  }

  @Override
  @Nonnull
  public EChange setNamespaceURI (@Nullable final String sNamespaceURI)
  {
    if (EqualsUtils.equals (this.m_sNamespaceURI, sNamespaceURI))
      return EChange.UNCHANGED;
    this.m_sNamespaceURI = sNamespaceURI;
    return EChange.CHANGED;
  }

  @Override
  public boolean hasNamespaceURI ()
  {
    return StringHelper.hasText (this.m_sNamespaceURI);
  }

  @Override
  public boolean hasNoNamespaceURI ()
  {
    return StringHelper.hasNoText (this.m_sNamespaceURI);
  }

  @Override
  public boolean hasNamespaceURI (@Nullable final String sNamespaceURI)
  {
    return EqualsUtils.equals (this.m_sNamespaceURI, sNamespaceURI);
  }

  @Override
  @Nullable
  public String getLocalName ()
  {
    return this.m_sNamespaceURI == null ? null : this.m_sTagName;
  }

  @Override
  @Nonnull
  public String getTagName ()
  {
    return this.m_sTagName;
  }

  @Override
  @Nonnegative
  public int getChildElementCount ()
  {
    int ret = 0;
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
      {
        if (aChild.isElement ())
        {
          ++ret;
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
                ++ret;
          }
      }
    return ret;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getAllChildElements ()
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

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getAllChildElements (@Nullable final String sTagName)
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

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getAllChildElements (@Nullable final String sNamespaceURI,
                                                   @Nullable final String sLocalName)
  {
    if (StringHelper.hasNoText (sNamespaceURI))
      return getAllChildElements (sLocalName);

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

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getAllChildElements (@Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider"); //$NON-NLS-1$
    return getAllChildElements (aElementNameProvider.getElementName ());
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getAllChildElements (@Nullable final String sNamespaceURI,
                                                   @Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider"); //$NON-NLS-1$
    return getAllChildElements (sNamespaceURI, aElementNameProvider.getElementName ());
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <IMicroElement> getAllChildElementsRecursive ()
  {
    final List <IMicroElement> ret = new ArrayList <IMicroElement> ();
    if (hasChildren ())
      for (final IMicroNode aChild : directGetChildren ())
        if (aChild.isElement ())
        {
          final IMicroElement aChildElement = (IMicroElement) aChild;
          ret.add (aChildElement);
          ret.addAll (aChildElement.getAllChildElementsRecursive ());
        }
        else
          if (aChild.isContainer () && aChild.hasChildren ())
          {
            for (final IMicroNode aContChild : aChild.getChildren ())
              if (aContChild.isElement ())
              {
                final MicroElement aContChildElement = (MicroElement) aContChild;
                ret.add (aContChildElement);
                ret.addAll (aContChildElement.getAllChildElementsRecursive ());
              }
          }
    return ret;
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
  public boolean hasChildElements (@Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider"); //$NON-NLS-1$
    return hasChildElements (aElementNameProvider.getElementName ());
  }

  @Override
  public boolean hasChildElements (@Nullable final String sNamespaceURI,
                                   @Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider"); //$NON-NLS-1$
    return hasChildElements (sNamespaceURI, aElementNameProvider.getElementName ());
  }

  @Override
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

  @Override
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

  @Override
  public String getFirstChildElementValue (final String sTagName)
  {
    final IMicroElement eValue = getFirstChildElement (sTagName);
    if (eValue != null)
    {
      return eValue.getTextContent ();
    }
    return null;
  }

  @Override
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

  @Override
  @Nullable
  public IMicroElement getFirstChildElement (@Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider"); //$NON-NLS-1$
    return getFirstChildElement (aElementNameProvider.getElementName ());
  }

  @Override
  @Nullable
  public IMicroElement getFirstChildElement (@Nullable final String sNamespaceURI,
                                             @Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider"); //$NON-NLS-1$
    return getFirstChildElement (sNamespaceURI, aElementNameProvider.getElementName ());
  }

  @Override
  @Nonnull
  public IMicroElement getClone ()
  {
    final MicroElement ret = new MicroElement (this.m_sNamespaceURI, this.m_sTagName);

    // Copy attributes
    if (this.m_aAttrs != null)
      ret.m_aAttrs = ContainerHelper.newOrderedMap (this.m_aAttrs);

    // Deep clone all child nodes
    if (hasChildren ())
      for (final IMicroNode aChildNode : directGetChildren ())
        ret.appendChild (aChildNode.getClone ());
    return ret;
  }

  @Override
  public boolean isEqualContent (@Nullable final IMicroNode o)
  {
    if (o == this)
      return true;
    if (!super.isEqualContent (o))
      return false;
    final MicroElement rhs = (MicroElement) o;
    return EqualsUtils.equals (this.m_sNamespaceURI, rhs.m_sNamespaceURI) &&
           this.m_sTagName.equals (rhs.m_sTagName) &&
           EqualsUtils.equals (this.m_aAttrs, rhs.m_aAttrs);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .appendIfNotNull ("namespace", this.m_sNamespaceURI) //$NON-NLS-1$
                            .append ("tagname", this.m_sTagName) //$NON-NLS-1$
                            .appendIfNotNull ("attrs", this.m_aAttrs) //$NON-NLS-1$
                            .toString ();
  }
}
