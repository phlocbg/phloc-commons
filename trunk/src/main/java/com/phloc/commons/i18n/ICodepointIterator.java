package com.phloc.commons.i18n;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * BAse interface for codepoint iterators
 * 
 * @author philip
 */
public interface ICodepointIterator extends Iterator <Codepoint>
{
  /**
   * True if there are codepoints remaining
   */
  boolean hasNext ();

  /**
   * Return the final index position
   */
  int lastPosition ();

  /**
   * Return the next chars. If the codepoint is not supplemental, the char array
   * will have a single member. If the codepoint is supplemental, the char array
   * will have two members, representing the high and low surrogate chars
   */
  @Nullable
  char [] nextChars ();

  /**
   * Peek the next chars in the iterator. If the codepoint is not supplemental,
   * the char array will have a single member. If the codepoint is supplemental,
   * the char array will have two members, representing the high and low
   * surrogate chars
   */
  @Nullable
  char [] peekChars ();

  /**
   * Return the next codepoint
   */
  @Nullable
  Codepoint next ();

  /**
   * Peek the next codepoint
   */
  @Nullable
  Codepoint peek ();

  /**
   * Peek the specified codepoint
   */
  @Nullable
  Codepoint peek (int index);

  /**
   * Set the iterator position
   */
  void position (int n);

  /**
   * Get the iterator position
   */
  int position ();

  /**
   * Return the iterator limit
   */
  int limit ();

  /**
   * Return the remaining iterator size
   */
  int remaining ();

  /**
   * Returns true if the char at the specified index is a high surrogate
   */
  boolean isHigh (int index);

  /**
   * Returns true if the char at the specified index is a low surrogate
   */
  boolean isLow (int index);

  void remove ();

  @Nonnull
  CodepointIteratorRestricted restrict (@Nonnull ICodepointFilter aFilter);

  @Nonnull
  CodepointIteratorRestricted restrict (@Nonnull ICodepointFilter aFilter, boolean scanning);

  @Nonnull
  CodepointIteratorRestricted restrict (@Nonnull ICodepointFilter aFilter, boolean scanning, boolean invert);
}
