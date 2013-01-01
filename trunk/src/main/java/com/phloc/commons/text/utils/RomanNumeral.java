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
package com.phloc.commons.text.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.StringHelper;

/**
 * An object of type RomanNumeral is an integer between 1 and 3999. It can be
 * constructed either from an integer or from a string that represents a Roman
 * numeral in this range. The function toString() will return a standardized
 * Roman numeral representation of the number. The function toInt() will return
 * the number as a value of type int.
 */
@Immutable
public final class RomanNumeral
{
  public static final int MIN_VAL = 1;
  public static final int MAX_VAL = 3999;

  /*
   * The following arrays are used to construct the standard Roman numeral
   * representation of the number. For each i, the number numbers[i] is
   * represented by the corresponding string, letters[i].
   */
  private static final int [] NUMBERS = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };

  private static final String [] ROMAN_LETTERS = { "M",
                                                  "CM",
                                                  "D",
                                                  "CD",
                                                  "C",
                                                  "XC",
                                                  "L",
                                                  "XL",
                                                  "X",
                                                  "IX",
                                                  "V",
                                                  "IV",
                                                  "I" };

  /** The number represented by this Roman numeral. */
  private final int m_nValue;

  /**
   * Constructor. Creates the Roman number with the int value specified by the
   * parameter. Throws a IllegalArgumentException if arabic is not in the range
   * 1 to 3999 inclusive.
   */
  private RomanNumeral (@Nonnegative final int nValue)
  {
    if (nValue < MIN_VAL || nValue > MAX_VAL)
      throw new IllegalArgumentException ("Value of RomanNumeral must be between " + MIN_VAL + " and " + MAX_VAL + ".");
    m_nValue = nValue;
  }

  /**
   * Constructor. Creates the Roman number with the given representation. For
   * example, RomanNumeral("xvii") is 17. If the parameter is not a legal Roman
   * numeral, a NumberFormatException is thrown. Both upper and lower case
   * letters are allowed.
   */
  private RomanNumeral (@Nonnull final String sRoman)
  {
    if (StringHelper.hasNoText (sRoman))
      throw new IllegalArgumentException ("An empty string does not define a Roman numeral.");

    // Convert to upper case letters.
    final String sRealRoman = sRoman.toUpperCase (); // NOPMD

    // A position in the string, roman;
    int nIndex = 0;

    // Arabic numeral equivalent of the part of the string that
    // has been converted so far.
    int nArabic = 0;

    while (nIndex < sRealRoman.length ())
    {
      // Letter at current position in string.
      final char cLetter = sRealRoman.charAt (nIndex);

      // Numerical equivalent of letter
      final int nNumber = _letterToNumber (cLetter);

      // Move on to next position in the string
      nIndex++;

      if (nIndex == sRealRoman.length ())
      {
        // There is no letter in the string following the one we have just
        // processed.
        // So just add the number corresponding to the single letter to arabic.
        nArabic += nNumber;
      }
      else
      {
        // Look at the next letter in the string. If it has a larger Roman
        // numeral equivalent than number, then the two letters are counted
        // together as a Roman numeral with value (nextNumber - number).
        final int nNextNumber = _letterToNumber (sRealRoman.charAt (nIndex));
        if (nNextNumber > nNumber)
        {
          // Combine the two letters to get one value, and move on to next
          // position in string.
          nArabic += (nNextNumber - nNumber);
          nIndex++;
        }
        else
        {
          // Don't combine the letters. Just add the value of the one letter
          // onto the number.
          nArabic += nNumber;
        }
      }
    }

    if (nArabic > MAX_VAL)
      throw new IllegalArgumentException ("Roman numeral must have value 3999 or less.");

    m_nValue = nArabic;
  }

  @Nonnegative
  private static int _letterToNumber (final char cLetter)
  {
    // Find the integer value of letter considered as a Roman numeral. Return
    // -1 if letter is not a legal Roman numeral. The letter must be upper case.
    switch (cLetter)
    {
      case 'I':
        return 1;
      case 'V':
        return 5;
      case 'X':
        return 10;
      case 'L':
        return 50;
      case 'C':
        return 100;
      case 'D':
        return 500;
      case 'M':
        return 1000;
      default:
        throw new IllegalArgumentException ("Unknown roman numeral char: " + cLetter);
    }
  }

  @Nonnull
  private static String _asRomanString (final int nValue)
  {
    // Return the standard representation of this Roman numeral.
    final StringBuilder aSB = new StringBuilder ();

    // represents the part of num that still has
    // to be converted to Roman numeral representation.
    int nWorkValue = nValue;
    for (int i = 0; i < NUMBERS.length; i++)
    {
      while (nWorkValue >= NUMBERS[i])
      {
        aSB.append (ROMAN_LETTERS[i]);
        nWorkValue -= NUMBERS[i];
      }
    }
    return aSB.toString ();
  }

  @Nonnull
  private String _asRomanString ()
  {
    return _asRomanString (m_nValue);
  }

  private int _asInt ()
  {
    // Return the value of this Roman numeral as an int.
    return m_nValue;
  }

  @Nonnegative
  public static int romanStringToInt (final String sRoman)
  {
    return new RomanNumeral (sRoman)._asInt ();
  }

  @Nonnull
  public static String intToRomanString (final int nValue)
  {
    return new RomanNumeral (nValue)._asRomanString ();
  }
}
