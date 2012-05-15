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

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;

/**
 * Small class helper utility stuff class.
 * 
 * @author philip
 */
@Immutable
public final class ClassHelper
{
  // WeakHashMap because class is used as a key
  private static final Map <Class <?>, Class <?>> PRIMITIVE_TO_WRAPPER = new WeakHashMap <Class <?>, Class <?>> (8);
  private static final Map <Class <?>, Class <?>> WRAPPER_TO_PRIMITIVE = new WeakHashMap <Class <?>, Class <?>> (8);

  static
  {
    _registerPrimitiveMapping (boolean.class, Boolean.class);
    _registerPrimitiveMapping (byte.class, Byte.class);
    _registerPrimitiveMapping (char.class, Character.class);
    _registerPrimitiveMapping (double.class, Double.class);
    _registerPrimitiveMapping (float.class, Float.class);
    _registerPrimitiveMapping (int.class, Integer.class);
    _registerPrimitiveMapping (long.class, Long.class);
    _registerPrimitiveMapping (short.class, Short.class);
  }

  private static void _registerPrimitiveMapping (@Nonnull final Class <?> aPrimitiveType,
                                                 @Nonnull final Class <?> aPrimitiveWrapperType)
  {
    PRIMITIVE_TO_WRAPPER.put (aPrimitiveType, aPrimitiveWrapperType);
    WRAPPER_TO_PRIMITIVE.put (aPrimitiveWrapperType, aPrimitiveType);
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ClassHelper s_aInstance = new ClassHelper ();

  private ClassHelper ()
  {}

  @Nonnull
  public static ClassLoader getDefaultClassLoader ()
  {
    ClassLoader ret = null;
    try
    {
      ret = Thread.currentThread ().getContextClassLoader ();
    }
    catch (final Exception ex) // NOPMD
    {
      // e.g. security exception
    }

    // Fallback to class loader of this class
    if (ret == null)
      ret = ClassHelper.class.getClassLoader (); // NOPMD

    return ret;
  }

  public static boolean isPublicClass (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;

    // Interfaces or annotations are not allowed
    if (aClass.isInterface () || aClass.isAnnotation ())
      return false;

    // Only public classes are allowed
    if (!isPublic (aClass))
      return false;

    // Abstract classes are not allowed
    if (isAbstractClass (aClass))
      return false;

    return true;
  }

  /**
   * Check if the passed class is public, instancable and has a no-argument
   * constructor.
   * 
   * @param aClass
   *        The class to check. May be <code>null</code>.
   * @return <code>true</code> if the class is public, instancable and has a
   *         no-argument constructor that is public.
   */
  public static boolean isInstancableClass (@Nullable final Class <?> aClass)
  {
    if (!isPublicClass (aClass))
      return false;

    // Check if a default constructor is present
    try
    {
      aClass.getConstructor ((Class <?> []) null);
    }
    catch (final NoSuchMethodException ex)
    {
      return false;
    }
    return true;
  }

  public static boolean isPublic (@Nullable final Class <?> aClass)
  {
    return aClass != null && Modifier.isPublic (aClass.getModifiers ());
  }

  /**
   * Check if the passed class is an interface or not. Please note that
   * annotations are also interfaces!
   * 
   * @param aClass
   *        The class to check.
   * @return <code>true</code> if the class is an interface (or an annotation)
   */
  public static boolean isInterface (@Nullable final Class <?> aClass)
  {
    return aClass != null && Modifier.isInterface (aClass.getModifiers ());
  }

  public static boolean isAnnotationClass (@Nullable final Class <?> aClass)
  {
    return aClass != null && aClass.isAnnotation ();
  }

  public static boolean isEnumClass (@Nullable final Class <?> aClass)
  {
    return aClass != null && aClass.isEnum ();
  }

  /**
   * Check if the passed class is abstract or not. Note: interfaces and
   * annotations are also considered as abstract whereas arrays are never
   * abstract.
   * 
   * @param aClass
   *        The class to check.
   * @return <code>true</code> if the passed class is abstract
   */
  public static boolean isAbstractClass (@Nullable final Class <?> aClass)
  {
    // Special case for arrays (see documentation of Class.getModifiers: only
    // final and interface are set, the rest is indeterministic)
    return aClass != null && !aClass.isArray () && Modifier.isAbstract (aClass.getModifiers ());
  }

  public static boolean isArrayClass (@Nullable final Class <?> aClass)
  {
    return aClass != null && aClass.isArray ();
  }

  public static boolean isPrimitiveType (@Nullable final Class <?> aClass)
  {
    return aClass != null && PRIMITIVE_TO_WRAPPER.containsKey (aClass);
  }

  public static boolean isPrimitiveWrapperType (@Nullable final Class <?> aClass)
  {
    return aClass != null && WRAPPER_TO_PRIMITIVE.containsKey (aClass);
  }

  /**
   * Get the primitive wrapper class of the the passed primitive class.
   * 
   * @param aClass
   *        The primitive class. May be <code>null</code>.
   * @return <code>null</code> if the passed class is not a primitive class.
   */
  @Nullable
  public static Class <?> getPrimitiveWrapperClass (@Nullable final Class <?> aClass)
  {
    if (isPrimitiveWrapperType (aClass))
      return aClass;
    return PRIMITIVE_TO_WRAPPER.get (aClass);
  }

  /**
   * Get the primitive class of the the passed primitive wrapper class.
   * 
   * @param aClass
   *        The primitive wrapper class. May be <code>null</code>.
   * @return <code>null</code> if the passed class is not a primitive wrapper
   *         class.
   */
  @Nullable
  public static Class <?> getPrimitiveClass (@Nullable final Class <?> aClass)
  {
    if (isPrimitiveType (aClass))
      return aClass;
    return WRAPPER_TO_PRIMITIVE.get (aClass);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Class <?>> getAllPrimitiveClasses ()
  {
    return ContainerHelper.makeUnmodifiable (PRIMITIVE_TO_WRAPPER.keySet ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Class <?>> getAllPrimitiveWrapperClasses ()
  {
    return ContainerHelper.makeUnmodifiable (WRAPPER_TO_PRIMITIVE.keySet ());
  }

  public static boolean isStringClass (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;
    // Base class of String, StringBuffer and StringBuilder
    return CharSequence.class.isAssignableFrom (aClass);
  }

  public static boolean isCharacterClass (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;
    return Character.class.isAssignableFrom (aClass) || char.class.isAssignableFrom (aClass);
  }

  public static boolean isBooleanClass (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;
    return Boolean.class.isAssignableFrom (aClass) || boolean.class.isAssignableFrom (aClass);
  }

  public static boolean isFloatingPointClass (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;
    return Double.class.isAssignableFrom (aClass) ||
           double.class.isAssignableFrom (aClass) ||
           Float.class.isAssignableFrom (aClass) ||
           float.class.isAssignableFrom (aClass) ||
           BigDecimal.class.isAssignableFrom (aClass);
  }

  public static boolean isIntegerClass (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;
    return Byte.class.isAssignableFrom (aClass) ||
           byte.class.isAssignableFrom (aClass) ||
           Integer.class.isAssignableFrom (aClass) ||
           int.class.isAssignableFrom (aClass) ||
           Long.class.isAssignableFrom (aClass) ||
           long.class.isAssignableFrom (aClass) ||
           Short.class.isAssignableFrom (aClass) ||
           short.class.isAssignableFrom (aClass) ||
           BigInteger.class.isAssignableFrom (aClass);
  }

  /**
   * Get the complete super class hierarchy of the passed class including all
   * super classes and all interfaces of the passed class and of all parent
   * classes.
   * 
   * @param aClass
   *        The source class to get the list from.
   * @return A non-<code>null</code> and non-empty list containing the passed
   *         class and all super classes, and all super-interfaces. This list
   *         may contain duplicates in case a certain interface is implemented
   *         more than once!
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public static Collection <Class <?>> getClassHierarchy (@Nonnull final Class <?> aClass)
  {
    return getClassHierarchy (aClass, false);
  }

  /**
   * Get the complete super class hierarchy of the passed class including all
   * super classes and all interfaces of the passed class and of all parent
   * classes.
   * 
   * @param aClass
   *        The source class to get the list from.
   * @param bUniqueClasses
   *        if <code>true</code> the returned type is a {@link LinkedHashSet} of
   *        all classes, otherwise the result is an {@link ArrayList} that
   *        potentially contains duplicates!
   * @return A non-<code>null</code> and non-empty collection containing the
   *         passed class and all super classes, and all super-interfaces. This
   *         collection may contain duplicates in case a certain interface is
   *         implemented more than once and bUniqueClasses is <code>false</code>
   *         !
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public static Collection <Class <?>> getClassHierarchy (@Nonnull final Class <?> aClass, final boolean bUniqueClasses)
  {
    if (aClass == null)
      throw new NullPointerException ("class");

    final List <Class <?>> aOpenSrc = new ArrayList <Class <?>> ();
    final Collection <Class <?>> ret = bUniqueClasses ? new LinkedHashSet <Class <?>> () : new ArrayList <Class <?>> ();
    // Check the whole class hierarchy of the source class
    aOpenSrc.add (aClass);
    while (!aOpenSrc.isEmpty ())
    {
      final Class <?> aCurClass = aOpenSrc.remove (0);
      ret.add (aCurClass);

      // Add super-classes and interfaces
      // Super-classes have precedence over interfaces!
      for (final Class <?> aInterface : aCurClass.getInterfaces ())
        aOpenSrc.add (0, aInterface);
      if (aCurClass.getSuperclass () != null)
        aOpenSrc.add (0, aCurClass.getSuperclass ());
    }

    return ret;
  }
}
