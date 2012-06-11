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
package com.phloc.commons.xml.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.CXML;

/**
 * Abstract XML serializer implementation that works with IMicroNode and
 * org.w3c.dom.Node objects.
 * 
 * @author philip
 * @param <NODETYPE>
 */
public abstract class AbstractSerializerPhloc <NODETYPE> implements IXMLSerializer <NODETYPE>
{
  protected static final String NEWLINE = CGlobal.LINE_SEPARATOR;
  protected static final String INDENT = "  ";

  /**
   * Contains the XML namespace definitions for a single element.
   * 
   * @author philip
   */
  protected static final class NamespaceLevel
  {
    private static final Logger s_aLogger = LoggerFactory.getLogger (NamespaceLevel.class);
    private String m_sDefaultNamespaceURI;
    private Map <String, String> m_aURL2PrefixMap;

    public NamespaceLevel (@Nullable final Map <String, String> aAttrs)
    {
      // check all attributes
      if (aAttrs != null)
        for (final Map.Entry <String, String> aEntry : aAttrs.entrySet ())
        {
          final String sAttrName = aEntry.getKey ();
          if (sAttrName.equals (CXML.XML_ATTR_XMLNS))
          {
            // set default namespace (xmlns)
            addPrefixNamespaceMapping (null, aEntry.getValue ());
          }
          else
            if (sAttrName.startsWith (CXML.XML_ATTR_XMLNS_WITH_SEP))
            {
              // prefixed namespace (xmlns:...)
              addPrefixNamespaceMapping (sAttrName.substring (CXML.XML_ATTR_XMLNS_WITH_SEP.length ()),
                                         aEntry.getValue ());
            }
        }
    }

    /**
     * Get the URL matching a given namespace prefix in this level.
     * 
     * @param sPrefix
     *        The prefix to be searched. If it is <code>null</code> the default
     *        namespace URL is returned.
     * @return <code>null</code> if the namespace mapping is not used or the URL
     *         otherwise.
     */
    @Nullable
    public String getNamespaceOfPrefix (@Nullable final String sPrefix)
    {
      if (StringHelper.hasNoText (sPrefix))
        return m_sDefaultNamespaceURI;

      if (m_aURL2PrefixMap != null)
        for (final Map.Entry <String, String> aEntry : m_aURL2PrefixMap.entrySet ())
          if (aEntry.getValue ().equals (sPrefix))
            return aEntry.getKey ();
      return null;
    }

    void addPrefixNamespaceMapping (@Nullable final String sPrefix, @Nonnull @Nonempty final String sNamespace)
    {
      if (s_aLogger.isTraceEnabled ())
        s_aLogger.trace ("Adding namespace mapping " + sPrefix + ":" + sNamespace);

      // namespace prefix uniqueness check
      final String sExistingURI = getNamespaceOfPrefix (sPrefix);
      if (sExistingURI != null && !sExistingURI.equals (sNamespace))
        s_aLogger.warn ("Overwriting namespace prefix '" +
                        sPrefix +
                        "' to use URL '" +
                        sNamespace +
                        "' instead of '" +
                        sExistingURI +
                        "'");

      if (StringHelper.hasNoText (sPrefix))
        m_sDefaultNamespaceURI = sNamespace;
      else
      {
        if (m_aURL2PrefixMap == null)
          m_aURL2PrefixMap = new HashMap <String, String> ();
        m_aURL2PrefixMap.put (sNamespace, sPrefix);
      }
    }

    @Nullable
    public String getDefaultNamespaceURI ()
    {
      return m_sDefaultNamespaceURI;
    }

    @Nullable
    public String getPrefixOfNamespace (@Nullable final String sNamespace)
    {
      // is it the default?
      if (m_sDefaultNamespaceURI != null && m_sDefaultNamespaceURI.equals (sNamespace))
        return null;

      return m_aURL2PrefixMap == null ? null : m_aURL2PrefixMap.get (sNamespace);
    }

    @Nonnegative
    int getNamespaceCount ()
    {
      return (m_aURL2PrefixMap == null ? 0 : m_aURL2PrefixMap.size ()) + (m_sDefaultNamespaceURI == null ? 0 : 1);
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("defaultNSURL", m_sDefaultNamespaceURI)
                                         .append ("url2prefix", m_aURL2PrefixMap)
                                         .toString ();
    }
  }

  /**
   * Contains the hierarchy of XML namespaces within a document structure.
   * 
   * @author philip
   */
  protected static final class NamespaceStack
  {
    private final List <NamespaceLevel> m_aStack = new ArrayList <NamespaceLevel> ();
    private final NamespaceContext m_aNamespaceCtx;

    public NamespaceStack (@Nullable final NamespaceContext aNamespaceCtx)
    {
      m_aNamespaceCtx = aNamespaceCtx;
    }

    public void push (@Nullable final Map <String, String> aAttrs)
    {
      // add at front
      final NamespaceLevel aNSL = new NamespaceLevel (aAttrs);
      m_aStack.add (0, aNSL);
    }

    public void addNamespaceMapping (final String sPrefix, final String sNamespace)
    {
      // Add the namespace to the current level
      ContainerHelper.getFirstElement (m_aStack).addPrefixNamespaceMapping (sPrefix, sNamespace);
    }

    public void pop ()
    {
      // remove at front
      m_aStack.remove (0);
    }

    @Nullable
    public String getDefaultNamespace ()
    {
      // iterate from front to end
      for (final NamespaceLevel aNSLevel : m_aStack)
        if (aNSLevel.getDefaultNamespaceURI () != null)
          return aNSLevel.getDefaultNamespaceURI ();

      // no default namespace
      return null;
    }

    /**
     * Resolve the given namespace URI to a prefix.
     * 
     * @param sNamespace
     *        The namespace URI to resolve. May be <code>null</code>.
     * @return <code>null</code> if no namespace prefix is required.
     */
    @Nullable
    public String findPrefix (@Nullable final String sNamespace)
    {
      // no namespace URI?
      if (StringHelper.hasNoText (sNamespace))
        return null;

      // is it the default namespace?
      if (sNamespace.equals (getDefaultNamespace ()))
        return null;

      // find existing prefix
      for (final NamespaceLevel aNSLevel : m_aStack)
      {
        final String sPrefix = aNSLevel.getPrefixOfNamespace (sNamespace);
        if (sPrefix != null)
          return sPrefix;
      }

      // no matching prefix found
      return null;
    }

    private boolean _containsAnyNamespace ()
    {
      for (final NamespaceLevel aNSLevel : m_aStack)
        if (aNSLevel.getNamespaceCount () > 0)
          return true;
      return false;
    }

    /**
     * Check if the whole prefix is used somewhere in the stack.
     * 
     * @param sPrefix
     *        The prefix to be checked
     * @return <code>true</code> if somewhere in the stack, the specified prefix
     *         is already used
     */
    private boolean _containsPrefix (@Nonnull final String sPrefix)
    {
      // find existing prefix
      for (final NamespaceLevel aNSLevel : m_aStack)
        if (aNSLevel.getNamespaceOfPrefix (sPrefix) != null)
          return true;
      return false;
    }

    /**
     * Create a new namespace prefix.
     * 
     * @param sElementNamespaceURI
     *        The namespace URI for which the prefix should be created.
     * @return <code>null</code> or empty if the default namespace is available,
     *         the prefix otherwise.
     */
    @Nullable
    public String createUniquePrefix (@Nonnull @Nonempty final String sElementNamespaceURI)
    {
      // If a mapping is defined, it always takes precedence over the default
      // namespace
      if (m_aNamespaceCtx != null)
      {
        // Is a mapping defined?
        final String sPrefix = m_aNamespaceCtx.getPrefix (sElementNamespaceURI);
        if (sPrefix != null)
          return sPrefix;
      }

      // Is the default namespace available?
      if (!_containsAnyNamespace ())
      {
        // Use the default namespace
        return null;
      }

      // find a unique prefix
      int nCount = 0;
      while (_containsPrefix ("ns" + nCount))
        ++nCount;
      return "ns" + nCount;
    }
  }

  /**
   * The serialization settings
   */
  protected final IXMLWriterSettings m_aSettings;

  /**
   * helper variable: current indentation.
   */
  protected final StringBuilder m_aIndent = new StringBuilder (32);

  /**
   * Current stack of namespaces.
   */
  protected final NamespaceStack m_aNSStack;

  public AbstractSerializerPhloc (@Nonnull final IXMLWriterSettings aSettings)
  {
    if (aSettings == null)
      throw new NullPointerException ("settings");
    m_aSettings = aSettings;
    m_aNSStack = new NamespaceStack (aSettings.getNamespaceContext ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("settings", m_aSettings)
                                       .append ("sIndent", m_aIndent.toString ())
                                       .append ("namespaceStack", m_aNSStack)
                                       .toString ();
  }
}
