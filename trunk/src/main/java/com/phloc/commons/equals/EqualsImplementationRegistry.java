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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ServiceLoaderBackport;

public final class EqualsImplementationRegistry implements IEqualsImplementationRegistry
{
  private static final class ArrayEqualsImplementation implements IEqualsImplementation
  {
    public ArrayEqualsImplementation ()
    {}

    public boolean areEqual (final Object aObj1, final Object aObj2)
    {
      final Object [] aArray1 = (Object []) aObj1;
      final Object [] aArray2 = (Object []) aObj2;
      // Size check
      final int nLength = aArray1.length;
      if (nLength != aArray2.length)
        return false;
      // Content check
      for (int i = 0; i < nLength; i++)
        if (!EqualsImplementationRegistry.areEqual (aArray1[i], aArray2[i]))
          return false;
      return true;
    }
  }

  private static final EqualsImplementationRegistry s_aInstance = new EqualsImplementationRegistry ();

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  // Use a weak hash map, because the key is a class
  private final Map <Class <?>, IEqualsImplementation> m_aMap = new WeakHashMap <Class <?>, IEqualsImplementation> ();

  // Use a weak hash map, because the key is a class
  private final Map <Class <?>, Boolean> m_aExceptions = new WeakHashMap <Class <?>, Boolean> ();

  private EqualsImplementationRegistry ()
  {
    // register special handler for Object array
    registerEqualsImplementation (Object [].class, new ArrayEqualsImplementation ());

    // Register all implementations via SPI
    for (final IEqualsImplementationRegistrarSPI aRegistrar : ServiceLoaderBackport.load (IEqualsImplementationRegistrarSPI.class))
      aRegistrar.registerEqualsImplementations (this);
  }

  @Nonnull
  public static EqualsImplementationRegistry getInstance ()
  {
    return s_aInstance;
  }

  public void registerEqualsImplementation (@Nonnull final Class <?> aClass, @Nonnull final IEqualsImplementation aImpl)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    if (aImpl == null)
      throw new NullPointerException ("implementation");

    if (aClass.equals (Object.class))
      throw new IllegalArgumentException ("You provide an equals implementation for Object.class!");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("Another equals implementation for class " +
                                            aClass +
                                            " is already implemented!");
      m_aMap.put (aClass, aImpl);

      // Get the corresponding array class and register a default converter
      @SuppressWarnings ("unchecked")
      final Class <Object []> aArrayClass = (Class <Object []>) Array.newInstance (aClass, 0).getClass ();
      m_aMap.put (aArrayClass, new ArrayEqualsImplementation ());
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  private boolean _implementsEqualsItself (final Class <?> aClass)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final Boolean aSavedState = m_aExceptions.get (aClass);
      if (aSavedState != null)
        return aSavedState.booleanValue ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Try again in write lock
      final Boolean aSavedState = m_aExceptions.get (aClass);
      if (aSavedState != null)
        return aSavedState.booleanValue ();

      // Determine
      boolean bRet = false;
      try
      {
        if (aClass.getDeclaredMethod ("equals", Object.class) != null)
          bRet = true;
      }
      catch (final Exception ex)
      {
        // ignore
      }
      m_aExceptions.put (aClass, Boolean.valueOf (bRet));
      return bRet;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public static <T> boolean areEqual (@Nullable final T aObj1, @Nullable final T aObj2)
  {
    // Same object - check first
    if (aObj1 == aObj2)
      return true;

    // Is only one value null?
    if (aObj1 == null || aObj2 == null)
      return false;

    // Check whether the implementation classes are identical
    final Class <?> aClass1 = aObj1.getClass ();
    final Class <?> aClass2 = aObj2.getClass ();
    if (!aClass1.equals (aClass2))
    {
      // Not the same class -> not equal!
      return false;
    }

    // Same class
    final IEqualsImplementation aImpl = s_aInstance.getBestMatchingEqualsImplementation (aClass1);

    // Start the main equals check
    boolean bAreEqual;
    if (aImpl == null)
    {
      // No special implementation found
      bAreEqual = aObj1.equals (aObj2);
    }
    else
    {
      // A special implementation was found!
      bAreEqual = aImpl.areEqual (aObj1, aObj2);
    }
    return bAreEqual;
  }

  public boolean hasEqualsImplementation (@Nullable final Class <?> aClass)
  {
    return getBestMatchingEqualsImplementation (aClass) != null;
  }

  @Nullable
  public IEqualsImplementation getBestMatchingEqualsImplementation (@Nullable final Class <?> aClass)
  {
    if (aClass != null)
    {
      Class <?> aMatchingConverterClass = null;
      IEqualsImplementation aMatchingImplementation = null;

      m_aRWLock.readLock ().lock ();
      try
      {
        for (final Class <?> aCurClass : ClassHelper.getClassHierarchy (aClass, true))
        {
          final IEqualsImplementation aImpl = m_aMap.get (aCurClass);
          if (aImpl != null)
          {
            aMatchingConverterClass = aCurClass;
            aMatchingImplementation = aImpl;
            break;
          }
        }
      }
      finally
      {
        m_aRWLock.readLock ().unlock ();
      }

      // Do this outside of the lock for performance reasons
      if (aMatchingImplementation != null)
      {
        // If the matching implementation is for an interface and the
        // implementation class implements equals, use the one from the class
        if (ClassHelper.isInterface (aMatchingConverterClass) && _implementsEqualsItself (aClass))
          return null;

        return aMatchingImplementation;
      }
    }

    // No special handler found

    // Handle arrays specially, because we cannot register a converter for
    // every potential array class
    if (ClassHelper.isArrayClass (aClass))
      return new ArrayEqualsImplementation ();

    // Definitely no special implementation
    return null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IEqualsImplementation> getAllEqualsImplementations (@Nullable final Class <?> aClass)
  {
    final List <IEqualsImplementation> ret = new ArrayList <IEqualsImplementation> ();
    if (aClass != null)
    {
      m_aRWLock.readLock ().lock ();
      try
      {
        for (final Class <?> aCurClass : ClassHelper.getClassHierarchy (aClass, true))
        {
          final IEqualsImplementation aImpl = m_aMap.get (aCurClass);
          if (aImpl != null)
            ret.add (aImpl);
        }
      }
      finally
      {
        m_aRWLock.readLock ().unlock ();
      }
    }
    return ret;
  }
}
