package com.phloc.commons.cache;

import javax.annotation.Nonnull;

import com.phloc.commons.IHasSize;
import com.phloc.commons.state.EChange;

/**
 * MBean interface for a simple cache
 * 
 * @author philip
 */
public interface SimpleCacheMBean extends IHasSize
{
  /**
   * Remove all cached elements.
   * 
   * @return {@link EChange}.
   */
  @Nonnull
  EChange clearCache ();
}
