package com.phloc.maven.dirindex;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class GenerateDirIndexMojoTestCase extends AbstractMojoTestCase
{
  /**
   * @throws Exception
   *         if any
   */
  public void testSomething () throws Exception
  {
    if (false)
    {
      final File aPOM = getTestFile ("src/test/resources/poms/phloc-schematron-testfiles.xml");
      assertNotNull (aPOM);
      assertTrue (aPOM.exists ());

      final GenerateDirIndexMojo myMojo = (GenerateDirIndexMojo) lookupMojo ("generate-dirindex", aPOM);
      assertNotNull (myMojo);
      myMojo.execute ();
    }
  }
}
