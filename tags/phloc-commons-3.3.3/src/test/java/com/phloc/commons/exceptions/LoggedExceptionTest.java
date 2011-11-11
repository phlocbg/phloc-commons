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
package com.phloc.commons.exceptions;

import org.junit.Test;

/**
 * Test class of class {@link LoggedException}.
 * 
 * @author philip
 */
public final class LoggedExceptionTest
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = { "RV_EXCEPTION_NOT_THROWN", "RV_RETURN_VALUE_IGNORED" }, justification = "only constructor tests")
  public void testAll ()
  {
    new LoggedException ();
    new LoggedException ("any text");
    new LoggedException (new Exception ());
    new LoggedException ("any text", new Exception ());
    new LoggedException (false);
    new LoggedException (false, "any text");
    new LoggedException (false, new Exception ());
    new LoggedException (false, "any text", new Exception ());
    LoggedException.newException (new Exception ());
    LoggedException.newException (new LoggedException ());
    LoggedException.newException (new LoggedRuntimeException ());
    LoggedException.newException ("any text", new Exception ());
    LoggedException.newException ("any text", new LoggedException ());
    LoggedException.newException ("any text", new LoggedRuntimeException ());
  }
}
