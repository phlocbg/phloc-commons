/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.types.beans;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;

/**
 * A helper class to create names required for bean handling.
 * 
 * @author Philip Helger
 */
@Immutable
public final class BeanNameHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final BeanNameHelper s_aInstance = new BeanNameHelper ();

  private BeanNameHelper ()
  {}

  @Nonnull
  public static String getGetterName (@Nullable final String s)
  {
    return "get" + beanUpperCaseFirstChar (s);
  }

  @Nonnull
  public static String getIsGetterName (@Nullable final String s)
  {
    return "is" + beanUpperCaseFirstChar (s);
  }

  @Nonnull
  public static String getSetterName (@Nullable final String s)
  {
    return "set" + beanUpperCaseFirstChar (s);
  }

  @Nullable
  public static String beanUpperCaseFirstChar (@Nullable final String s)
  {
    final int nLength = StringHelper.getLength (s);
    if (nLength == 0)
      return s;

    // The whole string can be upper-cased
    if (nLength == 1)
      return s.toUpperCase (Locale.US);

    // If the second char is upper case, don't change the case...
    if (Character.isUpperCase (s.charAt (1)))
      return s;

    // upper case the first letter
    return Character.toUpperCase (s.charAt (0)) + s.substring (1);
  }

  @Nullable
  public static String beanLowerCaseFirstChar (@Nullable final String s)
  {
    final int nLength = StringHelper.getLength (s);
    if (nLength == 0)
      return s;

    // The whole string can be lower-cased
    if (nLength == 1)
      return s.toLowerCase (Locale.US);

    // If the second char is upper case, don't change the case...
    if (Character.isUpperCase (s.charAt (1)))
      return s;

    // lower case the first letter
    return Character.toLowerCase (s.charAt (0)) + s.substring (1);
  }

  @Nullable
  public static String beanCamelCaseName (@Nullable final String sInput)
  {
    if (StringHelper.hasNoText (sInput))
      return sInput;

    // required if a string contains both "." and "_"
    final String s = RegExHelper.stringReplacePattern ("\\.", sInput, "_");

    // avoid creating StringBuilder if not necessary
    if (s.indexOf ('_') == -1)
      return beanUpperCaseFirstChar (RegExHelper.stringReplacePattern ("\\$", s, "_"));

    final StringBuilder ret = new StringBuilder (s.length ());
    for (final String sPart : StringHelper.getExploded ('_', s))
      ret.append (beanUpperCaseFirstChar (sPart));
    // This replacement is required for nested classes!
    return RegExHelper.stringReplacePattern ("\\$", ret.toString (), "_");
  }

  @Nullable
  public static String beanLowerCaseFirstCharCamelCaseName (@Nullable final String s)
  {
    return beanLowerCaseFirstChar (beanCamelCaseName (s));
  }

  @Nullable
  public static String beanLowerCaseFirstCharLocalName (@Nonnull final String s)
  {
    return beanLowerCaseFirstCharCamelCaseName (CGStringHelper.getClassLocalName (s));
  }

  @Nullable
  public static String beanUpperCaseFirstCharLocalName (@Nonnull final String s)
  {
    return beanCamelCaseName (CGStringHelper.getClassLocalName (s));
  }

  /**
   * Convert a given name to plural. For consistency only a "List" is appended.
   * 
   * @param sSingular
   *        The string to pluralize. May not be <code>null</code> or empty.
   * @return The pluralized string.
   */
  @Nonnull
  public static String getPlural (@Nonnull final String sSingular)
  {
    if (StringHelper.hasNoText (sSingular))
      throw new IllegalArgumentException ("passed text is empty");
    return sSingular + "List";
  }
}
