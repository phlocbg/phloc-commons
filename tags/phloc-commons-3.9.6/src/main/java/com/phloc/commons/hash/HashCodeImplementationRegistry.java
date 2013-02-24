/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.UseDirectEqualsAndHashCode;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.lang.ClassHierarchyCache;
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

  private static final Logger s_aLogger = LoggerFactory.getLogger (HashCodeImplementationRegistry.class);
  private static final HashCodeImplementationRegistry s_aInstance = new HashCodeImplementationRegistry ();

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  // Use a weak hash map, because the key is a class
  private final Map <Class <?>, IHashCodeImplementation> m_aMap = new WeakHashMap <Class <?>, IHashCodeImplementation> ();

  // Cache for classes where direct implementation should be used
  private final Map <String, Boolean> m_aDirectHashCode = new HashMap <String, Boolean> ();

  // Cache for classes that implement hashCode directly
  private final Map <String, Boolean> m_aImplementsHashCode = new HashMap <String, Boolean> ();

  private HashCodeImplementationRegistry ()
  {
    // Register all implementations via SPI
    for (final IHashCodeImplementationRegistrarSPI aRegistrar : ServiceLoader.load (IHashCodeImplementationRegistrarSPI.class))
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
        s_aLogger.warn ("Another hashCode implementation for class " + aClass + " is already registered!");
      else
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

  private boolean _isUseDirectHashCode (@Nonnull final Class <?> aClass)
  {
    final String sClassName = aClass.getName ();

    m_aRWLock.readLock ().lock ();
    try
    {
      final Boolean aSavedState = m_aDirectHashCode.get (sClassName);
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
      final Boolean aSavedState = m_aDirectHashCode.get (sClassName);
      if (aSavedState != null)
        return aSavedState.booleanValue ();

      // Determine
      final boolean bHasAnnotation = aClass.getAnnotation (UseDirectEqualsAndHashCode.class) != null;
      m_aDirectHashCode.put (sClassName, Boolean.valueOf (bHasAnnotation));
      return bHasAnnotation;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  private boolean _implementsHashCodeItself (@Nonnull final Class <?> aClass)
  {
    final String sClassName = aClass.getName ();

    m_aRWLock.readLock ().lock ();
    try
    {
      final Boolean aSavedState = m_aImplementsHashCode.get (sClassName);
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
      final Boolean aSavedState = m_aImplementsHashCode.get (sClassName);
      if (aSavedState != null)
        return aSavedState.booleanValue ();

      // Determine
      boolean bRet = false;
      try
      {
        final Method aMethod = aClass.getDeclaredMethod ("hashCode");
        if (aMethod != null && aMethod.getReturnType ().equals (int.class))
          bRet = true;
      }
      catch (final NoSuchMethodException ex)
      {
        // ignore
      }
      m_aImplementsHashCode.put (sClassName, Boolean.valueOf (bRet));
      return bRet;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public IHashCodeImplementation getBestMatchingHashCodeImplementation (@Nullable final Class <?> aClass)
  {
    if (aClass != null)
    {
      IHashCodeImplementation aMatchingImplementation = null;
      Class <?> aMatchingClass = null;

      // No check required?
      if (_isUseDirectHashCode (aClass))
        return null;

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
              final IHashCodeImplementation aImpl = m_aMap.get (aCurClass);
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
        // implementation class implements hashCode, use the one from the class
        // Example: a converter for "Map" is registered, but "LRUCache" comes
        // with its own "hashCode" implementation
        if (ClassHelper.isInterface (aMatchingClass) && _implementsHashCodeItself (aClass))
        {
          // Remember to use direct implementation
          m_aRWLock.writeLock ().lock ();
          try
          {
            m_aDirectHashCode.put (aClass.getName (), Boolean.TRUE);
          }
          finally
          {
            m_aRWLock.writeLock ().unlock ();
          }
          return null;
        }

        if (!aMatchingClass.equals (aClass))
        {
          // We found a match by walking the hierarchy -> put that match in the
          // direct hit list for further speed up
          registerHashCodeImplementation (aClass, aMatchingImplementation);
        }

        return aMatchingImplementation;
      }

      // Handle arrays specially, because we cannot register a converter for
      // every potential array class (but we allow for special implementations)
      if (ClassHelper.isArrayClass (aClass))
        return new ArrayHashCodeImplementation ();

      // Remember to use direct implementation
      m_aRWLock.writeLock ().lock ();
      try
      {
        m_aDirectHashCode.put (aClass.getName (), Boolean.TRUE);
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }

    // No special handler found
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Found no hashCode implementation for " + aClass);

    // Definitely no special implementation
    return null;
  }

  public static int getHashCode (@Nullable final Object aObj)
  {
    if (aObj == null)
      return HashCodeCalculator.HASHCODE_NULL;

    // Get the best matching implementation
    final Class <?> aClass = aObj.getClass ();
    final IHashCodeImplementation aImpl = s_aInstance.getBestMatchingHashCodeImplementation (aClass);
    return aImpl == null ? aObj.hashCode () : aImpl.getHashCode (aObj);
  }
}
