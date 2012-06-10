package com.phloc.commons.actor.experimental;


/**
 * Assorted utility functions (sort of a kitchen sink). Some methods used but
 * core to Actor implementation. Additional unused methods may be included and
 * should be ignored.
 * 
 * @author bfeigenb
 */
public class Utils
{
  /** Safely implement sleep(). */
  public static void sleep (final long millis)
  {
    if (millis >= 0)
    {
      try
      {
        Thread.sleep (millis);
      }
      catch (final InterruptedException e)
      {
        // e.printStackTrace(System.out);
      }
    }
  }
}
