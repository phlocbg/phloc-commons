package com.phloc.commons.i18n;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;

import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorReadableByteChannel extends CodepointIteratorByteBuffer
{
  @Nonnull
  private static ByteBuffer _convert (@Nonnull @WillClose final ReadableByteChannel aChannel) throws IOException
  {
    try
    {
      final ByteBuffer buf = ByteBuffer.allocate (1024);
      final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
      final WritableByteChannel aOutChannel = Channels.newChannel (aBAOS);
      while (aChannel.read (buf) > 0)
      {
        buf.flip ();
        aOutChannel.write (buf);
      }
      return ByteBuffer.wrap (aBAOS.toByteArray ());
    }
    finally
    {
      StreamUtils.close (aChannel);
    }
  }

  public CodepointIteratorReadableByteChannel (@Nonnull @WillClose final ReadableByteChannel aChannel,
                                               @Nonnull final Charset aCharset) throws IOException
  {
    super (_convert (aChannel), aCharset);
  }
}
