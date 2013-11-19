package com.phloc.commons.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * This class contains all the methods for masking XML content.
 * 
 * @author Philip Helger
 */
public final class XMLMaskHelper
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final XMLMaskHelper s_aInstance = new XMLMaskHelper ();

  private XMLMaskHelper ()
  {}

  private static final char [] MASK_PATTERNS_XML10 = new char [] {};
  private static final char [] MASK_PATTERNS_ALL = new char [] {};
  private static final char [][] MASK_REPLACE_XML10 = new char [] [] {};
  private static final char [][] MASK_REPLACE_ALL_EMPTY = new char [] [] {};
  private static final char [][] MASK_REPLACE_ALL_XML11 = new char [] [] {};

  @Nonnull
  public static char [] getMaskedXMLText (@Nonnull final EXMLVersion eXMLVersion,
                                          @Nonnull final EXMLCharMode eXMLCharMode,
                                          @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                          @Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return new char [0];

    if (eIncorrectCharHandling.isTestRequired ())
    {
      final char [] aChars = s.toCharArray ();
      if (XMLCharHelper.containsInvalidXMLChar (eXMLVersion, eXMLCharMode, aChars))
      {
        if (eIncorrectCharHandling.isNotifyRequired ())
        {
          final Set <Character> aAllInvalidChars = XMLCharHelper.getAllInvalidXMLChars (eXMLVersion,
                                                                                        eXMLCharMode,
                                                                                        aChars);
          eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, aAllInvalidChars);
        }
        if (eIncorrectCharHandling.isReplaceWithNothing ())
          return StringHelper.replaceMultiple (s, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_EMPTY);
      }
    }

    if (eXMLVersion.equals (EXMLVersion.XML_10))
    {
      return StringHelper.replaceMultiple (s, MASK_PATTERNS_XML10, MASK_REPLACE_XML10);
    }
    return StringHelper.replaceMultiple (s, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_XML11);
  }

  @Nonnegative
  public static int getMaskedXMLTextLength (@Nonnull final EXMLVersion eXMLVersion,
                                            @Nonnull final EXMLCharMode eXMLCharMode,
                                            @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                            @Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return 0;

    final char [] aChars = s.toCharArray ();
    if (eIncorrectCharHandling.isTestRequired () &&
        XMLCharHelper.containsInvalidXMLChar (eXMLVersion, eXMLCharMode, aChars))
    {
      if (eIncorrectCharHandling.isNotifyRequired ())
      {
        final Set <Character> aAllInvalidChars = XMLCharHelper.getAllInvalidXMLChars (eXMLVersion, eXMLCharMode, aChars);
        eIncorrectCharHandling.notifyOnInvalidXMLCharacter (s, aAllInvalidChars);
      }
      if (eIncorrectCharHandling.isReplaceWithNothing ())
      {
        final int nResLen = StringHelper.getReplaceMultipleResultLength (aChars,
                                                                         MASK_PATTERNS_ALL,
                                                                         MASK_REPLACE_ALL_EMPTY);
        return nResLen == CGlobal.ILLEGAL_UINT ? s.length () : nResLen;
      }
    }

    int nResLen;
    if (eXMLVersion.equals (EXMLVersion.XML_10))
      nResLen = StringHelper.getReplaceMultipleResultLength (aChars, MASK_PATTERNS_XML10, MASK_REPLACE_XML10);
    else
      nResLen = StringHelper.getReplaceMultipleResultLength (aChars, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_XML11);
    return nResLen == CGlobal.ILLEGAL_UINT ? s.length () : nResLen;
  }

  public static void maskXMLTextTo (@Nonnull final EXMLVersion eXMLVersion,
                                    @Nonnull final EXMLCharMode eXMLCharMode,
                                    @Nonnull final EXMLIncorrectCharacterHandling eIncorrectCharHandling,
                                    @Nullable final String sText,
                                    @Nonnull final Writer aWriter) throws IOException
  {
    if (StringHelper.hasNoText (sText))
      return;

    if (eIncorrectCharHandling.isTestRequired ())
    {
      final char [] aChars = sText.toCharArray ();
      if (XMLCharHelper.containsInvalidXMLChar (eXMLVersion, eXMLCharMode, aChars))
      {
        if (eIncorrectCharHandling.isNotifyRequired ())
        {
          final Set <Character> aAllInvalidChars = XMLCharHelper.getAllInvalidXMLChars (eXMLVersion,
                                                                                        eXMLCharMode,
                                                                                        aChars);
          eIncorrectCharHandling.notifyOnInvalidXMLCharacter (sText, aAllInvalidChars);
        }
        if (eIncorrectCharHandling.isReplaceWithNothing ())
        {
          StringHelper.replaceMultipleTo (sText, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_EMPTY, aWriter);
          return;
        }
      }
    }

    if (eXMLVersion.equals (EXMLVersion.XML_10))
      StringHelper.replaceMultipleTo (sText, MASK_PATTERNS_XML10, MASK_REPLACE_XML10, aWriter);
    else
      StringHelper.replaceMultipleTo (sText, MASK_PATTERNS_ALL, MASK_REPLACE_ALL_XML11, aWriter);
  }
}
