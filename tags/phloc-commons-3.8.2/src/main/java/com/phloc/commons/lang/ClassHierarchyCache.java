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
package com.phloc.commons.lang;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.LRUCache;

/**
 * A small class hierarchy cache
 * 
 * @author philip
 */
@ThreadSafe
public final class ClassHierarchyCache
{
  @Immutable
  private static final class ClassList
  {
    private final List <WeakReference <Class <?>>> m_aList = new ArrayList <WeakReference <Class <?>>> ();

    public ClassList (@Nonnull final Class <?> aClass)
    {
      if (aClass == null)
        throw new NullPointerException ("class");
      // Check the whole class hierarchy of the source class
      final List <Class <?>> aOpenSrc = new ArrayList <Class <?>> ();
      aOpenSrc.add (aClass);
      while (!aOpenSrc.isEmpty ())
      {
        final Class <?> aCurClass = aOpenSrc.remove (0);
        // Avoid duplicates
        if (!contains (aCurClass))
          m_aList.add (new WeakReference <Class <?>> (aCurClass));

        // Add super-classes and interfaces
        // Super-classes have precedence over interfaces!
        for (final Class <?> aInterface : aCurClass.getInterfaces ())
          aOpenSrc.add (0, aInterface);
        if (aCurClass.getSuperclass () != null)
          aOpenSrc.add (0, aCurClass.getSuperclass ());
      }
    }

    public boolean contains (@Nullable final Class <?> aClass)
    {
      if (aClass != null)
        for (final WeakReference <Class <?>> aRef : m_aList)
          if (aClass.equals (aRef.get ()))
            return true;
      return false;
    }

    @Nonnull
    @ReturnsMutableCopy
    public Set <Class <?>> getAsSet ()
    {
      // Use a linked hash set, to maintain the order
      final Set <Class <?>> ret = new LinkedHashSet <Class <?>> ();
      for (final WeakReference <Class <?>> aRef : m_aList)
      {
        final Class <?> aClass = aRef.get ();
        if (aClass != null)
          ret.add (aClass);
      }
      return ret;
    }
  }

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <String, ClassList> s_aClassHierarchy = new LRUCache <String, ClassList> (1000);

  private ClassHierarchyCache ()
  {}

  /**
   * It's important to clear the cache upon application shutdown, because for
   * web applications, keeping a cache of classes may prevent the web
   * application from unloading
   */
  public static void clearClassHierarchyCache ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aClassHierarchy.clear ();
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Get the complete super class hierarchy of the passed class including all
   * super classes and all interfaces of the passed class and of all parent
   * classes.
   * 
   * @param aClass
   *        The source class to get the hierarchy from.
   * @return A non-<code>null</code> and non-empty Set containing the passed
   *         class and all super classes, and all super-interfaces. This list
   *         may contain duplicates in case a certain interface is implemented
   *         more than once!
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Set <Class <?>> getClassHierarchy (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    final String sKey = aClass.getName ();

    // Get or update from cache
    ClassList aClassList;
    s_aRWLock.readLock ().lock ();
    try
    {
      aClassList = s_aClassHierarchy.get (sKey);
      if (aClassList != null)
        return aClassList.getAsSet ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    s_aRWLock.writeLock ().lock ();
    try
    {
      // try again in write lock
      aClassList = s_aClassHierarchy.get (sKey);
      if (aClassList == null)
      {
        aClassList = new ClassList (aClass);
        s_aClassHierarchy.put (sKey, aClassList);
      }
      return aClassList.getAsSet ();
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
