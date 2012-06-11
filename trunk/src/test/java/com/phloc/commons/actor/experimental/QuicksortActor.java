/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.commons.actor.experimental;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.mutable.MutableInt;

// Incomplete - ignore, quicksort not appropriate for Map/Reduce, need merge Sort instead
public class QuicksortActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (QuicksortActor.class);

  protected Map <String, MutableInt> completionCounts = new Hashtable <String, MutableInt> ();

  protected static int s_aActorCount, idCount;

  protected final class PendingMerge <T extends Comparable <T>>
  {
    public String m_sID;
    public T m_aPivot;
    public List <T> m_aLess;
    public List <T> m_aMore;
    public List <T> m_aMerge = new LinkedList <T> ();
    volatile public boolean m_bLessNeeded, m_bMoreNeeded;
    volatile public int m_nCount;

    public PendingMerge (final List <T> less, final List <T> more)
    {
      m_sID = nextId ();
      m_aLess = less;
      m_aMore = more;
      pendingMerges.put (m_sID, this);
    }

    public PendingMerge ()
    {
      this (new LinkedList <T> (), new LinkedList <T> ());
    }

    public void merge ()
    {
      m_aMerge.clear ();
      m_aMerge.addAll (m_aLess);
      m_aMerge.add (m_aPivot);
      m_aMerge.addAll (m_aMore);
    }
  }

  protected Map <String, PendingMerge <?>> pendingMerges = new HashMap <String, PendingMerge <?>> ();

  @Override
  protected void loopBody (final IActorMessage m)
  {
    try
    {
      final IActorManager manager = getManager ();

      final String subject = m.getSubject ();
      if ("sort".equals (subject))
      {
        doSort (m, manager);
      }
      else
        if ("merge".equals (subject))
        {
          doMerge (m);
        }
        else
          if ("sortComplete".equals (subject))
          {
            doSortComplete (m);
          }
          else
            if ("init".equals (subject))
            {
              doInit (m, manager);
            }
            else
            {
              s_aLogger.warn ("QuicksortActor:" + getName () + " loopBody unknown subject: " + subject);
            }
    }
    catch (final Exception e)
    {
      s_aLogger.error ("sort exception", e);
    }
  }

  private <T extends Comparable <T>> void doSort (final IActorMessage m, final IActorManager manager)
  {
    final Object [] params = (Object []) m.getData ();
    final int nest = ((Integer) params[0]).intValue ();
    @SuppressWarnings ("unchecked")
    final List <T> values = params.length > 1 ? (List <T>) params[1] : null;
    final String id = params.length > 2 ? (String) params[2] : null;
    @SuppressWarnings ("unchecked")
    final PendingMerge <T> pm = params.length > 3 ? (PendingMerge <T>) params[3] : null;
    if (values != null)
    {
      s_aLogger.info ("receive sort values " + id + "(" + nest + "): " + values);
      doSortWorker (manager, nest, values, id, pm);
    }
    else
      if (pm != null)
      {
        s_aLogger.info ("receive sort pm " +
                        id +
                        "(" +
                        nest +
                        "): count=" +
                        pm.m_nCount +
                        "/" +
                        pm.m_bLessNeeded +
                        "/" +
                        pm.m_bMoreNeeded +
                        ", " +
                        pm.m_aLess +
                        "<" +
                        pm.m_aPivot +
                        ">" +
                        pm.m_aMore);
        if (pm.m_nCount > 0)
        {
          if (pm.m_bLessNeeded)
          {
            s_aLogger.info ("receive sort less " + id + "(" + nest + "): " + pm.m_aLess);
            pm.m_bLessNeeded = false;
            pm.m_nCount--;
            doSortWorker (manager, nest + 1, pm.m_aLess, pm.m_sID, pm);
          }
          else
            if (pm.m_bMoreNeeded)
            {
              s_aLogger.info ("receive sort more " + id + "(" + nest + "):" + pm.m_aMore);
              pm.m_bMoreNeeded = false;
              pm.m_nCount--;
              doSortWorker (manager, nest + 1, pm.m_aMore, pm.m_sID, pm);
            }
          if (pm.m_nCount == 0)
          {
            final DefaultActorMessage dm = new DefaultActorMessage ("merge", new Object [] { Integer.valueOf (nest),
                                                                                            pm,
                                                                                            id });
            s_aLogger.info ("send merge " +
                            id +
                            "-" +
                            pm.m_sID +
                            "(" +
                            nest +
                            "): " +
                            pm.m_aLess +
                            "<" +
                            pm.m_aPivot +
                            ">" +
                            pm.m_aMore);
            recordSend (id, manager.send (dm, this, getCategoryName ()));
          }
        }
      }
      else
      {
        s_aLogger.error ("receive empty sort " + id + "(" + nest + ")");
      }
    recordComplete (id, 1);
  }

  protected <T extends Comparable <T>> void doSortWorker (final IActorManager manager,
                                                          final int nest,
                                                          final List <T> values,
                                                          final String id,
                                                          final PendingMerge <T> ppm)
  {
    PendingMerge <T> pm = ppm;
    Utils.sleep (1000);
    if (values.size () <= 1)
    {
      final DefaultActorMessage dm = new DefaultActorMessage ("sortComplete", new Object [] { Integer.valueOf (nest),
                                                                                             values,
                                                                                             id });
      s_aLogger.info ("send size complete " + id + "(" + nest + "): " + values);
      recordSend (id, manager.send (dm, this, getCategoryName ()));
    }
    else
    {
      pm = new PendingMerge <T> ();
      final T pivot = values.remove (0);
      pm.m_aPivot = pivot;
      for (final T c : values)
      {
        if (c.compareTo (pivot) <= 0)
        {
          pm.m_aLess.add (c);
        }
        else
        {
          pm.m_aMore.add (c);
        }
      }
      if (pm.m_aLess.size () > 0)
      {
        pm.m_bLessNeeded = true;
        pm.m_nCount++;
      }
      if (pm.m_aMore.size () > 0)
      {
        pm.m_bMoreNeeded = true;
        pm.m_nCount++;
      }
      s_aLogger.info ("send pm sort " +
                      id +
                      "(" +
                      nest +
                      "): count=" +
                      pm.m_nCount +
                      ", " +
                      pm.m_aLess +
                      "<" +
                      pm.m_aPivot +
                      ">" +
                      pm.m_aMore);
      if (pm.m_nCount > 0)
      {
        if (pm.m_bLessNeeded)
        {
          final DefaultActorMessage lm = new DefaultActorMessage ("sort", new Object [] { Integer.valueOf (nest + 1),
                                                                                         null,
                                                                                         pm.m_sID,
                                                                                         pm });
          s_aLogger.info ("send sort less " + pm.m_sID + ": " + pm.m_aLess);
          recordSend (id, manager.send (lm, this, getCategoryName ()));
        }
        if (pm.m_bMoreNeeded)
        {
          final DefaultActorMessage mm = new DefaultActorMessage ("sort", new Object [] { Integer.valueOf (nest + 1),
                                                                                         null,
                                                                                         pm.m_sID,
                                                                                         pm });
          s_aLogger.info ("send sort more " + pm.m_sID + ": " + pm.m_aMore);
          recordSend (id, manager.send (mm, this, getCategoryName ()));
        }
      }
    }
  }

  protected void doMerge (final IActorMessage m)
  {
    Utils.sleep (1000);
    final Object [] params = (Object []) m.getData ();
    final Integer nest = params.length > 0 ? (Integer) params[0] : null;
    final PendingMerge <?> pm = (PendingMerge <?>) params[1];
    final String id = params.length > 2 ? (String) params[2] : null;
    pm.merge ();
    s_aLogger.info ("merge " +
                    pm.m_sID +
                    "(" +
                    nest +
                    "): " +
                    pm.m_aMerge +
                    " <= " +
                    pm.m_aLess +
                    "<" +
                    pm.m_aPivot +
                    ">" +
                    pm.m_aMore);
    final DefaultActorMessage dm = new DefaultActorMessage ("sortComplete",
                                                            new Object [] { nest, pm.m_aMerge, pm.m_sID });
    s_aLogger.info ("send sort complete " + pm.m_sID + "(" + nest + ")");
    recordSend (pm.m_sID, getManager ().send (dm, this, getCategoryName ()));
    recordComplete (id, 1);
  }

  protected void doSortComplete (final IActorMessage m)
  {
    final Object [] params = (Object []) m.getData ();
    final int nest = ((Integer) params[0]).intValue ();
    final List <?> values = params.length > 1 ? (List <?>) params[1] : null;
    final String id = params.length > 2 ? (String) params[2] : null;

    s_aLogger.info ("receive complete " + id + "(" + nest + "): " + values);
    recordComplete (id, 1);
    if (isComplete (id))
    {
      if (nest == 0)
      {
        s_aLogger.trace ("**** Sort %s complete: %s", id, values);
      }
    }
  }

  protected void doInit (final IActorMessage m, final IActorManager manager)
  {
    final Object [] params = (Object []) m.getData ();
    final int max = !ArrayHelper.isEmpty (params) ? ((Integer) params[0]).intValue () : 50;
    // ensure enough actors exist
    final int count = manager.getActorCount (QuicksortActor.class);
    for (int i = count; i < max; i++)
    {
      createQuicksortActor (manager);
    }
  }

  protected void resetPending (final String id)
  {
    setPending (id, 0);
  }

  protected void setPending (final String id, final int count)
  {
    completionCounts.put (id, new MutableInt (count));
    if (false)
      s_aLogger.info ("setPending " + id + ": " + count);
  }

  protected int recordSend (final String id, final int count)
  {
    if (!completionCounts.containsKey (id))
      resetPending (id);
    final MutableInt res = completionCounts.get (id);
    res.inc (count);
    if (false)
      s_aLogger.info ("recordSend " + id + ": " + res.intValue ());
    return res.intValue ();
  }

  protected int recordComplete (final String id, final int count)
  {
    if (!completionCounts.containsKey (id))
      resetPending (id);
    final MutableInt res = completionCounts.get (id);
    res.dec (count);
    if (res.isSmaller0 ())
    {
      s_aLogger.error ("**** recordComplete " + id + " count < 0: " + res.intValue ());
      res.inc (count);
    }
    if (false)
      s_aLogger.info ("recordComplete " + id + ": " + res.intValue ());
    return res.intValue ();
  }

  protected boolean isComplete (final String id)
  {
    final boolean res = completionCounts.containsKey (id) ? completionCounts.get (id).intValue () <= 0 : true;
    s_aLogger.info ("isCommplete " + id + ": " + res);
    return res;
  }

  protected static QuicksortActor createQuicksortActor (final IActorManager manager)
  {
    final QuicksortActor a = (QuicksortActor) manager.createAndStartActor (QuicksortActor.class,
                                                                           QuicksortActor.class.getSimpleName () +
                                                                               s_aActorCount++);
    a.setCategory (getCategoryName ());
    return a;
  }

  Random rand = new Random ();

  // test case

  public void run (final IActorManager am)
  {
    createQuicksortActor (am);
  }

  public static String nextId ()
  {
    return "id_" + idCount++;
  }

  public static String getCategoryName ()
  {
    return "quicksortActor";
  }

  public static void main (final String [] args)
  {
    try
    {
      final IActorManager am = DefaultActorManager.getDefaultInstance ();
      final QuicksortActor qa = new QuicksortActor ();
      qa.run (am);
    }
    catch (final NumberFormatException e)
    {
      e.printStackTrace ();
    }
  }
}
