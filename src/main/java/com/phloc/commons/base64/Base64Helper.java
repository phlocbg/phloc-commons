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
package com.phloc.commons.base64;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CharsetManager;

/**
 * A small helper class for easier usage of the {@link Base64} class without
 * exception catching.
 * 
 * @author philip
 */
@Immutable
public final class Base64Helper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final Base64Helper s_aInstance = new Base64Helper ();

  private Base64Helper ()
  {}

  /**
   * Decode the string with the default encoding (US-ASCII is the preferred
   * one).
   * 
   * @param sEncoded
   *        The encoded string.
   * @return <code>null</code> if decoding failed.
   */
  @Nullable
  public static byte [] safeDecode (@Nonnull final String sEncoded)
  {
    try
    {
      return Base64.decode (sEncoded);
    }
    catch (final Exception ex)
    {
      return null;
    }
  }

  /**
   * Decode the byte array.
   * 
   * @param aEncodedBytes
   *        The encoded byte array.
   * @return <code>null</code> if decoding failed.
   */
  @Nullable
  public static byte [] safeDecode (@Nonnull final byte [] aEncodedBytes)
  {
    try
    {
      return Base64.decode (aEncodedBytes);
    }
    catch (final Exception ex)
    {
      return null;
    }
  }

  /**
   * Decode the string and convert it back to a string.
   * 
   * @param sEncoded
   *        The encoded byte array.
   * @param sCharset
   *        The character set to be used.
   * @return <code>null</code> if decoding failed.
   */
  @Nullable
  public static String safeDecodeAsString (@Nonnull final String sEncoded, @Nonnull final String sCharset)
  {
    try
    {
      return CharsetManager.getAsString (Base64.decode (sEncoded), sCharset);
    }
    catch (final Exception ex)
    {
      return null;
    }
  }

  /**
   * Decode the string and convert it back to a string.
   * 
   * @param sEncoded
   *        The encoded byte array.
   * @param aCharset
   *        The character set to be used.
   * @return <code>null</code> if decoding failed.
   */
  @Nullable
  public static String safeDecodeAsString (@Nonnull final String sEncoded, @Nonnull final Charset aCharset)
  {
    try
    {
      return CharsetManager.getAsString (Base64.decode (sEncoded), aCharset);
    }
    catch (final Exception ex)
    {
      return null;
    }
  }

  /**
   * Decode the byte array and convert it to a string.
   * 
   * @param aEncodedBytes
   *        The encoded byte array.
   * @param sCharset
   *        The character set to be used.
   * @return <code>null</code> if decoding failed.
   */
  @Nullable
  public static String safeDecodeAsString (@Nonnull final byte [] aEncodedBytes, @Nonnull final String sCharset)
  {
    try
    {
      return CharsetManager.getAsString (Base64.decode (aEncodedBytes), sCharset);
    }
    catch (final Exception ex)
    {
      return null;
    }
  }

  /**
   * Decode the byte array and convert it to a string.
   * 
   * @param aEncodedBytes
   *        The encoded byte array.
   * @param aCharset
   *        The character set to be used.
   * @return <code>null</code> if decoding failed.
   */
  @Nullable
  public static String safeDecodeAsString (@Nonnull final byte [] aEncodedBytes, @Nonnull final Charset aCharset)
  {
    try
    {
      return CharsetManager.getAsString (Base64.decode (aEncodedBytes), aCharset);
    }
    catch (final Exception ex)
    {
      return null;
    }
  }

  /**
   * @param s
   *        The string to be encoded
   * @param sCharset
   *        The charset to be used
   * @return The encoded byte array.
   */
  @Nullable
  public static String safeEncode (@Nonnull final String s, @Nonnull final String sCharset)
  {
    return Base64.encodeBytes (CharsetManager.getAsBytes (s, sCharset));
  }

  /**
   * @param s
   *        The string to be encoded
   * @param aCharset
   *        The charset to be used
   * @return The encoded byte array.
   */
  @Nullable
  public static String safeEncode (@Nonnull final String s, @Nonnull final Charset aCharset)
  {
    return Base64.encodeBytes (CharsetManager.getAsBytes (s, aCharset));
  }
}
