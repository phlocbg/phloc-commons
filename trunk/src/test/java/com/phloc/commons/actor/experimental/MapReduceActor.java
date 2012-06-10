package com.phloc.commons.actor.experimental;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;

/**
 * An actor that can do calculations via a suppled MapReducer.
 * 
 * @author BFEIGENB
 */
public class MapReduceActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MapReduceActor.class);
  protected int m_nPartitionSize = 10;

  public int getPartitionSize ()
  {
    return m_nPartitionSize;
  }

  public void setPartitionSize (final int partitionSize)
  {
    m_nPartitionSize = partitionSize;
  }

  public static final int MAX_ACTOR_COUNT = 25;

  // carries state as a message parameter.
  protected static class MapReduceParameters implements Comparable <MapReduceParameters>
  {
    protected static int s_sParamIDCount;
    protected Integer m_aID = Integer.valueOf (s_sParamIDCount++);
    public Object [] m_aValues;
    public int m_aStart;
    public int m_aEnd;
    public Object [] m_aTarget;
    public int m_aPosn;
    // depth && targetDepth currently unused
    // public int depth;
    // public int targetDepth;
    public MapReduceer m_aMR;
    public IActor m_aSender;
    protected String m_sSet;
    public static Map <String, Map <Integer, MapReduceParameters>> m_aPendingResponses = new ConcurrentHashMap <String, Map <Integer, MapReduceParameters>> ();

    public MapReduceParameters (final String set,
                                final Object [] values,
                                final Object [] target,
                                final MapReduceer mr,
                                final IActor sender)
    {
      this (set, values, 0, values.length - 1, target, 0, 0, 5, mr, sender);
    }

    public MapReduceParameters (final String set,
                                final Object [] values,
                                final Object [] target,
                                final int depth,
                                final int targetDepth,
                                final MapReduceer mr,
                                final IActor sender)
    {
      this (set, values, 0, values.length - 1, target, 0, depth, targetDepth, mr, sender);
    }

    @SuppressWarnings ("unused")
    public MapReduceParameters (final String set,
                                final Object [] values,
                                final int start,
                                final int end,
                                final Object [] target,
                                final int posn,
                                final int depth,
                                final int targetDepth,
                                final MapReduceer mr,
                                final IActor sender)
    {
      this.m_sSet = set;
      this.m_aValues = values;
      this.m_aStart = start;
      this.m_aEnd = end;
      this.m_aTarget = target;
      this.m_aPosn = posn;
      // this.depth = depth;
      // this.targetDepth = targetDepth;
      this.m_aMR = mr;
      this.m_aSender = sender;
      // rememberReference();
    }

    public MapReduceParameters (final MapReduceParameters p)
    {
      this.m_sSet = p.m_sSet;
      this.m_aValues = p.m_aValues;
      this.m_aStart = p.m_aStart;
      this.m_aEnd = p.m_aEnd;
      this.m_aTarget = p.m_aTarget;
      this.m_aPosn = p.m_aPosn;
      // this.depth = p.depth;
      // this.targetDepth = p.targetDepth;
      this.m_aMR = p.m_aMR;
      this.m_aSender = p.m_aSender;
      rememberReference ();
    }

    private void rememberReference ()
    {
      Map <Integer, MapReduceParameters> m = m_aPendingResponses.get (m_sSet);
      if (m == null)
      {
        m = new ConcurrentHashMap <Integer, MapReduceParameters> ();
        m_aPendingResponses.put (m_sSet, m);
      }
      m.put (m_aID, this);
    }

    public void complete ()
    {
      final Map <Integer, MapReduceParameters> m = m_aPendingResponses.get (m_sSet);
      if (m != null)
      {
        m.remove (m_aID);
        if (m.isEmpty ())
        {
          m_aPendingResponses.remove (m_sSet);
        }
      }
    }

    public boolean isSetComplete ()
    {
      return ContainerHelper.isEmpty (m_aPendingResponses.get (m_sSet));
    }

    @Override
    public int compareTo (final MapReduceParameters o)
    {
      return m_aID.intValue () - o.m_aID.intValue ();
    }

    @Override
    public String toString ()
    {
      return getClass ().getName () +
             "[id=" +
             m_aID +
             ", values.length=" +
             (m_aValues != null ? m_aValues.length : 0) +
             ", start=" +
             m_aStart +
             ", end=" +
             m_aEnd +
             ", target.length=" +
             (m_aTarget != null ? m_aTarget.length : 0) +
             ", posn=" +
             m_aPosn +
             ", sender=" +
             m_aSender +
             ", set=" +
             m_sSet +
             "]";
    }
  }

  @Override
  protected void loopBody (final IActorMessage m)
  {
    final IActorManager manager = getManager ();
    final String subject = m.getSubject ();
    if ("mapReduce".equals (subject))
    {
      try
      {
        final MapReduceParameters p = (MapReduceParameters) m.getData ();
        int index = 0;
        final int count = (p.m_aEnd - p.m_aStart + 1 + m_nPartitionSize - 1) / m_nPartitionSize;
        s_aLogger.info ("mapReduce i=" + index + ", c=" + count + ", s=" + m_nPartitionSize + ": " + p);
        Utils.sleep (1000);
        // split up into partition size chunks
        while (p.m_aEnd - p.m_aStart + 1 >= m_nPartitionSize)
        {
          final MapReduceParameters xp = new MapReduceParameters (p);
          xp.m_aEnd = xp.m_aStart + m_nPartitionSize - 1;
          final DefaultActorMessage lm = new DefaultActorMessage ("createPartition",
                                                                  new Object [] { xp,
                                                                                 Integer.valueOf (index),
                                                                                 Integer.valueOf (count) });
          manager.send (lm, this, getCategory ());
          p.m_aStart += m_nPartitionSize;
          index++;
        }
        if (p.m_aEnd - p.m_aStart + 1 > 0)
        {
          final DefaultActorMessage lm = new DefaultActorMessage ("createPartition",
                                                                  new Object [] { p,
                                                                                 Integer.valueOf (index),
                                                                                 Integer.valueOf (count) });
          manager.send (lm, this, getCategory ());
        }
      }
      catch (final Exception e)
      {
        triageException ("mapFailed", m, e);
      }
    }
    else
      if ("createPartition".equals (subject))
      {
        try
        {
          final Object [] oa = (Object []) m.getData ();
          final MapReduceParameters p = (MapReduceParameters) oa[0];
          final int index = ((Integer) oa[1]).intValue ();
          final int count = ((Integer) oa[2]).intValue ();
          s_aLogger.info ("createPartition i=" + index + ", c=" + count + ": " + p);
          Utils.sleep (500);
          createMapReduceActor (this);
          final DefaultActorMessage lm = new DefaultActorMessage ("mapWorker", new Object [] { p,
                                                                                              Integer.valueOf (index),
                                                                                              Integer.valueOf (count) });
          manager.send (lm, this, getCategory ());
        }
        catch (final Exception e)
        {
          triageException ("createPartitionFailed", m, e);
        }
      }
      else
        if ("mapWorker".equals (subject))
        {
          try
          {
            final Object [] oa = (Object []) m.getData ();
            final MapReduceParameters p = (MapReduceParameters) oa[0];
            final int index = ((Integer) oa[1]).intValue ();
            final int count = ((Integer) oa[2]).intValue ();
            s_aLogger.info ("mapWorker " + index + ": " + p);
            Utils.sleep (100);
            p.m_aMR.map (p.m_aValues, p.m_aStart, p.m_aEnd);
            final DefaultActorMessage rm = new DefaultActorMessage ("mapResponse",
                                                                    new Object [] { p,
                                                                                   Integer.valueOf (index),
                                                                                   Integer.valueOf (count) });
            manager.send (rm, this, getCategoryName ());
          }
          catch (final Exception e)
          {
            triageException ("mapWorkerFailed", m, e);
          }
        }
        else
          if ("mapResponse".equals (subject))
          {
            try
            {
              final Object [] oa = (Object []) m.getData ();
              final MapReduceParameters p = (MapReduceParameters) oa[0];
              final int index = ((Integer) oa[1]).intValue ();
              final int count = ((Integer) oa[2]).intValue ();
              s_aLogger.info ("mapResponse i=" + index + ", c=" + count + ": " + p);
              Utils.sleep (100);
              p.complete ();
              // if (p.isSetComplete()) {
              final DefaultActorMessage rm = new DefaultActorMessage ("reduce",
                                                                      new Object [] { p,
                                                                                     Integer.valueOf (index),
                                                                                     Integer.valueOf (count) });
              manager.send (rm, this, getCategoryName ());
              // }
            }
            catch (final Exception e)
            {
              triageException ("mapResponseFailed", m, e);
            }
          }
          else
            if ("reduce".equals (subject))
            {
              try
              {
                MapReduceParameters p = null;
                int index = 0, count = 0;
                final Object o = m.getData ();
                if (o instanceof MapReduceParameters)
                {
                  p = (MapReduceParameters) o;
                }
                else
                {
                  final Object [] oa = (Object []) o;
                  p = (MapReduceParameters) oa[0];
                  index = ((Integer) oa[1]).intValue ();
                  count = ((Integer) oa[2]).intValue ();
                }
                s_aLogger.info ("reduce i=" + index + ": " + p);
                Utils.sleep (100);
                if (p.m_aEnd - p.m_aStart + 1 > 0)
                {
                  createMapReduceActor (this);
                  final MapReduceParameters xp = new MapReduceParameters (p);
                  final DefaultActorMessage lm = new DefaultActorMessage ("reduceWorker",
                                                                          new Object [] { xp,
                                                                                         Integer.valueOf (index),
                                                                                         Integer.valueOf (count) });
                  manager.send (lm, this, getCategory ());
                }
              }
              catch (final Exception e)
              {
                triageException ("reduceFailed", m, e);
              }
            }
            else
              if ("reduceWorker".equals (subject))
              {
                try
                {
                  final Object [] oa = (Object []) m.getData ();
                  final MapReduceParameters p = (MapReduceParameters) oa[0];
                  final int index = ((Integer) oa[1]).intValue ();
                  final int count = ((Integer) oa[2]).intValue ();
                  s_aLogger.info ("reduceWorker i=" + index + ", c=" + count + ": " + p);
                  Utils.sleep (100);
                  if (index >= 0)
                  {
                    p.m_aMR.reduce (p.m_aValues, p.m_aStart, p.m_aEnd, p.m_aTarget, index);
                    final DefaultActorMessage rm = new DefaultActorMessage ("reduceResponse",
                                                                            new Object [] { p,
                                                                                           Integer.valueOf (index),
                                                                                           Integer.valueOf (count) });
                    manager.send (rm, this, getCategory ());
                  }
                  else
                  {
                    final Object [] res = new Object [1];
                    p.m_aMR.reduce (p.m_aTarget, 0, count - 1, res, 0);
                    final DefaultActorMessage rm = new DefaultActorMessage ("done", new Object [] { p, res[0] });
                    manager.send (rm, this, getCategory ());
                  }
                }
                catch (final Exception e)
                {
                  triageException ("reduceWorkerFailed", m, e);
                }
              }
              else
                if ("reduceResponse".equals (subject))
                {
                  try
                  {
                    final Object [] oa = (Object []) m.getData ();
                    final MapReduceParameters p = (MapReduceParameters) oa[0];
                    final int index = ((Integer) oa[1]).intValue ();
                    final int count = ((Integer) oa[2]).intValue ();
                    s_aLogger.info ("reduceResponse i=" + index + ", c=" + count + ": " + p);
                    Utils.sleep (100);
                    p.complete ();
                    if (p.isSetComplete ())
                    {
                      if (count > 0)
                      {
                        createMapReduceActor (this);
                        final MapReduceParameters xp = new MapReduceParameters (p);
                        final DefaultActorMessage lm = new DefaultActorMessage ("reduceWorker",
                                                                                new Object [] { xp,
                                                                                               Integer.valueOf (-1),
                                                                                               Integer.valueOf (count) });
                        manager.send (lm, this, getCategory ());
                      }
                    }
                  }
                  catch (final Exception e)
                  {
                    triageException ("mapResponseFailed", m, e);
                  }
                }
                else
                  if ("done".equals (subject))
                  {
                    try
                    {
                      final Object [] oa = (Object []) m.getData ();
                      final MapReduceParameters p = (MapReduceParameters) oa[0];
                      final Object res = oa[1];
                      s_aLogger.info ("done %s: %s", res, p);
                      Utils.sleep (100);
                      s_aLogger.trace ("**** mapReduce done with result %s", res);
                    }
                    catch (final Exception e)
                    {
                      triageException ("mapResponseFailed", m, e);
                    }
                  }
                  else
                    if ("init".equals (subject))
                    {
                      try
                      {
                        final Object [] params = (Object []) m.getData ();
                        if (params != null)
                        {
                          final Object [] values = (Object []) params[0];
                          final Object [] targets = (Object []) params[1];
                          final Class <?> clazz = (Class <?>) params[2];
                          s_aLogger.info ("init " + values.length + ", " + targets.length + ", " + clazz);
                          final MapReduceer mr = (MapReduceer) clazz.newInstance ();

                          Utils.sleep (2 * 1000);
                          final MapReduceParameters p = new MapReduceParameters ("mrSet_" + s_nSetCount++,
                                                                                 values,
                                                                                 targets,
                                                                                 mr,
                                                                                 this);
                          final DefaultActorMessage rm = new DefaultActorMessage ("mapReduce", p);
                          manager.send (rm, this, getCategoryName ());
                        }
                      }
                      catch (final Exception e)
                      {
                        triageException ("initFailed", m, e);
                      }
                    }
                    else
                    {
                      s_aLogger.warn ("**** MapReduceActor:" + getName () + " loopBody unexpected subject: " + subject);
                    }
  }

  protected void triageException (final String subject, final IActorMessage m, final Exception e)
  {
    s_aLogger.error ("triageException " + subject + ": " + m, e);
    IActor target = m.getSource ();
    final DefaultActorMessage dm = new DefaultActorMessage (subject, new Object [] { m.getData (), e });
    if (m.getData () instanceof MapReduceParameters)
    {
      final MapReduceParameters p = (MapReduceParameters) m.getData ();
      if (p != null && p.m_aSender != null)
      {
        target = p.m_aSender;
      }
    }
    getManager ().send (dm, this, target);
  }

  public static MapReduceActor createMapReduceActor (final MapReduceActor mra)
  {
    final IActorManager manager = mra.getManager ();
    return (manager.getActorCount (MapReduceActor.class) < MAX_ACTOR_COUNT
                                                                          ? createMapReduceActor (manager,
                                                                                                  mra.getPartitionSize ())
                                                                          : mra);
  }

  public static MapReduceActor createMapReduceActor (final IActorManager manager, final int partitionSize)
  {
    final MapReduceActor res = (MapReduceActor) manager.createAndStartActor (MapReduceActor.class, getActorName ());
    res.setCategory (getCategoryName ());
    res.setPartitionSize (partitionSize);
    return res;
  }

  public static String getCategoryName ()
  {
    return "mapReduceActor";
  }

  protected static int s_nSetCount;
  protected static int s_nIDCount;

  public static String nextId ()
  {
    return "mapReduce_" + s_nIDCount++;
  }

  protected static int s_nActorCount;

  protected static String getActorName ()
  {
    return MapReduceActor.class.getSimpleName () + '_' + s_nActorCount++;
  }

}
