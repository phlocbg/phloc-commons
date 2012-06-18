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
package com.phloc.commons.xml;

import javax.annotation.concurrent.Immutable;
import javax.xml.XMLConstants;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * This is just a workaround if Xerces is not in the build path. Normally you
 * would use the constants from the file org.apache.xerces.impl.Constants
 * 
 * @author Philip
 */
@Immutable
public final class CXML
{
  // DOM Level 3 features defined in Core:
  public static final String DOM_DISCARD_DEFAULT_CONTENT = "discard-default-content";
  public static final String DOM_NORMALIZE_CHARACTERS = "normalize-characters";
  public static final String DOM_CHECK_CHAR_NORMALIZATION = "check-character-normalization";
  public static final String DOM_WELLFORMED = "well-formed";
  public static final String DOM_SPLIT_CDATA = "split-cdata-sections";

  // DOM Load and Save:
  public static final String DOM_FORMAT_PRETTY_PRINT = "format-pretty-print";
  public static final String DOM_XMLDECL = "xml-declaration";
  public static final String DOM_UNKNOWNCHARS = "unknown-characters";
  public static final String DOM_CERTIFIED = "certified";
  public static final String DOM_DISALLOW_DOCTYPE = "disallow-doctype";
  public static final String DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS = "ignore-unknown-character-denormalizations";

  public static final String EVENT_DOMNODE_INSERTED = "DOMNodeInserted";

  // XML default names, namespaces and prefixes

  // xmlns:*
  public static final char XML_PREFIX_NAMESPACE_SEP = ':';
  public static final String XML_PREFIX_NAMESPACE_SEP_STR = Character.toString (XML_PREFIX_NAMESPACE_SEP);
  public static final String XML_ATTR_XMLNS = XMLConstants.XMLNS_ATTRIBUTE;
  public static final String XML_ATTR_XMLNS_WITH_SEP = XML_ATTR_XMLNS + XML_PREFIX_NAMESPACE_SEP;

  // xml:*
  public static final String XML_ATTR_XML_WITH_SEP = XMLConstants.XML_NS_PREFIX + XML_PREFIX_NAMESPACE_SEP;
  public static final String XML_ATTR_LANG = XML_ATTR_XML_WITH_SEP + "lang";
  public static final String XML_ATTR_BASE = XML_ATTR_XML_WITH_SEP + "base";

  // XML Schema Definition (XS and XSD) stuff:
  public static final String XML_NS_XSD = XMLConstants.W3C_XML_SCHEMA_NS_URI;
  public static final String XML_NS_PREFIX_XSD = "xsd";
  public static final String XMLNS_XSD = XML_ATTR_XMLNS_WITH_SEP + XML_NS_PREFIX_XSD;

  // XML Schema Instance (XSI) stuff:
  public static final String XML_NS_XSI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
  public static final String XML_NS_PREFIX_XSI = "xsi";
  public static final String XML_ATTR_XSI_SCHEMALOCATION = "schemaLocation";
  public static final String XML_ATTR_XSI_NONAMESPACESCHEMALOCATION = "noNamespaceSchemaLocation";
  public static final String XML_ATTR_XSD_TARGETNAMESPACE = "targetNamespace";

  public static final String XMLNS_XSI = XML_ATTR_XMLNS_WITH_SEP + XML_NS_PREFIX_XSI;
  public static final String XSI_SCHEMALOCATION = XML_NS_PREFIX_XSI +
                                                  XML_PREFIX_NAMESPACE_SEP +
                                                  XML_ATTR_XSI_SCHEMALOCATION;
  public static final String XSI_NONAMESPACESCHEMALOCATION = XML_NS_PREFIX_XSI +
                                                             XML_PREFIX_NAMESPACE_SEP +
                                                             XML_ATTR_XSI_NONAMESPACESCHEMALOCATION;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CXML s_aInstance = new CXML ();

  private CXML ()
  {}
}
