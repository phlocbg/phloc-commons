package com.phloc.commons.actor.experimental;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.string.StringHelper;

/**
 * An actor that asks Consumer actors to produce items.
 * 
 * @author BFEIGENB
 */
public class ProducerActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProducerActor.class);

  @Override
  public void activate ()
  {
    s_aLogger.trace ("ProducerActor activate: " + toString ());
    super.activate ();
    // s_aLogger.trace("expected: %s", expected);
  }

  @Override
  public void deactivate ()
  {
    s_aLogger.trace ("ProducerActor deactivate: " + toString ());
    if (m_aMessages.size () > 0)
    {
      s_aLogger.trace ("expected: %s", expected);
      DefaultActorTest.dumpMessages (m_aMessages);
    }
    super.deactivate ();
  }

  protected Map <String, Integer> expected = new ConcurrentHashMap <String, Integer> ();

  @Override
  protected void loopBody (final IActorMessage pm)
  {
    IActorMessage m = pm;
    final String subject = m.getSubject ();
    if ("produceN".equals (subject))
    {
      final Object [] input = (Object []) m.getData ();
      final int count = ((Integer) input[0]).intValue ();
      if (count > 0)
      {
        DefaultActorTest.sleeper (1); // this takes some time
        final String type = (String) input[1];
        // s_aLogger.trace("ProducerActor:%s produceN %d x %s; pending=%d",
        // getName(), count, type, messages.size());
        s_aLogger.trace ("ProducerActor:" + getName () + " produceN " + count + " x " + type);
        // request the consumers to consume work (i.e., produce)
        final Integer mcount = expected.get (type);
        expected.put (type, Integer.valueOf (mcount == null ? count : mcount.intValue () + count));

        final DefaultActorMessage dm = new DefaultActorMessage ("produce1", new Object [] { Integer.valueOf (count),
                                                                                           type });
        getManager ().send (dm, this, this);
      }
    }
    else
      if ("produce1".equals (subject))
      {
        final Object [] input = (Object []) m.getData ();
        final int count = ((Integer) input[0]).intValue ();
        if (count > 0)
        {
          Utils.sleep (100); // take a little time
          final String type = (String) input[1];
          // s_aLogger.trace("ProducerActor:%s produce1 %d x %s; pending=%d",
          // getName(), count, type, messages.size());
          s_aLogger.trace ("ProducerActor:" + getName () + " produce1 " + count + " x " + type);
          m = new DefaultActorMessage ("construct", type);
          getManager ().send (m, this, getConsumerCategory ());

          m = new DefaultActorMessage ("produce1", new Object [] { Integer.valueOf (count - 1), type });
          getManager ().send (m, this, this);
        }
      }
      else
        if ("constructionComplete".equals (subject))
        {
          final String type = (String) m.getData ();
          // s_aLogger.trace("ProducerActor:%s constructionComplete %s; pending=%d",
          // getName(), type, messages.size());
          s_aLogger.trace ("ProducerActor:" +
                           getName () +
                           " constructionComplete " +
                           type +
                           " from " +
                           m.getSource ().getName ());
          final Integer mcount = expected.get (type);
          if (mcount != null)
            expected.put (type, Integer.valueOf (mcount.intValue () - 1));
        }
        else
          if ("init".equals (subject))
          {
            s_aLogger.trace ("ProducerActor:%s init", getName ());
            // create some consumers
            // 1 to 3 x consumers per producer
            for (int i = 0; i < DefaultActorTest.nextInt (3) + 1; i++)
            {
              final IActor a = getManager ().createAndStartActor (ConsumerActor.class,
                                                                  getName () +
                                                                      "_consumer" +
                                                                      StringHelper.getLeadingZero (i, 2));
              a.setCategory (getConsumerCategory ());
              if (m_aActorTest != null)
              {
                m_aActorTest.getTestActors ().put (a.getName (), a);
                // s_aLogger.trace("created: %s", a);
              }
            }
            // request myself create some work items
            for (int i = 0; i < DefaultActorTest.nextInt (10) + 1; i++)
            {
              m = new DefaultActorMessage ("produceN",
                                           new Object [] { Integer.valueOf (DefaultActorTest.nextInt (10) + 1),
                                                          DefaultActorTest.getItemTypes ()[DefaultActorTest.nextInt (DefaultActorTest.getItemTypes ().length)] });
              getManager ().send (m, this, this);
            }
          }
          else
          {
            s_aLogger.warn ("ProducerActor:" + getName () + " loopBody unknown subject: " + subject);
          }
  }

  protected String getConsumerCategory ()
  {
    return getName () + "_consumer";
  }
}
