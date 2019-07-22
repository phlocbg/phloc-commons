/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.annotation.Nullable;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link StackTraceHelper}.
 *
 * @author Philip Helger
 */
public final class StackTraceHelperTest
{
  private static class Root
  {
    public int getX ()
    {
      throw new UnsupportedOperationException ();
    }
  }

  private static class Derived extends Root
  {
    public int getY ()
    {
      try
      {
        return getX ();
      }
      catch (final UnsupportedOperationException ex)
      {
        throw new IllegalArgumentException ("Mock", ex);
      }
    }
  }

  @Test
  public void testAll ()
  {
    assertNotNull (StackTraceHelper.getStackAsString (Thread.currentThread ()));
    assertNotNull (StackTraceHelper.getStackAsString (new Exception ()));
    assertNotNull (StackTraceHelper.getStackAsString (new Exception (new Exception ())));
    assertNotNull (StackTraceHelper.getStackAsString (new Exception (), false));
    assertNotNull (StackTraceHelper.getStackAsString (new Exception ().getStackTrace ()));
    assertNotNull (StackTraceHelper.getStackAsString (new Exception ().getStackTrace (), false));
    assertEquals ("", StackTraceHelper.getStackAsString ((Throwable) null, false));

    // AppServer stacktrace :)
    final StackTraceElement [] ste = ArrayHelper.newArray (new StackTraceElement ("org.eclipse.jetty.Server",
                                                                                  "start",
                                                                                  "Server.java",
                                                                                  100));
    assertNotNull (StackTraceHelper.getStackAsString (ste));

    try
    {
      new Root ().getX ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {
      assertNotNull (StackTraceHelper.getStackAsString (ex));
    }
    try
    {
      new Derived ().getY ();
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      assertNotNull (StackTraceHelper.getStackAsString (ex));
    }
    assertTrue (StackTraceHelper.containsUnitTestElement (new Exception ().getStackTrace ()));
    new Thread (new Runnable ()
    {
      @Override
      public void run ()
      {
        assertNotNull (StackTraceHelper.getCurrentThreadStackAsString ());
        assertFalse (StackTraceHelper.containsUnitTestElement (new Exception ().getStackTrace ()));
      }
    }).start ();
    final StringBuilder aSB = new StringBuilder ();
    StackTraceHelper.appendStackToString (aSB, new Exception ().getStackTrace ());
    assertTrue (aSB.length () > 0);
    assertNotNull (StackTraceHelper.getCurrentThreadStackAsString ());
  }

  @Test
  public void testCustomizer ()
  {
    final StringBuilder aBigMessage = new StringBuilder ();
    for (int i = 0; i < 10000; i++)
    {
      aBigMessage.append ("abcdefghijklmnopqrstuvwxyz\n"); //$NON-NLS-1$
    }
    final Exception aInnerEx = new IllegalStateException ("Something is wrong \n in the sate of Denmark: \n\n" + //$NON-NLS-1$
                                                          aBigMessage.toString ());
    final Exception aOuterEx = new InitializationException ("Huston, we \n got \n a problem!\n\n" + //$NON-NLS-1$
                                                            aBigMessage.toString (),
                                                            aInnerEx);
    final String sStackTrace = StackTraceHelper.getStackAsString (aOuterEx, false, new IStackTraceCustomizer ()
    {
      @Override
      public String customizeMessage (@Nullable final String sMessage)
      {
        return StringHelper.getUntilFirstExcl (sMessage, '\n');
      }
    });
    Assert.assertEquals ("com.phloc.commons.exceptions.InitializationException: Huston, we \n",
                         StringHelper.getUntilFirstExcl (sStackTrace, "1.:"));
    Assert.assertEquals ("Something is wrong \n",
                         StringHelper.getUntilFirstExcl (StringHelper.getFromFirstExcl (sStackTrace,
                                                                                        "==> [1] caused by java.lang.IllegalStateException: "),
                                                         "1.:"));
  }
}
