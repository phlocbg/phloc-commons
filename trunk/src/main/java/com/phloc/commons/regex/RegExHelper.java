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
package com.phloc.commons.regex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;

/**
 * This class offers helper methods that work on cached regular expression
 * pattern as offered by {@link RegExPool}.
 * 
 * @author philip
 */
@Immutable
public final class RegExHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final RegExHelper s_aInstance = new RegExHelper ();

  private RegExHelper ()
  {}

  /**
   * Split the passed text with the given regular expression.
   * 
   * @param sText
   *        The text to be split. May be <code>null</code>.
   * @param sRegEx
   *        The regular expression to use for splitting. May neither be
   *        <code>null</code> nor empty.
   * @return An empty array if the text is <code>null</code>, a non-
   *         <code>null</code> array otherwise. If both text and regular
   *         expression are <code>null</code> an empty array is returned as well
   *         since the text parameter is checked first.
   */
  @Nonnull
  public static String [] split (@Nullable final CharSequence sText, @Nonnull @RegEx final String sRegEx)
  {
    if (sText == null)
      return new String [0];
    return RegExPool.getPattern (sRegEx).split (sText);
  }

  /**
   * Split the passed text with the given regular expression returning at most
   * the given number of tokens.
   * 
   * @param sText
   *        The text to be split. May be <code>null</code>.
   * @param sRegEx
   *        The regular expression to use for splitting. May neither be
   *        <code>null</code> nor empty.
   * @param nLimit
   *        The maximum number of tokens to return if the value is &gt; 0. If
   *        the value is &le; 0 it has no effect and all tokens are returned.
   * @return An empty array if the text is <code>null</code>, a non-
   *         <code>null</code> array otherwise. If both text and regular
   *         expression are <code>null</code> an empty array is returned as well
   *         since the text parameter is checked first.
   */
  @Nonnull
  public static String [] split (@Nullable final CharSequence sText,
                                 @Nonnull @RegEx final String sRegEx,
                                 @Nonnegative final int nLimit)
  {
    if (sText == null)
      return new String [0];
    return RegExPool.getPattern (sRegEx).split (sText, nLimit);
  }

  /**
   * Split the passed text with the given regular expression.
   * 
   * @param sText
   *        The text to be split. May be <code>null</code>.
   * @param sRegEx
   *        The regular expression to use for splitting. May neither be
   *        <code>null</code> nor empty.
   * @return An empty list if the text is <code>null</code>, a non-
   *         <code>null</code> list otherwise. If both text and regular
   *         expression are <code>null</code> an empty list is returned as well
   *         since the text parameter is checked first.
   */
  @Nonnull
  public static List <String> splitToList (@Nullable final CharSequence sText, @Nonnull @RegEx final String sRegEx)
  {
    return ContainerHelper.newList (split (sText, sRegEx));
  }

  /**
   * Split the passed text with the given regular expression.
   * 
   * @param sText
   *        The text to be split. May be <code>null</code>.
   * @param sRegEx
   *        The regular expression to use for splitting. May neither be
   *        <code>null</code> nor empty.
   * @param nLimit
   *        The maximum number of tokens to return if the value is &gt; 0. If
   *        the value is &le; 0 it has no effect and all tokens are returned.
   * @return An empty list if the text is <code>null</code>, a non-
   *         <code>null</code> list otherwise. If both text and regular
   *         expression are <code>null</code> an empty list is returned as well
   *         since the text parameter is checked first.
   */
  @Nonnull
  public static List <String> splitToList (@Nullable final CharSequence sText,
                                           @Nonnull @RegEx final String sRegEx,
                                           @Nonnegative final int nLimit)
  {
    return ContainerHelper.newList (split (sText, sRegEx, nLimit));
  }

  /**
   * Get the Java Matcher object for the passed pair of regular expression and
   * value.
   * 
   * @param sRegEx
   *        The regular expression to use. May neither be <code>null</code> nor
   *        empty.
   * @param sValue
   *        The value to create the matcher for. May not be <code>null</code>.
   * @return A non-<code>null</code> matcher.
   */
  @Nonnull
  public static Matcher getMatcher (@Nonnull @RegEx final String sRegEx, @Nonnull final String sValue)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    return RegExPool.getPattern (sRegEx).matcher (sValue);
  }

  /**
   * Get the Java Matcher object for the passed pair of regular expression and
   * value.
   * 
   * @param sRegEx
   *        The regular expression to use. May neither be <code>null</code> nor
   *        empty.
   * @param nOptions
   *        The pattern compilations options to be used.
   * @param sValue
   *        The value to create the matcher for. May not be <code>null</code>.
   * @return A non-<code>null</code> matcher.
   * @see Pattern#compile(String, int)
   */
  @Nonnull
  public static Matcher getMatcher (@Nonnull @RegEx final String sRegEx,
                                    @Nonnegative final int nOptions,
                                    @Nonnull final String sValue)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    return RegExPool.getPattern (sRegEx, nOptions).matcher (sValue);
  }

  /**
   * A shortcut helper method to determine whether a string matches a certain
   * regular expression or not.
   * 
   * @param sRegEx
   *        The regular expression to be used. The compiled regular expression
   *        pattern is cached. May neither be <code>null</code> nor empty.
   * @param sValue
   *        The string value to compare against the regular expression.
   * @return <code>true</code> if the string matches the regular expression,
   *         <code>false</code> otherwise.
   */
  public static boolean stringMatchesPattern (@Nonnull @RegEx final String sRegEx, @Nonnull final String sValue)
  {
    return getMatcher (sRegEx, sValue).matches ();
  }

  /**
   * A shortcut helper method to determine whether a string matches a certain
   * regular expression or not.
   * 
   * @param sRegEx
   *        The regular expression to be used. The compiled regular expression
   *        pattern is cached. May neither be <code>null</code> nor empty.
   * @param nOptions
   *        The pattern compilations options to be used.
   * @param sValue
   *        The string value to compare against the regular expression.
   * @return <code>true</code> if the string matches the regular expression,
   *         <code>false</code> otherwise.
   * @see Pattern#compile(String, int)
   */
  public static boolean stringMatchesPattern (@Nonnull @RegEx final String sRegEx,
                                              @Nonnegative final int nOptions,
                                              @Nonnull final String sValue)
  {
    return getMatcher (sRegEx, nOptions, sValue).matches ();
  }

  @Nonnull
  public static String stringReplacePattern (@Nonnull @RegEx final String sRegEx,
                                             @Nonnull final String sValue,
                                             @Nullable final String sReplacement)
  {
    // Avoid NPE on invalid replacement parameter
    return getMatcher (sRegEx, sValue).replaceAll (StringHelper.getNotNull (sReplacement));
  }

  @Nonnull
  public static String stringReplacePattern (@Nonnull @RegEx final String sRegEx,
                                             @Nonnegative final int nOptions,
                                             @Nonnull final String sValue,
                                             @Nullable final String sReplacement)
  {
    // Avoid NPE on invalid replacement parameter
    return getMatcher (sRegEx, nOptions, sValue).replaceAll (StringHelper.getNotNull (sReplacement));
  }

  /**
   * Convert an identifier to a programming language identifier by replacing all
   * non-word characters with an underscore.
   * 
   * @param s
   *        The string to convert. May be <code>null</code> or empty.
   * @return The converted string or <code>null</code> if the input string is
   *         <code>null</code>.
   */
  @Nullable
  public static String makeIdentifier (@Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return s;

    // replace all non-word characters with under scores
    final String ret = stringReplacePattern ("\\W", s, "_");
    if (!Character.isJavaIdentifierStart (ret.charAt (0)))
      return "_" + ret;
    return ret;
  }

  /**
   * Check if the passed regular expression is invalid.<br>
   * Note: this method may be a performance killer, as it calls
   * {@link Pattern#compile(String)} each time, which is CPU intensive and has a
   * synchronization penalty.
   * 
   * @param sRegEx
   *        The regular expression to validate. May not be <code>null</code>.
   * @return <code>true</code> if the pattern is valid, <code>false</code>
   *         otherwise.
   */
  public static boolean isValidPattern (@Nonnull @RegEx final String sRegEx)
  {
    try
    {
      Pattern.compile (sRegEx);
      return true;
    }
    catch (final PatternSyntaxException ex)
    {
      return false;
    }
  }
}
