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
package com.phloc.settings.xchange.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.joda.time.Hours;
import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.string.StringParser;
import com.phloc.datetime.PDTFactory;
import com.phloc.settings.ISettings;
import com.phloc.settings.impl.Settings;

public final class SettingsPersistencePropertiesTest
{
  @Test
  public void testViceVersaConversion () throws UnsupportedEncodingException
  {
    // Name is important!
    final ISettings aSrc = new Settings ("anonymous");
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

    final SettingsPersistenceProperties aSPP = new SettingsPersistenceProperties ();
    final String sSrc = aSPP.writeSettings (aSrc);
    assertNotNull (sSrc);

    // The created object is different, because now all values are String typed!
    final ISettings aDst1 = aSPP.readSettings (sSrc);
    assertNotNull (aDst1);

    // Reading the String typed version again should result in the same object
    final ISettings aDst2 = aSPP.readSettings (aSPP.writeSettings (aDst1));
    assertNotNull (aDst2);
    assertEquals (aDst1, aDst2);

    assertNotNull (aDst2.getDateTimeValue ("fieldxd"));
  }
}
