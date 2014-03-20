/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A filter that only accepts certain file names, based on a regular expression.
 * If at least one regular expressions is fulfilled, the file is accepted. The
 * filter is applied on directories and files!
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class FilenameFilterMatchAnyRegEx implements FilenameFilter
{
  private final List <String> m_aRegExs;

  public FilenameFilterMatchAnyRegEx (@Nonnull @Nonempty final String sRegEx)
  {
    this (new String [] { sRegEx });
  }

  public FilenameFilterMatchAnyRegEx (@Nonnull @Nonempty final String... aRegExs)
  {
    m_aRegExs = ContainerHelper.newList (ValueEnforcer.notEmpty (aRegExs, "RegularExpressions"));
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getRegularExpressions ()
  {
    return ContainerHelper.newList (m_aRegExs);
  }

  public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
  {
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    if (sRealName == null)
      return false;
    return m_aRegExs.stream ().filter (p -> RegExHelper.stringMatchesPattern (p, sRealName)).findFirst ().isPresent ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("regExs", m_aRegExs).toString ();
  }
}
