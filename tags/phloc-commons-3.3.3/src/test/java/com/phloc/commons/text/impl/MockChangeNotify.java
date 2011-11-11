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
package com.phloc.commons.text.impl;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import com.phloc.commons.callback.IChangeNotify;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.text.IMultiLingualText;

public final class MockChangeNotify implements IChangeNotify <IMultiLingualText>
{
  private int m_nCallCountBefore = 0;
  private int m_nCallCountAfter = 0;

  public MockChangeNotify ()
  {}

  @Nonnull
  public EContinue beforeChange (final IMultiLingualText aObjectToChange)
  {
    assertNotNull (aObjectToChange);
    m_nCallCountBefore++;
    return EContinue.CONTINUE;
  }

  public void afterChange (final IMultiLingualText aChangedObject)
  {
    assertNotNull (aChangedObject);
    m_nCallCountAfter++;
  }

  public int getCallCountBefore ()
  {
    return m_nCallCountBefore;
  }

  public int getCallCountAfter ()
  {
    return m_nCallCountAfter;
  }
}
