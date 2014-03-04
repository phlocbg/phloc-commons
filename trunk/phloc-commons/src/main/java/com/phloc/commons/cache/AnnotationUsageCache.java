package com.phloc.commons.cache;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.string.ToStringGenerator;

/**
 * A simple cache for the usage of a certain annotation class at other classes.<br>
 * Note: cannot use {@link com.phloc.commons.cache.AbstractNotifyingCache}
 * because it would need a <code>Class&lt;?&gt;</code> as a key and this would
 * be a hard wired reference.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class AnnotationUsageCache
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Class <? extends Annotation> m_aAnnotationClass;
  @GuardedBy ("m_aRWLock")
  private final Map <String, Boolean> m_aMap = new HashMap <String, Boolean> ();

  /**
   * Constructor
   * 
   * @param aAnnotationClass
   *        The annotation class to store the existance of. It must have the
   *        {@link RetentionPolicy#RUNTIME} to be usable within this class!
   */
  public AnnotationUsageCache (@Nonnull final Class <? extends Annotation> aAnnotationClass)
  {
    if (aAnnotationClass == null)
      throw new NullPointerException ("AnnotationClass");

    // Check retention policy
    final Retention aRetention = aAnnotationClass.getAnnotation (Retention.class);
    final RetentionPolicy eRetentionPolicy = aRetention == null ? RetentionPolicy.CLASS : aRetention.value ();
    if (eRetentionPolicy != RetentionPolicy.RUNTIME)
      throw new IllegalArgumentException ("RetentionPolicy must be of type RUNTIME to be used within this cache. The current value ist " +
                                          eRetentionPolicy);

    // Save to members
    m_aAnnotationClass = aAnnotationClass;
  }

  /**
   * @return The annotation class passed in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final Class <? extends Annotation> getAnnotationClass ()
  {
    return m_aAnnotationClass;
  }

  public boolean hasAnnotation (@Nonnull final Object aObject)
  {
    if (aObject == null)
      throw new NullPointerException ("Object");

    return hasAnnotation (aObject.getClass ());
  }

  public boolean hasAnnotation (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("Class");

    final String sClassName = aClass.getName ();

    Boolean aIs;
    m_aRWLock.readLock ().lock ();
    try
    {
      aIs = m_aMap.get (sClassName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
    if (aIs == null)
    {
      m_aRWLock.writeLock ().lock ();
      try
      {
        // Try again in write-lock
        aIs = m_aMap.get (sClassName);
        if (aIs == null)
        {
          aIs = Boolean.valueOf (aClass.getAnnotation (m_aAnnotationClass) != null);
          m_aMap.put (sClassName, aIs);
        }
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
    return aIs.booleanValue ();
  }

  public void setAnnotation (@Nonnull final Class <?> aClass, final boolean bHasAnnotation)
  {
    if (aClass == null)
      throw new NullPointerException ("Class");

    final String sClassName = aClass.getName ();

    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aMap.put (sClassName, Boolean.valueOf (bHasAnnotation));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("annotationClass", m_aAnnotationClass)
                                       .append ("map", m_aMap)
                                       .toString ();
  }
}
