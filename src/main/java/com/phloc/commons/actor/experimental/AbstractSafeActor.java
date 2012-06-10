package com.phloc.commons.actor.experimental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An actor with exception trapping.
 * 
 * @author BFEIGENB
 */
public abstract class AbstractSafeActor extends AbstractActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractSafeActor.class);

  @Override
  protected void loopBody (final IActorMessage aMessage)
  {
    try
    {
      if (s_aLogger.isTraceEnabled ())
        s_aLogger.trace ("SafeActor loopBody: " + aMessage);
      doBody ((DefaultActorMessage) aMessage);
    }
    catch (final Exception e)
    {
      s_aLogger.error ("SafeActor: exception", e);
    }
  }

  @Override
  protected void runBody ()
  {
    // by default, nothing to do
  }

  /**
   * Override to define message reception behaviour.
   * 
   * @param aMessage
   * @throws Exception
   */
  protected abstract void doBody (DefaultActorMessage aMessage) throws Exception;
}
