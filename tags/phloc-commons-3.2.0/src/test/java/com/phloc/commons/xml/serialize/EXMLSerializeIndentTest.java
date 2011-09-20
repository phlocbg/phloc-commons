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
package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link EXMLSerializeIndent}.
 * 
 * @author philip
 */
public final class EXMLSerializeIndentTest
{
  @Test
  public void testAll ()
  {
    for (final EXMLSerializeIndent e : EXMLSerializeIndent.values ())
      assertSame (e, EXMLSerializeIndent.valueOf (e.name ()));

    assertFalse (EXMLSerializeIndent.NONE.isIndent ());
    assertFalse (EXMLSerializeIndent.NONE.isAlign ());
    assertFalse (EXMLSerializeIndent.ALIGN_ONLY.isIndent ());
    assertTrue (EXMLSerializeIndent.ALIGN_ONLY.isAlign ());
    assertTrue (EXMLSerializeIndent.INDENT_AND_ALIGN.isIndent ());
    assertTrue (EXMLSerializeIndent.INDENT_AND_ALIGN.isAlign ());
  }
}
