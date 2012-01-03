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
package com.phloc.commons.xml.namespace;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.iterate.SingleElementIterator;

/**
 * Represents an abstract namespace context that does the predefined mapping as
 * stated in the Javadoc.
 * 
 * @author philip
 */
public abstract class AbstractNamespaceContext implements NamespaceContext
{
  @Nullable
  public abstract String getDefaultNamespaceURI ();

  @Nullable
  protected abstract Iterator <String> getCustomPrefixes (@Nonnull String sNamespaceURI);

  @Nonnull
  public final Iterator <?> getPrefixes (@Nonnull final String sNamespaceURI)
  {
    // According to JavaDoc
    if (sNamespaceURI == null)
      throw new IllegalArgumentException ();
    if (sNamespaceURI.equals (XMLConstants.XML_NS_URI))
      return new SingleElementIterator <String> (XMLConstants.XML_NS_PREFIX);
    if (sNamespaceURI.equals (XMLConstants.XMLNS_ATTRIBUTE_NS_URI))
      return new SingleElementIterator <String> (XMLConstants.XMLNS_ATTRIBUTE);

    final Iterator <?> ret = getCustomPrefixes (sNamespaceURI);
    return ret != null ? ret : ContainerHelper.<String> getEmptyIterator ();
  }

  @Nullable
  protected abstract String getCustomPrefix (@Nonnull String sNamespaceURI);

  @Nullable
  public final String getPrefix (@Nonnull final String sNamespaceURI)
  {
    // According to JavaDoc
    if (sNamespaceURI == null)
      throw new IllegalArgumentException ();
    if (sNamespaceURI.equals (XMLConstants.XML_NS_URI))
      return XMLConstants.XML_NS_PREFIX;
    if (sNamespaceURI.equals (XMLConstants.XMLNS_ATTRIBUTE_NS_URI))
      return XMLConstants.XMLNS_ATTRIBUTE;
    if (sNamespaceURI.equals (getDefaultNamespaceURI ()))
      return XMLConstants.DEFAULT_NS_PREFIX;

    return getCustomPrefix (sNamespaceURI);
  }

  @Nullable
  protected abstract String getCustomNamespaceURI (@Nonnull String sPrefix);

  @Nonnull
  public final String getNamespaceURI (@Nonnull final String sPrefix)
  {
    // According to JavaDoc
    if (sPrefix == null)
      throw new IllegalArgumentException ();
    if (sPrefix.equals (XMLConstants.XML_NS_PREFIX))
      return XMLConstants.XML_NS_URI;
    if (sPrefix.equals (XMLConstants.XMLNS_ATTRIBUTE))
      return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
    if (sPrefix.equals (XMLConstants.DEFAULT_NS_PREFIX))
    {
      final String sDefNSURI = getDefaultNamespaceURI ();
      return sDefNSURI != null ? sDefNSURI : XMLConstants.NULL_NS_URI;
    }

    final String ret = getCustomNamespaceURI (sPrefix);
    return ret != null ? ret : XMLConstants.NULL_NS_URI;
  }
}
