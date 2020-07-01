package com.phloc.commons.zip;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.string.StringHelper;

public class GZIPUtilsTest
{
  private static final String TEST = "An preost wes on leoden, Laȝamon was ihoten" +
                                     System.lineSeparator () +
                                     "He wes Leovenaðes sone -- liðe him be Drihten." +
                                     System.lineSeparator () +
                                     "He wonede at Ernleȝe at æðelen are chirechen," +
                                     System.lineSeparator () +
                                     "Uppen Sevarne staþe, sel þar him þuhte," +
                                     System.lineSeparator () +
                                     System.lineSeparator () +
                                     "Onfest Radestone, þer he bock radde.";

  private static final int MULTIPLIER = 100000;
  private static final String LARGE = StringHelper.getRepeated (TEST, MULTIPLIER);

  @Test
  public void testCompressDecompress () throws IOException
  {
    Assert.assertEquals (LARGE, GZIPUtils.decompress (GZIPUtils.compress (LARGE)));
  }

  @Test
  public void testCompress () throws IOException
  {
    final byte [] aCompressed = GZIPUtils.compress (LARGE);
    Assert.assertNotNull (aCompressed);
    Assert.assertTrue (aCompressed.length > (MULTIPLIER / 2));
    Assert.assertTrue (aCompressed.length < MULTIPLIER);
  }

}
