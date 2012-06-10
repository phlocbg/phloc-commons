package com.phloc.commons.actor.experimental;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An actor that sends messages while counting down a send count.
 * 
 * @author BFEIGENB
 */
public class TestActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (TestActor.class);

  @Override
  public void activate ()
  {
    s_aLogger.trace ("TestActor activate: " + toString ());
    super.activate ();
  }

  @Override
  public void deactivate ()
  {
    s_aLogger.trace ("TestActor deactivate: " + toString ());
    super.deactivate ();
  }

  @Override
  protected void runBody ()
  {
    if (false)
      s_aLogger.trace ("TestActor:" + getName () + " runBody: " + toString ());
    DefaultActorTest.sleeper (1);
    final DefaultActorMessage m = new DefaultActorMessage ("init", 8);
    getManager ().send (m, null, this);
  }

  @Override
  protected void loopBody (final IActorMessage pm)
  {
    IActorMessage m = pm;
    if (false)
      s_aLogger.trace ("TestActor:" + getName () + " loopBody " + m + ": " + toString ());
    DefaultActorTest.sleeper (1);
    final String subject = m.getSubject ();
    if ("repeat".equals (subject))
    {
      final int count = ((Integer) m.getData ()).intValue ();
      s_aLogger.trace ("TestActor:" + getName () + " repeat(" + count + ") " + m + ": " + toString ());
      if (count > 0)
      {
        m = new DefaultActorMessage ("repeat", count - 1);
        if (false)
          s_aLogger.trace ("TestActor loopBody send " + m + ": " + toString ());
        final String toName = "actor" + DefaultActorTest.nextInt (DefaultActorTest.TEST_ACTOR_COUNT);
        final IActor to = m_aActorTest.getTestActors ().get (toName);
        if (to != null)
        {
          getManager ().send (m, this, to);
        }
        else
        {
          s_aLogger.warn ("repeat:" + getName () + " to is null: " + toName);
        }
      }
    }
    else
      if ("init".equals (subject))
      {
        int count = ((Integer) m.getData ()).intValue ();
        count = DefaultActorTest.nextInt (count) + 1;
        s_aLogger.trace ("TestActor:" + getName () + " init(" + count + "): " + toString ());
        for (int i = 0; i < count; i++)
        {
          DefaultActorTest.sleeper (1);
          m = new DefaultActorMessage ("repeat", count);
          if (false)
            s_aLogger.trace ("TestActor runBody send " + m + ": " + toString ());
          final String toName = "actor" + DefaultActorTest.nextInt (DefaultActorTest.TEST_ACTOR_COUNT);
          final IActor to = m_aActorTest.getTestActors ().get (toName);
          if (to != null)
          {
            getManager ().send (m, this, to);
          }
          else
          {
            s_aLogger.warn ("init:" + getName () + " to is null: " + toName);
          }
          final DefaultActorMessage dm = new DefaultActorMessage ("repeat", count);
          dm.setDelayUntil (new Date ().getTime () + (DefaultActorTest.nextInt (5) + 1) * 1000);
          getManager ().send (dm, this, this.getClass ().getSimpleName ());
        }
      }
      else
      {
        s_aLogger.warn ("TestActor:" + getName () + " loopBody unknown subject: " + subject);
      }
  }
}
