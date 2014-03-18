/**
 * Copyright (C) 2006-2014 phloc systems
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
