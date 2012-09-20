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
package com.phloc.commons.stats.utils;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.stats.visit.StatisticsWalker;

/**
 * Class for exporting statistics.
 * 
 * @author philip
 */
@Immutable
public final class StatisticsExporter
{
  public static final String ELEMENT_STATISTICS = "statistics";
  public static final String ELEMENT_CACHE = "cache";
  public static final String ATTR_HITS = "hits";
  public static final String ATTR_MISSES = "misses";
  public static final String ELEMENT_TIMER = "timer";
  public static final String ELEMENT_KEYEDTIMER = "keyedtimer";
  public static final String ELEMENT_SIZE = "size";
  public static final String ELEMENT_KEYEDSIZE = "keyedsize";
  public static final String ATTR_MIN = "min";
  public static final String ATTR_AVERAGE = "average";
  public static final String ATTR_MAX = "max";
  public static final String ATTR_SUM = "sum";
  public static final String ELEMENT_COUNTER = "counter";
  public static final String ELEMENT_KEYEDCOUNTER = "keyedcounter";
  public static final String ELEMENT_KEY = "key";
  public static final String ATTR_NAME = "name";
  public static final String ATTR_INVOCATIONCOUNT = "invocationcount";
  public static final String ATTR_COUNT = "count";

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final StatisticsExporter s_aInstance = new StatisticsExporter ();

  private StatisticsExporter ()
  {}

  public static IMicroDocument getAsXMLDocument ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_STATISTICS);
    StatisticsWalker.walkStatistics (new StatisticsVisitorToXML (eRoot));
    return aDoc;
  }
}
