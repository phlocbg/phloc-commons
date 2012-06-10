package com.phloc.commons.actor.experimental;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.string.StringHelper;

/**
 * A set of runtime services for testing actors and a test case driver.
 * 
 * @author BFEIGENB
 */
public class DefaultActorTest extends Utils
{
  public static final int MAX_IDLE_SECONDS = 10;

  // public static final int STEP_COUNT = 3 * 60;
  public static final int TEST_VALUE_COUNT = 1000; // TODO: make bigger
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultActorTest.class);

  public DefaultActorTest ()
  {
    super ();
  }

  private Map <String, IActor> m_aTestActors = new ConcurrentHashMap <String, IActor> ();

  static Random rand = new Random ();

  public static int nextInt (final int limit)
  {
    return rand.nextInt (limit);
  }

  protected DefaultActorManager getManager ()
  {
    final DefaultActorManager am = m_aActorManager != null ? m_aActorManager : new DefaultActorManager ();
    return am;
  }

  protected int m_nStepCount = 120;

  public void setStepCount (final int stepCount)
  {
    this.m_nStepCount = stepCount;
  }

  public int getStepCount ()
  {
    return m_nStepCount;
  }

  protected int m_nThreadCount = 10;

  public int getThreadCount ()
  {
    return m_nThreadCount;
  }

  public void setThreadCount (final int threadCount)
  {
    this.m_nThreadCount = threadCount;
  }

  public void setTestActors (final Map <String, IActor> testActors)
  {
    this.m_aTestActors = testActors;
  }

  public Map <String, IActor> getTestActors ()
  {
    return m_aTestActors;
  }

  public static final int COMMON_ACTOR_COUNT = 10;
  public static final int TEST_ACTOR_COUNT = 25;
  public static final int PRODUCER_ACTOR_COUNT = 25;

  public static void sleeper (final int seconds)
  {
    final int millis = seconds * 1000 + -50 + nextInt (100); // a little
    // variation
    // s_aLogger.trace("sleep: %dms", millis);
    sleep (millis);
  }

  public static void dumpMessages (final List <DefaultActorMessage> messages)
  {
    synchronized (messages)
    {
      if (messages.size () > 0)
      {
        for (final DefaultActorMessage m : messages)
        {
          s_aLogger.info ("%s", m);
        }
      }
    }
  }

  protected List <ChangeListener> listeners = new LinkedList <ChangeListener> ();

  public void addChangeListener (final ChangeListener l)
  {
    if (!listeners.contains (l))
    {
      listeners.add (l);
    }
  }

  public void removeChangeListener (final ChangeListener l)
  {
    listeners.remove (l);
  }

  protected void fireChangeListeners (final ChangeEvent e)
  {
    for (final ChangeListener l : listeners)
    {
      l.stateChanged (e);
    }
  }

  protected static String [] types = new String [] { "widget", "framit", "frizzle", "gothca", "splat" };

  public static String [] getItemTypes ()
  {
    return types;
  }

  public static void main (final String [] args)
  {
    final DefaultActorTest at = new DefaultActorTest ();
    at.run (args);
    s_aLogger.trace ("Done");
  }

  protected String title;

  public String getTitle ()
  {
    return title;
  }

  public class ActorX extends AbstractActor
  {

    public ActorX ()
    {
      super ();
      // TODO Auto-generated constructor stub
    }

    @Override
    protected void loopBody (final IActorMessage pm)
    {
      IActorMessage m = pm;
      // s_aLogger.trace("ActorX:%s loopBody %s: %s", getName(), m, this);
      sleeper (1);
      final String subject = m.getSubject ();
      if ("repeat".equals (subject))
      {
        final int count = ((Integer) m.getData ()).intValue ();
        s_aLogger.trace ("ActorX:" + getName () + " repeat(" + count + ") " + m + ": " + toString ());
        if (count > 0)
        {
          m = new DefaultActorMessage ("repeat", count - 1);
          // s_aLogger.trace("TestActor loopBody send %s: %s", m, this);
          final String toName = "actor" + nextInt (TEST_ACTOR_COUNT);
          final IActor to = getTestActors ().get (toName);
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
          count = nextInt (count) + 1;
          s_aLogger.trace ("ActorX:" + getName () + " init(" + count + "): " + toString ());
          for (int i = 0; i < count; i++)
          {
            sleeper (1);
            m = new DefaultActorMessage ("repeat", count);
            // s_aLogger.trace("TestActor runBody send %s: %s", m, this);
            final String toName = "actor" + nextInt (TEST_ACTOR_COUNT);
            final IActor to = getTestActors ().get (toName);
            if (to != null)
            {
              getManager ().send (m, this, to);
            }
            else
            {
              s_aLogger.warn ("init:" + getName () + " to is null: " + toName);
            }
            final DefaultActorMessage dm = new DefaultActorMessage ("repeat", count);
            dm.setDelayUntil (new Date ().getTime () + (nextInt (5) + 1) * 1000);
            getManager ().send (dm, this, this.getClass ().getSimpleName ());
          }
        }
        else
        {
          s_aLogger.warn ("ActorX:" + getName () + " loopBody unknown subject: " + subject);
        }
    }

  }

  volatile protected boolean done;

  public void terminateRun ()
  {
    done = true;
  }

  public static String [] getTestNames ()
  {
    return new String [] { "Countdown", "Producer Consumer", /* "Quicksort", */"MapReduce", "Virus Scan", "All" };
  }

  DefaultActorManager m_aActorManager;

  public DefaultActorManager getActorManager ()
  {
    return m_aActorManager;
  }

  public void setActorManager (final DefaultActorManager actorManager)
  {
    this.m_aActorManager = actorManager;
  }

  public void run (final String [] args)
  {
    done = false;

    int sc = m_nStepCount;
    int tc = m_nThreadCount;
    boolean doTest = false, doProduceConsume = false, doQuicksort = false, doMapReduce = false, doVirusScan = false;
    title = "";
    for (final String arg2 : args)
    {
      String arg = arg2.toLowerCase ();
      if (arg.startsWith ("-"))
      {
        arg = arg.substring (1);
        if (arg.toLowerCase ().startsWith ("stepcount:"))
        {
          sc = Integer.parseInt (arg.substring ("stepcount:".length ()));
        }
        else
          if (arg.startsWith ("sc:"))
          {
            sc = Integer.parseInt (arg.substring ("sc:".length ()));
          }
          else
            if (arg.toLowerCase ().startsWith ("threadcount:"))
            {
              tc = Integer.parseInt (arg.substring ("threadcount:".length ()));
            }
            else
              if (arg.startsWith ("tc:"))
              {
                tc = Integer.parseInt (arg.substring ("tc:".length ()));
              }
              else
              {
                System.out.printf ("Unknown switch: %s%n", arg);
              }
      }
      else
      {
        if (arg.equalsIgnoreCase ("test") || arg.equalsIgnoreCase ("countdown") || arg.equalsIgnoreCase ("cd"))
        {
          doTest = true;
        }
        else
          if (arg.equalsIgnoreCase ("producerconsumer") || arg.equalsIgnoreCase ("pc"))
          {
            doProduceConsume = true;
          }
          else
            if (arg.equalsIgnoreCase ("quicksort") || arg.equalsIgnoreCase ("qs"))
            {
              doQuicksort = true;
            }
            else
              if (arg.equalsIgnoreCase ("mapreduce") || arg.equalsIgnoreCase ("mr"))
              {
                doMapReduce = true;
              }
              else
                if (arg.equalsIgnoreCase ("virusscan") || arg.equalsIgnoreCase ("vs"))
                {
                  doVirusScan = true;
                }
                else
                  if (arg.equalsIgnoreCase ("all"))
                  {
                    doProduceConsume = true;
                    doTest = true;
                    doMapReduce = true;
                    doQuicksort = true;
                    doVirusScan = true;
                  }
                  else
                  {
                    System.out.printf ("Unknown parameter: %s%n", arg);
                  }
      }
    }
    if (!doTest && !doProduceConsume && !doQuicksort && !doMapReduce && !doVirusScan)
    {
      doTest = true;
    }
    if (doTest)
    {
      if (title.length () > 0)
      {
        title += " ";
      }
      title += "(Countdown Test)";
    }
    if (doProduceConsume)
    {
      if (title.length () > 0)
      {
        title += " ";
      }
      title += "(Producer+Consumer)";
    }
    if (doQuicksort)
    {
      if (title.length () > 0)
      {
        title += " ";
      }
      title += "(Quicksort)";
    }
    if (doMapReduce)
    {
      if (title.length () > 0)
      {
        title += " ";
      }
      title += "(MapReduce)";
    }
    if (doVirusScan)
    {
      if (title.length () > 0)
      {
        title += " ";
      }
      title += "(VirusScan)";
    }

    final DefaultActorManager am = getManager ();
    try
    {
      final Map <String, Object> options = new HashMap <String, Object> ();
      options.put (DefaultActorManager.ACTOR_THREAD_COUNT, Integer.valueOf (tc));
      am.initialize (options);
      if (doTest)
      {
        for (int i = 0; i < COMMON_ACTOR_COUNT; i++)
        {
          final IActor a = am.createActor (TestActor.class, "common" + StringHelper.getLeadingZero (i, 2));
          if (a instanceof AbstractTestableActor)
          {
            final AbstractTestableActor ta = (AbstractTestableActor) a;
            ta.setActorTest (this);
          }
          a.setCategory (TestActor.class.getSimpleName ());
          getTestActors ().put (a.getName (), a);
          // s_aLogger.trace("created: %s", a);
        }
        for (int i = 0; i < TEST_ACTOR_COUNT; i++)
        {
          final IActor a = am.createActor (TestActor.class, "actor" + StringHelper.getLeadingZero (i, 2));
          if (a instanceof AbstractTestableActor)
          {
            final AbstractTestableActor ta = (AbstractTestableActor) a;
            ta.setActorTest (this);
          }
          getTestActors ().put (a.getName (), a);
          // s_aLogger.trace("created: %s", a);
        }
      }

      if (doProduceConsume)
      {
        for (int i = 0; i < PRODUCER_ACTOR_COUNT; i++)
        {
          final IActor a = am.createActor (ProducerActor.class, "producer" + StringHelper.getLeadingZero (i, 2));
          getTestActors ().put (a.getName (), a);
          // s_aLogger.trace("created: %s", a);
        }
      }

      if (doVirusScan)
      {
        VirusScanActor.createVirusScanActor (am);

        final DefaultActorMessage dm = new DefaultActorMessage ("init", "/downloads");
        am.send (dm, null, VirusScanActor.getCategoryName ());
      }

      if (doMapReduce)
      {
        final BigInteger [] values = new BigInteger [TEST_VALUE_COUNT];
        for (int i = 0; i < values.length; i++)
        {
          values[i] = new BigInteger (Long.toString (rand.nextInt (values.length)));
        }
        final BigInteger [] targets = new BigInteger [Math.max (1, values.length / 10)];

        BigInteger res = new BigInteger ("0");
        for (final BigInteger value : values)
        {
          res = res.add (value.multiply (value));
        }

        final String id = MapReduceActor.nextId ();
        s_aLogger.trace ("**** MapReduce " + id + " (expected=" + res + ") start: " + Arrays.toString (values));

        // start at least 5 actors
        MapReduceActor.createMapReduceActor (am, 10);
        MapReduceActor.createMapReduceActor (am, 10);
        MapReduceActor.createMapReduceActor (am, 10);
        MapReduceActor.createMapReduceActor (am, 10);
        MapReduceActor.createMapReduceActor (am, 10);
        // getTestActors().put(mra.getName(), mra);

        final DefaultActorMessage dm = new DefaultActorMessage ("init", new Object [] { values,
                                                                                       targets,
                                                                                       SumOfSquaresReducer.class });
        am.send (dm, null, MapReduceActor.getCategoryName ());
      }

      for (final String key : getTestActors ().keySet ())
      {
        am.startActor (getTestActors ().get (key));
      }

      for (int i = sc; i > 0; i--)
      {
        if (done)
        {
          break;
        }
        // see if idle a while
        final long now = new Date ().getTime ();
        if (am.getActiveRunnableCount () == 0)
        {
          if (now - am.getLastDispatchTime () > MAX_IDLE_SECONDS * 1000 &&
              now - am.getLastSendTime () > MAX_IDLE_SECONDS * 1000)
          {
            break;
          }
        }
        setStepCount (i);
        fireChangeListeners (new ChangeEvent (this));
        if (i < 10 || i % 10 == 0)
        {
          s_aLogger.trace ("main waiting: " + i + "...");
        }
        sleeper (1);
      }
      setStepCount (0);
      fireChangeListeners (new ChangeEvent (this));

      // s_aLogger.trace("main terminating");
      am.terminateAndWait ();
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
    }
  }
}
