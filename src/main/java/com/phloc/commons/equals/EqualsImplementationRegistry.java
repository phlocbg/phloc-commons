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

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ClassHierarchyCache;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.commons.state.EChange;

@ThreadSafe
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

  private static final Logger s_aLogger = LoggerFactory.getLogger (EqualsImplementationRegistry.class);
  private static final EqualsImplementationRegistry s_aInstance = new EqualsImplementationRegistry ();

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  // Use a weak hash map, because the key is a class
  private final Map <Class <?>, IEqualsImplementation> m_aMap = new WeakHashMap <Class <?>, IEqualsImplementation> ();

  // Cache for classes that implement equals directly
  private final Map <String, Boolean> m_aImplementsEquals = new HashMap <String, Boolean> ();

  private EqualsImplementationRegistry ()
  {
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
      throw new IllegalArgumentException ("You cannot provide an equals implementation for Object.class!");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("Another equals implementation for class " +
                                            aClass +
                                            " is already implemented!");
      m_aMap.put (aClass, aImpl);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange unregisterEqualsImplementation (@Nonnull final Class <?> aClass)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (m_aMap.remove (aClass) != null);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  private boolean _implementsEqualsItself (@Nonnull final Class <?> aClass)
  {
    final String sClassName = aClass.getName ();

    m_aRWLock.readLock ().lock ();
    try
    {
      final Boolean aSavedState = m_aImplementsEquals.get (sClassName);
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
      final Boolean aSavedState = m_aImplementsEquals.get (sClassName);
      if (aSavedState != null)
        return aSavedState.booleanValue ();

      // Determine
      boolean bRet = false;
      try
      {
        final Method aMethod = aClass.getDeclaredMethod ("equals", Object.class);
        if (aMethod != null && aMethod.getReturnType ().equals (boolean.class))
          bRet = true;
      }
      catch (final NoSuchMethodException ex)
      {
        // ignore
      }
      m_aImplementsEquals.put (sClassName, Boolean.valueOf (bRet));
      return bRet;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public IEqualsImplementation getBestMatchingEqualsImplementation (@Nullable final Class <?> aClass)
  {
    if (aClass != null)
    {
      IEqualsImplementation aMatchingImplementation = null;
      Class <?> aMatchingClass = null;

      m_aRWLock.readLock ().lock ();
      try
      {
        // Check for an exact match first
        aMatchingImplementation = m_aMap.get (aClass);
        if (aMatchingImplementation != null)
          aMatchingClass = aClass;
        else
        {
          // Scan hierarchy in most efficient way
          for (final WeakReference <Class <?>> aCurWRClass : ClassHierarchyCache.getClassHierarchyIterator (aClass))
          {
            final Class <?> aCurClass = aCurWRClass.get ();
            if (aCurClass != null)
            {
              final IEqualsImplementation aImpl = m_aMap.get (aCurClass);
              if (aImpl != null)
              {
                aMatchingImplementation = aImpl;
                aMatchingClass = aCurClass;
                if (s_aLogger.isDebugEnabled ())
                  s_aLogger.debug ("Found hierarchical match with class " +
                                   aMatchingClass +
                                   " when searching for " +
                                   aClass);
                break;
              }
            }
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
        // Example: a converter for "Map" is registered, but "LRUCache" comes
        // with its own "equals" implementation
        if (ClassHelper.isInterface (aMatchingClass) && _implementsEqualsItself (aClass))
          return null;

        if (!aMatchingClass.equals (aClass))
        {
          // We found a match by walking the hierarchy -> put that match in the
          // direct hit list for further speed up
          m_aRWLock.writeLock ().lock ();
          try
          {
            if (!m_aMap.containsKey (aClass))
              m_aMap.put (aClass, aMatchingImplementation);
            else
              s_aLogger.warn ("We iterated the hierarchy for " +
                              aClass +
                              " and found " +
                              aMatchingClass +
                              ", but the sources class is already in the direct access map!");
          }
          finally
          {
            m_aRWLock.writeLock ().unlock ();
          }
        }

        return aMatchingImplementation;
      }

      // Handle arrays specially, because we cannot register a converter for
      // every potential array class (but we allow for special implementations)
      if (ClassHelper.isArrayClass (aClass))
        return new ArrayEqualsImplementation ();
    }

    // No special handler found
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Found no equals implementation for " + aClass);

    // Definitely no special implementation
    return null;
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
}
