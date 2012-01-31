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
package com.phloc.commons.xml.ls;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.resource.URLResource;
import com.phloc.commons.string.StringHelper;

/**
 * A simple LS resource resolver that can handle URLs, JAR files and file system
 * resources.
 * 
 * @author philip
 */
public class SimpleLSResourceResolver implements LSResourceResolver
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SimpleLSResourceResolver.class);
  private final LSResourceResolver m_aParentResolver;

  public SimpleLSResourceResolver ()
  {
    this (null);
  }

  public SimpleLSResourceResolver (@Nullable final LSResourceResolver aParentResolver)
  {
    m_aParentResolver = aParentResolver;
  }

  /**
   * Do the standard resource resolving of sSystemId relative to sBaseURI
   * 
   * @param sSystemId
   *        The resource to search
   * @param sBaseURI
   *        The base URI from where the search is initiated.
   * @return The non-<code>null</code> resource. May be non-existing!
   * @throws URISyntaxException
   *         In case the conversion from system ID to URL fails
   * @throws IOException
   *         In case the file resolution (to an absolute file) fails.
   */
  @Nonnull
  public static final IReadableResource doStandardResourceResolving (final String sSystemId, final String sBaseURI) throws URISyntaxException,
                                                                                                                   IOException
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Trying to resolve  resource " + sSystemId + " from base " + sBaseURI);

    if (sSystemId.contains ("://"))
    {
      // Destination system ID seems to be an absolute URL!
      return new URLResource (sSystemId);
    }

    if (StringHelper.startsWith (sBaseURI, "jar:file:"))
    {
      // Base URI is inside a jar file
      final int i = sBaseURI.indexOf ('!');
      String sRelativePath = i < 0 ? sBaseURI : sBaseURI.substring (i + 1);

      // Skip any potentially leading path separator
      if (FilenameHelper.startsWithPathSeparatorChar (sRelativePath))
        sRelativePath = sRelativePath.substring (1);

      // Create relative URL!
      final File aParent = new File (sRelativePath).getParentFile ();
      final String sPath = FilenameHelper.getCleanPath (aParent == null ? sSystemId : aParent.getPath () +
                                                                                      '/' +
                                                                                      sSystemId);

      // Build result (must contain forward slashes!)
      return new ClassPathResource (sPath);
    }

    URL aBaseURL = null;
    try
    {
      // Try whether the base is a URI
      aBaseURL = new URL (sBaseURI);
      if (!aBaseURL.getProtocol ().equals (URLResource.PROTOCOL_FILE))
      {
        // Handle "file" protocol separately
        return new URLResource (FilenameHelper.getCleanConcatenatedUrlPath (sBaseURI, sSystemId));
      }
    }
    catch (final MalformedURLException ex)
    {
      // Base is not a URL
    }

    // Base is not a URL
    File aBase;
    if (aBaseURL != null)
      aBase = new File (aBaseURL.toURI ().getSchemeSpecificPart ());
    else
      aBase = new File (sBaseURI);

    if (StringHelper.hasNoText (sSystemId))
    {
      // Nothing to resolve
      return new FileSystemResource (aBase);
    }

    // Get the system ID file
    File aSystemId;
    try
    {
      aSystemId = new File (new URL (sSystemId).toURI ().getSchemeSpecificPart ());
    }
    catch (final MalformedURLException ex)
    {
      aSystemId = new File (sSystemId);
    }

    final File aParent = aBase.getParentFile ();
    final File aResFile = new File (aParent, aSystemId.getPath ()).getCanonicalFile ().getAbsoluteFile ();
    return new FileSystemResource (aResFile);
  }

  @OverrideOnDemand
  @Nullable
  protected IReadableResource resolveResource (final String sSystemId, final String sBaseURI) throws Exception
  {
    return doStandardResourceResolving (sSystemId, sBaseURI);
  }

  /**
   * @param sType
   *        e.g. XSD - some constant - don't ask me
   * @param sNamespaceURI
   *        The destination namespace
   * @param sPublicId
   *        null
   * @param sSystemId
   *        the path of the resource to find - may be relative to the including
   *        resource
   * @param sBaseURI
   *        The systemId of the including resource.
   */
  @Nullable
  public final LSInput resolveResource (final String sType,
                                        final String sNamespaceURI,
                                        final String sPublicId,
                                        final String sSystemId,
                                        final String sBaseURI)
  {
    try
    {
      // Try to get the resource
      final IReadableResource aResolvedResource = resolveResource (sSystemId, sBaseURI);
      if (aResolvedResource != null)
        return new ResourceLSInput (aResolvedResource);
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("Failed to resolve resource relative to '" + sBaseURI + "'", ex);
    }

    // Pass to parent (if available)
    return m_aParentResolver == null ? null : m_aParentResolver.resolveResource (sType,
                                                                                 sNamespaceURI,
                                                                                 sPublicId,
                                                                                 sSystemId,
                                                                                 sBaseURI);
  }
}
