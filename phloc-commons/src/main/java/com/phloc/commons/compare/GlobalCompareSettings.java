package com.phloc.commons.compare;

import com.phloc.commons.state.EChange;

/**
 * Settings for comparison
 * 
 * @author Boris Gregorcic
 */
public class GlobalCompareSettings
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final GlobalCompareSettings INSTANCE = new GlobalCompareSettings ();
  }

  /** By default <code>null</code> values come first */
  public static final boolean DEFAULT_NULL_VALUES_COME_FIRST = true;

  /**
   * By default <code>null</code> values come first for backwards compatibility
   * reasons
   */
  private boolean m_bSortNullValuesFirst = DEFAULT_NULL_VALUES_COME_FIRST;

  /**
   * Ctor for singleton creation
   */
  protected GlobalCompareSettings ()
  {
    // protected
  }

  /**
   * Ctor
   * 
   * @return the singleton instance
   */
  public static GlobalCompareSettings getInstance ()
  {
    return SingletonHolder.INSTANCE;
  }

  /**
   * @return Whether or not <code>null</code> values will be considered smaller
   *         than any other value and therefore will be sorted before other
   *         values
   */
  public boolean isSortNullValuesFirst ()
  {
    return this.m_bSortNullValuesFirst;
  }

  /**
   * This API can be used to influence the comparison behavior concerning
   * <code>null</code> values. By default, <code>null</code> values will be
   * considered smaller than any other value and therefore will be sorted before
   * other values.
   * 
   * @param bNullValuesFirst
   * @return
   */
  public EChange setSortNullValuesFirst (boolean bNullValuesFirst)
  {
    EChange eChange = EChange.valueOf (bNullValuesFirst != this.m_bSortNullValuesFirst);
    this.m_bSortNullValuesFirst = bNullValuesFirst;
    return eChange;
  }
}
