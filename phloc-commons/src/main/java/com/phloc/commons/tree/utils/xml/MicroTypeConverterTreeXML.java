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
package com.phloc.commons.tree.utils.xml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;

/**
 * A special implementation of {@link IConverterTreeXML} that uses the
 * conversion rules stored in the
 * {@link com.phloc.commons.microdom.convert.MicroTypeConverterRegistry}.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type to be converted
 */
@NotThreadSafe
public final class MicroTypeConverterTreeXML <DATATYPE> implements IConverterTreeXML <DATATYPE>
{
  private String m_sNamespaceURI;
  private final String m_sElementName;
  private final Class <? extends DATATYPE> m_aNativeClass;

  /**
   * Constructor
   * 
   * @param sElementName
   *        The element name to use. May neither be <code>null</code> nor empty
   * @param aNativeClass
   *        The data type class - required for reading. May be in an interface
   *        as well.
   */
  public MicroTypeConverterTreeXML (@Nonnull @Nonempty final String sElementName,
                                    @Nonnull final Class <? extends DATATYPE> aNativeClass)
  {
    this (null, sElementName, aNativeClass);
  }

  /**
   * Constructor
   * 
   * @param sNamespaceURI
   *        The namespace URI for the created element. May be <code>null</code>.
   * @param sElementName
   *        The element name to use. May neither be <code>null</code> nor empty
   * @param aNativeClass
   *        The data type class - required for reading. May be in an interface
   *        as well.
   */
  public MicroTypeConverterTreeXML (@Nullable final String sNamespaceURI,
                                    @Nonnull @Nonempty final String sElementName,
                                    @Nonnull final Class <? extends DATATYPE> aNativeClass)
  {
    this.m_sNamespaceURI = sNamespaceURI;
    this.m_sElementName = ValueEnforcer.notEmpty (sElementName, "ElementName");
    this.m_aNativeClass = ValueEnforcer.notNull (aNativeClass, "NativeClass");
  }

  @Override
  public void appendDataValue (@Nonnull final IMicroElement eDataElement, @Nullable final DATATYPE aObject)
  {
    // Append created element - or null if the passed object is null
    final IMicroElement eElement = MicroTypeConverter.convertToMicroElement (aObject,
                                                                             this.m_sNamespaceURI,
                                                                             this.m_sElementName);
    eDataElement.appendChild (eElement);
  }

  @Override
  @Nullable
  public DATATYPE getAsDataValue (@Nonnull final IMicroElement eDataElement)
  {
    final IMicroElement eChildElement = eDataElement.getFirstChildElement ();
    if (eChildElement != null)
    {
      if (!EqualsUtils.equals (this.m_sNamespaceURI, eChildElement.getNamespaceURI ()))
        throw new IllegalStateException ("Namespace mismatch! Expected: " + this.m_sNamespaceURI);
      if (!this.m_sElementName.equals (eChildElement.getTagName ()))
        throw new IllegalStateException ("Tag name mismatch! Expected: " + this.m_sElementName);
    }
    return MicroTypeConverter.convertToNative (eChildElement, this.m_aNativeClass);
  }

  /**
   * Factory method.
   * 
   * @param <DATATYPE>
   *        data type
   * @param sElementName
   *        The element name to use. May neither be <code>null</code> nor empty
   * @param aNativeClass
   *        The data type class - required for reading. May be in an interface
   *        as well.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> MicroTypeConverterTreeXML <DATATYPE> create (@Nonnull @Nonempty final String sElementName,
                                                                        @Nonnull final Class <? extends DATATYPE> aNativeClass)
  {
    return new MicroTypeConverterTreeXML <DATATYPE> (sElementName, aNativeClass);
  }

  /**
   * Factory method
   * 
   * @param <DATATYPE>
   *        data type
   * @param sNamespaceURI
   *        The namespace URI for the created element. May be <code>null</code>.
   * @param sElementName
   *        The element name to use. May neither be <code>null</code> nor empty
   * @param aNativeClass
   *        The data type class - required for reading. May be in an interface
   *        as well.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static <DATATYPE> MicroTypeConverterTreeXML <DATATYPE> create (@Nullable final String sNamespaceURI,
                                                                        @Nonnull @Nonempty final String sElementName,
                                                                        @Nonnull final Class <? extends DATATYPE> aNativeClass)
  {
    return new MicroTypeConverterTreeXML <DATATYPE> (sNamespaceURI, sElementName, aNativeClass);
  }
}
