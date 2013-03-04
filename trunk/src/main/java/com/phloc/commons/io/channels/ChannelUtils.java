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
package com.phloc.commons.io.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Some very basic NIO channel utility stuff.
 * 
 * @author philip
 */
@Immutable
public final class ChannelUtils
{
  // Use version 1 as it seems to be faster
  private static final boolean USE_COPY_V1 = true;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ChannelUtils s_aInstance = new ChannelUtils ();

  private ChannelUtils ()
  {}

  /**
   * Copy all content from the source channel to the destination channel.
   * 
   * @param aSrc
   *        Source channel. May not be <code>null</code>. Is not closed after
   *        the operation.
   * @param aDest
   *        Destination channel. May not be <code>null</code>. Is not closed
   *        after the operation.
   */
  public static void channelCopy (@Nonnull @WillNotClose final ReadableByteChannel aSrc,
                                  @Nonnull @WillNotClose final WritableByteChannel aDest) throws IOException
  {
    if (aSrc == null)
      throw new NullPointerException ("sourceChannel");
    if (!aSrc.isOpen ())
      throw new IllegalArgumentException ("sourceChannel is not open!");
    if (aDest == null)
      throw new NullPointerException ("desitnationChannel");
    if (!aDest.isOpen ())
      throw new IllegalArgumentException ("desitnationChannel is not open!");

    if (USE_COPY_V1)
      _channelCopy1 (aSrc, aDest);
    else
      _channelCopy2 (aSrc, aDest);
  }

  /**
   * Channel copy method 1. This method copies data from the src channel and
   * writes it to the dest channel until EOF on src. This implementation makes
   * use of compact( ) on the temp buffer to pack down the data if the buffer
   * wasn't fully drained. This may result in data copying, but minimizes system
   * calls. It also requires a cleanup loop to make sure all the data gets sent.<br>
   * Source: Java NIO, page 60
   * 
   * @param aSrc
   *        Source channel. May not be <code>null</code>. Is not closed after
   *        the operation.
   * @param aDest
   *        Destination channel. May not be <code>null</code>. Is not closed
   *        after the operation.
   */
  private static void _channelCopy1 (@Nonnull @WillNotClose final ReadableByteChannel aSrc,
                                     @Nonnull @WillNotClose final WritableByteChannel aDest) throws IOException
  {
    final ByteBuffer aBuffer = ByteBuffer.allocateDirect (16 * 1024);
    while (aSrc.read (aBuffer) != -1)
    {
      // Prepare the buffer to be drained
      aBuffer.flip ();

      // Write to the channel; may block
      aDest.write (aBuffer);

      // If partial transfer, shift remainder down
      // If buffer is empty, same as doing clear()
      aBuffer.compact ();
    }

    // EOF will leave buffer in fill state
    aBuffer.flip ();

    // Make sure that the buffer is fully drained
    while (aBuffer.hasRemaining ())
      aDest.write (aBuffer);
  }

  /**
   * Channel copy method 2. This method performs the same copy, but assures the
   * temp buffer is empty before reading more data. This never requires data
   * copying but may result in more systems calls. No post-loop cleanup is
   * needed because the buffer will be empty when the loop is exited.<br>
   * Source: Java NIO, page 60
   * 
   * @param aSrc
   *        Source channel. May not be <code>null</code>. Is not closed after
   *        the operation.
   * @param aDest
   *        Destination channel. May not be <code>null</code>. Is not closed
   *        after the operation.
   */
  private static void _channelCopy2 (@Nonnull @WillNotClose final ReadableByteChannel aSrc,
                                     @Nonnull @WillNotClose final WritableByteChannel aDest) throws IOException
  {
    final ByteBuffer aBuffer = ByteBuffer.allocateDirect (16 * 1024);
    while (aSrc.read (aBuffer) != -1)
    {
      // Prepare the buffer to be drained
      aBuffer.flip ();

      // Make sure that the buffer was fully drained
      while (aBuffer.hasRemaining ())
        aDest.write (aBuffer);

      // Make the buffer empty, ready for filling
      aBuffer.clear ();
    }
  }
}
