/**
 * Copyright (C) 2013-2014 phloc systems
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
package com.phloc.settings.xchange.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.joda.time.Hours;
import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.string.StringParser;
import com.phloc.datetime.PDTFactory;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;
import com.phloc.settings.factory.SettingsFactoryNewInstance;
import com.phloc.settings.impl.Settings;

public final class SettingsMicroDocumentConverterTest
{
  @Test
  public void testViceVersaConversion () throws UnsupportedEncodingException
  {
    final ISettings aSrc = new Settings ("myName");
    aSrc.setValue ("field1a", BigInteger.valueOf (1234));
    aSrc.setValue ("field1b", BigInteger.valueOf (-23423424));
    aSrc.setValue ("field2a", BigDecimal.valueOf (12.34));
    aSrc.setValue ("field2b", BigDecimal.valueOf (-2342.334599424));
    aSrc.setValue ("field3a", "My wonderbra string\n(incl newline)");
    aSrc.setValue ("field3b", "");
    aSrc.setValue ("field9a", Boolean.TRUE);
    aSrc.setValue ("field9b", StringParser.parseByteObj ("5"));
    aSrc.setValue ("field9c", Character.valueOf ('ä'));
    aSrc.setValue ("fieldxa", PDTFactory.getCurrentLocalDate ());
    aSrc.setValue ("fieldxb", PDTFactory.getCurrentLocalTime ());
    aSrc.setValue ("fieldxc", PDTFactory.getCurrentLocalDateTime ());
    aSrc.setValue ("fieldxd", PDTFactory.getCurrentDateTime ());
    aSrc.setValue ("fieldxe", Hours.hours (5).toStandardDuration ());
    aSrc.setValue ("fieldxf", Hours.hours (5).toPeriod ());
    aSrc.setValue ("fieldxg", "Any byte ärräy".getBytes (CCharset.CHARSET_UTF_8));

    final ISettings aNestedSettings = new Settings ("nestedSettings");
    aNestedSettings.setValue ("a", "b");
    aNestedSettings.setValue ("c", "d");
    aNestedSettings.setValue ("e", PDTFactory.getCurrentMillis ());
    aSrc.setValue ("fieldxh", aNestedSettings);

    final SettingsMicroDocumentConverter aConverter = new SettingsMicroDocumentConverter (true,
                                                                                          SettingsFactoryNewInstance.getInstance ());
    final IMicroElement eSrcElement = aConverter.convertToMicroElement (aSrc, null, "root");
    assertNotNull (eSrcElement);

    // To XML
    final IMicroElement eSrcElement3 = MicroTypeConverter.convertToMicroElement (aSrc, "root");
    assertNotNull (eSrcElement3);
    System.out.println (MicroWriter.getXMLString (eSrcElement3));

    // From XML
    final IReadonlySettings aDst = aConverter.convertToNative (eSrcElement);
    assertNotNull (aDst);
    assertEquals (aSrc, aDst);

    // From XML
    final IReadonlySettings aDst3 = MicroTypeConverter.convertToNative (eSrcElement, Settings.class);
    assertNotNull (aDst3);
    assertEquals (aSrc, aDst3);

    // Compare list
    assertEquals (BigInteger.valueOf (1234), aDst3.getValue ("field1a"));

    final ISettings aDst2 = new Settings (aDst.getName ());
    aDst2.setValues (aDst);
    assertEquals (aDst, aDst2);
    assertTrue (aDst2.setValue ("field3b", "doch was").isChanged ());
    assertFalse (aDst.equals (aDst2));
  }
}
