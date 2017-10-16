package com.phloc.commons.concurrent.deadlock;

import java.lang.management.LockInfo;

public interface IThreadDeadlockListener
{
  void deadlockDetected (Thread [] aDeadlockedThreads);

  void dumpLockInfo (long nThreadID, LockInfo [] aLocks);
}
