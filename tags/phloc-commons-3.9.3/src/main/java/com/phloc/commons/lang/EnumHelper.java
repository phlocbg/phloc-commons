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
package com.phloc.commons.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.id.IHasSimpleIntID;
import com.phloc.commons.name.IHasName;

/**
 * Some enum utility methods.
 * 
 * @author philip
 */
@Immutable
public final class EnumHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final EnumHelper s_aInstance = new EnumHelper ();

  private EnumHelper ()
  {}

  /**
   * Get the enum value with the passed ID
   * 
   * @param <KEYTYPE>
   *        The ID type
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param aID
   *        The ID to search
   * @return <code>null</code> if no enum item with the given ID is present.
   */
  @Nullable
  public static <KEYTYPE, ENUMTYPE extends Enum <ENUMTYPE> & IHasID <KEYTYPE>> ENUMTYPE getFromIDOrNull (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                         @Nullable final KEYTYPE aID)
  {
    return getFromIDOrDefault (aClass, aID, null);
  }

  /**
   * Get the enum value with the passed ID
   * 
   * @param <KEYTYPE>
   *        The ID type
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param aID
   *        The ID to search
   * @param aDefault
   *        The default value to be returned, if the ID was not found.
   * @return The default parameter if no enum item with the given ID is present.
   */
  @Nullable
  public static <KEYTYPE, ENUMTYPE extends Enum <ENUMTYPE> & IHasID <KEYTYPE>> ENUMTYPE getFromIDOrDefault (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                            @Nullable final KEYTYPE aID,
                                                                                                            @Nullable final ENUMTYPE aDefault)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    if (aID != null)
      for (final ENUMTYPE aElement : aClass.getEnumConstants ())
        if (aElement.getID ().equals (aID))
          return aElement;
    return aDefault;
  }

  /**
   * Get the enum value with the passed ID. If no such ID is present, an
   * {@link IllegalArgumentException} is thrown.
   * 
   * @param <KEYTYPE>
   *        The ID type
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param aID
   *        The ID to search
   * @return The enum item with the given ID. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         if no enum item with the given ID is present
   */
  @Nonnull
  public static <KEYTYPE, ENUMTYPE extends Enum <ENUMTYPE> & IHasID <KEYTYPE>> ENUMTYPE getFromIDOrThrow (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                          @Nullable final KEYTYPE aID)
  {
    final ENUMTYPE aEnum = getFromIDOrNull (aClass, aID);
    if (aEnum == null)
      throw new IllegalArgumentException ("Failed to resolve ID " + aID + " within class " + aClass);
    return aEnum;
  }

  /**
   * Get the enum value with the passed string ID case insensitive
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sID
   *        The ID to search
   * @return <code>null</code> if no enum item with the given ID is present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasID <String>> ENUMTYPE getFromIDCaseInsensitiveOrNull (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                              @Nullable final String sID)
  {
    return getFromIDCaseInsensitiveOrDefault (aClass, sID, null);
  }

  /**
   * Get the enum value with the passed string ID case insensitive
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sID
   *        The ID to search
   * @param aDefault
   *        The default value to be returned, if the ID was not found.
   * @return The default parameter if no enum item with the given ID is present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasID <String>> ENUMTYPE getFromIDCaseInsensitiveOrDefault (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                                 @Nullable final String sID,
                                                                                                                 @Nullable final ENUMTYPE aDefault)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    if (sID != null)
      for (final ENUMTYPE aElement : aClass.getEnumConstants ())
        if (aElement.getID ().equalsIgnoreCase (sID))
          return aElement;
    return aDefault;
  }

  /**
   * Get the enum value with the passed string ID (case insensitive). If no such
   * ID is present, an {@link IllegalArgumentException} is thrown.
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sID
   *        The ID to search
   * @return The enum item with the given ID. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         if no enum item with the given ID is present
   */
  @Nonnull
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasID <String>> ENUMTYPE getFromIDCaseInsensitiveOrThrow (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                               @Nullable final String sID)
  {
    final ENUMTYPE aEnum = getFromIDCaseInsensitiveOrNull (aClass, sID);
    if (aEnum == null)
      throw new IllegalArgumentException ("Failed to resolve ID " + sID + " within class " + aClass);
    return aEnum;
  }

  /**
   * Get the enum value with the passed ID
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param nID
   *        The ID to search
   * @return <code>null</code> if no enum item with the given ID is present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasSimpleIntID> ENUMTYPE getFromIDOrNull (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                               final int nID)
  {
    return getFromIDOrDefault (aClass, nID, null);
  }

  /**
   * Get the enum value with the passed ID
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param nID
   *        The ID to search
   * @param aDefault
   *        The default value to be returned, if the ID was not found.
   * @return The default parameter if no enum item with the given ID is present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasSimpleIntID> ENUMTYPE getFromIDOrDefault (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                  final int nID,
                                                                                                  @Nullable final ENUMTYPE aDefault)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    for (final ENUMTYPE aElement : aClass.getEnumConstants ())
      if (aElement.getID () == nID)
        return aElement;
    return aDefault;
  }

  /**
   * Get the enum value with the passed ID. If no such ID is present, an
   * {@link IllegalArgumentException} is thrown.
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param nID
   *        The ID to search
   * @return The enum item with the given ID. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         if no enum item with the given ID is present
   */
  @Nonnull
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasSimpleIntID> ENUMTYPE getFromIDOrThrow (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                final int nID)
  {
    final ENUMTYPE aEnum = getFromIDOrNull (aClass, nID);
    if (aEnum == null)
      throw new IllegalArgumentException ("Failed to resolve ID " + nID + " within class " + aClass);
    return aEnum;
  }

  /**
   * Get the enum value with the passed name
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sName
   *        The name to search
   * @return <code>null</code> if no enum item with the given name is present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasName> ENUMTYPE getFromNameOrNull (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                          @Nullable final String sName)
  {
    return getFromNameOrDefault (aClass, sName, null);
  }

  /**
   * Get the enum value with the passed name
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sName
   *        The name to search
   * @param aDefault
   *        The default value to be returned, if the name was not found.
   * @return The default parameter if no enum item with the given name is
   *         present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasName> ENUMTYPE getFromNameOrDefault (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                             @Nullable final String sName,
                                                                                             @Nullable final ENUMTYPE aDefault)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    if (sName != null)
      for (final ENUMTYPE aElement : aClass.getEnumConstants ())
        if (aElement.getName ().equals (sName))
          return aElement;
    return aDefault;
  }

  /**
   * Get the enum value with the passed name. If no such name is present, an
   * {@link IllegalArgumentException} is thrown.
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sName
   *        The name to search
   * @return The enum item with the given name. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         if no enum item with the given name is present
   */
  @Nonnull
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasName> ENUMTYPE getFromNameOrThrow (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                           @Nullable final String sName)
  {
    final ENUMTYPE aEnum = getFromNameOrNull (aClass, sName);
    if (aEnum == null)
      throw new IllegalArgumentException ("Failed to resolve name " + sName + " within class " + aClass);
    return aEnum;
  }

  /**
   * Get the enum value with the passed name case insensitive
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sName
   *        The name to search
   * @return <code>null</code> if no enum item with the given ID is present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasName> ENUMTYPE getFromNameCaseInsensitiveOrNull (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                         @Nullable final String sName)
  {
    return getFromNameCaseInsensitiveOrDefault (aClass, sName, null);
  }

  /**
   * Get the enum value with the passed name case insensitive
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sName
   *        The name to search
   * @param aDefault
   *        The default value to be returned, if the name was not found.
   * @return The default parameter if no enum item with the given name is
   *         present.
   */
  @Nullable
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasName> ENUMTYPE getFromNameCaseInsensitiveOrDefault (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                            @Nullable final String sName,
                                                                                                            @Nullable final ENUMTYPE aDefault)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    if (sName != null)
      for (final ENUMTYPE aElement : aClass.getEnumConstants ())
        if (aElement.getName ().equalsIgnoreCase (sName))
          return aElement;
    return aDefault;
  }

  /**
   * Get the enum value with the passed name (case insensitive). If no such name
   * is present, an {@link IllegalArgumentException} is thrown.
   * 
   * @param <ENUMTYPE>
   *        The enum type
   * @param aClass
   *        The enum class
   * @param sName
   *        The name to search
   * @return The enum item with the given name. Never <code>null</code>.
   * @throws IllegalArgumentException
   *         if no enum item with the given name is present
   */
  @Nonnull
  public static <ENUMTYPE extends Enum <ENUMTYPE> & IHasName> ENUMTYPE getFromNameCaseInsensitiveOrThrow (@Nonnull final Class <ENUMTYPE> aClass,
                                                                                                          @Nullable final String sName)
  {
    final ENUMTYPE aEnum = getFromNameCaseInsensitiveOrNull (aClass, sName);
    if (aEnum == null)
      throw new IllegalArgumentException ("Failed to resolve name " + sName + " within class " + aClass);
    return aEnum;
  }

  /**
   * Get the unique name of the passed enum entry.
   * 
   * @param aEnum
   *        The enum to use. May not be <code>null</code>.
   * @return The unique ID as a combination of the class name and the enum entry
   *         name. Never <code>null</code>.
   */
  @Nonnull
  public static String getEnumID (@Nonnull final Enum <?> aEnum)
  {
    // No explicit null check, because this method is used heavily in pdaf
    // locale resolving, so we want to spare some CPU cycles :)
    return aEnum.getClass ().getName () + '.' + aEnum.name ();
  }
}
