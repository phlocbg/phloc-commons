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
package com.phloc.types.dyntypes.base;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.serialize.XMLReader;
import com.phloc.commons.xml.serialize.XMLWriter;
import com.phloc.types.datatype.impl.SimpleDataTypeRegistry;
import com.phloc.types.dyntypes.impl.AbstractDynamicValue;

/**
 * Dynamic value for objects of class {@link Node}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueDOMNode extends AbstractDynamicValue <Node>
{
  public DynamicValueDOMNode ()
  {
    this (null);
  }

  public DynamicValueDOMNode (@Nullable final Node aNode)
  {
    super (SimpleDataTypeRegistry.DT_DOMNODE, aNode);
  }

  @Nullable
  public String getAsSerializationText ()
  {
    final Node aValue = getValue ();
    return aValue == null ? null : XMLWriter.getXMLString (aValue);
  }

  @Nonnull
  public ESuccess setAsSerializationText (@Nullable final String sText)
  {
    if (StringHelper.hasNoText (sText))
    {
      setValue (null);
      return ESuccess.SUCCESS;
    }

    Node aNode = null;
    try
    {
      aNode = XMLReader.readXMLDOM (sText);
      if (aNode != null)
      {
        setValue (aNode);
        return ESuccess.SUCCESS;
      }
    }
    catch (final SAXException ex)
    {
      // fall-through
    }
    return ESuccess.FAILURE;
  }

  @Nullable
  public String getAsDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    return getAsSerializationText ();
  }

  @Nonnull
  public DynamicValueDOMNode getClone ()
  {
    // Also clone the contained node
    final Node aValue = getValue ();
    return new DynamicValueDOMNode (aValue == null ? null : aValue.cloneNode (true));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof DynamicValueDOMNode))
      return false;
    final DynamicValueDOMNode rhs = (DynamicValueDOMNode) o;
    final Node aObj1 = getValue ();
    final Node aObj2 = rhs.getValue ();
    return (aObj1 == aObj2) || (aObj1 != null && aObj1.isEqualNode (aObj2));
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (getValue ()).getHashCode ();
  }
}
