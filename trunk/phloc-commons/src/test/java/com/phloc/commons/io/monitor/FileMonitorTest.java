/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.io.monitor;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.concurrent.ThreadUtils;

public class FileMonitorTest
{
  @Test
  public void testBasic ()
  {
    final IFileListener aDeleteListener = new DefaultFileListener ()
    {
      @Override
      public void onFileDeleted (final FileChangeEvent event)
      {
        System.out.println ("File deleted: " + event.getFile ().getAbsolutePath ());
      }
    };
    final IFileListener aCreateListener = new DefaultFileListener ()
    {
      @Override
      public void onFileCreated (final FileChangeEvent event)
      {
        System.out.println ("File created: " + event.getFile ().getAbsolutePath ());
      }
    };
    final IFileListener aChangeListener = new DefaultFileListener ()
    {
      @Override
      public void onFileChanged (final FileChangeEvent event)
      {
        System.out.println ("File changed: " + event.getFile ().getAbsolutePath ());
      }
    };
    final FileMonitorManager aMgr = new FileMonitorManager ();
    final boolean bRecursive = true;
    final File aMonitorFile = new File ("src/main");
    aMgr.createFileMonitor (aDeleteListener).setRecursive (bRecursive).addMonitoredFile (aMonitorFile);
    aMgr.createFileMonitor (aCreateListener).setRecursive (bRecursive).addMonitoredFile (aMonitorFile);
    aMgr.createFileMonitor (aChangeListener).setRecursive (bRecursive).addMonitoredFile (aMonitorFile);
    aMgr.start ();
    assertTrue (aMgr.isRunning ());
    ThreadUtils.sleepSeconds (5);
    aMgr.stop ();
  }
}
