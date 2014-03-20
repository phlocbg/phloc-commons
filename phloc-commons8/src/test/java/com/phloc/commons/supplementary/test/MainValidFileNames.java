/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.supplementary.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.phloc.commons.io.file.FileUtils;

public final class MainValidFileNames
{
  private MainValidFileNames ()
  {}

  static void testName (final String sName)
  {
    final String fileName = sName + ".txt";
    final File f = new File (fileName);

    System.out.println ("--- testing name: " + sName + " ---");
    System.out.println (sName + ".exists()=" + f.exists ());
    System.out.println (sName + ".getAbsolutePath()=" + f.getAbsolutePath ());
    System.out.println (sName + ".canRead()=" + FileUtils.canRead (f));
    System.out.println (sName + ".canWrite()=" + FileUtils.canWrite (f));

    System.out.println ("Writing into " + fileName + " ...");
    try (OutputStream out = FileUtils.getOutputStream (f))
    {
      out.write (42);
    }// try
    catch (final IOException ex)
    {
      System.err.println (ex);
    }

    System.out.println (sName + ".exists()=" + f.exists ());
    System.out.println (sName + ".getAbsolutePath()=" + f.getAbsolutePath ());
    System.out.println (sName + ".canRead()=" + FileUtils.canRead (f));
    System.out.println (sName + ".canWrite()=" + FileUtils.canWrite (f));
    System.out.println ("Reading from " + fileName + " ...");
    try (FileInputStream in = new FileInputStream (f))
    {
      System.out.println (in.read ());
    }
    catch (final FileNotFoundException ex)
    {
      System.err.println (fileName + " is not found");
    }
    catch (final IOException ex)
    {
      System.err.println (ex);
    }
  }

  public static void main (final String [] args)
  {
    // no problem
    testName ("PRN1");
    // printer
    testName ("PRN");
    // null device
    testName ("nul");
    // 1st parallel port
    testName ("LPT1");
    // 2nd parallel port
    testName ("LPT2");
    // console
    testName ("CoN");
    // serial port
    testName ("aUX");
  }
}
