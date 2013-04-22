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
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.file.iterate.FileSystemFolderTree;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.folder.DefaultFolderTreeItem;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

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
  /**
   * The Maven Project.
   * 
   * @parameter property="project"
   * @required
   * @readonly
   */
  MavenProject project;

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

  /**
   * The directory within the target artifact where the file should reside.
   * 
   * @required
   * @parameter property=targetDirectory default-value="META-INF"
   */
  private String targetDirectory;

  /**
   * The filename within the temp and the target directory to be used.
   * 
   * @required
   * @parameter property=targetFilename default-value="dirindex.xml"
   */
  private String targetFilename;

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

  @Nonnull
  private IMicroDocument _getAsXML (@Nonnull final FileSystemFolderTree aFileTree) throws IOException
  {
    final String sBase = sourceDirectory.getCanonicalPath ();
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("index");
    eRoot.setAttribute ("sourcedirectory", sBase);
    final NonBlockingStack <String> aDirs = new NonBlockingStack <String> ();
    TreeWalker.walkTree (aFileTree,
                         new DefaultHierarchyWalkerCallback <DefaultFolderTreeItem <String, File, List <File>>> ()
                         {
                           @Override
                           public void onItemBeforeChildren (@Nonnull final DefaultFolderTreeItem <String, File, List <File>> aItem)
                           {
                             final String sDirName = aItem.getID ();
                             final int nSubDirCount = aItem.getChildCount ();
                             final List <File> aFiles = aItem.getData ();

                             aDirs.push (sDirName);

                             final String sImplodedDirName = StringHelper.getImploded (File.separator, aDirs);
                             final IMicroElement eDir = eRoot.appendElement ("directory");
                             eDir.setAttribute ("name", sImplodedDirName);
                             eDir.setAttribute ("subdircount", Integer.toString (nSubDirCount));
                             eDir.setAttribute ("filecount", aFiles == null ? "0" : Integer.toString (aFiles.size ()));

                             if (aFiles != null)
                               for (final File aFile : aFiles)
                               {
                                 final IMicroElement eFile = eRoot.appendElement ("file");
                                 eFile.setAttribute ("name", sImplodedDirName + File.separator + aFile.getName ());
                                 eFile.setAttribute ("filesize", Long.toString (aFile.length ()));
                               }
                           }

                           @Override
                           public void onItemAfterChildren (@Nonnull final DefaultFolderTreeItem <String, File, List <File>> aItem)
                           {
                             aDirs.pop ();
                           }
                         });
    if (false)
      System.out.println (MicroWriter.getXMLString (eRoot));
    return aDoc;
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

    try
    {
      // Build the index
      final FileSystemFolderTree aFileTree = new FileSystemFolderTree (sourceDirectory);
      final IMicroDocument aDoc = _getAsXML (aFileTree);
      final File aTempFile = new File (tempDirectory, targetFilename);
      SimpleFileIO.writeFile (aTempFile, MicroWriter.getXMLString (aDoc), XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ);

      // Add output directory as a resource-directory
      final Resource aResource = new Resource ();
      aResource.setDirectory (tempDirectory.getAbsolutePath ());
      aResource.addInclude (aTempFile.getName ());
      aResource.setFiltering (false);
      aResource.setTargetPath (targetDirectory);
      project.addResource (aResource);
    }
    catch (final IOException ex)
    {
      throw new MojoExecutionException ("Failed to build directory index!", ex);
    }
  }
}
