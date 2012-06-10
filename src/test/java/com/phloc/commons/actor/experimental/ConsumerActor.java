package com.phloc.commons.actor.experimental;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.actor.experimental.DefaultActorMessage;
import com.phloc.commons.actor.experimental.IActorMessage;

/**
 * An Actor that constructs items.
 *
 * @author BFEIGENB
 */
public class ConsumerActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ConsumerActor.class);

  @Override
  public void activate ()
  {
    s_aLogger.trace ("ConsumerActor activate: %s", this);
    super.activate ();
  }

  @Override
  public void deactivate ()
  {
    s_aLogger.trace ("ConsumerActor deactivate: %s", this);
    DefaultActorTest.dumpMessages (m_aMessages);
    super.deactivate ();
  }

  Map <String, Integer> expected = new ConcurrentHashMap <String, Integer> ();

  @Override
  protected void loopBody (final IActorMessage m)
  {
    // s_aLogger.trace("ConsumerActor:%s loopBody %s: %s", getName(), m,
    // this);
    final String subject = m.getSubject ();
    if ("construct".equals (subject))
    {
      final String type = (String) m.getData ();
      // s_aLogger.trace("ConsumerActor:%s construct %s; pending=%d",
      // getName(), type, messages.size());
      s_aLogger.trace ("ConsumerActor:%s constructing %s", getName (), type);
      delay (type); // takes ~ 1 to N seconds

      final DefaultActorMessage xm = new DefaultActorMessage ("constructionComplete", type);
      // s_aLogger.info("ConsumerActor:%s reply to %s", getName(),
      // m.getSource());
      getManager ().send (xm, this, m.getSource ());
    }
    else
      if ("init".equals (subject))
      {
        // nothing to do
      }
      else
      {
        s_aLogger.warn ("ConsumerActor:%s loopBody unknown subject: %s", getName (), subject);
      }
  }

  protected void delay (final String type)
  {
    int delay = 1;
    for (int i = 0; i < DefaultActorTest.getItemTypes ().length; i++)
    {
      if (DefaultActorTest.getItemTypes ()[i].equals (type))
      {
        break;
      }
      delay++;
    }
    DefaultActorTest.sleeper (DefaultActorTest.nextInt (delay) + 1);
    // sleep(100);
  }
}
