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
package com.phloc.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class MainJavaCreateNewEnum
{
  public enum Day
  {
    MON,
    TUE,
    WED,
    TH,
    FRI,
    SAT,
    SUN
  }

  public static void main (final String [] args) throws IllegalArgumentException,
                                                IllegalAccessException,
                                                InvocationTargetException,
                                                SecurityException,
                                                NoSuchMethodException
  {
    final Constructor <?> con = Day.class.getDeclaredConstructors ()[0];
    final Method [] methods = con.getClass ().getDeclaredMethods ();
    for (final Method m : methods)
    {
      if (m.getName ().equals ("acquireConstructorAccessor"))
      {
        AccessController.doPrivileged (new PrivilegedAction <Object> ()
        {
          public Object run ()
          {
            m.setAccessible (true);
            return null;
          }
        });
        m.invoke (con, new Object [0]);
      }
    }
    final Field [] fields = con.getClass ().getDeclaredFields ();
    Object ca = null;
    for (final Field f : fields)
    {
      if (f.getName ().equals ("constructorAccessor"))
      {
        AccessController.doPrivileged (new PrivilegedAction <Object> ()
        {
          public Object run ()
          {
            f.setAccessible (true);
            return null;
          }
        });
        ca = f.get (con);
      }
    }
    if (ca == null)
      throw new IllegalStateException ();
    final Method m = ca.getClass ().getMethod ("newInstance", new Class [] { Object [].class });
    AccessController.doPrivileged (new PrivilegedAction <Object> ()
    {
      public Object run ()
      {
        m.setAccessible (true);
        return null;
      }
    });
    final Day v = (Day) m.invoke (ca, new Object [] { new Object [] { "VACATION", Integer.valueOf (4711) } });
    System.out.println (v.getClass () + ":" + v.name () + ":" + v.ordinal ());
  }
}
