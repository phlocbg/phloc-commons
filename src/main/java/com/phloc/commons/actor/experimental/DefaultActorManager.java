package com.phloc.commons.actor.experimental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Default ActorManager implementation.
 * 
 * @author BFEIGENB
 */
public class DefaultActorManager extends Utils implements IActorManager
{
  public static final int DEFAULT_ACTOR_THREAD_COUNT = 10;
  /** Configuration key for thread count. */
  public static final String ACTOR_THREAD_COUNT = "threadCount";
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultActorManager.class);
  private static volatile DefaultActorManager s_aInstance;
  private static int s_nGroupCount;

  private final Map <String, AbstractActor> m_aActors = new LinkedHashMap <String, AbstractActor> ();
  private final Map <String, AbstractActor> m_aRunnables = new LinkedHashMap <String, AbstractActor> ();
  private final Map <String, AbstractActor> m_aWaiters = new LinkedHashMap <String, AbstractActor> ();
  private final Map <String, ActorRunnable> m_aThreadRunnables = new HashMap <String, ActorRunnable> ();
  private final Random m_aRand = new Random ();
  private final Map <String, List <IActorMessage>> m_aSentMessages = new HashMap <String, List <IActorMessage>> ();
  private boolean m_bRecordSentMessages = true;
  volatile private long m_nLastSendTime;
  volatile private long m_nLastDispatchTime;
  volatile private int m_nSendCount, m_nLastSendCount;
  volatile private int m_nDispatchCount, m_nLastDispatchCount;
  private final List <Thread> m_aThreads = new LinkedList <Thread> ();
  private int m_nTrendValue = 0, m_nMaxTrendValue = 10;
  private boolean m_bRunning, m_bTerminated;
  private ThreadGroup m_aThreadGroup;
  private boolean m_bInitialized;

  /**
   * Get the default instance. Uses ActorManager.properties for configuration.
   * 
   * @return shared instance
   */
  @Nonnull
  public static DefaultActorManager getDefaultInstance ()
  {
    DefaultActorManager ret = s_aInstance;
    if (ret == null)
      synchronized (DefaultActorManager.class)
      {
        ret = s_aInstance;
        if (ret == null)
        {
          ret = new DefaultActorManager ();
          final Map <String, Object> options = new HashMap <String, Object> ();
          options.put (ACTOR_THREAD_COUNT, Integer.valueOf (DEFAULT_ACTOR_THREAD_COUNT));
          ret.initialize (options);
          s_aInstance = ret;
        }
      }
    return ret;
  }

  /**
   * Detach an actor.
   */
  public void detachActor (final IActor actor)
  {
    if (((AbstractActor) actor).getManager () != this)
      throw new IllegalStateException ("actor not owned by this manager");
    final String name = actor.getName ();
    boolean bDeactivate = false;
    synchronized (m_aActors)
    {
      if (m_aActors.containsKey (name))
      {
        ((AbstractActor) actor).setManager (null);
        m_aActors.remove (name);
        m_aRunnables.remove (name);
        m_aWaiters.remove (name);
        bDeactivate = true;
      }
    }
    if (bDeactivate)
      actor.deactivate ();
  }

  /**
   * Detach all actors.
   */
  public void detachAllActors ()
  {
    final Set <String> xkeys = new HashSet <String> ();
    xkeys.addAll (m_aActors.keySet ());
    final Iterator <String> i = xkeys.iterator ();
    while (i.hasNext ())
    {
      detachActor (m_aActors.get (i.next ()));
    }
    synchronized (m_aActors)
    {
      m_aActors.clear ();
      m_aRunnables.clear ();
      m_aWaiters.clear ();
    }
  }

  /**
   * Create a list of actors in a pseudo-random order.
   */
  public void randomizeActors ()
  {
    synchronized (m_aActors)
    {
      final AbstractActor [] xactors = getActors ();
      final List <AbstractActor> zactors = new ArrayList <AbstractActor> (xactors.length);
      for (final AbstractActor a : xactors)
      {
        zactors.add (m_aRand.nextInt (zactors.size () + 1), a);
      }
      m_aActors.clear ();
      for (final AbstractActor a : zactors)
      {
        m_aActors.put (a.getName (), a);
      }
    }
  }

  /**
   * Count the number of actors of a given type.
   * 
   * @param type
   *        the class to count (also its subclasses)
   */
  public int getActorCount (final Class <? extends IActor> type)
  {
    int res = 0;
    if (type != null)
    {
      synchronized (m_aActors)
      {
        for (final String key : m_aActors.keySet ())
        {
          final IActor a = m_aActors.get (key);
          if (type.isAssignableFrom (a.getClass ()))
          {
            res++;
          }
        }
      }
    }
    else
    {
      synchronized (m_aActors)
      {
        res = m_aActors.size ();
      }
    }
    return res;
  }

  /**
   * Get actors managed by this manager.
   * 
   * @return actors
   */
  public AbstractActor [] getActors ()
  {
    final AbstractActor [] res = new AbstractActor [m_aActors.size ()];
    copyMembers (res);
    return res;
  }

  protected void copyMembers (final AbstractActor [] res)
  {
    int count = 0;
    synchronized (m_aActors)
    {
      for (final String key : m_aActors.keySet ())
      {
        res[count++] = m_aActors.get (key);
      }
    }
  }

  public boolean getRecordSentMessages ()
  {
    return m_bRecordSentMessages;
  }

  public void setRecordSentMessages (final boolean recordSentMessages)
  {
    m_bRecordSentMessages = recordSentMessages;
  }

  /**
   * Get a list of pending messages and then clear it.
   * 
   * @param actor
   *        receiving actor
   * @return may be <code>null</code>
   */
  public IActorMessage [] getAndClearSentMessages (final IActor actor)
  {
    List <IActorMessage> res = null;
    synchronized (m_aSentMessages)
    {
      final List <IActorMessage> l = m_aSentMessages.get (actor.getName ());
      if (!ContainerHelper.isEmpty (l))
      {
        res = new LinkedList <IActorMessage> ();
        res.addAll (l);
        l.clear ();
      }
    }
    return res != null ? res.toArray (new IActorMessage [res.size ()]) : null;
  }

  public long getLastSendTime ()
  {
    return m_nLastSendTime;
  }

  public long getLastDispatchTime ()
  {
    return m_nLastDispatchTime;
  }

  /** Get most recent sends/second count. */
  public int getSendPerSecondCount ()
  {
    return m_nLastSendCount;
  }

  /** Get most recent thread dispatches/second count. */
  public int getDispatchPerSecondCount ()
  {
    synchronized (m_aActors)
    {
      return m_nLastDispatchCount;
    }
  }

  protected void incDispatchCount ()
  {
    synchronized (m_aActors)
    {
      m_nDispatchCount += 1;
      m_nLastDispatchTime = new Date ().getTime ();
      if (false)
        s_aLogger.info ("incDispatchCount: dc=" + m_nDispatchCount);
    }
  }

  protected void clearDispatchCount ()
  {
    synchronized (m_aActors)
    {
      m_nDispatchCount = 0;
      m_nLastDispatchCount = 0;
      if (false)
        s_aLogger.info ("clearDispatchCount: dc=" + m_nDispatchCount + ", ldc=" + m_nLastDispatchCount);
    }
  }

  protected void updateLastDispatchCount ()
  {
    synchronized (m_aActors)
    {
      m_nLastDispatchCount = m_nDispatchCount;
      m_nDispatchCount = 0;
      if (false)
        s_aLogger.info ("updateLastDispatchCount: dc=" + m_nDispatchCount + ", ldc=" + m_nLastDispatchCount);
    }
  }

  /**
   * Send a message.
   * 
   * @param message
   *        message to
   * @param from
   *        source actor
   * @param to
   *        target actor
   * @return number of receiving actors
   */
  public int send (@Nullable final IActorMessage message, @Nullable final IActor from, @Nullable final IActor to)
  {
    int count = 0;
    if (message != null)
    {
      final AbstractActor aTo = (AbstractActor) to;
      if (aTo != null)
      {
        if (!aTo.isShutdown () && !aTo.isSuspended () && aTo.willReceive (message.getSubject ()))
        {
          final DefaultActorMessage xmessage = (DefaultActorMessage) ((DefaultActorMessage) message).assignSender (from);
          if (false)
            s_aLogger.trace (" " + xmessage + " to " + to);
          aTo.addMessage (xmessage);
          xmessage.fireMessageListeners (new ActorMessageEvent (aTo, xmessage, EMessageStatus.SENT));
          m_nSendCount++;
          m_nLastSendTime = new Date ().getTime ();
          if (m_bRecordSentMessages)
          {
            synchronized (m_aSentMessages)
            {
              final String aname = aTo.getName ();
              List <IActorMessage> l = m_aSentMessages.get (aname);
              if (l == null)
              {
                l = new LinkedList <IActorMessage> ();
                m_aSentMessages.put (aname, l);
              }
              // keep from getting too big
              if (l.size () < 100)
              {
                l.add (xmessage);
              }
            }
          }
          count++;
          synchronized (m_aActors)
          {
            m_aActors.notifyAll ();
          }
        }
      }
    }
    return count;
  }

  /**
   * Send a message.
   * 
   * @param message
   *        message to
   * @param from
   *        source actor
   * @param to
   *        target actors
   * @return number of receiving actors
   */
  public int send (final IActorMessage message, final IActor from, final IActor [] to)
  {
    int count = 0;
    for (final IActor a : to)
    {
      count += send (message, from, a);
    }
    return count;
  }

  /**
   * Send a message.
   * 
   * @param message
   *        message to
   * @param from
   *        source actor
   * @param to
   *        target actors
   * @return number of receiving actors
   */
  public int send (final IActorMessage message, final IActor from, final Collection <IActor> to)
  {
    int count = 0;
    for (final IActor a : to)
    {
      count += send (message, from, a);
    }
    return count;
  }

  /**
   * Send a message.
   * 
   * @param message
   *        message to
   * @param from
   *        source actor
   * @param category
   *        target actor category
   * @return number of receiving actors
   */
  public int send (final IActorMessage message, final IActor from, final String category)
  {
    int count = 0;
    final Map <String, ? extends IActor> xactors = cloneActors ();
    final List <IActor> catMembers = new LinkedList <IActor> ();
    for (final IActor to : xactors.values ())
      if (category.equals (to.getCategory ()) && (to.getMessageCount () < to.getMaxMessageCount ()))
        catMembers.add (to);

    // find an actor with lowest message count
    int min = Integer.MAX_VALUE;
    IActor amin = null;
    for (final IActor a : catMembers)
    {
      final int mcount = a.getMessageCount ();
      if (mcount < min)
      {
        min = mcount;
        amin = a;
      }
    }
    if (amin != null)
    {
      count += send (message, from, amin);
      // } else {
      // throw new
      // IllegalStateException("no capable actors for category: " +
      // category);
    }
    return count;
  }

  /**
   * Send a message to all actors.
   * 
   * @param message
   *        message to
   * @param from
   *        source actor
   * @return number of receiving actors
   */
  public int broadcast (final IActorMessage message, final IActor from)
  {
    int count = 0;
    final Map <String, ? extends IActor> xactors = cloneActors ();
    for (final IActor to : xactors.values ())
      count += send (message, from, to);
    return count;
  }

  /**
   * Get the current categories.
   * 
   * @return categories
   */
  public Set <String> getCategories ()
  {
    final Map <String, ? extends IActor> xactors = cloneActors ();
    final Set <String> res = new TreeSet <String> ();
    for (final IActor a : xactors.values ())
      res.add (a.getCategory ());
    return res;
  }

  /**
   * Get the number of actors in a category.
   * 
   * @param name
   * @return Always &ge; 0
   */
  public int getCategorySize (final String name)
  {
    final Map <String, ? extends IActor> xactors = cloneActors ();
    int res = 0;
    for (final IActor a : xactors.values ())
      if (a.getCategory ().equals (name))
        res++;
    return res;
  }

  protected Map <String, ? extends IActor> cloneActors ()
  {
    synchronized (m_aActors)
    {
      return ContainerHelper.newMap (m_aActors);
    }
  }

  /**
   * Suspend an actor until it has a read message.
   * 
   * @param aActor
   *        receiving actor
   */
  public void awaitMessage (@Nonnull final AbstractActor aActor)
  {
    synchronized (m_aActors)
    {
      m_aWaiters.put (aActor.getName (), aActor);
      // actors.notifyAll();
      if (false)
        s_aLogger.trace ("awaitMessage waiters=" + m_aWaiters.size () + ": " + aActor);
    }
  }

  /**
   * Get the Runnable by name.
   * 
   * @param sName
   *        thread name
   * @return runnable
   */
  @Nullable
  public ActorRunnable getRunnable (final String sName)
  {
    return m_aThreadRunnables.get (sName);
  }

  /**
   * Get the number of busy runnables (equivalent to threads).
   * 
   * @return Always &ge; 0
   */
  @Nonnegative
  public int getActiveRunnableCount ()
  {
    int res = 0;
    synchronized (m_aActors)
    {
      for (final String key : m_aThreadRunnables.keySet ())
        if (m_aThreadRunnables.get (key).m_bHasThread)
          res++;
    }
    return res;
  }

  /**
   * Add a dynamic thread.
   * 
   * @param name
   * @return Never <code>null</code>
   */
  @Nonnull
  public Thread addThread (@Nonnull final String name)
  {
    Thread t;
    synchronized (m_aActors)
    {
      if (m_aThreadRunnables.containsKey (name))
        throw new IllegalStateException ("already exists: " + name);
      final ActorRunnable r = new ActorRunnable ();
      m_aThreadRunnables.put (name, r);
      t = new Thread (m_aThreadGroup, r, name);
      m_aThreads.add (t);
      if (false)
        s_aLogger.trace ("addThread: " + name);
    }
    t.setDaemon (true);
    t.setPriority (getThreadPriority ());
    return t;
  }

  /**
   * Remove a dynamic thread.
   * 
   * @param sName
   */
  public void removeThread (@Nonnull final String sName)
  {
    synchronized (m_aActors)
    {
      if (!m_aThreadRunnables.containsKey (sName))
        throw new IllegalStateException ("not running: " + sName);
      if (false)
        s_aLogger.trace ("removeThread: " + sName);
      m_aThreadRunnables.remove (sName);
      final Iterator <Thread> i = m_aThreads.iterator ();
      while (i.hasNext ())
      {
        final Thread xt = i.next ();
        if (xt.getName ().equals (sName))
        {
          i.remove ();
          xt.interrupt ();
          break;
        }
      }
    }
  }

  public ThreadGroup getThreadGroup ()
  {
    return m_aThreadGroup;
  }

  protected void createThread (final int i)
  {
    addThread ("actor" + i);
  }

  /**
   * Initialize this manager. Call only once.
   */
  public void initialize ()
  {
    initialize (null);
  }

  /**
   * Initialize this manager. Call only once.
   * 
   * @param options
   *        map of options
   */
  public void initialize (final Map <String, Object> options)
  {
    if (!m_bInitialized)
    {
      m_bInitialized = true;
      final int nCount = getThreadCount (options);
      final ThreadGroup aThreadGroup = new ThreadGroup ("ActorManager" + s_nGroupCount++);
      m_aThreadGroup = aThreadGroup;
      for (int i = 0; i < nCount; i++)
        createThread (i);
      m_bRunning = true;
      for (final Thread aThread : m_aThreads)
      {
        if (false)
          s_aLogger.trace ("initialize starting " + aThread);
        aThread.start ();
      }

      final Thread Counter = new Thread (new Runnable ()
      {
        public void run ()
        {
          while (m_bRunning)
          {
            try
            {
              m_nTrendValue = m_nSendCount - m_nDispatchCount;
              if (false)
                s_aLogger.trace ("Counter thread: sc=" +
                                 m_nSendCount +
                                 ", dc=" +
                                 m_nDispatchCount +
                                 ", t=" +
                                 m_nTrendValue);
              m_nLastSendCount = m_nSendCount;
              m_nSendCount = 0;
              updateLastDispatchCount ();
              Thread.sleep (1000);
            }
            catch (final InterruptedException e)
            {
              break;
            }
          }
          m_nSendCount = m_nLastSendCount = 0;
          clearDispatchCount ();
        }
      });
      Counter.setDaemon (true);
      m_nLastDispatchTime = m_nLastSendTime = new Date ().getTime ();
      Counter.start ();
    }
  }

  /**
   * Get the thread priority to use. Default is 1 less than current.
   * 
   * @return priority value
   */
  public int getThreadPriority ()
  {
    return Math.max (Thread.MIN_PRIORITY, Thread.currentThread ().getPriority () - 1);
  }

  protected int getThreadCount (@Nullable final Map <String, Object> options)
  {
    final Object xcount = options != null ? options.get (ACTOR_THREAD_COUNT) : null;
    return StringHelper.parseInt (xcount, DEFAULT_ACTOR_THREAD_COUNT);
  }

  /** public intended only for "friend" access. */
  public final class ActorRunnable implements Runnable
  {
    public boolean m_bHasThread;
    public AbstractActor m_aActor;

    public void run ()
    {
      if (false)
        s_aLogger.trace ("processNextActor starting");
      int delay = 1;
      while (m_bRunning)
      {
        try
        {
          if (!processNextActor ())
          {
            if (false)
              s_aLogger.trace ("procesNextActor waiting on actor");
            // sleep(delay * 1000);
            synchronized (m_aActors)
            {
              // TOOD: adjust this delay; possible parameter
              // we want to minizmize overhead (make bigger);
              // but it has a big impact on message processing
              // rate (makesmaller)
              // actors.wait(delay * 1000);
              m_aActors.wait (100);
            }
            delay = Math.max (5, delay + 1);
          }
          else
          {
            delay = 1;
          }
        }
        catch (final InterruptedException e)
        {}
        catch (final Exception e)
        {
          s_aLogger.error ("procesNextActor exception", e);
        }
      }
      if (false)
        s_aLogger.trace ("processNextActor ended");
    }

    protected boolean processNextActor ()
    {
      boolean run = false, res = false;
      m_aActor = null;
      synchronized (m_aActors)
      {
        for (final String key : m_aRunnables.keySet ())
        {
          m_aActor = m_aRunnables.remove (key);
          break;
        }
      }
      if (m_aActor != null)
      {
        // first run never started
        run = true;
        m_aActor.setThreadAssigned (true);
        m_bHasThread = true;
        try
        {
          m_aActor.run ();
        }
        finally
        {
          m_aActor.setThreadAssigned (false);
          m_bHasThread = false;
        }
      }
      else
      {
        synchronized (m_aActors)
        {
          for (final String key : m_aWaiters.keySet ())
          {
            m_aActor = m_aWaiters.remove (key);
            break;
          }
        }
        if (m_aActor != null)
        {
          // then waiting for responses
          m_aActor.setThreadAssigned (true);
          m_bHasThread = true;
          try
          {
            res = m_aActor.receive ();
            if (res)
              incDispatchCount ();
          }
          finally
          {
            m_aActor.setThreadAssigned (false);
            m_bHasThread = false;
          }
        }
      }
      // if (!(!run && wait && !res) && a != null) {
      if (false)
        s_aLogger.trace ("procesNextActor " + run + "/" + res + ": " + m_aActor);
      // }
      return run || res;
    }
  }

  /**
   * Get the actor threads.
   * 
   * @return Never <code>null</code>
   */
  public Thread [] getThreads ()
  {
    return m_aThreads.toArray (new Thread [m_aThreads.size ()]);
  }

  /**
   * Terminate processing and wait for all threads to stop.
   */
  public void terminateAndWait ()
  {
    s_aLogger.trace ("terminateAndWait waiting on termination of " + m_aThreads.size () + " threads");
    terminate ();
    waitForThreads ();
  }

  /**
   * Wait for all threads to stop. Must have issued terminate.
   */
  public void waitForThreads ()
  {
    if (!m_bTerminated)
    {
      throw new IllegalStateException ("not terminated");
    }
    for (final Thread t : m_aThreads)
    {
      try
      {
        if (false)
          s_aLogger.info ("terminateAndWait waiting for " + t + "...");
        t.join ();
      }
      catch (final InterruptedException e)
      {
        if (false)
          s_aLogger.info ("terminateAndWait interrupt");
      }
    }
  }

  /**
   * Terminate processing.
   */
  public void terminate ()
  {
    m_bTerminated = true;
    m_bRunning = false;
    for (final Thread t : m_aThreads)
    {
      t.interrupt ();
    }
    synchronized (m_aActors)
    {
      for (final String key : m_aActors.keySet ())
      {
        m_aActors.get (key).deactivate ();
      }
    }
    m_aSentMessages.clear ();
    m_nSendCount = m_nLastSendCount = 0;
    clearDispatchCount ();
  }

  /**
   * Create an actor and associate it with this manager.
   * 
   * @param clazz
   *        the actor class
   * @param name
   *        the actor name; must be unique
   */
  public IActor createActor (final Class <? extends IActor> clazz, final String name)
  {
    return createActor (clazz, name, null);
  }

  /**
   * Create an actor and associate it with this manager then start it
   * 
   * @param clazz
   *        the actor class
   * @param name
   *        the actor name; must be unique
   */
  public IActor createAndStartActor (final Class <? extends IActor> clazz, final String name)
  {
    return createAndStartActor (clazz, name, null);
  }

  /**
   * Create an actor and associate it with this manager then start it.
   * 
   * @param clazz
   *        the actor class
   * @param name
   *        the actor name; must be unique
   * @param options
   *        actor options
   */
  public IActor createAndStartActor (final Class <? extends IActor> clazz,
                                     final String name,
                                     final Map <String, Object> options)
  {
    final IActor res = createActor (clazz, name, options);
    startActor (res);
    return res;
  }

  /**
   * Create an actor and associate it with this manager.
   * 
   * @param clazz
   *        the actor class
   * @param name
   *        the actor name; must be unique
   * @param options
   *        actor options
   */
  public IActor createActor (final Class <? extends IActor> clazz, final String name, final Map <String, Object> options)
  {
    AbstractActor a = null;
    synchronized (m_aActors)
    {
      if (!m_aActors.containsKey (name))
      {
        try
        {
          a = (AbstractActor) clazz.newInstance ();
          a.setName (name);
          a.setManager (this);
        }
        catch (final Exception e)
        {
          throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException ("mapped exception: " + e,
                                                                                             e);
        }
      }
      else
      {
        throw new IllegalArgumentException ("name already in use: " + name);
      }
    }
    return a;
  }

  /**
   * Start an actor. Must have been created by this manager.
   * 
   * @param actor
   *        the actor
   */
  public void startActor (@Nonnull final IActor actor)
  {
    if (((AbstractActor) actor).getManager () != this)
      throw new IllegalStateException ("actor not owned by this manager");

    final String name = actor.getName ();
    synchronized (m_aActors)
    {
      if (m_aActors.containsKey (name))
        throw new IllegalStateException ("already started");
      ((AbstractActor) actor).resetShutdown ();
      m_aActors.put (name, (AbstractActor) actor);
      m_aRunnables.put (name, (AbstractActor) actor);
    }
    actor.activate ();
  }

  public int getTrendValue ()
  {
    return m_nTrendValue;
  }

  public void setTrendValue (final int trendValue)
  {
    m_nTrendValue = trendValue;
  }

  public int getMaxTrendValue ()
  {
    return m_nMaxTrendValue;
  }

  public void setMaxTrendValue (final int maxTrendValue)
  {
    m_nMaxTrendValue = maxTrendValue;
  }
}
