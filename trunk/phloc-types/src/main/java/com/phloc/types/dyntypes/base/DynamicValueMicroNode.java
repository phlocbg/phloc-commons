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
package com.phloc.types.dyntypes.base;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.types.datatype.impl.SimpleDataTypeRegistry;
import com.phloc.types.dyntypes.impl.AbstractDynamicValue;

/**
 * Dynamic value for objects of class {@link IMicroNode}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueMicroNode extends AbstractDynamicValue <IMicroNode>
{
  public DynamicValueMicroNode ()
  {
    this (null);
  }

  public DynamicValueMicroNode (@Nullable final IMicroNode aNode)
  {
    super (SimpleDataTypeRegistry.DT_MICRONODE, aNode);
  }

  @Nullable
  public String getAsSerializationText ()
  {
    final IMicroNode aValue = getValue ();
    return aValue == null ? null : MicroWriter.getXMLString (aValue);
  }

  @Nonnull
  public ESuccess setAsSerializationText (@Nullable final String sText)
  {
    if (StringHelper.hasNoText (sText))
      setValue (null);
    else
    {
      final IMicroNode aNode = MicroReader.readMicroXML (sText);
      if (aNode != null)
        setValue (aNode);
      else
        return ESuccess.FAILURE;
    }
    return ESuccess.SUCCESS;
  }

  @Nullable
  public String getAsDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    return getAsSerializationText ();
  }

  @Nonnull
  public DynamicValueMicroNode getClone ()
  {
    // Also clone the contained node
    final IMicroNode aValue = getValue ();
    return new DynamicValueMicroNode (aValue == null ? null : aValue.getClone ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof DynamicValueMicroNode))
      return false;
    final DynamicValueMicroNode rhs = (DynamicValueMicroNode) o;
    final IMicroNode aObj1 = getValue ();
    final IMicroNode aObj2 = rhs.getValue ();
    return (aObj1 == aObj2) || (aObj1 != null && aObj1.isEqualContent (aObj2));
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (getValue ()).getHashCode ();
  }
}
