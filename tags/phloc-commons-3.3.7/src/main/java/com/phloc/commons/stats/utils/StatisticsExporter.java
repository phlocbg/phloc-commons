/**
 * Copyright (C) 2006-2011 phloc systems
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
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.stats.IStatisticsHandlerCache;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.IStatisticsHandlerTimer;
import com.phloc.commons.stats.visit.IStatisticsVisitor;
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
  public static final String ELEMENT_SIZE = "size";
  public static final String ATTR_MIN = "min";
  public static final String ATTR_AVERAGE = "average";
  public static final String ATTR_MAX = "max";
  public static final String ATTR_SUM = "sum";
  public static final String ELEMENT_COUNTER = "counter";
  public static final String ELEMENT_KEYEDCOUNTER = "keyedcounter";
  public static final String ELEMENT_KEY = "key";
  public static final String ATTR_NAME = "name";
  public static final String ATTR_INVOCATIONCOUNT = "invocationcount";

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final StatisticsExporter s_aInstance = new StatisticsExporter ();

  private StatisticsExporter ()
  {}

  public static IMicroDocument getAsXMLDocument ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_STATISTICS);
    StatisticsWalker.walkStatistics (new IStatisticsVisitor ()
    {
      public void onCache (final String sName, final IStatisticsHandlerCache aHandler)
      {
        if (aHandler.getInvocationCount () > 0)
          eRoot.appendElement (ELEMENT_CACHE)
               .setAttribute (ATTR_NAME, sName)
               .setAttribute (ATTR_INVOCATIONCOUNT, Integer.toString (aHandler.getInvocationCount ()))
               .setAttribute (ATTR_HITS, Integer.toString (aHandler.getHits ()))
               .setAttribute (ATTR_MISSES, Integer.toString (aHandler.getMisses ()));
      }

      public void onTimer (final String sName, final IStatisticsHandlerTimer aHandler)
      {
        if (aHandler.getInvocationCount () > 0)
          eRoot.appendElement (ELEMENT_TIMER)
               .setAttribute (ATTR_NAME, sName)
               .setAttribute (ATTR_INVOCATIONCOUNT, Integer.toString (aHandler.getInvocationCount ()))
               .setAttribute (ATTR_MIN, Long.toString (aHandler.getMin ()))
               .setAttribute (ATTR_AVERAGE, Long.toString (aHandler.getAverage ()))
               .setAttribute (ATTR_MAX, Long.toString (aHandler.getMax ()))
               .setAttribute (ATTR_SUM, aHandler.getSum ().toString ());
      }

      public void onSize (final String sName, final IStatisticsHandlerSize aHandler)
      {
        if (aHandler.getInvocationCount () > 0)
          eRoot.appendElement (ELEMENT_SIZE)
               .setAttribute (ATTR_NAME, sName)
               .setAttribute (ATTR_INVOCATIONCOUNT, Integer.toString (aHandler.getInvocationCount ()))
               .setAttribute (ATTR_MIN, Long.toString (aHandler.getMin ()))
               .setAttribute (ATTR_AVERAGE, Long.toString (aHandler.getAverage ()))
               .setAttribute (ATTR_MAX, Long.toString (aHandler.getMax ()))
               .setAttribute (ATTR_SUM, aHandler.getSum ().toString ());
      }

      public void onCounter (final String sName, final IStatisticsHandlerCounter aHandler)
      {
        if (aHandler.getInvocationCount () > 0)
          eRoot.appendElement (ELEMENT_COUNTER)
               .setAttribute (ATTR_NAME, sName)
               .setAttribute (ATTR_INVOCATIONCOUNT, Integer.toString (aHandler.getInvocationCount ()));
      }

      public void onKeyedCounter (final String sName, final IStatisticsHandlerKeyedCounter aHandler)
      {
        if (aHandler.getInvocationCount () > 0)
        {
          final IMicroElement eKeyedCounter = eRoot.appendElement (ELEMENT_KEYEDCOUNTER)
                                                   .setAttribute (ATTR_NAME, sName)
                                                   .setAttribute (ATTR_INVOCATIONCOUNT,
                                                                  Integer.toString (aHandler.getInvocationCount ()));
          for (final String sKey : ContainerHelper.getSorted (aHandler.getAllKeys ()))
          {
            eKeyedCounter.appendElement (ELEMENT_KEY)
                         .setAttribute (ATTR_NAME, sKey)
                         .setAttribute (ATTR_INVOCATIONCOUNT, Integer.toString (aHandler.getKeyCount (sKey)));
          }
        }
      }
    });
    return aDoc;
  }
}
