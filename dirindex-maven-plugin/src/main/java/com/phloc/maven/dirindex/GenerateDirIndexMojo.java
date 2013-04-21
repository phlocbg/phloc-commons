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
package com.phloc.maven.dirindex;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileOperations;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Philip Helger, phloc systems
 * @goal generate-dirindex
 * @phase generate-resources
 * @description Create the index of a directory and store it into an XML file.
 *              The information will be part of the created JAR/WAR/... file.
 *              The resulting file will reside in a custom directory of the
 *              created artifact.
 */
@SuppressFBWarnings (value = { "UWF_UNWRITTEN_FIELD", "NP_UNWRITTEN_FIELD" }, justification = "set via maven property")
public final class GenerateDirIndexMojo extends AbstractMojo
{
  /** The name of the XML file */
  private static final String DEFAULT_FILENAME_BUILDINFO_XML = "dirindex.xml";
  /** The name of the properties file */
  private static final String DEFAULT_FILENAME_BUILDINFO_PROPERTIES = "dirindex.properties";

  /**
   * The Maven Project.
   * 
   * @parameter property=project
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * @parameter property=reactorProjects
   * @required
   * @readonly
   */
  private List <MavenProject> reactorProjects;

  /**
   * The directory which should be index. This directory must be specified!
   * 
   * @required
   * @parameter property=sourceDirectory
   */
  private File sourceDirectory;

  /**
   * The directory where the temporary index file will be saved.
   * 
   * @required
   * @parameter property=tempDirectory
   *            default-value="${project.build.directory}/dirindex-maven-plugin"
   */
  private File tempDirectory;

  public void setSourceDirectory (@Nonnull final File aDir)
  {
    sourceDirectory = aDir;
    if (!sourceDirectory.isAbsolute ())
      sourceDirectory = new File (project.getBasedir (), aDir.getPath ());
    if (!sourceDirectory.exists ())
      getLog ().error ("Source directory " + sourceDirectory.toString () + " does not exist!");
  }

  public void setTempDirectory (@Nonnull final File aDir)
  {
    tempDirectory = aDir;
    if (!tempDirectory.isAbsolute ())
      tempDirectory = new File (project.getBasedir (), aDir.getPath ());
    final FileIOError aResult = FileOperations.createDirRecursiveIfNotExisting (tempDirectory);
    if (aResult.isFailure ())
      getLog ().error ("Failed to create temp directory " + aResult.toString ());
    else
      getLog ().info ("Successfully created temp directory " + aDir.toString ());
  }

  public void execute () throws MojoExecutionException
  {
    StaticLoggerBinder.getSingleton ().setMavenLog (getLog ());
    if (tempDirectory == null)
      throw new MojoExecutionException ("No dirindex temp directory specified!");
    if (tempDirectory.exists () && !tempDirectory.isDirectory ())
      throw new MojoExecutionException ("The specified dirindex temp directory " +
                                        tempDirectory +
                                        " is not a directory!");
    if (!tempDirectory.exists ())
    {
      // Ensure that the directory exists
      if (!tempDirectory.mkdirs ())
        throw new MojoExecutionException ("Failed to create dirindex temp directory " + tempDirectory);
      getLog ().info ("Created dirindex temp directory " + tempDirectory);
    }

    if (sourceDirectory == null)
      throw new MojoExecutionException ("No dirindex source directory specified!");
    if (sourceDirectory.exists () && !tempDirectory.isDirectory ())
      throw new MojoExecutionException ("The specified dirindex source directory " +
                                        sourceDirectory +
                                        " is not a directory!");
    if (!sourceDirectory.exists ())
      throw new MojoExecutionException ("The specified dirindex source directory " +
                                        sourceDirectory +
                                        " does not exist!");

    // Add output directory as a resource-directory
    final Resource aResource = new Resource ();
    aResource.setDirectory (tempDirectory.getAbsolutePath ());
    aResource.addInclude ("**/*");
    aResource.setFiltering (false);
    aResource.setTargetPath ("META-INF");
    project.addResource (aResource);
  }
}
