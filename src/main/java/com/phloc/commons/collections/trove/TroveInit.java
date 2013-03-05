package com.phloc.commons.collections.trove;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Trove4J initialization helper.
 * 
 * @author philip
 */
@Immutable
public final class TroveInit
{
  @PresentForCodeCoverage
  private static final TroveInit s_aInstance = new TroveInit ();

  private TroveInit ()
  {}

  /**
   * Initialize all system properties for {@link gnu.trove.impl.Constants}. This
   * must be called, before the first Trove object is created!
   * 
   * @param bTroveVerbose
   *        <code>true</code> to log the created constants, <code>false</code>
   *        to do it silently.
   */
  public static void initTrove (final boolean bTroveVerbose)
  {
    if (bTroveVerbose)
      SystemProperties.setPropertyValue ("gnu.trove.verbose", "true");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.byte", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.char", "MAX_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.float", "NEGATIVE_INFINITY");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.double", "NEGATIVE_INFINITY");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.int", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.long", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.short", "MIN_VALUE");
  }
}
