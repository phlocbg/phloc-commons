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
package com.phloc.commons.io.resourceprovider;

import java.net.MalformedURLException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IReadableResourceProvider;
import com.phloc.commons.io.resource.URLResource;
import com.phloc.commons.string.ToStringGenerator;

/**
 * The URL resource provider.
 * 
 * @author philip
 */
@Immutable
public final class URLResourceProvider implements IReadableResourceProvider
{
  public boolean supportsReading (@Nullable final String sName)
  {
    return URLResource.isExplicitURLResource (sName);
  }

  public IReadableResource getReadableResource (@Nonnull final String sURL)
  {
    if (sURL == null)
      throw new NullPointerException ("URL");

    try
    {
      return new URLResource (sURL);
    }
    catch (final MalformedURLException ex)
    {
      throw new IllegalArgumentException ("Passed name '" + sURL + "' is not an URL!", ex);
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof URLResourceProvider))
      return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
