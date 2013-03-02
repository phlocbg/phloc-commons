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
package com.phloc.commons.i18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UnsupportedOperation;

/**
 * Provides an iterator over Unicode Codepoints
 * 
 * @author Apache Abdera
 */
public abstract class AbstractCodepointIterator implements ICodepointIterator
{
  protected int m_nPosition = -1;
  protected int m_nLimit = -1;

  protected AbstractCodepointIterator ()
  {}

  protected AbstractCodepointIterator (final int nPosition, final int nLimit)
  {
    m_nPosition = nPosition;
    m_nLimit = nLimit;
  }

  /**
   * Get the next char
   */
  protected abstract char get ();

  /**
   * Get the specified char
   */
  protected abstract char get (int index);

  /**
   * True if there are codepoints remaining
   */
  public boolean hasNext ()
  {
    return remaining () > 0;
  }

  /**
   * Return the final index position
   */
  public int lastPosition ()
  {
    final int p = position ();
    return p > -1 ? p >= limit () ? p : p - 1 : -1;
  }

  /**
   * Return the next chars. If the codepoint is not supplemental, the char array
   * will have a single member. If the codepoint is supplemental, the char array
   * will have two members, representing the high and low surrogate chars
   */
  @Nullable
  public char [] nextChars ()
  {
    if (hasNext ())
    {
      if (_isNextSurrogate ())
      {
        final char c1 = get ();
        if (Character.isHighSurrogate (c1) && position () < limit ())
        {
          final char c2 = get ();
          if (Character.isLowSurrogate (c2))
            return new char [] { c1, c2 };
          throw new InvalidCharacterException (c2);
        }
        else
          if (Character.isLowSurrogate (c1) && position () > 0)
          {
            final char c2 = get (position () - 2);
            if (Character.isHighSurrogate (c2))
              return new char [] { c1, c2 };
            throw new InvalidCharacterException (c2);
          }
      }
      return new char [] { get () };
    }
    return null;
  }

  /**
   * Peek the next chars in the iterator. If the codepoint is not supplemental,
   * the char array will have a single member. If the codepoint is supplemental,
   * the char array will have two members, representing the high and low
   * surrogate chars
   */
  @Nullable
  public char [] peekChars ()
  {
    return _peekChars (position ());
  }

  /**
   * Peek the specified chars in the iterator. If the codepoint is not
   * supplemental, the char array will have a single member. If the codepoint is
   * supplemental, the char array will have two members, representing the high
   * and low surrogate chars
   */
  @Nullable
  private char [] _peekChars (final int pos)
  {
    if (pos < 0 || pos >= limit ())
      return null;
    final char c1 = get (pos);
    if (Character.isHighSurrogate (c1) && pos < limit ())
    {
      final char c2 = get (pos + 1);
      if (Character.isLowSurrogate (c2))
        return new char [] { c1, c2 };
      throw new InvalidCharacterException (c2);
    }
    else
      if (Character.isLowSurrogate (c1) && pos > 1)
      {
        final char c2 = get (pos - 1);
        if (Character.isHighSurrogate (c2))
          return new char [] { c2, c1 };
        throw new InvalidCharacterException (c2);
      }
      else
        return new char [] { c1 };
  }

  /**
   * Return the next codepoint
   */
  @Nullable
  public Codepoint next ()
  {
    return _toCodepoint (nextChars ());
  }

  /**
   * Peek the next codepoint
   */
  @Nullable
  public Codepoint peek ()
  {
    return _toCodepoint (peekChars ());
  }

  /**
   * Peek the specified codepoint
   */
  @Nullable
  public Codepoint peek (final int index)
  {
    return _toCodepoint (_peekChars (index));
  }

  @Nullable
  private static Codepoint _toCodepoint (@Nullable final char [] chars)
  {
    if (chars == null || chars.length == 0)
      return null;
    if (chars.length == 1)
      return new Codepoint (chars[0]);
    return new Codepoint (chars[0], chars[1]);
  }

  /**
   * Set the iterator position
   */
  public void position (final int n)
  {
    if (n < 0 || n > limit ())
      throw new ArrayIndexOutOfBoundsException (n);
    m_nPosition = n;
  }

  /**
   * Get the iterator position
   */
  public int position ()
  {
    return m_nPosition;
  }

  /**
   * Return the iterator limit
   */
  public int limit ()
  {
    return m_nLimit;
  }

  /**
   * Return the remaining iterator size
   */
  public int remaining ()
  {
    return m_nLimit - position ();
  }

  private boolean _isNextSurrogate ()
  {
    if (!hasNext ())
      return false;
    final char c = get (position ());
    return Character.isHighSurrogate (c) || Character.isLowSurrogate (c);
  }

  /**
   * Returns true if the char at the specified index is a high surrogate
   */
  public boolean isHigh (final int index)
  {
    if (index < 0 || index > limit ())
      throw new ArrayIndexOutOfBoundsException (index);
    return Character.isHighSurrogate (get (index));
  }

  /**
   * Returns true if the char at the specified index is a low surrogate
   */
  public boolean isLow (final int index)
  {
    if (index < 0 || index > limit ())
      throw new ArrayIndexOutOfBoundsException (index);
    return Character.isLowSurrogate (get (index));
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public CodepointIteratorRestricted restrict (@Nonnull final ICodepointFilter aFilter)
  {
    return new CodepointIteratorRestricted (this, aFilter, false);
  }

  @Nonnull
  public CodepointIteratorRestricted restrict (@Nonnull final ICodepointFilter aFilter, final boolean bScanning)
  {
    return new CodepointIteratorRestricted (this, aFilter, bScanning);
  }

  @Nonnull
  public CodepointIteratorRestricted restrict (@Nonnull final ICodepointFilter aFilter,
                                               final boolean bScanning,
                                               final boolean bInvert)
  {
    return new CodepointIteratorRestricted (this, aFilter, bScanning, bInvert);
  }
}
