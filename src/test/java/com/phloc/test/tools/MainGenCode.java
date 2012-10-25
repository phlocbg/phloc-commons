package com.phloc.test.tools;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.file.filter.FilenameFilterEndsWith;
import com.phloc.commons.io.file.iterate.FileSystemIterator;
import com.phloc.commons.string.StringHelper;

public class MainGenCode
{
  private static final String [][] DIRS = new String [] [] { { "gen/src", "src/main/java/" },
                                                            { "gen/test", "src/test/java/" } };

  private static final String [][] TYPES = new String [] [] { { "boolean", "Boolean" },
                                                             { "byte", "Byte" },
                                                             { "char", "Character" },
                                                             { "double", "Double" },
                                                             { "float", "Float" },
                                                             { "int", "Integer" },
                                                             { "long", "Long" },
                                                             { "short", "Short" } };

  private static final Map <String, String> DUMMIES = new HashMap <String, String> ()
  {
    {
      put ("boolean", "true");
      put ("byte", "(byte) 1");
      put ("char", "'c'");
      put ("double", "3.1415");
      put ("float", "47.11f");
      put ("int", "42");
      put ("long", "424242L");
      put ("short", "(short) 4712");
    }
  };

  private static final Set <String> CASTINT = new HashSet <String> ()
  {
    {
      add ("byte");
      add ("char");
      add ("short");
    }
  };

  private static String _get (final String s, final Map <String, String> aMacros)
  {
    String ret = s;
    for (final Map.Entry <String, String> e : aMacros.entrySet ())
      ret = StringHelper.replaceAll (ret, e.getKey (), e.getValue ());
    return ret;
  }

  public static void main (final String [] args)
  {
    final Set <String> aAvoidFilenames = ContainerHelper.newSet ();
    aAvoidFilenames.add ("TestBooleanCollections.java");
    aAvoidFilenames.add ("TestBooleanIterator.java");
    aAvoidFilenames.add ("TestBooleanListIterator.java");
    aAvoidFilenames.add ("TestBooleanListList.java");
    aAvoidFilenames.add ("TestCollectionBooleanCollection.java");

    for (final String [] aDirs : DIRS)
    {
      System.out.println (aDirs[0]);
      for (final File aFile : FileSystemIterator.create (new File ("src/test/resources/" + aDirs[0]),
                                                         new FilenameFilterEndsWith (".jav")))
      {
        System.out.println ("  " + aFile.getName ());
        for (final String [] aPart : TYPES)
        {
          final Map <String, String> aMacros = new HashMap <String, String> ();
          aMacros.put ("YYY", aPart[0]);
          aMacros.put ("YYY$UC$", aPart[0].toUpperCase (Locale.US));
          aMacros.put ("XXX", aPart[1]);
          aMacros.put ("ZZZ", aPart[1].substring (0, aPart[0].length ()));
          aMacros.put ("$DUMMY$", DUMMIES.get (aPart[0]));
          aMacros.put ("$CASTINT$", CASTINT.contains (aPart[0]) ? "(" + aPart[0] + ")" : "");

          // Target filename
          final String sTargetFilename = _get (aFile.getName (), aMacros) + 'a';
          if (aAvoidFilenames.contains (sTargetFilename))
            continue;

          // Read File
          final String sContent = SimpleFileIO.readFileAsString (aFile, CCharset.CHARSET_UTF_8_OBJ);

          // Extract package
          final int nIndex = sContent.indexOf ("package ");
          final String sPackage = sContent.substring (nIndex + 8, sContent.indexOf (';', nIndex));
          final String sTargetFile = aDirs[1] + sPackage.replace ('.', '/') + '/' + sTargetFilename;

          // Write file
          final String sRealContent = _get (sContent, aMacros);
          SimpleFileIO.writeFile (new File (sTargetFile), sRealContent, CCharset.CHARSET_UTF_8_OBJ);
        }
      }
    }
  }
}
