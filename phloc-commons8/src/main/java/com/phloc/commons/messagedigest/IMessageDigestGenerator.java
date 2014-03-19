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
package com.phloc.commons.messagedigest;

import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Base interface for a message digest generator (using hash algorithms like MD5
 * or SHA 512)
 *
 * @author Philip Helger
 */
public interface IMessageDigestGenerator
{
  /**
   * The default algorithm that should be used.
   */
  EMessageDigestAlgorithm DEFAULT_ALGORITHM = EMessageDigestAlgorithm.SHA_512;

  /**
   * @return The name of the hashing algorithm used.
   */
  @Nonnull
  String getAlgorithmName ();

  /**
   * @return Returns the length of the digest in bytes, or 0 if this operation
   *         is not supported by the provider.
   */
  @Nonnegative
  int getDigestLength ();

  /**
   * Update the hash with the given byte. After calling {@link #getDigest()}
   * once, no further update is possible.
   *
   * @param aValue
   *        The byte value to update the hash
   * @return this
   */
  @Nonnull
  IMessageDigestGenerator update (byte aValue);

  /**
   * Update the hash with the bytes of the given string in the given charset.
   * After calling {@link #getDigest()} once, no further update is possible.
   *
   * @param sValue
   *        The string value to update the hash. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used for extraction of the bytes. May not be
   *        <code>null</code>.
   * @return this
   */
  @Nonnull
  IMessageDigestGenerator update (@Nonnull String sValue, @Nonnull Charset aCharset);

  /**
   * Update the hash with the given byte array. After calling
   * {@link #getDigest()} once, no further update is possible.
   *
   * @param aValue
   *        The byte array to update the hash. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  IMessageDigestGenerator update (@Nonnull byte [] aValue);

  /**
   * Update the hash with a slice of the given byte array. After calling
   * {@link #getDigest()} once, no further update is possible.
   *
   * @param aValue
   *        The byte array to update the hash. May not be <code>null</code>.
   * @param nOffset
   *        The offset within the byte array from which the value should be
   *        taken. May not be &lt; 0.
   * @param nLength
   *        The number of bytes to use, starting from the passed offset. May not
   *        be &lt; 0.
   * @return this
   */
  @Nonnull
  IMessageDigestGenerator update (@Nonnull byte [] aValue, @Nonnegative int nOffset, @Nonnegative int nLength);

  /**
   * Reset the current hash so that generation can start from scratch again.
   */
  void reset ();

  /**
   * Finish calculation of the hash value and return the digest. Afterwards no
   * update is possible before {@link #reset()} is called.
   *
   * @return A copy of the array with the message digest.
   */
  @Nonnull
  @ReturnsMutableCopy
  byte [] getDigest ();

  /**
   * Get only a part of the digest, namely the first number of bytes. Calls
   * {@link #getDigest()} internally so no further update is possible after this
   * method is called.
   *
   * @param nLength
   *        The number of bytes to be retrieved. Must be &gt; 0.
   * @return A copy of the first n bytes of the message digest array.
   */
  @Nonnull
  @ReturnsMutableCopy
  byte [] getDigest (@Nonnegative int nLength);

  /**
   * This method finalizes the hash generation and creates the index. Calls
   * {@link #getDigest()} internally so no further update is possible after this
   * method is called.
   *
   * @return the generated hash value.
   */
  long getDigestLong ();

  /**
   * This method converts the current hash digest to a hex string. Calls
   * {@link #getDigest()} internally so no further update is possible after this
   * method is called.
   *
   * @return The hex value of the hash digest. Never <code>null</code>.
   */
  @Nonnull
  String getDigestHexString ();
}
