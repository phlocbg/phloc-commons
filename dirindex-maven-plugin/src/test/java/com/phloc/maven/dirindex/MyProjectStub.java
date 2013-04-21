package com.phloc.maven.dirindex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.codehaus.plexus.util.ReaderFactory;

public class MyProjectStub extends MavenProjectStub
{
  /**
   * Default constructor
   */
  public MyProjectStub ()
  {
    final MavenXpp3Reader pomReader = new MavenXpp3Reader ();
    Model model;
    try
    {
      model = pomReader.read (ReaderFactory.newXmlReader (new File (getBasedir (), "pom.xml")));
      setModel (model);
    }
    catch (final Exception e)
    {
      throw new RuntimeException (e);
    }

    setGroupId (model.getGroupId ());
    setArtifactId (model.getArtifactId ());
    setVersion (model.getVersion ());
    setName (model.getName ());
    setUrl (model.getUrl ());
    setPackaging (model.getPackaging ());

    final Build build = new Build ();
    build.setFinalName (model.getArtifactId ());
    build.setDirectory (getBasedir () + "/target");
    build.setSourceDirectory (getBasedir () + "/src/main/java");
    build.setOutputDirectory (getBasedir () + "/target/classes");
    build.setTestSourceDirectory (getBasedir () + "/src/test/java");
    build.setTestOutputDirectory (getBasedir () + "/target/test-classes");
    setBuild (build);

    final List <String> compileSourceRoots = new ArrayList <String> ();
    compileSourceRoots.add (getBasedir () + "/src/main/java");
    setCompileSourceRoots (compileSourceRoots);

    final List <String> testCompileSourceRoots = new ArrayList <String> ();
    testCompileSourceRoots.add (getBasedir () + "/src/test/java");
    setTestCompileSourceRoots (testCompileSourceRoots);
  }

  /** {@inheritDoc} */
  @Override
  public File getBasedir ()
  {
    return new File (super.getBasedir () + "/src/test/resources/poms/phloc-schematron-testfiles/");
  }
}
