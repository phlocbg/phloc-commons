package com.phloc.commons.zip;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Nonnull;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;

public class GZIPUtils
{
  private static final Charset CHARSET = CCharset.CHARSET_UTF_8_OBJ;

  public static byte [] compress (@Nonnull final String sUncompressed) throws IOException
  {
    NonBlockingByteArrayOutputStream aBAOS = null;
    GZIPOutputStream aGZIP = null;
    try
    {
      aBAOS = new NonBlockingByteArrayOutputStream ();
      aGZIP = new GZIPOutputStream (aBAOS);
      StreamUtils.writeStream (aGZIP, sUncompressed, CHARSET);
      final byte [] compressed = aBAOS.toByteArray ();
      aBAOS.close ();
      return compressed;
    }
    finally
    {
      StreamUtils.close (aGZIP);
      StreamUtils.close (aBAOS);
    }
  }

  public static String decompress (@Nonnull final byte [] aCompressed) throws IOException
  {
    NonBlockingByteArrayInputStream aBAIS = null;
    GZIPInputStream aGZIP = null;
    try
    {
      aBAIS = new NonBlockingByteArrayInputStream (aCompressed);
      aGZIP = new GZIPInputStream (aBAIS);
      return StringHelper.getImploded (System.lineSeparator (), StreamUtils.readStreamLines (aGZIP, CHARSET));
    }
    finally
    {
      StreamUtils.close (aGZIP);
      StreamUtils.close (aBAIS);
    }
  }

}
