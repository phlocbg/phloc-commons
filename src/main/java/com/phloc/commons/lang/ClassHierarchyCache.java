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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.LRUCache;

/**
 * A small class hierarchy cache
 * 
 * @author philip
 */
@Immutable
public final class ClassHierarchyCache
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <String, Set <Class <?>>> s_aClassHierarchy = new LRUCache <String, Set <Class <?>>> (1000);

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
   * @return A non-<code>null</code> and non-empty list containing the passed
   *         class and all super classes, and all super-interfaces. This list
   *         may contain duplicates in case a certain interface is implemented
   *         more than once!
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public static Set <Class <?>> getClassHierarchy (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    final String sKey = aClass.getName ();

    // Get or update from cache
    Set <Class <?>> ret;
    s_aRWLock.readLock ().lock ();
    try
    {
      ret = s_aClassHierarchy.get (sKey);
      if (ret != null)
        return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    s_aRWLock.writeLock ().lock ();
    try
    {
      // try again in write lock
      ret = s_aClassHierarchy.get (sKey);
      if (ret != null)
        return ret;

      final Set <Class <?>> aSet = new LinkedHashSet <Class <?>> ();

      // Check the whole class hierarchy of the source class
      final List <Class <?>> aOpenSrc = new ArrayList <Class <?>> ();
      aOpenSrc.add (aClass);
      while (!aOpenSrc.isEmpty ())
      {
        final Class <?> aCurClass = aOpenSrc.remove (0);
        aSet.add (aCurClass);

        // Add super-classes and interfaces
        // Super-classes have precedence over interfaces!
        for (final Class <?> aInterface : aCurClass.getInterfaces ())
          aOpenSrc.add (0, aInterface);
        if (aCurClass.getSuperclass () != null)
          aOpenSrc.add (0, aCurClass.getSuperclass ());
      }

      // Put as unmodifiable inside the container
      ret = ContainerHelper.makeUnmodifiable (aSet);
      s_aClassHierarchy.put (sKey, ret);
      return ret;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
