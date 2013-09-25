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

import java.io.File;

import org.junit.Test;

import com.phloc.commons.concurrent.ThreadUtils;

public class FileMonitorTest
{
  @Test
  public void testBasic ()
  {
    final IFileListener aListener = new DefaultFileListener ()
    {
      @Override
      public void onFileDeleted (final FileChangeEvent event) throws Exception
      {
        System.out.println ("File deleted: " + event.getFile ().getAbsolutePath ());
      }

      @Override
      public void onFileCreated (final FileChangeEvent event) throws Exception
      {
        System.out.println ("File created: " + event.getFile ().getAbsolutePath ());
      }

      @Override
      public void onFileChanged (final FileChangeEvent event) throws Exception
      {
        System.out.println ("File changed: " + event.getFile ().getAbsolutePath ());
      }
    };
    final FileMonitor aMon = new FileMonitor (aListener);
    aMon.setRecursive (true);
    aMon.addFile (new File ("."));
    aMon.start ();
    ThreadUtils.sleepSeconds (5);
    aMon.stop ();
  }
}
