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
package com.phloc.commons.url;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IURLData} interface.
 * 
 * @author philip
 */
@Immutable
public final class URLData implements IURLData
{
  private final String m_sPath;
  private final Map <String, String> m_aParams;
  private final String m_sAnchor;

  public URLData (@Nonnull final String sPath)
  {
    this (sPath, null, null);
  }

  public URLData (@Nonnull final String sPath, @Nullable final Map <String, String> aParams)
  {
    this (sPath, aParams, null);
  }

  public URLData (@Nonnull final String sPath,
                  @Nullable final Map <String, String> aParams,
                  @Nullable final String sAnchor)
  {
    if (sPath == null)
      throw new NullPointerException ("href may not be null");
    m_sPath = sPath;
    m_aParams = aParams;
    m_sAnchor = sAnchor;
  }

  @Nonnull
  public String getPath ()
  {
    return m_sPath;
  }

  @Nullable
  @ReturnsImmutableObject
  public Map <String, String> getParams ()
  {
    return ContainerHelper.makeUnmodifiable (m_aParams);
  }

  @Nullable
  @ReturnsMutableObject (reason = "Performance reasons")
  public Map <String, String> directGetParams ()
  {
    return m_aParams;
  }

  @Nullable
  public String getAnchor ()
  {
    return m_sAnchor;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("path", m_sPath)
                                       .appendIfNotNull ("params", m_aParams)
                                       .appendIfNotNull ("anchor", m_sAnchor)
                                       .toString ();
  }
}