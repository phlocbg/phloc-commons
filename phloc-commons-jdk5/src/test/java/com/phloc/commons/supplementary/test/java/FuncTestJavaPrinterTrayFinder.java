/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.supplementary.test.java;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ArrayHelper;

public final class FuncTestJavaPrinterTrayFinder
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FuncTestJavaPrinterTrayFinder.class);

  @Test
  public void testListPrinterTrays ()
  {
    final PrintService [] aAllServices = PrintServiceLookup.lookupPrintServices (null, null);
    for (final PrintService aService : aAllServices)
    {
      s_aLogger.info (aService.toString ());
      final Object aAttrs = aService.getSupportedAttributeValues (Media.class,
                                                                  DocFlavor.SERVICE_FORMATTED.PAGEABLE,
                                                                  null);
      if (ArrayHelper.isArray (aAttrs))
      {
        for (final Media aElement : (Media []) aAttrs)
          if (aElement instanceof MediaTray)
            s_aLogger.info ("  " + aElement);
      }
    }
  }
}
