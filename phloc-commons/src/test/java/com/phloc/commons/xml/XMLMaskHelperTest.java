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
package com.phloc.commons.xml;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link XMLMaskHelper}.
 * 
 * @author Philip Helger
 */
public final class XMLMaskHelperTest extends AbstractPhlocTestCase
{
  @Test
  public void testGetMaskedXMLText ()
  {
    // Emit as usual
    assertArrayEquals (new char [] { 1 },
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                       "\u0001"));
    assertEquals ("&#1;",
                  new String (XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                                              EXMLCharMode.ATTRIBUTE_VALUE,
                                                              EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                              "\u0001")));

    // Replace with ""
    assertArrayEquals (new char [0],
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "\u0001"));
    assertArrayEquals (new char [0],
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "\u0000"));
    assertArrayEquals ("abc".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "ab\u0001c"));
    assertArrayEquals ("abc".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "ab\u0000c"));

    // Throw exception
    try
    {
      XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                      EXMLCharMode.ATTRIBUTE_VALUE,
                                      EXMLIncorrectCharacterHandling.THROW_EXCEPTION,
                                      "\u0001");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                      EXMLCharMode.ATTRIBUTE_VALUE,
                                      EXMLIncorrectCharacterHandling.THROW_EXCEPTION,
                                      "\u0000");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Emit as usual
    assertArrayEquals ("1 &amp; \u0001".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                       "1 & \u0001"));
    assertArrayEquals ("1 &amp; &#1;".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                       "1 & \u0001"));
    // Emit as usual
    assertArrayEquals ("1 &amp; ".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "1 & \u0001"));
    assertArrayEquals ("1 &amp; ".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "1 & \u0000"));

    // Special chars
    assertArrayEquals ("ab&lt;cd>ef".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_10,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "ab<cd>ef"));
    assertArrayEquals ("ab&lt;cd>ef".toCharArray (),
                       XMLMaskHelper.getMaskedXMLText (EXMLVersion.XML_11,
                                                       EXMLCharMode.ATTRIBUTE_VALUE,
                                                       EXMLIncorrectCharacterHandling.DO_NOT_WRITE_LOG_WARNING,
                                                       "ab<cd>ef"));
  }

  @Test
  public void testGetMaskedXMLTextLength ()
  {
    assertEquals (1, XMLMaskHelper.getMaskedXMLTextLength (EXMLVersion.XML_10,
                                                           EXMLCharMode.ATTRIBUTE_VALUE,
                                                           EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                           "\u0001"));
    assertEquals (4, XMLMaskHelper.getMaskedXMLTextLength (EXMLVersion.XML_11,
                                                           EXMLCharMode.ATTRIBUTE_VALUE,
                                                           EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                           "\u0001"));
    assertEquals (2 + 5 + 1 + 1,
                  XMLMaskHelper.getMaskedXMLTextLength (EXMLVersion.XML_10,
                                                        EXMLCharMode.ATTRIBUTE_VALUE,
                                                        EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                        "1 & \u0001"));
    assertEquals (2 + 5 + 1 + 4,
                  XMLMaskHelper.getMaskedXMLTextLength (EXMLVersion.XML_11,
                                                        EXMLCharMode.ATTRIBUTE_VALUE,
                                                        EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG,
                                                        "1 & \u0001"));
  }
}
