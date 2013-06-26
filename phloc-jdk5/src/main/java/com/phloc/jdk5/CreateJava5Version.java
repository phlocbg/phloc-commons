package com.phloc.jdk5;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.filter.collections.FilterIterator;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.file.FileOperationManager;
import com.phloc.commons.io.file.LoggingFileOperationCallback;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.file.filter.FileFilterToIFilterAdapter;
import com.phloc.commons.io.file.filter.FilenameFilterMatchNoRegEx;
import com.phloc.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.microdom.utils.MicroWalker;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.namespace.MapBasedNamespaceContext;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Main logic for creating JDK5 versions of a phloc project by handling certain
 * special comments in Java files.
 * 
 * @author Philip Helger
 */
// SKIPJDK5
public final class CreateJava5Version
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CreateJava5Version.class);

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CreateJava5Version s_aInstance = new CreateJava5Version ();

  private CreateJava5Version ()
  {}

  @Nonnull
  @Nonempty
  private static String _getNewArtifactName (@Nonnull final String sOldName)
  {
    return sOldName + "-jdk5";
  }

  private static void _updatePOM (@Nonnull final File aSrcFile,
                                  @Nonnull final File aDstFile,
                                  @Nonnull final String sSrcComponentName,
                                  @Nonnull final String sDstComponentName)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    final IMicroElement eRoot = aDoc.getDocumentElement ();

    // Global artifact ID
    {
      final IMicroElement eArtifact = eRoot.getFirstChildElement ("artifactId");
      if (!sSrcComponentName.equals (eArtifact.getTextContent ()))
        throw new IllegalStateException ("Illegal artifactId");
      eArtifact.removeAllChildren ();
      eArtifact.appendText (sDstComponentName);
    }

    // Global artifact name
    {
      final IMicroElement eName = eRoot.getFirstChildElement ("name");
      if (!sSrcComponentName.equals (eName.getTextContent ()))
        throw new IllegalStateException ("Illegal name");
      eName.removeAllChildren ();
      eName.appendText (sDstComponentName);
    }

    // Global doc URL
    {
      final IMicroElement eUrl = eRoot.getFirstChildElement ("url");
      if (eUrl != null)
      {
        final String sOld = eUrl.getTextContent ();
        eUrl.removeAllChildren ();
        eUrl.appendText (sOld.replace (sSrcComponentName, sDstComponentName));
      }
    }

    // SCM settings
    {
      final IMicroElement eScm = eRoot.getFirstChildElement ("scm");
      if (eScm != null)
        for (final IMicroElement eChild : eScm.getAllChildElements ())
        {
          final String sOld = eChild.getTextContent ();
          if (sOld.endsWith (sSrcComponentName))
          {
            eChild.removeAllChildren ();
            eChild.appendText (sOld.substring (0, sOld.length () - sSrcComponentName.length ()) + sDstComponentName);
          }
        }
    }

    // Set compiler plugin
    {
      IMicroElement eBuild = eRoot.getFirstChildElement ("build");
      if (eBuild == null)
        eBuild = eRoot.appendElement (eRoot.getNamespaceURI (), "build");

      IMicroElement ePlugins = eBuild.getFirstChildElement ("plugins");
      if (ePlugins == null)
        ePlugins = eBuild.appendElement (eRoot.getNamespaceURI (), "plugins");

      final IMicroElement ePlugin = ePlugins.appendElement (eRoot.getNamespaceURI (), "plugin");
      ePlugin.appendElement (eRoot.getNamespaceURI (), "groupId").appendText ("org.apache.maven.plugins");
      ePlugin.appendElement (eRoot.getNamespaceURI (), "artifactId").appendText ("maven-compiler-plugin");
      final IMicroElement eConfiguration = ePlugin.appendElement (eRoot.getNamespaceURI (), "configuration");
      eConfiguration.appendElement (eRoot.getNamespaceURI (), "source").appendText ("1.5");
      eConfiguration.appendElement (eRoot.getNamespaceURI (), "target").appendText ("1.5");
    }

    // Modify all dependencies
    MicroWalker.walkNode (aDoc, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aItem)
      {
        if (aItem.isElement ())
        {
          final IMicroElement eItem = (IMicroElement) aItem;
          if (eItem.getLocalName ().equals ("groupId"))
          {
            final String sGroupID = eItem.getTextContent ();
            if (sGroupID.equals ("com.phloc"))
            {
              final IMicroElement eArtifactId = ((IMicroElement) eItem.getParent ()).getFirstChildElement ("artifactId");
              final String sArtifactId = eArtifactId.getTextContent ();
              if (!"parent-pom".equals (sArtifactId))
              {
                eArtifactId.removeAllChildren ();
                eArtifactId.appendText (_getNewArtifactName (sArtifactId));
              }
            }
          }
        }
      }
    });

    // Write
    final XMLWriterSettings aXWS = new XMLWriterSettings ().setNamespaceContext (new MapBasedNamespaceContext ().addMapping ("xsi",
                                                                                                                             "http://www.w3.org/2001/XMLSchema-instance"))
                                                           .setPutNamespaceContextPrefixesInRoot (true);
    MicroWriter.writeToFile (aDoc, aDstFile, aXWS);

    s_aLogger.info ("Modified pom.xml");
  }

  private static void _updateEclipseProject (@Nonnull final File aSrcFile,
                                             @Nonnull final File aDstFile,
                                             @Nonnull final String sSrcComponentName,
                                             @Nonnull final String sDstComponentName)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    final IMicroElement eRoot = aDoc.getDocumentElement ();

    final IMicroElement eName = eRoot.getFirstChildElement ("name");
    if (!sSrcComponentName.equals (eName.getTextContent ()))
      throw new IllegalStateException ("Illegal name");
    eName.removeAllChildren ();
    eName.appendText (sDstComponentName);

    // Write
    MicroWriter.writeToFile (aDoc, aDstFile);

    s_aLogger.info ("Modified Eclipse project file");
  }

  private static void _updateEclipseClasspath (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    final IMicroElement eRoot = aDoc.getDocumentElement ();

    for (final IMicroElement eEntry : eRoot.getAllChildElements ("classpathentry"))
      if ("con".equals (eEntry.getAttribute ("kind")))
      {
        final String sPath = eEntry.getAttribute ("path");
        final int nIndex = sPath.indexOf ("JavaSE-");
        if (nIndex >= 0)
        {
          // Main modification
          eEntry.setAttribute ("path", sPath.replaceAll ("JavaSE-1\\.[0-9]", "J2SE-1.5"));
        }
      }

    // Write
    MicroWriter.writeToFile (aDoc, aDstFile);

    s_aLogger.info ("Modified Eclipse classpath file");
  }

  private static void _updateEclipsePrefs (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    String sContent = SimpleFileIO.readFileAsString (aSrcFile, CCharset.CHARSET_UTF_8_OBJ);
    sContent = sContent.replace ("=1.6", "=1.5");
    SimpleFileIO.writeFile (aDstFile, sContent, CCharset.CHARSET_UTF_8_OBJ);

    s_aLogger.info ("Modified Eclipse preferences file");
  }

  private static void _processJavaFile (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    boolean bSkipFile = false;
    boolean bModifiedFile = false;
    ETriState eJDK5Mode = ETriState.UNDEFINED;
    final List <String> aLines = SimpleFileIO.readFileLines (aSrcFile, CCharset.CHARSET_UTF_8_OBJ);
    for (int i = 0; i < aLines.size (); ++i)
    {
      final String sLine = aLines.get (i);

      String sTrimmedLine = sLine.trim ();
      if (sTrimmedLine.startsWith ("//"))
      {
        sTrimmedLine = sTrimmedLine.substring (2).trim ();
        if (sTrimmedLine.startsWith ("SKIPJDK5"))
        {
          // Skip the whole file - no need to continue parsing the file
          bSkipFile = true;
          break;
        }
        if (sTrimmedLine.startsWith ("IFJDK5"))
        {
          // The following lines are for JDK5
          if (eJDK5Mode.isDefined ())
            throw new IllegalStateException ("Cannot enter JDK5 mode!");
          eJDK5Mode = ETriState.TRUE;
        }
        else
          if (sTrimmedLine.startsWith ("ELSE"))
          {
            // Switch from JDK5 to default mode
            if (!eJDK5Mode.isTrue ())
              throw new IllegalStateException ("Not in JDK5 mode!");
            eJDK5Mode = ETriState.FALSE;
          }
          else
            if (sTrimmedLine.startsWith ("ENDIF"))
            {
              // We're back to regular mode which is valid for all versions
              // Note: this is different from "isTrue"!!!
              if (!eJDK5Mode.isFalse ())
                throw new IllegalStateException ("Not in default mode!");
              eJDK5Mode = ETriState.UNDEFINED;
            }
            else
              if (eJDK5Mode.isTrue ())
              {
                // Remove the comment from the line
                aLines.set (i, sLine.replace ("//", ""));
                bModifiedFile = true;
              }
      }
      else
        if (eJDK5Mode.isFalse ())
        {
          // Comment this line, as we're in JDK5 mode
          aLines.set (i, "//" + sLine);
          bModifiedFile = true;
        }
        else
        {
          // All the hacks start here
          if (sLine.equals ("import java.util.ServiceLoader;"))
            aLines.set (i, "// " + sLine);
        }
    }

    // The following should never happen!
    if (eJDK5Mode.isDefined ())
      throw new IllegalStateException ("JDK5 comments are inconsistent in file " + aSrcFile.getAbsolutePath ());

    if (bSkipFile)
      s_aLogger.info ("Skipping file " + aSrcFile.getName ());
    else
    {
      if (bModifiedFile)
        s_aLogger.info ("Modified file " + aSrcFile.getName ());

      final String sContent = StringHelper.getImploded (CGlobal.LINE_SEPARATOR, aLines) + CGlobal.LINE_SEPARATOR;
      SimpleFileIO.writeFile (aDstFile, sContent, CCharset.CHARSET_UTF_8_OBJ);
    }
  }

  public static void createJDK5Version (@Nonnull final File aSrcBaseDirParam) throws IOException
  {
    if (aSrcBaseDirParam == null)
      throw new NullPointerException ("Null directory");
    final File aSrcBaseDir = aSrcBaseDirParam.getAbsoluteFile ();
    if (!aSrcBaseDir.isDirectory ())
      throw new IllegalArgumentException ("Illegal base directory provided: " + aSrcBaseDir);

    final String sSrcComponentName = aSrcBaseDir.getName ();
    final String sDstComponentName = _getNewArtifactName (sSrcComponentName);

    final FileOperationManager aFOM = new FileOperationManager (new LoggingFileOperationCallback ());
    final File aDstBaseDir = new File (aSrcBaseDir, "../" + sDstComponentName).getCanonicalFile ();
    s_aLogger.info ("From " + aSrcBaseDir.toString ());
    s_aLogger.info ("To " + aDstBaseDir.toString ());
    final String sSrcBasePath = aSrcBaseDir.getAbsolutePath ();
    final FilenameFilter aFF = new FilenameFilterMatchNoRegEx ("target");
    for (final File aSrcFile : new FilterIterator <File> (new FileSystemRecursiveIterator (aSrcBaseDir, aFF),
                                                          new FileFilterToIFilterAdapter (aFF)))
      if (aSrcFile.isFile ())
      {
        final String sSrcName = aSrcFile.getName ();
        final String sSrcAbsPath = aSrcFile.getAbsolutePath ();
        final String sSrcRelPath = sSrcAbsPath.substring (sSrcBasePath.length () + 1);
        final File aDstFile = new File (aDstBaseDir, sSrcRelPath);

        if (sSrcName.equals ("pom.xml"))
          _updatePOM (aSrcFile, aDstFile, sSrcComponentName, sDstComponentName);
        else
          if (sSrcName.equals (".project"))
            _updateEclipseProject (aSrcFile, aDstFile, sSrcComponentName, sDstComponentName);
          else
            if (sSrcName.equals (".classpath"))
              _updateEclipseClasspath (aSrcFile, aDstFile);
            else
              if (sSrcName.equals ("org.eclipse.jdt.core.prefs"))
                _updateEclipsePrefs (aSrcFile, aDstFile);
              else
                if (sSrcName.endsWith (".java"))
                  _processJavaFile (aSrcFile, aDstFile);
                else
                  if (sSrcName.equals (sSrcComponentName + ".iml"))
                  {
                    // Skip the file - Idea project file
                  }
                  else
                  {
                    aFOM.deleteFileIfExisting (aDstFile);
                    aFOM.copyFile (aSrcFile, aDstFile);
                  }
      }

    // Add a simple text file as information!
    SimpleFileIO.writeFile (new File (aDstBaseDir, "readme-created.1st"),
                            "The content of this project is 100% automatically generated.\n"
                                + "Do not alter anything in here, as it will be overwritten next time the project is generated!!!",
                            CCharset.CHARSET_UTF_8_OBJ);
    s_aLogger.info ("Done creating JDK5 version of " + sSrcComponentName);
  }
}
