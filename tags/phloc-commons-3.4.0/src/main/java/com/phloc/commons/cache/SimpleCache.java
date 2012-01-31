package com.phloc.commons.cache;

import javax.annotation.Nonnull;

import com.phloc.commons.state.EChange;

/**
 * Implementation of the {@link SimpleCacheMBean} interface. Is a wrapper around
 * an {@link AbstractCache}.
 * 
 * @author philip
 */
final class SimpleCache implements SimpleCacheMBean
{
  private final AbstractCache <?, ?> m_aCache;

  public SimpleCache (final AbstractCache <?, ?> aCache)
  {
    m_aCache = aCache;
  }

  public int size ()
  {
    return m_aCache.size ();
  }

  public boolean isEmpty ()
  {
    return m_aCache.isEmpty ();
  }

  @Nonnull
  public EChange clearCache ()
  {
    return m_aCache.clearCache ();
  }
}
