package com.sun.tools.xjc.addon.phloc;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.phloc.commons.annotations.DevelopersNote;
import com.sun.tools.xjc.Driver;

public class XJCLoaderTest {
  @Ignore
  @Test
  @DevelopersNote ("Ignored because System.exit is called :(")
  public void testLoadXJC () throws Throwable {
    Driver.main (new String [] { new File ("src/test/resources/changelog-1.0.xsd").getAbsolutePath (),
                                "-d",
                                new File ("target").getAbsolutePath () });
  }
}
