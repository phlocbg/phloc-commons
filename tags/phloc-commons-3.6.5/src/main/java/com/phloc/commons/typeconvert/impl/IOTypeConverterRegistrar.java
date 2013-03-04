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
package com.phloc.commons.typeconvert.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.resource.URLResource;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;

/**
 * Register the IO specific type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class IOTypeConverterRegistrar implements ITypeConverterRegistrarSPI
{
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    // File
    aRegistry.registerTypeConverter (File.class, String.class, new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        return ((File) aSource).getAbsolutePath ();
      }
    });
    aRegistry.registerTypeConverter (String.class, File.class, new ITypeConverter ()
    {
      public File convert (@Nonnull final Object aSource)
      {
        return new File ((String) aSource);
      }
    });

    final ITypeConverter aConvertResourceToString = new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        return ((IReadableResource) aSource).getPath ();
      }
    };
    final ITypeConverter aConvertResourceToURL = new ITypeConverter ()
    {
      public URL convert (@Nonnull final Object aSource)
      {
        return ((IReadableResource) aSource).getAsURL ();
      }
    };
    // ClassPathResource
    aRegistry.registerTypeConverter (ClassPathResource.class, String.class, aConvertResourceToString);
    aRegistry.registerTypeConverter (String.class, ClassPathResource.class, new ITypeConverter ()
    {
      public ClassPathResource convert (@Nonnull final Object aSource)
      {
        return new ClassPathResource ((String) aSource);
      }
    });
    aRegistry.registerTypeConverter (ClassPathResource.class, URL.class, aConvertResourceToURL);
    aRegistry.registerTypeConverter (URL.class, ClassPathResource.class, new ITypeConverter ()
    {
      public ClassPathResource convert (@Nonnull final Object aSource)
      {
        return new ClassPathResource (((URL) aSource).toExternalForm ());
      }
    });

    // FileSystemResource
    aRegistry.registerTypeConverter (FileSystemResource.class, String.class, aConvertResourceToString);
    aRegistry.registerTypeConverter (String.class, FileSystemResource.class, new ITypeConverter ()
    {
      public FileSystemResource convert (@Nonnull final Object aSource)
      {
        return new FileSystemResource ((String) aSource);
      }
    });
    aRegistry.registerTypeConverter (FileSystemResource.class, URL.class, aConvertResourceToURL);
    aRegistry.registerTypeConverter (URL.class, FileSystemResource.class, new ITypeConverter ()
    {
      public FileSystemResource convert (@Nonnull final Object aSource)
      {
        try
        {
          final URI aURI = ((URL) aSource).toURI ();
          return new FileSystemResource (new File (aURI));
        }
        catch (final IllegalArgumentException e)
        {
          // When passing a "http://..." URL into the file ctor
        }
        catch (final URISyntaxException e)
        {}
        return null;
      }
    });

    // URLResource
    aRegistry.registerTypeConverter (URLResource.class, String.class, aConvertResourceToString);
    aRegistry.registerTypeConverter (String.class, URLResource.class, new ITypeConverter ()
    {
      public URLResource convert (@Nonnull final Object aSource)
      {
        try
        {
          return new URLResource ((String) aSource);
        }
        catch (final MalformedURLException e)
        {
          return null;
        }
      }
    });
    aRegistry.registerTypeConverter (URLResource.class, URL.class, aConvertResourceToURL);
    aRegistry.registerTypeConverter (URL.class, URLResource.class, new ITypeConverter ()
    {
      public URLResource convert (@Nonnull final Object aSource)
      {
        return new URLResource ((URL) aSource);
      }
    });
  }
}