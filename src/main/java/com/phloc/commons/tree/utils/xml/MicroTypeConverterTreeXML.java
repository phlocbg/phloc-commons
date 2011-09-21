package com.phloc.commons.tree.utils.xml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverterRegistry;
import com.phloc.commons.string.StringHelper;

/**
 * A special implementation of {@link IConverterTreeXML} that uses the
 * conversion rules stored in the {@link MicroTypeConverterRegistry}.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The data type to be converted
 */
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
    if (StringHelper.hasNoText (sElementName))
      throw new IllegalArgumentException ("elementName");
    if (aNativeClass == null)
      throw new NullPointerException ("nativeClass");
    m_sNamespaceURI = sNamespaceURI;
    m_sElementName = sElementName;
    m_aNativeClass = aNativeClass;
  }

  public void appendDataValue (@Nonnull final IMicroElement eDataElement, @Nullable final DATATYPE aObject)
  {
    // Append created element - or null if the passed object is null
    eDataElement.appendChild (MicroTypeConverterRegistry.convertToMicroElement (aObject,
                                                                                m_sNamespaceURI,
                                                                                m_sElementName));
  }

  @Nullable
  public DATATYPE getAsDataValue (@Nonnull final IMicroElement eDataElement)
  {
    final IMicroElement eChildElement = eDataElement.getFirstChildElement ();
    if (eChildElement != null)
    {
      if (!CompareUtils.nullSafeEquals (m_sNamespaceURI, eChildElement.getNamespaceURI ()))
        throw new IllegalStateException ("Namespace mismatch! Expected: " + m_sNamespaceURI);
      if (!m_sElementName.equals (eChildElement.getTagName ()))
        throw new IllegalStateException ("Tag name mismatch! Expected: " + m_sElementName);
    }
    return MicroTypeConverterRegistry.convertToNative (eChildElement, m_aNativeClass);
  }
}
