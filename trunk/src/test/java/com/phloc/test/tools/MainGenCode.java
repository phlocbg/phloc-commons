package com.phloc.test.tools;

import java.io.File;
import java.util.Locale;
import java.util.Set;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.file.filter.FilenameFilterEndsWith;
import com.phloc.commons.io.file.iterate.FileSystemIterator;
import com.phloc.commons.string.StringHelper;

public class MainGenCode
{
  private static String _get (final String s, final String sXXX, final String sYYY, final String sZZZ)
  {
    String ret = s;
    ret = StringHelper.replaceAll (ret, "XXX", sXXX);
    ret = StringHelper.replaceAll (ret, "YYY$UC$", sYYY.toUpperCase (Locale.US));
    ret = StringHelper.replaceAll (ret, "YYY", sYYY);
    ret = StringHelper.replaceAll (ret, "ZZZ", sZZZ);
    return ret;
  }

  public static void main (final String [] args)
  {
    final Set <String> aAvoidFilenames = ContainerHelper.newSet ();
    if (false)
      aAvoidFilenames.add ("BooleanCollections.java");

    for (final File aFile : FileSystemIterator.create (new File ("src/test/resources/srcgen"),
                                                       new FilenameFilterEndsWith (".jav")))
    {
      for (final String [] aPart : new String [] [] { { "boolean", "Boolean" },
                                                     { "byte", "Byte" },
                                                     { "char", "Character" },
                                                     { "double", "Double" },
                                                     { "float", "Float" },
                                                     { "int", "Integer" },
                                                     { "long", "Long" },
                                                     { "short", "Short" } })
      {
        final String sYYY = aPart[0];
        final String sXXX = aPart[1];
        final String sZZZ = sXXX.substring (0, sYYY.length ());

        // Target filename
        final String sTargetFilename = _get (aFile.getName (), sXXX, sYYY, sZZZ) + 'a';
        if (aAvoidFilenames.contains (sTargetFilename))
          continue;

        // Read File
        final String sContent = SimpleFileIO.readFileAsString (aFile, CCharset.CHARSET_UTF_8_OBJ);

        // Extract package
        final int nIndex = sContent.indexOf ("package ");
        final String sPackage = sContent.substring (nIndex + 8, sContent.indexOf (';', nIndex));
        final String sTargetFile = "src/main/java/" + sPackage.replace ('.', '/') + '/' + sTargetFilename;

        // Write file
        final String sRealContent = _get (sContent, sXXX, sYYY, sZZZ);
        SimpleFileIO.writeFile (new File (sTargetFile), sRealContent, CCharset.CHARSET_UTF_8_OBJ);
      }
    }
  }
}
