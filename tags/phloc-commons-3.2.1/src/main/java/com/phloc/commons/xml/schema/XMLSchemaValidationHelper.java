/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.xml.schema;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.xml.sax.CollectingSAXErrorHandler;
import com.phloc.commons.xml.transform.ResourceStreamSource;

/**
 * A helper class for simple XSD validation.
 * 
 * @author philip
 */
public final class XMLSchemaValidationHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final XMLSchemaValidationHelper s_aInstance = new XMLSchemaValidationHelper ();

  private XMLSchemaValidationHelper ()
  {}

  @Nonnull
  public static IResourceErrorGroup validate (@Nonnull final IReadableResource aSchema,
                                              @Nonnull final IReadableResource aXML)
  {
    return validate (new IReadableResource [] { aSchema }, aXML);
  }

  @Nonnull
  public static IResourceErrorGroup validate (@Nonnull @Nonempty final IReadableResource [] aSchemas,
                                              @Nonnull final IReadableResource aXML)
  {
    if (aXML == null)
      throw new NullPointerException ("XMLresource");

    return validate (aSchemas, new ResourceStreamSource (aXML));
  }

  @Nonnull
  public static IResourceErrorGroup validate (@Nonnull final Schema aSchema, @Nonnull final IReadableResource aXML)
  {
    if (aXML == null)
      throw new NullPointerException ("XMLresource");

    return validate (aSchema, new ResourceStreamSource (aXML));
  }

  @Nonnull
  public static IResourceErrorGroup validate (@Nonnull @Nonempty final IReadableResource aSchema,
                                              @Nonnull final Source aXML)
  {
    return validate (new IReadableResource [] { aSchema }, aXML);
  }

  @Nonnull
  public static IResourceErrorGroup validate (@Nonnull @Nonempty final IReadableResource [] aSchemas,
                                              @Nonnull final Source aXML)
  {
    // Get Schema from XMLSchemaCache
    return validate (XMLSchemaCache.getInstance ().getSchema (aSchemas), aXML);
  }

  @Nonnull
  public static IResourceErrorGroup validate (@Nonnull final Schema aSchema, @Nonnull final Source aXML)
  {
    if (aSchema == null)
      throw new NullPointerException ("schema");
    if (aXML == null)
      throw new NullPointerException ("XMLsource");

    // Build the validator
    final Validator aValidator = aSchema.newValidator ();
    final CollectingSAXErrorHandler aErrHdl = new CollectingSAXErrorHandler ();
    aValidator.setErrorHandler (aErrHdl);
    try
    {
      aValidator.validate (aXML, null);
    }
    catch (final Exception ex)
    {
      // Most likely the input XML document is invalid
      throw new IllegalArgumentException ("Failed to validate the XML " + aXML + " against " + aSchema, ex);
    }
    return aErrHdl.getResourceErrors ();
  }
}
