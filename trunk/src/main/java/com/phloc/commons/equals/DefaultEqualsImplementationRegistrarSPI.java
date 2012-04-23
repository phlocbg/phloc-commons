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
package com.phloc.commons.equals;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBElement;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DefaultEqualsImplementationRegistrarSPI implements IEqualsImplementationRegistrarSPI
{
  public void registerEqualsImplementations (@Nonnull final IEqualsImplementationRegistry aRegistry)
  {
    /**
     * Special equals implementation for BigDecimal because
     * <code>BigDecimal.equals</code> returns <code>false</code> if they have a
     * different scale so that "5.5" is not equal "5.50".
     */
    aRegistry.registerEqualsImplementation (BigDecimal.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final BigDecimal aRealObj1 = (BigDecimal) aObj1;
        final BigDecimal aRealObj2 = (BigDecimal) aObj2;
        final int nMaxScale = Math.max (aRealObj1.scale (), aRealObj2.scale ());
        return aRealObj1.setScale (nMaxScale).equals (aRealObj2.setScale (nMaxScale));
      }
    });

    // Special overload for "Double" required!
    aRegistry.registerEqualsImplementation (Double.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final Double aRealObj1 = (Double) aObj1;
        final Double aRealObj2 = (Double) aObj2;
        return aRealObj1.compareTo (aRealObj2) == 0;
      }
    });

    // Special overload for "Float" required!
    aRegistry.registerEqualsImplementation (Float.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final Float aRealObj1 = (Float) aObj1;
        final Float aRealObj2 = (Float) aObj2;
        return aRealObj1.compareTo (aRealObj2) == 0;
      }
    });

    // StringBuffer does not implement equals!
    aRegistry.registerEqualsImplementation (StringBuffer.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return aObj1.toString ().equals (aObj2.toString ());
      }
    });

    // StringBuilder does not implement equals!
    aRegistry.registerEqualsImplementation (StringBuilder.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return aObj1.toString ().equals (aObj2.toString ());
      }
    });

    // Node does not implement equals
    aRegistry.registerEqualsImplementation (Node.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final Node aRealObj1 = (Node) aObj1;
        final Node aRealObj2 = (Node) aObj2;
        if (aRealObj1.getNodeType () != aRealObj2.getNodeType ())
          return false;
        if (!EqualsImplementationRegistry.areEqual (aRealObj1.getNodeName (), aRealObj2.getNodeName ()))
          return false;
        if (!EqualsImplementationRegistry.areEqual (aRealObj1.getLocalName (), aRealObj2.getLocalName ()))
          return false;
        if (!EqualsImplementationRegistry.areEqual (aRealObj1.getNamespaceURI (), aRealObj2.getNamespaceURI ()))
          return false;
        if (!EqualsImplementationRegistry.areEqual (aRealObj1.getPrefix (), aRealObj2.getPrefix ()))
          return false;
        if (!EqualsImplementationRegistry.areEqual (aRealObj1.getNodeValue (), aRealObj2.getNodeValue ()))
          return false;

        // For all children
        final NodeList aNL1 = aRealObj1.getChildNodes ();
        final NodeList aNL2 = aRealObj2.getChildNodes ();

        final int nLength = aNL1.getLength ();
        if (nLength != aNL2.getLength ())
          return false;

        for (int i = 0; i < nLength; ++i)
        {
          final Node aChild1 = aNL1.item (i);
          final Node aChild2 = aNL2.item (i);
          if (!EqualsImplementationRegistry.areEqual (aChild1, aChild2))
            return false;
        }

        return true;
      }
    });

    /**
     * Special equals implementation for URLs because <code>URL.equals</code>
     * performs a host lookup.<br>
     * <a href=
     * "http://michaelscharf.blogspot.com/2006/11/javaneturlequals-and-hashcode-make.html"
     * >Click here for details</a>
     */
    aRegistry.registerEqualsImplementation (URL.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final URL aRealObj1 = (URL) aObj1;
        final URL aRealObj2 = (URL) aObj2;
        return aRealObj1.toExternalForm ().equals (aRealObj2.toExternalForm ());
      }
    });

    // AtomicBoolean does not implement equals!
    aRegistry.registerEqualsImplementation (AtomicBoolean.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final AtomicBoolean aRealObj1 = (AtomicBoolean) aObj1;
        final AtomicBoolean aRealObj2 = (AtomicBoolean) aObj2;
        return aRealObj1.get () == aRealObj2.get ();
      }
    });

    // AtomicInteger does not implement equals!
    aRegistry.registerEqualsImplementation (AtomicInteger.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final AtomicInteger aRealObj1 = (AtomicInteger) aObj1;
        final AtomicInteger aRealObj2 = (AtomicInteger) aObj2;
        return aRealObj1.get () == aRealObj2.get ();
      }
    });

    // AtomicLong does not implement equals!
    aRegistry.registerEqualsImplementation (AtomicLong.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final AtomicLong aRealObj1 = (AtomicLong) aObj1;
        final AtomicLong aRealObj2 = (AtomicLong) aObj2;
        return aRealObj1.get () == aRealObj2.get ();
      }
    });

    // JAXBElement does not implement equals!
    aRegistry.registerEqualsImplementation (JAXBElement.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final JAXBElement <?> aRealObj1 = (JAXBElement <?>) aObj1;
        final JAXBElement <?> aRealObj2 = (JAXBElement <?>) aObj2;
        return EqualsImplementationRegistry.areEqual (aRealObj1.getDeclaredType (), aRealObj2.getDeclaredType ()) &&
               EqualsImplementationRegistry.areEqual (aRealObj1.getName (), aRealObj2.getName ()) &&
               EqualsImplementationRegistry.areEqual (aRealObj1.getScope (), aRealObj2.getScope ()) &&
               EqualsUtils.equals (aRealObj1.isNil (), aRealObj2.isNil ()) &&
               EqualsImplementationRegistry.areEqual (aRealObj1.getValue (), aRealObj2.getValue ());
      }
    });

    // Default array implementations
    // (Object[].class is handled specially!)
    aRegistry.registerEqualsImplementation (boolean [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((boolean []) aObj1, (boolean []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (byte [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((byte []) aObj1, (byte []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (char [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((char []) aObj1, (char []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (double [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((double []) aObj1, (double []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (float [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((float []) aObj1, (float []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (int [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((int []) aObj1, (int []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (long [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((long []) aObj1, (long []) aObj2);
      }
    });
    aRegistry.registerEqualsImplementation (short [].class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        return Arrays.equals ((short []) aObj1, (short []) aObj2);
      }
    });

    // Special handling for Map
    aRegistry.registerEqualsImplementation (Map.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final Map <?, ?> aRealObj1 = (Map <?, ?>) aObj1;
        final Map <?, ?> aRealObj2 = (Map <?, ?>) aObj2;

        // Size check
        if (aRealObj1.size () != aRealObj2.size ())
          return false;

        // Content check
        for (final Map.Entry <?, ?> aEntry1 : aRealObj1.entrySet ())
        {
          final Object aKey1 = aEntry1.getKey ();
          final Object aValue1 = aEntry1.getValue ();
          if (aValue1 == null)
          {
            // Second map must also contain null value
            if (!(aRealObj2.get (aKey1) == null && aRealObj2.containsKey (aKey1)))
              return false;
          }
          else
          {
            // Check value
            final Object aValue2 = aRealObj2.get (aKey1);
            if (!EqualsImplementationRegistry.areEqual (aValue1, aValue2))
              return false;
          }
        }
        return true;
      }
    });

    // Special handling for Collection
    aRegistry.registerEqualsImplementation (Collection.class, new IEqualsImplementation ()
    {
      public boolean areEqual (final Object aObj1, final Object aObj2)
      {
        final Collection <?> aRealObj1 = (Collection <?>) aObj1;
        final Collection <?> aRealObj2 = (Collection <?>) aObj2;

        // Size check
        if (aRealObj1.size () != aRealObj2.size ())
          return false;

        // Content check
        final Object [] aData1 = aRealObj1.toArray ();
        final Object [] aData2 = aRealObj2.toArray ();
        return EqualsImplementationRegistry.areEqual (aData1, aData2);
      }
    });
  }
}
