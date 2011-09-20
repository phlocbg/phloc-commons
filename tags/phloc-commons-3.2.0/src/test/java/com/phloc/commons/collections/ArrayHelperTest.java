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
package com.phloc.commons.collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for {@link ArrayHelper}
 * 
 * @author philip
 */
public final class ArrayHelperTest extends AbstractPhlocTestCase
{
  @Test
  public void testGetComponentClass ()
  {
    assertEquals (String.class, ArrayHelper.getComponentType (new String [0]));
    try
    {
      ArrayHelper.getComponentType (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testFirstElement ()
  {
    assertEquals ("d", ArrayHelper.getFirst ("d", "c", "b", "a"));
    assertNull (ArrayHelper.getFirst ((String []) null));
    assertNull (ArrayHelper.getFirst (new String [0]));
  }

  @Test
  public void testLastElement ()
  {
    assertEquals ("a", ArrayHelper.getLast ("d", "c", "b", "a"));
    assertNull (ArrayHelper.getLast ((String []) null));
    assertNull (ArrayHelper.getLast (new String [0]));
  }

  @Test
  public void testIsEmpty ()
  {
    assertTrue (ArrayHelper.isEmpty ((Object []) null));
    assertTrue (ArrayHelper.isEmpty (new Boolean [0]));

    assertTrue (ArrayHelper.isEmpty ((Object []) null));
    assertFalse (ArrayHelper.isEmpty (new Object [] { "x", Boolean.FALSE }));

    assertTrue (ArrayHelper.isEmpty ((boolean []) null));
    assertFalse (ArrayHelper.isEmpty (new boolean [] { true, false }));

    assertTrue (ArrayHelper.isEmpty ((byte []) null));
    assertFalse (ArrayHelper.isEmpty (new byte [] { (byte) 17, (byte) 18 }));

    assertTrue (ArrayHelper.isEmpty ((char []) null));
    assertFalse (ArrayHelper.isEmpty (new char [] { 'a', 'Z' }));

    assertTrue (ArrayHelper.isEmpty ((double []) null));
    assertFalse (ArrayHelper.isEmpty (new double [] { 3.14, 75712.324 }));

    assertTrue (ArrayHelper.isEmpty ((float []) null));
    assertFalse (ArrayHelper.isEmpty (new float [] { 3.14f, 75712.324f }));

    assertTrue (ArrayHelper.isEmpty ((int []) null));
    assertFalse (ArrayHelper.isEmpty (new int [] { 314, 75712324 }));

    assertTrue (ArrayHelper.isEmpty ((long []) null));
    assertFalse (ArrayHelper.isEmpty (new long [] { 314L, 75712324L }));

    assertTrue (ArrayHelper.isEmpty ((short []) null));
    assertFalse (ArrayHelper.isEmpty (new short [] { 32, 223 }));
  }

  @Test
  public void testSize ()
  {
    assertEquals (0, ArrayHelper.getSize ((Object []) null));
    assertEquals (2, ArrayHelper.getSize (new Object [] { "x", Boolean.FALSE }));

    assertEquals (0, ArrayHelper.getSize ((boolean []) null));
    assertEquals (2, ArrayHelper.getSize (new boolean [] { true, false }));

    assertEquals (0, ArrayHelper.getSize ((byte []) null));
    assertEquals (2, ArrayHelper.getSize (new byte [] { (byte) 17, (byte) 18 }));

    assertEquals (0, ArrayHelper.getSize ((char []) null));
    assertEquals (2, ArrayHelper.getSize (new char [] { 'a', 'Z' }));

    assertEquals (0, ArrayHelper.getSize ((double []) null));
    assertEquals (2, ArrayHelper.getSize (new double [] { 3.14, 75712.324 }));

    assertEquals (0, ArrayHelper.getSize ((float []) null));
    assertEquals (2, ArrayHelper.getSize (new float [] { 3.14f, 75712.324f }));

    assertEquals (0, ArrayHelper.getSize ((int []) null));
    assertEquals (2, ArrayHelper.getSize (new int [] { 314, 75712324 }));

    assertEquals (0, ArrayHelper.getSize ((long []) null));
    assertEquals (2, ArrayHelper.getSize (new long [] { 314L, 75712324L }));

    assertEquals (0, ArrayHelper.getSize ((short []) null));
    assertEquals (2, ArrayHelper.getSize (new short [] { 32, 223 }));
  }

  @Test
  public void testContains ()
  {
    {
      final String [] x = new String [] { "Hallo", "Welt", "aus", "Kopenhagen" };
      assertTrue (ArrayHelper.contains (x, "Hallo"));
      assertTrue (ArrayHelper.contains (x, "Kopenhagen"));
      assertFalse (ArrayHelper.contains (x, "hallo"));
      assertFalse (ArrayHelper.contains (null, "hallo"));
      assertFalse (ArrayHelper.contains (new String [0], "hallo"));
    }

    {
      final boolean [] x = new boolean [] { true, true };
      assertTrue (ArrayHelper.contains (x, true));
      assertFalse (ArrayHelper.contains (x, false));
      assertFalse (ArrayHelper.contains ((boolean []) null, false));
    }

    {
      final byte [] x = new byte [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, (byte) 1));
      assertFalse (ArrayHelper.contains (x, (byte) 4));
      assertFalse (ArrayHelper.contains ((byte []) null, (byte) 1));
    }

    {
      final char [] x = new char [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, (char) 1));
      assertFalse (ArrayHelper.contains (x, (char) 4));
      assertFalse (ArrayHelper.contains ((char []) null, 'c'));
    }

    {
      final double [] x = new double [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, 1.0));
      assertFalse (ArrayHelper.contains (x, 1.1));
      assertFalse (ArrayHelper.contains ((double []) null, 1.0));
    }

    {
      final float [] x = new float [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, 1.0F));
      assertFalse (ArrayHelper.contains (x, 1.1F));
      assertFalse (ArrayHelper.contains ((float []) null, 1.5f));
    }

    {
      final int [] x = new int [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, 1));
      assertFalse (ArrayHelper.contains (x, 4));
      assertFalse (ArrayHelper.contains ((int []) null, 7));
    }

    {
      final long [] x = new long [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, 1L));
      assertFalse (ArrayHelper.contains (x, 4L));
      assertFalse (ArrayHelper.contains ((long []) null, 7));
    }

    {
      final short [] x = new short [] { 1, 2, 3 };
      assertTrue (ArrayHelper.contains (x, (short) 1));
      assertFalse (ArrayHelper.contains (x, (short) 4));
      assertFalse (ArrayHelper.contains ((short []) null, (short) 6));
    }
  }

  @Test
  public void testIsArray ()
  {
    assertTrue (ArrayHelper.isArray (new String [] { "Hallo" }));
    assertTrue (ArrayHelper.isArray (new String [0]));
    assertTrue (ArrayHelper.isArray (new boolean [1]));
    assertTrue (ArrayHelper.isArray (new boolean [0]));
    assertFalse (ArrayHelper.isArray ((boolean []) null));
    assertFalse (ArrayHelper.isArray ((String) null));
    assertFalse (ArrayHelper.isArray (Boolean.TRUE));
    assertFalse (ArrayHelper.isArray ("Hi there"));
  }

  @Test
  public void testArrayEquals ()
  {
    assertTrue (ArrayHelper.isArrayEquals (null, null));
    assertFalse (ArrayHelper.isArrayEquals (new boolean [0], null));
    assertFalse (ArrayHelper.isArrayEquals (null, new boolean [0]));
    assertTrue (ArrayHelper.isArrayEquals (new boolean [0], new boolean [0]));
    assertFalse (ArrayHelper.isArrayEquals (new String [0], "Hello"));
    assertFalse (ArrayHelper.isArrayEquals ("Hello", new String [0]));
    assertFalse (ArrayHelper.isArrayEquals (new boolean [0], new byte [0]));
    assertFalse (ArrayHelper.isArrayEquals (new boolean [0], new boolean [1]));
    assertFalse (ArrayHelper.isArrayEquals (new boolean [1] [0], new boolean [1] [1]));
    assertTrue (ArrayHelper.isArrayEquals (new boolean [1] [1], new boolean [1] [1]));
    assertFalse (ArrayHelper.isArrayEquals (new String [] { "Hallo" }, new String [] { "Welt" }));
    assertTrue (ArrayHelper.isArrayEquals (new String [] { "Hallo" }, new String [] { "Hallo" }));
  }

  @Test
  public void testGetCopy ()
  {
    {
      final Object [] x = new Object [] { "Any", Integer.valueOf (1), " is ", Double.valueOf (1) };
      assertNull (ArrayHelper.getCopy ((Object []) null));
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((Object []) null, 1));
      assertArrayEquals (new Object [] { "Any" }, ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((Object []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0]);
    }

    {
      assertNull (ArrayHelper.getCopy ((boolean []) null));
      final boolean [] x = new boolean [] { true, true, true, false };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((boolean []) null, 1, 1));
      assertEquals (Boolean.valueOf (x[1]), Boolean.valueOf (ArrayHelper.getCopy (x, 1, 1)[0]));
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((boolean []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((byte []) null));
      final byte [] x = new byte [] { (byte) 17, (byte) 22, (byte) 255, (byte) 0 };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((byte []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0]);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((byte []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((char []) null));
      final char [] x = new char [] { 'a', 'Z', '0', '#' };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((char []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0]);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((char []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((double []) null));
      final double [] x = new double [] { 3.14, 22.45, -34, 255.99 };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((double []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0], 0);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((double []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((float []) null));
      final float [] x = new float [] { 3.14f, 22.45f, -34f, 255.99f };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((float []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0], 0);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((float []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((int []) null));
      final int [] x = new int [] { 314, 2245, -34, 25599 };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((int []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0]);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((int []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((long []) null));
      final long [] x = new long [] { 314, 2245, -34, 25599 };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((long []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0]);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((long []) null, 1));
    }

    {
      assertNull (ArrayHelper.getCopy ((short []) null));
      final short [] x = new short [] { 14, 22, -34, 127 };
      assertTrue (ArrayHelper.isArrayEquals (x, ArrayHelper.getCopy (x)));
      assertNull (ArrayHelper.getCopy ((short []) null, 1, 1));
      assertEquals (x[1], ArrayHelper.getCopy (x, 1, 1)[0]);
      assertNotNull (ArrayHelper.getCopy (x, 1));
      assertNull (ArrayHelper.getCopy ((short []) null, 1));
    }
  }

  /**
   * Test for method asObjectArray
   */
  @Test
  public void testAsObjectArray ()
  {
    assertNull (ArrayHelper.getAsObjectArray (null));
    assertNull (ArrayHelper.getAsObjectArray (new ArrayList <String> ()));
    assertArrayEquals (new Object [] { "Hallo" }, ArrayHelper.getAsObjectArray (ContainerHelper.newList ("Hallo")));
    assertArrayEquals (new Object [] { "Hallo", "Welt" },
                       ArrayHelper.getAsObjectArray (ContainerHelper.newList ("Hallo", "Welt")));
    assertArrayEquals (new Object [] { I1, "Welt" },
                       ArrayHelper.getAsObjectArray (ContainerHelper.<Object> newList (I1, "Welt")));
  }

  /**
   * Test method safeGetElement
   */
  @Test
  public void testSafeGetElement ()
  {
    final String [] x = new String [] { "a", "b", "c" };
    assertEquals ("a", ArrayHelper.getSafeElement (x, 0));
    assertEquals ("b", ArrayHelper.getSafeElement (x, 1));
    assertEquals ("c", ArrayHelper.getSafeElement (x, 2));
    assertNull (ArrayHelper.getSafeElement (x, 3));
    assertNull (ArrayHelper.getSafeElement (x, 12345));
    assertNull (ArrayHelper.getSafeElement (x, -1));
    assertNull (ArrayHelper.getSafeElement (new String [0], 0));
    assertNull (ArrayHelper.getSafeElement ((String []) null, 0));
  }

  /**
   * Test for method concatenate
   */
  @SuppressWarnings ("deprecation")
  @Test
  public void testGetConcatenated ()
  {
    {
      final String [] a = new String [] { "a", "b" };
      final String [] b = new String [] { "c", "c2" };
      assertArrayEquals (new String [] { "a", "b", "c", "c2" }, ArrayHelper.getConcatenated (a, b));
      assertArrayEquals (new String [] { "c", "c2", "a", "b" }, ArrayHelper.getConcatenated (b, a));
      assertArrayEquals (a, ArrayHelper.getConcatenated (a, (String []) null));
      assertArrayEquals (b, ArrayHelper.getConcatenated ((String []) null, b));
      assertArrayEquals (new String [] { "a", "b", "c" }, ArrayHelper.getConcatenated (a, "c"));
      assertArrayEquals (new String [] { "c" }, ArrayHelper.getConcatenated ((String []) null, "c"));
      assertArrayEquals (new String [] { "c", "a", "b" }, ArrayHelper.getConcatenated ("c", a));
      assertArrayEquals (new String [] { "c" }, ArrayHelper.getConcatenated ("c", (String []) null));
    }

    {
      final Integer [] a = new Integer [] { I1, I2 };
      final Integer [] b = new Integer [] { I3, I4 };

      // Generic
      assertArrayEquals (new Integer [] { I1, I2, I3, I4 }, ArrayHelper.getConcatenated (a, b, Integer.class));
      assertArrayEquals (new Integer [] { I1, I2, I3, I4 }, ArrayHelper.getConcatenated (a, b));
      assertArrayEquals (new Integer [] { I3, I4, I1, I2 }, ArrayHelper.getConcatenated (b, a));
      assertArrayEquals (a, ArrayHelper.getConcatenated (a, (Integer []) null));
      assertArrayEquals (b, ArrayHelper.getConcatenated ((Integer []) null, b));
      assertArrayEquals (new Integer [] { I1, I2, I5 }, ArrayHelper.getConcatenated (a, I5, Integer.class));
      assertArrayEquals (new Integer [] { I5 }, ArrayHelper.getConcatenated ((Integer []) null, I5, Integer.class));
      assertArrayEquals (new Integer [] { I6, I1, I2 }, ArrayHelper.getConcatenated (I6, a, Integer.class));
      assertArrayEquals (new Integer [] { I6 }, ArrayHelper.getConcatenated (I6, (Integer []) null, Integer.class));
      assertArrayEquals (new Integer [] { I1, I2, null },
                         ArrayHelper.getConcatenated (a, (Integer) null, Integer.class));
      assertArrayEquals (new Integer [] { null, I3, I4 },
                         ArrayHelper.getConcatenated ((Integer) null, b, Integer.class));
    }

    {
      assertTrue (Arrays.equals (new boolean [] { false, false, true },
                                 ArrayHelper.getConcatenated (new boolean [] { false, false }, new boolean [] { true })));
      assertTrue (Arrays.equals (new boolean [] { false, false },
                                 ArrayHelper.getConcatenated (new boolean [] { false, false }, (boolean []) null)));
      assertTrue (Arrays.equals (new boolean [] { true },
                                 ArrayHelper.getConcatenated ((boolean []) null, new boolean [] { true })));
      assertTrue (Arrays.equals (new boolean [] { false, false, true },
                                 ArrayHelper.getConcatenated (new boolean [] { false, false }, true)));
      assertTrue (Arrays.equals (new boolean [] { true }, ArrayHelper.getConcatenated ((boolean []) null, true)));
      assertTrue (Arrays.equals (new boolean [] { false, false, true },
                                 ArrayHelper.getConcatenated (false, new boolean [] { false, true })));
      assertTrue (Arrays.equals (new boolean [] { false }, ArrayHelper.getConcatenated (false, (boolean []) null)));
    }

    {
      assertTrue (Arrays.equals (new byte [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new byte [] { 1, 2 }, new byte [] { 3, 4 })));
      assertTrue (Arrays.equals (new byte [] { 1, 2 },
                                 ArrayHelper.getConcatenated (new byte [] { 1, 2 }, (byte []) null)));
      assertTrue (Arrays.equals (new byte [] { 3, 4 },
                                 ArrayHelper.getConcatenated ((byte []) null, new byte [] { 3, 4 })));
      assertTrue (Arrays.equals (new byte [] { 1, 2, 3 }, ArrayHelper.getConcatenated (new byte [] { 1, 2 }, (byte) 3)));
      assertTrue (Arrays.equals (new byte [] { 3 }, ArrayHelper.getConcatenated ((byte []) null, (byte) 3)));
      assertTrue (Arrays.equals (new byte [] { 1, 2, 3 }, ArrayHelper.getConcatenated ((byte) 1, new byte [] { 2, 3 })));
      assertTrue (Arrays.equals (new byte [] { 1 }, ArrayHelper.getConcatenated ((byte) 1, (byte []) null)));
    }

    {
      assertTrue (Arrays.equals (new char [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new char [] { 1, 2 }, new char [] { 3, 4 })));
      assertTrue (Arrays.equals (new char [] { 1, 2 },
                                 ArrayHelper.getConcatenated (new char [] { 1, 2 }, (char []) null)));
      assertTrue (Arrays.equals (new char [] { 3, 4 },
                                 ArrayHelper.getConcatenated ((char []) null, new char [] { 3, 4 })));
      assertTrue (Arrays.equals (new char [] { 1, 2, 3 }, ArrayHelper.getConcatenated (new char [] { 1, 2 }, (char) 3)));
      assertTrue (Arrays.equals (new char [] { 3 }, ArrayHelper.getConcatenated ((char []) null, (char) 3)));
      assertTrue (Arrays.equals (new char [] { 1, 2, 3 }, ArrayHelper.getConcatenated ((char) 1, new char [] { 2, 3 })));
      assertTrue (Arrays.equals (new char [] { 1 }, ArrayHelper.getConcatenated ((char) 1, (char []) null)));
    }

    {
      assertTrue (Arrays.equals (new double [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new double [] { 1, 2 }, new double [] { 3, 4 })));
      assertTrue (Arrays.equals (new double [] { 1, 2 },
                                 ArrayHelper.getConcatenated (new double [] { 1, 2 }, (double []) null)));
      assertTrue (Arrays.equals (new double [] { 3, 4 },
                                 ArrayHelper.getConcatenated ((double []) null, new double [] { 3, 4 })));
      assertTrue (Arrays.equals (new double [] { 1, 2, 3 }, ArrayHelper.getConcatenated (new double [] { 1, 2 }, 3)));
      assertTrue (Arrays.equals (new double [] { 3 }, ArrayHelper.getConcatenated ((double []) null, 3)));
      assertTrue (Arrays.equals (new double [] { 1, 2, 3 }, ArrayHelper.getConcatenated (1, new double [] { 2, 3 })));
      assertTrue (Arrays.equals (new double [] { 1 }, ArrayHelper.getConcatenated (1, (double []) null)));
    }

    {
      assertTrue (Arrays.equals (new float [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new float [] { 1, 2 }, new float [] { 3, 4 })));
      assertTrue (Arrays.equals (new float [] { 1, 2 },
                                 ArrayHelper.getConcatenated (new float [] { 1, 2 }, (float []) null)));
      assertTrue (Arrays.equals (new float [] { 3, 4 },
                                 ArrayHelper.getConcatenated ((float []) null, new float [] { 3, 4 })));
      assertTrue (Arrays.equals (new float [] { 1, 2, 3 }, ArrayHelper.getConcatenated (new float [] { 1, 2 }, 3)));
      assertTrue (Arrays.equals (new float [] { 3 }, ArrayHelper.getConcatenated ((float []) null, 3)));
      assertTrue (Arrays.equals (new float [] { 1, 2, 3 }, ArrayHelper.getConcatenated (1, new float [] { 2, 3 })));
      assertTrue (Arrays.equals (new float [] { 1 }, ArrayHelper.getConcatenated (1, (float []) null)));
    }

    {
      assertTrue (Arrays.equals (new int [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new int [] { 1, 2 }, new int [] { 3, 4 })));
      assertTrue (Arrays.equals (new int [] { 1, 2 }, ArrayHelper.getConcatenated (new int [] { 1, 2 }, (int []) null)));
      assertTrue (Arrays.equals (new int [] { 3, 4 }, ArrayHelper.getConcatenated ((int []) null, new int [] { 3, 4 })));
      assertTrue (Arrays.equals (new int [] { 1, 2, 3 }, ArrayHelper.getConcatenated (new int [] { 1, 2 }, 3)));
      assertTrue (Arrays.equals (new int [] { 3 }, ArrayHelper.getConcatenated ((int []) null, 3)));
      assertTrue (Arrays.equals (new int [] { 1, 2, 3 }, ArrayHelper.getConcatenated (1, new int [] { 2, 3 })));
      assertTrue (Arrays.equals (new int [] { 1 }, ArrayHelper.getConcatenated (1, (int []) null)));
    }

    {
      assertTrue (Arrays.equals (new long [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new long [] { 1, 2 }, new long [] { 3, 4 })));
      assertTrue (Arrays.equals (new long [] { 1, 2 },
                                 ArrayHelper.getConcatenated (new long [] { 1, 2 }, (long []) null)));
      assertTrue (Arrays.equals (new long [] { 3, 4 },
                                 ArrayHelper.getConcatenated ((long []) null, new long [] { 3, 4 })));
      assertTrue (Arrays.equals (new long [] { 1, 2, 3 }, ArrayHelper.getConcatenated (new long [] { 1, 2 }, 3L)));
      assertTrue (Arrays.equals (new long [] { 3 }, ArrayHelper.getConcatenated ((long []) null, 3L)));
      assertTrue (Arrays.equals (new long [] { 1, 2, 3 }, ArrayHelper.getConcatenated (1L, new long [] { 2, 3 })));
      assertTrue (Arrays.equals (new long [] { 1 }, ArrayHelper.getConcatenated (1L, (long []) null)));
    }

    {
      assertTrue (Arrays.equals (new short [] { 1, 2, 3, 4 },
                                 ArrayHelper.getConcatenated (new short [] { 1, 2 }, new short [] { 3, 4 })));
      assertTrue (Arrays.equals (new short [] { 1, 2 },
                                 ArrayHelper.getConcatenated (new short [] { 1, 2 }, (short []) null)));
      assertTrue (Arrays.equals (new short [] { 3, 4 },
                                 ArrayHelper.getConcatenated ((short []) null, new short [] { 3, 4 })));
      assertTrue (Arrays.equals (new short [] { 1, 2, 3 },
                                 ArrayHelper.getConcatenated (new short [] { 1, 2 }, (short) 3)));
      assertTrue (Arrays.equals (new short [] { 3 }, ArrayHelper.getConcatenated ((short []) null, (short) 3)));
      assertTrue (Arrays.equals (new short [] { 1, 2, 3 },
                                 ArrayHelper.getConcatenated ((short) 1, new short [] { 2, 3 })));
      assertTrue (Arrays.equals (new short [] { 1 }, ArrayHelper.getConcatenated ((short) 1, (short []) null)));
    }
  }

  @Test
  public void testNewArray ()
  {
    final String [] a = ArrayHelper.newArray (String.class, 3);
    assertNotNull (a);
    assertTrue (ArrayHelper.isArray (a));
    assertEquals (3, a.length);
    for (int i = 0; i < 3; ++i)
      assertNull (a[i]);

    try
    {
      ArrayHelper.newArray ((Class <?>) null, 5);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      ArrayHelper.newArray (byte.class, 5);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      ArrayHelper.newArray (String.class, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testNewArrayFromCollection ()
  {
    String [] x = ArrayHelper.newArray (ContainerHelper.newList ("s1", "s2", "s3"), String.class);
    assertNotNull (x);
    assertEquals (3, x.length);

    x = ArrayHelper.newArray (new ArrayList <String> (), String.class);
    assertNull (x);

    x = ArrayHelper.newArray ((List <String>) null, String.class);
    assertNull (x);
  }

  @Test
  public void testNewArrayFromArray ()
  {
    String [] x = ArrayHelper.newArray ("s1", "s2", "s3");
    assertNotNull (x);
    assertEquals (3, x.length);

    x = ArrayHelper.newArray (new String [0]);
    assertNotNull (x);
    assertEquals (0, x.length);
  }

  @Test
  public void testNewArraySizeValue ()
  {
    String [] ret = ArrayHelper.newArray (1, "6", String.class);
    assertNotNull (ret);
    assertEquals (1, ret.length);
    assertEquals ("6", ret[0]);

    ret = ArrayHelper.newArray (0, "Hello world", String.class);
    assertNotNull (ret);
    assertEquals (0, ret.length);

    ret = ArrayHelper.newArray (10, "Hello world", String.class);
    assertNotNull (ret);
    assertEquals (10, ret.length);
    for (int i = 0; i < ret.length; ++i)
      assertEquals ("Hello world", ret[i]);

    try
    {
      // negative size
      ArrayHelper.newArray (-1, "6", String.class);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // no class
      ArrayHelper.newArray (1, "6", null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  /**
   * Test for method getAllExceptFirst
   */
  @Test
  public void testGetAllExceptFirst ()
  {
    // Generic
    {
      final String [] x = new String [] { "s1", "s2", "s3" };
      final String [] y = new String [] { "s2", "s3" };
      final String [] z = new String [] { "s3" };
      assertArrayEquals (y, ArrayHelper.getAllExceptFirst (x));
      assertArrayEquals (x, ArrayHelper.getAllExceptFirst (x, 0));
      assertArrayEquals (y, ArrayHelper.getAllExceptFirst (x, 1));
      assertArrayEquals (z, ArrayHelper.getAllExceptFirst (x, 2));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (x[0]));
      assertNull (ArrayHelper.getAllExceptFirst (new String [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((String []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // boolean
    {
      final boolean [] x = new boolean [] { true, false, true };
      final boolean [] y = new boolean [] { false, true };
      final boolean [] z = new boolean [] { true };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new boolean [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new boolean [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((boolean []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // byte
    {
      final byte [] x = new byte [] { 5, 9, 14 };
      final byte [] y = new byte [] { 9, 14 };
      final byte [] z = new byte [] { 14 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new byte [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new byte [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((byte []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // char
    {
      final char [] x = new char [] { 'a', 'B', 'c' };
      final char [] y = new char [] { 'B', 'c' };
      final char [] z = new char [] { 'c' };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new char [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new char [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((char []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // double
    {
      final double [] x = new double [] { -1.1, 0, 1.1 };
      final double [] y = new double [] { 0, 1.1 };
      final double [] z = new double [] { 1.1 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new double [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new double [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((double []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // float
    {
      final float [] x = new float [] { -3.2f, -0.01f, 99.8f };
      final float [] y = new float [] { -0.01f, 99.8f };
      final float [] z = new float [] { 99.8f };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new float [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new float [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((float []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // int
    {
      final int [] x = new int [] { -5, 2, 9 };
      final int [] y = new int [] { 2, 9 };
      final int [] z = new int [] { 9 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new int [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new int [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((int []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // long
    {
      final long [] x = new long [] { -6, 2, 10 };
      final long [] y = new long [] { 2, 10 };
      final long [] z = new long [] { 10 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new long [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new long [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((long []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // short
    {
      final short [] x = new short [] { -100, -10, 1 };
      final short [] y = new short [] { -10, 1 };
      final short [] z = new short [] { 1 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptFirst (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptFirst (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptFirst (x, 2)));
      assertNull (ArrayHelper.getAllExceptFirst (x, 3));
      assertNull (ArrayHelper.getAllExceptFirst (x, 4));
      assertNull (ArrayHelper.getAllExceptFirst (new short [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptFirst (new short [0]));
      assertNull (ArrayHelper.getAllExceptFirst ((short []) null));
      try
      {
        ArrayHelper.getAllExceptFirst (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }
  }

  /**
   * Test for method getAllExceptLast
   */
  @Test
  public void testGetAllExceptLast ()
  {
    // Generic
    {
      final String [] x = new String [] { "s1", "s2", "s3" };
      final String [] y = new String [] { "s1", "s2" };
      final String [] z = new String [] { "s1" };
      assertArrayEquals (y, ArrayHelper.getAllExceptLast (x));
      assertArrayEquals (x, ArrayHelper.getAllExceptLast (x, 0));
      assertArrayEquals (y, ArrayHelper.getAllExceptLast (x, 1));
      assertArrayEquals (z, ArrayHelper.getAllExceptLast (x, 2));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (x[0]));
      assertNull (ArrayHelper.getAllExceptLast (new String [0]));
      assertNull (ArrayHelper.getAllExceptLast ((String []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // boolean
    {
      final boolean [] x = new boolean [] { true, false, true };
      final boolean [] y = new boolean [] { true, false };
      final boolean [] z = new boolean [] { true };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new boolean [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new boolean [0]));
      assertNull (ArrayHelper.getAllExceptLast ((boolean []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // byte
    {
      final byte [] x = new byte [] { 5, 9, 14 };
      final byte [] y = new byte [] { 5, 9 };
      final byte [] z = new byte [] { 5 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new byte [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new byte [0]));
      assertNull (ArrayHelper.getAllExceptLast ((byte []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // char
    {
      final char [] x = new char [] { 'a', 'B', 'c' };
      final char [] y = new char [] { 'a', 'B' };
      final char [] z = new char [] { 'a' };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new char [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new char [0]));
      assertNull (ArrayHelper.getAllExceptLast ((char []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // double
    {
      final double [] x = new double [] { -1.1, 0, 1.1 };
      final double [] y = new double [] { -1.1, 0 };
      final double [] z = new double [] { -1.1 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new double [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new double [0]));
      assertNull (ArrayHelper.getAllExceptLast ((double []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // float
    {
      final float [] x = new float [] { -3.2f, -0.01f, 99.8f };
      final float [] y = new float [] { -3.2f, -0.01f };
      final float [] z = new float [] { -3.2f };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new float [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new float [0]));
      assertNull (ArrayHelper.getAllExceptLast ((float []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // int
    {
      final int [] x = new int [] { -5, 2, 9 };
      final int [] y = new int [] { -5, 2 };
      final int [] z = new int [] { -5 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new int [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new int [0]));
      assertNull (ArrayHelper.getAllExceptLast ((int []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // long
    {
      final long [] x = new long [] { -6, 2, 10 };
      final long [] y = new long [] { -6, 2 };
      final long [] z = new long [] { -6 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new long [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new long [0]));
      assertNull (ArrayHelper.getAllExceptLast ((long []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }

    // short
    {
      final short [] x = new short [] { -100, -10, 1 };
      final short [] y = new short [] { -100, -10 };
      final short [] z = new short [] { -100 };
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x)));
      assertTrue (Arrays.equals (x, ArrayHelper.getAllExceptLast (x, 0)));
      assertTrue (Arrays.equals (y, ArrayHelper.getAllExceptLast (x, 1)));
      assertTrue (Arrays.equals (z, ArrayHelper.getAllExceptLast (x, 2)));
      assertNull (ArrayHelper.getAllExceptLast (x, 3));
      assertNull (ArrayHelper.getAllExceptLast (x, 4));
      assertNull (ArrayHelper.getAllExceptLast (new short [] { x[0] }));
      assertNull (ArrayHelper.getAllExceptLast (new short [0]));
      assertNull (ArrayHelper.getAllExceptLast ((short []) null));
      try
      {
        ArrayHelper.getAllExceptLast (x, -1);
        fail ();
      }
      catch (final IllegalArgumentException ex)
      {}
    }
  }
}
