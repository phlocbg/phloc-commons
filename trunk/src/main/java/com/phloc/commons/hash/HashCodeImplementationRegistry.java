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
package com.phloc.commons.hash;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.commons.state.EChange;

/**
 * The main registry for the different {@link IHashCodeImplementation}
 * implementations.
 * 
 * @author philip
 */
@ThreadSafe
public final class HashCodeImplementationRegistry implements IHashCodeImplementationRegistry
{
  private static final class ArrayHashCodeImplementation implements IHashCodeImplementation
  {
    public ArrayHashCodeImplementation ()
    {}

    public int getHashCode (final Object aObj)
    {
      final Object [] aArray = (Object []) aObj;
      final int nLength = aArray.length;

      HashCodeGenerator aHC = new HashCodeGenerator (aObj).append (nLength);
      for (int i = 0; i < nLength; i++)
        aHC = aHC.append (aArray[i]);
      return aHC.getHashCode ();
    }
  }

  private static final HashCodeImplementationRegistry s_aInstance = new HashCodeImplementationRegistry ();

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  // Use a weak hash map, because the key is a class
  private final Map <Class <?>, IHashCodeImplementation> m_aMap = new WeakHashMap <Class <?>, IHashCodeImplementation> ();

  // Use a weak hash map, because the key is a class
  private final Map <Class <?>, Boolean> m_aExceptions = new WeakHashMap <Class <?>, Boolean> ();

  private HashCodeImplementationRegistry ()
  {
    // Register all implementations via SPI
    for (final IHashCodeImplementationRegistrarSPI aRegistrar : ServiceLoaderBackport.load (IHashCodeImplementationRegistrarSPI.class))
      aRegistrar.registerHashCodeImplementations (this);
  }

  @Nonnull
  public static HashCodeImplementationRegistry getInstance ()
  {
    return s_aInstance;
  }

  public void registerHashCodeImplementation (@Nonnull final Class <?> aClass,
                                              @Nonnull final IHashCodeImplementation aImpl)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    if (aImpl == null)
      throw new NullPointerException ("implementation");

    if (aClass.equals (Object.class))
      throw new IllegalArgumentException ("You cannot provide a hashCode implementation for Object.class!");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("Another hashCode implementation for class " +
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
  public EChange unregisterHashCodeImplementation (@Nonnull final Class <?> aClass)
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

  private boolean _implementsHashCodeItself (final Class <?> aClass)
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
        if (aClass.getDeclaredMethod ("hashCode") != null)
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

  public static int getHashCode (@Nullable final Object aObj)
  {
    if (aObj == null)
      return HashCodeCalculator.HASHCODE_NULL;
    final Class <?> aClass = aObj.getClass ();

    // Get the best matching implementation
    final IHashCodeImplementation aImpl = s_aInstance.getBestMatchingHashCodeImplementation (aClass);
    return aImpl == null ? aObj.hashCode () : aImpl.getHashCode (aObj);
  }

  @Nullable
  public IHashCodeImplementation getBestMatchingHashCodeImplementation (@Nullable final Class <?> aClass)
  {
    if (aClass != null)
    {
      IHashCodeImplementation aMatchingImplementation = null;
      Class <?> aMatchingConverterClass = null;

      m_aRWLock.readLock ().lock ();
      try
      {
        // Check for an exact match first
        aMatchingImplementation = m_aMap.get (aClass);
        if (aMatchingImplementation != null)
          aMatchingConverterClass = aClass;
        else
        {
          // Scan hierarchy
          for (final Class <?> aCurClass : ClassHelper.getClassHierarchy (aClass, true))
          {
            final IHashCodeImplementation ret = m_aMap.get (aCurClass);
            if (ret != null)
            {
              aMatchingImplementation = ret;
              aMatchingConverterClass = aCurClass;
              break;
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
        // implementation class implements hashCode, use the one from the class
        // Example: a converter for "Map" is registered, but "LRUCache" comes
        // with its own "hashCode" implementation
        if (ClassHelper.isInterface (aMatchingConverterClass) && _implementsHashCodeItself (aClass))
          return null;

        return aMatchingImplementation;
      }
    }

    // No special handler found

    // Handle arrays specially, because we cannot register a converter for
    // every potential array class
    if (ClassHelper.isArrayClass (aClass))
      return new ArrayHashCodeImplementation ();

    // Definitely no special implementation
    return null;
  }
}
