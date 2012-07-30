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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasName;

/**
 * Contains constants for parser features.
 * 
 * @author philip
 */
public enum EXMLParserFeature implements IHasName
{
  /**
   * Namespaces feature id (http://xml.org/sax/features/namespaces).
   */
  SAX_FEATURE_NAMESPACES ("http://xml.org/sax/features/namespaces"),

  /**
   * Namespace prefixes feature id
   * (http://xml.org/sax/features/namespace-prefixes).
   */
  SAX_FEATURE_NAMESPACE_PREFIXES ("http://xml.org/sax/features/namespace-prefixes"),

  /**
   * Validation feature id (http://xml.org/sax/features/validation).
   */
  SAX_FEATURE_VALIDATION ("http://xml.org/sax/features/validation"),

  /**
   * Schema validation feature id
   * (http://apache.org/xml/features/validation/schema).
   */
  SAX_FEATURE_SCHEMA_VALIDATION ("http://apache.org/xml/features/validation/schema"),

  /**
   * Schema full checking feature id
   * (http://apache.org/xml/features/validation/schema-full-checking).
   */
  SCHEMA_FULL_CHECKING_FEATURE_ID ("http://apache.org/xml/features/validation/schema-full-checking"),

  /**
   * Honour all schema locations feature id
   * (http://apache.org/xml/features/honour-all-schemaLocations).
   */
  HONOUR_ALL_SCHEMA_LOCATIONS_ID ("http://apache.org/xml/features/honour-all-schemaLocations"),

  /**
   * Validate schema annotations feature id
   * (http://apache.org/xml/features/validate-annotations).
   */
  VALIDATE_ANNOTATIONS_ID ("http://apache.org/xml/features/validate-annotations"),

  /**
   * Dynamic validation feature id
   * (http://apache.org/xml/features/validation/dynamic).
   */
  DYNAMIC_VALIDATION_FEATURE_ID ("http://apache.org/xml/features/validation/dynamic"),

  /**
   * XInclude feature id (http://apache.org/xml/features/xinclude).
   */
  XINCLUDE_FEATURE_ID ("http://apache.org/xml/features/xinclude"),

  /**
   * XInclude fixup base URIs feature id
   * (http://apache.org/xml/features/xinclude/fixup-base-uris).
   */
  XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID ("http://apache.org/xml/features/xinclude/fixup-base-uris"),

  /**
   * XInclude fixup language feature id
   * (http://apache.org/xml/features/xinclude/fixup-language).
   */
  XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID ("http://apache.org/xml/features/xinclude/fixup-language"),

  /**
   * Default parser name.
   */
  DEFAULT_PARSER_NAME ("org.apache.xerces.parsers.SAXParser"),

  /**
   * Standalone SAX parser? Read-only!
   */
  SAX_FEATURE_IS_STANDALONE ("http://xml.org/sax/features/is-standalone"),

  /**
   * SAX lexical handler. Directly pass in the object!
   */
  SAX_FEATURE_LEXICAL_HANDLER ("http://xml.org/sax/properties/lexical-handler");

  private final String m_sName;

  private EXMLParserFeature (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nullable
  public static EXMLParserFeature getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (EXMLParserFeature.class, sName);
  }
}
