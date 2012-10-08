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
package com.phloc.commons.mock;

import java.util.Locale;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.CodingStyleguideUnaware;

/**
 * Base class for all JUnit tests requiring setup and teardown handling.<br>
 * Note: Annotating a public static void no-argument method with \@BeforeClass
 * causes it to be run once before any of the test methods in the class. The
 * \@BeforeClass methods of super classes will be run before those the current
 * class.
 * 
 * @author philip
 */
@Immutable
public abstract class AbstractPhlocTestCase
{
  // For test case classes it is ok to have a protected logger
  @CodingStyleguideUnaware
  protected final Logger m_aLogger = LoggerFactory.getLogger (getClass ());

  protected static final Integer I_1 = Integer.valueOf (-1);
  protected static final Integer I0 = Integer.valueOf (0);
  protected static final Integer I1 = Integer.valueOf (1);
  protected static final Integer I2 = Integer.valueOf (2);
  protected static final Integer I3 = Integer.valueOf (3);
  protected static final Integer I4 = Integer.valueOf (4);
  protected static final Integer I5 = Integer.valueOf (5);
  protected static final Integer I6 = Integer.valueOf (6);
  protected static final Integer I7 = Integer.valueOf (7);

  protected static final Long L_1 = Long.valueOf (-1);
  protected static final Long L0 = Long.valueOf (0);
  protected static final Long L1 = Long.valueOf (1);

  protected static final Locale L_DE = new Locale ("de");
  protected static final Locale L_DE_AT = new Locale ("de", "AT");
  protected static final Locale L_DE_DE = new Locale ("de", "DE");
  protected static final Locale L_EN = new Locale ("en");
  protected static final Locale L_EN_GB = new Locale ("en", "GB");
  protected static final Locale L_EN_US = new Locale ("en", "US");
  protected static final Locale L_FR = new Locale ("fr");
  protected static final Locale L_FR_FR = new Locale ("fr", "FR");
}
