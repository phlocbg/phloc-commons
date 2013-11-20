package com.phloc.commons.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;

/**
 * This class contains all the methods for masking XML content.
 * 
 * @author Philip Helger
 */
public final class XMLMaskHelper
{
  private static final char [] MASK_ATTRIBUTE_VALUE_XML10 = new char [] { '\t', '\n', '\r', '"', '&', '<' };
  private static final char [] MASK_TEXT_XML10 = new char [] { '\r',
                                                              '&',
                                                              '<',
                                                              '>',
                                                              0x7f,
                                                              0x80,
                                                              0x81,
                                                              0x82,
                                                              0x83,
                                                              0x84,
                                                              0x85,
                                                              0x86,
                                                              0x87,
                                                              0x88,
                                                              0x89,
                                                              0x8a,
                                                              0x8b,
                                                              0x8c,
                                                              0x8d,
                                                              0x8e,
                                                              0x8f,
                                                              0x90,
                                                              0x91,
                                                              0x92,
                                                              0x93,
                                                              0x94,
                                                              0x95,
                                                              0x96,
                                                              0x97,
                                                              0x98,
                                                              0x99,
                                                              0x9a,
                                                              0x9b,
                                                              0x9c,
                                                              0x9d,
                                                              0x9e,
                                                              0x9f };
  private static final char [] MASK_ATTRIBUTE_VALUE_XML11 = new char [] { 0x1,
                                                                         0x2,
                                                                         0x3,
                                                                         0x4,
                                                                         0x5,
                                                                         0x6,
                                                                         0x7,
                                                                         0x8,
                                                                         0x9,
                                                                         0xa,
                                                                         0xb,
                                                                         0xc,
                                                                         0xd,
                                                                         0xe,
                                                                         0xf,
                                                                         0x10,
                                                                         0x11,
                                                                         0x12,
                                                                         0x13,
                                                                         0x14,
                                                                         0x15,
                                                                         0x16,
                                                                         0x17,
                                                                         0x18,
                                                                         0x19,
                                                                         0x1a,
                                                                         0x1b,
                                                                         0x1c,
                                                                         0x1d,
                                                                         0x1e,
                                                                         0x1f,
                                                                         '"',
                                                                         '&',
                                                                         '<' };
  private static final char [] MASK_TEXT_XML11 = new char [] { 0x1,
                                                              0x2,
                                                              0x3,
                                                              0x4,
                                                              0x5,
                                                              0x6,
                                                              0x7,
                                                              0x8,
                                                              0xb,
                                                              0xc,
                                                              0xd,
                                                              0xe,
                                                              0xf,
                                                              0x10,
                                                              0x11,
                                                              0x12,
                                                              0x13,
                                                              0x14,
                                                              0x15,
                                                              0x16,
                                                              0x17,
                                                              0x18,
                                                              0x19,
                                                              0x1a,
                                                              0x1b,
                                                              0x1c,
                                                              0x1d,
                                                              0x1e,
                                                              0x1f,
                                                              '&',
                                                              '<',
                                                              '>',
                                                              0x7f,
                                                              0x80,
                                                              0x81,
                                                              0x82,
                                                              0x83,
                                                              0x84,
                                                              0x85,
                                                              0x86,
                                                              0x87,
                                                              0x88,
                                                              0x89,
                                                              0x8a,
                                                              0x8b,
                                                              0x8c,
                                                              0x8d,
                                                              0x8e,
                                                              0x8f,
                                                              0x90,
                                                              0x91,
                                                              0x92,
                                                              0x93,
                                                              0x94,
                                                              0x95,
                                                              0x96,
                                                              0x97,
                                                              0x98,
                                                              0x99,
                                                              0x9a,
                                                              0x9b,
                                                              0x9c,
                                                              0x9d,
                                                              0x9e,
                                                              0x9f };
  private static final char [] MASK_CDATA_XML11 = new char [] { 0x1,
                                                               0x2,
                                                               0x3,
                                                               0x4,
                                                               0x5,
                                                               0x6,
                                                               0x7,
                                                               0x8,
                                                               0xb,
                                                               0xc,
                                                               0xe,
                                                               0xf,
                                                               0x10,
                                                               0x11,
                                                               0x12,
                                                               0x13,
                                                               0x14,
                                                               0x15,
                                                               0x16,
                                                               0x17,
                                                               0x18,
                                                               0x19,
                                                               0x1a,
                                                               0x1b,
                                                               0x1c,
                                                               0x1d,
                                                               0x1e,
                                                               0x1f };
  private static final char [][] MASK_ATTRIBUTE_VALUE_XML10_REPLACE = new char [MASK_ATTRIBUTE_VALUE_XML10.length] [];
  private static final char [][] MASK_TEXT_XML10_REPLACE = new char [MASK_TEXT_XML10.length] [];
  private static final char [][] MASK_ATTRIBUTE_VALUE_XML11_REPLACE = new char [MASK_ATTRIBUTE_VALUE_XML11.length] [];
  private static final char [][] MASK_TEXT_XML11_REPLACE = new char [MASK_TEXT_XML11.length] [];
  private static final char [][] MASK_CDATA_XML11_REPLACE = new char [MASK_CDATA_XML11.length] [];

  /**
   * Get the entity reference for the specified character. This returns e.g.
   * &amp;lt; for '&lt;' etc.
   * 
   * @param c
   *        Character to use.
   * @return The entity reference string. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public static String getEntityReferenceString (final char c)
  {
    if (c == '<')
      return "&lt;";
    if (c == '>')
      return "&gt;";
    if (c == '&')
      return "&amp;";
    if (c == '"')
      return "&quot;";
    return "&#" + (int) c + ";";
  }

  static
  {
    for (int i = 0; i < MASK_ATTRIBUTE_VALUE_XML10.length; ++i)
      MASK_ATTRIBUTE_VALUE_XML10_REPLACE[i] = getEntityReferenceString (MASK_ATTRIBUTE_VALUE_XML10[i]).toCharArray ();
    for (int i = 0; i < MASK_TEXT_XML10.length; ++i)
      MASK_TEXT_XML10_REPLACE[i] = getEntityReferenceString (MASK_TEXT_XML10[i]).toCharArray ();
    for (int i = 0; i < MASK_ATTRIBUTE_VALUE_XML11.length; ++i)
      MASK_ATTRIBUTE_VALUE_XML11_REPLACE[i] = getEntityReferenceString (MASK_ATTRIBUTE_VALUE_XML11[i]).toCharArray ();
    for (int i = 0; i < MASK_TEXT_XML11.length; ++i)
      MASK_TEXT_XML11_REPLACE[i] = getEntityReferenceString (MASK_TEXT_XML11[i]).toCharArray ();
    for (int i = 0; i < MASK_CDATA_XML11.length; ++i)
      MASK_CDATA_XML11_REPLACE[i] = getEntityReferenceString (MASK_CDATA_XML11[i]).toCharArray ();
  }

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final XMLMaskHelper s_aInstance = new XMLMaskHelper ();

  private XMLMaskHelper ()
  {}

  @Nullable
  private static char [] _findSourceMap (@Nonnull final EXMLVersion eXMLVersion,
                                         @Nonnull final EXMLCharMode eXMLCharMode)
  {
    switch (eXMLVersion)
    {
      case XML_10:
        switch (eXMLCharMode)
        {
          case ATTRIBUTE_VALUE:
            return MASK_ATTRIBUTE_VALUE_XML10;
          case TEXT:
            return MASK_TEXT_XML10;
          default:
            break;
        }
        break;
      case XML_11:
        switch (eXMLCharMode)
        {
          case ATTRIBUTE_VALUE:
            return MASK_ATTRIBUTE_VALUE_XML11;
          case TEXT:
            return MASK_TEXT_XML11;
          case CDATA:
            return MASK_CDATA_XML11;
          default:
            break;
        }
        break;
    }
    return null;
  }

  @Nullable
  private static char [][] _findReplaceMap (@Nonnull final EXMLVersion eXMLVersion,
                                            @Nonnull final EXMLCharMode eXMLCharMode)
  {
    switch (eXMLVersion)
    {
      case XML_10:
        switch (eXMLCharMode)
        {
          case ATTRIBUTE_VALUE:
            return MASK_ATTRIBUTE_VALUE_XML10_REPLACE;
          case TEXT:
            return MASK_TEXT_XML10_REPLACE;
          default:
            break;
        }
        break;
      case XML_11:
        switch (eXMLCharMode)
        {
          case ATTRIBUTE_VALUE:
            return MASK_ATTRIBUTE_VALUE_XML11_REPLACE;
          case TEXT:
            return MASK_TEXT_XML11_REPLACE;
          case CDATA:
            return MASK_CDATA_XML11_REPLACE;
          default:
            break;
        }
        break;
    }
    return null;
  }

  /**
   * Convert the passed set to an array
   * 
   * @param aChars
   *        Character set to use. May not be <code>null</code>.
   * @return A new array with the same length as the source set.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static char [] getAsCharArray (@Nonnull final Set <Character> aChars)
  {
    if (aChars == null)
      throw new NullPointerException ("chars");

    final char [] ret = new char [aChars.size ()];
    int nIndex = 0;
    for (final Character aChar : aChars)
      ret[nIndex++] = aChar.charValue ();
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  private static char [][] _createEmptyReplacement (@Nonnull final char [] aSrcMap)
  {
    final char [][] ret = new char [aSrcMap.length] [];
    final char [] aEmpty = new char [0];
    for (int i = 0; i < aSrcMap.length; ++i)
      ret[i] = aEmpty;
    return ret;
  }

  @Nonnull
  public static char [] getMaskedXMLText (@Nonnull final EXMLVersion eXMLVersion,
                                          @Nonnull final EXMLCharMode eXMLCharMode,
                                          @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                          @Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return new char [0];

    char [] aChars = s.toCharArray ();

    // 1. do incorrect character handling
    if (eIncorrectCharHandling.isTestRequired ())
    {
      if (XMLCharHelper.containsInvalidXMLChar (eXMLVersion, eXMLCharMode, aChars))
      {
        final Set <Character> aAllInvalidChars = XMLCharHelper.getAllInvalidXMLChars (eXMLVersion, eXMLCharMode, aChars);
        eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, aAllInvalidChars);
        if (eIncorrectCharHandling.isReplaceWithNothing ())
        {
          final char [] aSrcMap = getAsCharArray (aAllInvalidChars);
          final char [][] aDstMap = _createEmptyReplacement (aSrcMap);
          aChars = StringHelper.replaceMultiple (s, aSrcMap, aDstMap);
        }
      }
    }

    // 2. perform entity replacements if necessary
    final char [] aSrcMap = _findSourceMap (eXMLVersion, eXMLCharMode);
    if (aSrcMap == null)
    {
      // Nothing to replace
      return aChars;
    }
    final char [][] aDstMap = _findReplaceMap (eXMLVersion, eXMLCharMode);
    return StringHelper.replaceMultiple (aChars, aSrcMap, aDstMap);
  }

  @Nonnegative
  public static int getMaskedXMLTextLength (@Nonnull final EXMLVersion eXMLVersion,
                                            @Nonnull final EXMLCharMode eXMLCharMode,
                                            @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                            @Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return 0;

    char [] aChars = s.toCharArray ();

    // 1. do incorrect character handling
    if (eIncorrectCharHandling.isTestRequired () &&
        XMLCharHelper.containsInvalidXMLChar (eXMLVersion, eXMLCharMode, aChars))
    {
      final Set <Character> aAllInvalidChars = XMLCharHelper.getAllInvalidXMLChars (eXMLVersion, eXMLCharMode, aChars);
      eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, aAllInvalidChars);
      if (eIncorrectCharHandling.isReplaceWithNothing ())
      {
        final char [] aSrcMap = getAsCharArray (aAllInvalidChars);
        final char [][] aDstMap = _createEmptyReplacement (aSrcMap);
        aChars = StringHelper.replaceMultiple (s, aSrcMap, aDstMap);
      }
    }

    // 2. perform entity replacements if necessary
    final char [] aSrcMap = _findSourceMap (eXMLVersion, eXMLCharMode);
    if (aSrcMap == null)
    {
      // Nothing to replace
      return aChars.length;
    }
    final char [][] aDstMap = _findReplaceMap (eXMLVersion, eXMLCharMode);
    final int nResLen = StringHelper.getReplaceMultipleResultLength (aChars, aSrcMap, aDstMap);
    return nResLen == CGlobal.ILLEGAL_UINT ? aChars.length : nResLen;
  }

  public static void maskXMLTextTo (@Nonnull final EXMLVersion eXMLVersion,
                                    @Nonnull final EXMLCharMode eXMLCharMode,
                                    @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                    @Nullable final String s,
                                    @Nonnull final Writer aWriter) throws IOException
  {
    if (StringHelper.hasNoText (s))
      return;

    char [] aChars = s.toCharArray ();

    // 1. do incorrect character handling
    if (eIncorrectCharHandling.isTestRequired ())
    {
      if (XMLCharHelper.containsInvalidXMLChar (eXMLVersion, eXMLCharMode, aChars))
      {
        final Set <Character> aAllInvalidChars = XMLCharHelper.getAllInvalidXMLChars (eXMLVersion, eXMLCharMode, aChars);
        eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, aAllInvalidChars);
        if (eIncorrectCharHandling.isReplaceWithNothing ())
        {
          final char [] aSrcMap = getAsCharArray (aAllInvalidChars);
          final char [][] aDstMap = _createEmptyReplacement (aSrcMap);
          aChars = StringHelper.replaceMultiple (s, aSrcMap, aDstMap);
        }
      }
    }

    // 2. perform entity replacements if necessary
    final char [] aSrcMap = _findSourceMap (eXMLVersion, eXMLCharMode);
    if (aSrcMap == null)
    {
      // Nothing to replace
      aWriter.write (aChars);
    }
    else
    {
      final char [][] aDstMap = _findReplaceMap (eXMLVersion, eXMLCharMode);
      StringHelper.replaceMultipleTo (aChars, aSrcMap, aDstMap, aWriter);
    }
  }
}
