/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.io.streams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.mock.IMockException;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerSize;
import com.phloc.commons.stats.StatisticsManager;

/**
 * Some very basic IO stream utility stuff. All input stream (=reading) related
 * stuff is quite <code>null</code> aware, where on writing an output stream may
 * never be null.
 * 
 * @author Philip Helger
 * @author Boris Gregorcic
 */
@Immutable
public final class StreamUtils
{
  /** buffer size for copy operations */
  private static final int DEFAULT_BUFSIZE = 16 * CGlobal.BYTES_PER_KILOBYTE;

  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (StreamUtils.class);

  private static final IStatisticsHandlerSize s_aByteSizeHdl = StatisticsManager.getSizeHandler (StreamUtils.class.getName () +
                                                                                                 "$COPY"); //$NON-NLS-1$
  private static final IStatisticsHandlerSize s_aCharSizeHdl = StatisticsManager.getSizeHandler (StreamUtils.class.getName () +
                                                                                                 "$COPYCHARS"); //$NON-NLS-1$

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final StreamUtils s_aInstance = new StreamUtils ();

  private StreamUtils ()
  {}

  /**
   * Check if the passed exception is a known EOF exception.
   * 
   * @param t
   *        The throwable/exception to be checked. May be <code>null</code>.
   * @return <code>true</code> if it is a user-created EOF exception
   */
  public static boolean isKnownEOFException (@Nullable final Throwable t)
  {
    return t != null && isKnownEOFException (t.getClass ());
  }

  /**
   * Check if the passed class is a known EOF exception class.
   * 
   * @param aClass
   *        The class to be checked. May be <code>null</code>.
   * @return <code>true</code> if it is a known EOF exception class.
   */
  public static boolean isKnownEOFException (@Nullable final Class <?> aClass)
  {
    if (aClass == null)
      return false;

    final String sClass = aClass.getName ();
    return sClass.equals ("java.io.EOFException") || //$NON-NLS-1$
           sClass.equals ("org.mortbay.jetty.EofException") || //$NON-NLS-1$
           sClass.equals ("org.eclipse.jetty.io.EofException") || //$NON-NLS-1$
           sClass.equals ("org.apache.catalina.connector.ClientAbortException"); //$NON-NLS-1$
  }

  /**
   * Close the passed object, without trying to call flush on it.
   * 
   * @param aCloseable
   *        The object to be closed. May be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if the object was successfully closed.
   */
  @Nonnull
  public static ESuccess closeWithoutFlush (@Nullable @WillClose final Closeable aCloseable)
  {
    if (aCloseable != null)
    {
      try
      {
        // close stream
        aCloseable.close ();
        return ESuccess.SUCCESS;
      }
      catch (final IOException ex)
      {
        if (!isKnownEOFException (ex))
          s_aLogger.error ("Failed to close stream " + aCloseable.getClass ().getName (), //$NON-NLS-1$
                           ex instanceof IMockException ? null : ex);
      }
    }
    return ESuccess.FAILURE;
  }

  /**
   * Close the passed stream by encapsulating the declared {@link IOException}.
   * If the passed object also implements the {@link Flushable} interface, it is
   * tried to be flushed before it is closed.
   * 
   * @param aCloseable
   *        The object to be closed. May be <code>null</code>.
   * @return {@link ESuccess} if the object was successfully closed.
   */
  @Nonnull
  public static ESuccess close (@Nullable @WillClose final Closeable aCloseable)
  {
    if (aCloseable != null)
    {
      try
      {
        // flush object (if available)
        if (aCloseable instanceof Flushable)
          flush ((Flushable) aCloseable);

        // close object
        aCloseable.close ();
        return ESuccess.SUCCESS;
      }
      catch (final NullPointerException ex)
      {
        // Happens if a java.io.FilterInputStream or java.io.FilterOutputStream
        // has no underlying stream!
      }
      catch (final IOException ex)
      {
        if (!isKnownEOFException (ex))
          s_aLogger.error ("Failed to close object " + aCloseable.getClass ().getName (), //$NON-NLS-1$
                           ex instanceof IMockException ? null : ex);
      }
    }

    return ESuccess.FAILURE;
  }

  /**
   * Special close version for {@link Socket} as they are not implementing
   * {@link Closeable} :(
   * 
   * @param aSocket
   *        The socket to be closed. May be <code>null</code>.
   * @return {@link ESuccess} if the object was successfully closed.
   */
  @Nonnull
  public static ESuccess close (@Nullable @WillClose final Socket aSocket)
  {
    if (aSocket != null && !aSocket.isClosed ())
    {
      try
      {
        // close object
        aSocket.close ();
        return ESuccess.SUCCESS;
      }
      catch (final IOException ex)
      {
        if (!isKnownEOFException (ex))
          s_aLogger.error ("Failed to close socket " + aSocket.getClass ().getName (), //$NON-NLS-1$
                           ex instanceof IMockException ? null : ex);
      }
    }
    return ESuccess.FAILURE;
  }

  /**
   * Special close version for {@link ServerSocket} as they are not implementing
   * {@link Closeable} :(
   * 
   * @param aSocket
   *        The socket to be closed. May be <code>null</code>.
   * @return {@link ESuccess} if the object was successfully closed.
   */
  @Nonnull
  public static ESuccess close (@Nullable @WillClose final ServerSocket aSocket)
  {
    if (aSocket != null && !aSocket.isClosed ())
    {
      try
      {
        // close object
        aSocket.close ();
        return ESuccess.SUCCESS;
      }
      catch (final IOException ex)
      {
        if (!isKnownEOFException (ex))
          s_aLogger.error ("Failed to close server socket " + aSocket.getClass ().getName (), //$NON-NLS-1$
                           ex instanceof IMockException ? null : ex);
      }
    }
    return ESuccess.FAILURE;
  }

  /**
   * Flush the passed object encapsulating the declared {@link IOException}.
   * 
   * @param aFlushable
   *        The flushable to be flushed. May be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if the object was successfully flushed.
   */
  @Nonnull
  public static ESuccess flush (@Nullable final Flushable aFlushable)
  {
    if (aFlushable != null)
      try
      {
        aFlushable.flush ();
        return ESuccess.SUCCESS;
      }
      catch (final NullPointerException ex)
      {
        // Happens if a java.io.FilterOutputStream is already closed!
      }
      catch (final IOException ex)
      {
        if (!isKnownEOFException (ex))
          s_aLogger.error ("Failed to flush object " + aFlushable.getClass ().getName (), //$NON-NLS-1$
                           ex instanceof IMockException ? null : ex);
      }
    return ESuccess.FAILURE;
  }

  /**
   * Pass the content of the given input stream to the given output stream. Both
   * the input stream and the output stream are automatically closed.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>.
   *        Automatically closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStreamAndCloseOS (@WillClose @Nullable final InputStream aIS,
                                                                  @WillClose @Nullable final OutputStream aOS)
  {
    try
    {
      return copyInputStreamToOutputStream (aIS, aOS, new byte [DEFAULT_BUFSIZE], (MutableLong) null, (Long) null);
    }
    finally
    {
      close (aOS);
    }
  }

  /**
   * Pass the content of the given input stream to the given output stream. Both
   * the input stream and the output stream are automatically closed.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>.
   *        Automatically closed!
   * @param nLimit
   *        The maximum number of bytes to be copied to the output stream. Must
   *        be &ge; 0.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStreamWithLimitAndCloseOS (@WillClose @Nullable final InputStream aIS,
                                                                           @WillClose @Nullable final OutputStream aOS,
                                                                           @Nonnegative final long nLimit)
  {
    try
    {
      return copyInputStreamToOutputStream (aIS,
                                            aOS,
                                            new byte [DEFAULT_BUFSIZE],
                                            (MutableLong) null,
                                            Long.valueOf (nLimit));
    }
    finally
    {
      close (aOS);
    }
  }

  /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStream (@WillClose @Nullable final InputStream aIS,
                                                        @WillNotClose @Nullable final OutputStream aOS)
  {
    return copyInputStreamToOutputStream (aIS, aOS, new byte [DEFAULT_BUFSIZE], (MutableLong) null, (Long) null);
  }

  /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aCopyByteCount
   *        An optional mutable long object that will receive the total number
   *        of copied bytes. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStream (@WillClose @Nullable final InputStream aIS,
                                                        @WillNotClose @Nullable final OutputStream aOS,
                                                        @Nullable final MutableLong aCopyByteCount)
  {
    return copyInputStreamToOutputStream (aIS, aOS, new byte [DEFAULT_BUFSIZE], aCopyByteCount, (Long) null);
  }

  /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param nLimit
   *        The maximum number of bytes to be copied to the output stream. Must
   *        be &ge; 0.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStreamWithLimit (@WillClose @Nullable final InputStream aIS,
                                                                 @WillNotClose @Nullable final OutputStream aOS,
                                                                 @Nonnegative final long nLimit)
  {
    return copyInputStreamToOutputStream (aIS,
                                          aOS,
                                          new byte [DEFAULT_BUFSIZE],
                                          (MutableLong) null,
                                          Long.valueOf (nLimit));
  }

  /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStream (@WillClose @Nullable final InputStream aIS,
                                                        @WillNotClose @Nullable final OutputStream aOS,
                                                        @Nonnull final byte [] aBuffer)
  {
    return copyInputStreamToOutputStream (aIS, aOS, aBuffer, (MutableLong) null, (Long) null);
  }

  @Nonnegative
  private static long _copyInputStreamToOutputStream (@Nonnull @WillNotClose final InputStream aIS,
                                                      @Nonnull @WillNotClose final OutputStream aOS,
                                                      @Nonnull @WillNotClose final byte [] aBuffer) throws IOException
  {
    long nTotalBytesWritten = 0;
    int nBytesRead;
    while ((nBytesRead = aIS.read (aBuffer, 0, aBuffer.length)) > -1)
    {
      aOS.write (aBuffer, 0, nBytesRead);
      nTotalBytesWritten += nBytesRead;
    }
    return nTotalBytesWritten;
  }

  @Nonnegative
  private static long _copyInputStreamToOutputStreamWithLimit (@Nonnull @WillNotClose final InputStream aIS,
                                                               @Nonnull @WillNotClose final OutputStream aOS,
                                                               @Nonnull final byte [] aBuffer,
                                                               @Nonnegative final long nLimit) throws IOException
  {
    long nRest = nLimit;
    long nTotalBytesWritten = 0;
    while (true)
    {
      // if nRest is smaller than aBuffer.length, which is an int, it is safe to
      // cast nRest also to an int!
      final int nBytesToRead = nRest >= aBuffer.length ? aBuffer.length : (int) nRest;
      if (nBytesToRead == 0)
        break;
      final int nBytesRead = aIS.read (aBuffer, 0, nBytesToRead);
      if (nBytesRead == -1)
      {
        // EOF
        break;
      }
      if (nBytesRead > 0)
      {
        // At least one byte read
        aOS.write (aBuffer, 0, nBytesRead);
        nTotalBytesWritten += nBytesRead;
        nRest -= nBytesRead;
      }
    }
    return nTotalBytesWritten;
  }

  /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @param aCopyByteCount
   *        An optional mutable long object that will receive the total number
   *        of copied bytes. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStream (@WillClose @Nullable final InputStream aIS,
                                                        @WillNotClose @Nullable final OutputStream aOS,
                                                        @Nonnull @Nonempty final byte [] aBuffer,
                                                        @Nullable final MutableLong aCopyByteCount)
  {
    return copyInputStreamToOutputStream (aIS, aOS, aBuffer, aCopyByteCount, (Long) null);
  }

  /**
   * Pass the content of the given input stream to the given output stream. The
   * input stream is automatically closed, whereas the output stream stays open!
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   *        Automatically closed!
   * @param aOS
   *        The output stream to write to. May be <code>null</code>. Not
   *        automatically closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @param aCopyByteCount
   *        An optional mutable long object that will receive the total number
   *        of copied bytes. Note: and optional old value is overwritten!
   * @param aLimit
   *        An optional maximum number of bytes to copied from the input stream
   *        to the output stream. May be <code>null</code> to indicate no limit,
   *        meaning all bytes are copied.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyInputStreamToOutputStream (@WillClose @Nullable final InputStream aIS,
                                                        @WillNotClose @Nullable final OutputStream aOS,
                                                        @Nonnull @Nonempty final byte [] aBuffer,
                                                        @Nullable final MutableLong aCopyByteCount,
                                                        @Nullable final Long aLimit)
  {
    ValueEnforcer.notEmpty (aBuffer, "Buffer"); //$NON-NLS-1$
    if (aLimit != null && aLimit.longValue () < 0)
      throw new IllegalArgumentException ("Limit may not be negative!"); //$NON-NLS-1$

    try
    {
      if (aIS != null && aOS != null)
      {
        // both streams are not null
        long nTotalBytesCopied;
        if (aLimit == null)
          nTotalBytesCopied = _copyInputStreamToOutputStream (aIS, aOS, aBuffer);
        else
          nTotalBytesCopied = _copyInputStreamToOutputStreamWithLimit (aIS, aOS, aBuffer, aLimit.longValue ());

        // Add to statistics
        s_aByteSizeHdl.addSize (nTotalBytesCopied);

        // Remember copied bytes?
        if (aCopyByteCount != null)
          aCopyByteCount.set (nTotalBytesCopied);
        return ESuccess.SUCCESS;
      }
    }
    catch (final IOException ex)
    {
      if (!isKnownEOFException (ex))
        s_aLogger.error ("Failed to copy from stream to stream", ex instanceof IMockException ? null : ex); //$NON-NLS-1$
    }
    finally
    {
      // Ensure input stream is closed, even if output stream is null
      close (aIS);
    }
    return ESuccess.FAILURE;
  }

  /**
   * Get the number of available bytes in the passed input stream.
   * 
   * @param aIS
   *        The input stream to use. May be <code>null</code>.
   * @return 0 in case of an error or if the parameter was <code>null</code>.
   */
  public static int getAvailable (@Nullable final InputStream aIS)
  {
    if (aIS != null)
      try
      {
        return aIS.available ();
      }
      catch (final IOException ex)
      {
        // Fall through
      }
    return 0;
  }

  /**
   * Get a byte buffer with all the available content of the passed input
   * stream.
   * 
   * @param aIS
   *        The source input stream. May not be <code>null</code>.
   * @return A new {@link NonBlockingByteArrayOutputStream} with all available
   *         content inside.
   */
  @Nonnull
  public static NonBlockingByteArrayOutputStream getCopy (@Nonnull @WillClose final InputStream aIS)
  {
    final int nAvailable = Math.max (DEFAULT_BUFSIZE, getAvailable (aIS));
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream (nAvailable);
    copyInputStreamToOutputStreamAndCloseOS (aIS, aBAOS);
    return aBAOS;
  }

  /**
   * Get a byte buffer with all the available content of the passed input
   * stream.
   * 
   * @param aIS
   *        The source input stream. May not be <code>null</code>.
   * @param nLimit
   *        The maximum number of bytes to be copied to the output stream. Must
   *        be &ge; 0.
   * @return A new {@link NonBlockingByteArrayOutputStream} with all available
   *         content inside.
   */
  @Nonnull
  public static NonBlockingByteArrayOutputStream getCopyWithLimit (@Nonnull @WillClose final InputStream aIS,
                                                                   @Nonnegative final long nLimit)
  {
    final int nAvailable = Math.max (DEFAULT_BUFSIZE, getAvailable (aIS));
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream (nAvailable);
    copyInputStreamToOutputStreamWithLimitAndCloseOS (aIS, aBAOS, nLimit);
    return aBAOS;
  }

  /**
   * Read all bytes from the passed input stream into a byte array.
   * 
   * @param aISP
   *        The input stream provider to read from. May be <code>null</code> .
   * @return The byte array or <code>null</code> if the parameter or the
   *         resolved input stream is <code>null</code>.
   */
  @Nullable
  public static byte [] getAllBytes (@Nullable final IInputStreamProvider aISP)
  {
    if (aISP == null)
      return null;

    return getAllBytes (aISP.getInputStream ());
  }

  /**
   * Read all bytes from the passed input stream into a byte array.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @return The byte array or <code>null</code> if the input stream is
   *         <code>null</code>.
   */
  @Nullable
  public static byte [] getAllBytes (@Nullable @WillClose final InputStream aIS)
  {
    if (aIS == null)
      return null;

    return getCopy (aIS).toByteArray ();
  }

  /**
   * Read all bytes from the passed input stream into a string.
   * 
   * @param aISP
   *        The input stream provider to read from. May be <code>null</code> .
   * @param sCharset
   *        The charset to use. May not be <code>null</code> .
   * @return The String or <code>null</code> if the parameter or the resolved
   *         input stream is <code>null</code>.
   */
  @Nullable
  @Deprecated
  public static String getAllBytesAsString (@Nullable final IInputStreamProvider aISP,
                                            @Nonnull @Nonempty final String sCharset)
  {
    if (aISP == null)
      return null;

    return getAllBytesAsString (aISP.getInputStream (), sCharset);
  }

  /**
   * Read all bytes from the passed input stream into a string.
   * 
   * @param aISP
   *        The input stream provider to read from. May be <code>null</code> .
   * @param aCharset
   *        The charset to use. May not be <code>null</code> .
   * @return The String or <code>null</code> if the parameter or the resolved
   *         input stream is <code>null</code>.
   */
  @Nullable
  public static String getAllBytesAsString (@Nullable final IInputStreamProvider aISP,
                                            @Nonnull @Nonempty final Charset aCharset)
  {
    if (aISP == null)
      return null;

    return getAllBytesAsString (aISP.getInputStream (), aCharset);
  }

  /**
   * Read all bytes from the passed input stream into a string.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The charset to use. May not be <code>null</code> .
   * @return The String or <code>null</code> if the input stream is
   *         <code>null</code>.
   */
  @Nullable
  @Deprecated
  public static String getAllBytesAsString (@Nullable @WillClose final InputStream aIS,
                                            @Nonnull @Nonempty final String sCharset)
  {
    ValueEnforcer.notEmpty (sCharset, "Charset"); //$NON-NLS-1$

    if (aIS == null)
      return null;

    return getCopy (aIS).getAsString (sCharset);
  }

  /**
   * Read all bytes from the passed input stream into a string.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param aCharset
   *        The charset to use. May not be <code>null</code> .
   * @return The String or <code>null</code> if the input stream is
   *         <code>null</code>.
   */
  @Nullable
  public static String getAllBytesAsString (@Nullable @WillClose final InputStream aIS,
                                            @Nonnull @Nonempty final Charset aCharset)
  {
    ValueEnforcer.notNull (aCharset, "Charset"); //$NON-NLS-1$

    if (aIS == null)
      return null;

    return getCopy (aIS).getAsString (aCharset);
  }

  /**
   * Pass the content of the given reader to the given writer. The reader and
   * the writer are automatically closed!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Automatically
   *        closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriterAndCloseWriter (@WillClose @Nullable final Reader aReader,
                                                           @WillClose @Nullable final Writer aWriter)
  {
    try
    {
      return copyReaderToWriter (aReader, aWriter, new char [DEFAULT_BUFSIZE], (MutableLong) null, (Long) null);
    }
    finally
    {
      close (aWriter);
    }
  }

  /**
   * Pass the content of the given reader to the given writer. The reader and
   * the writer are automatically closed!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Automatically
   *        closed!
   * @param nLimit
   *        The maximum number of chars to be copied to the writer. Must be &ge;
   *        0.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriterWithLimitAndCloseWriter (@WillClose @Nullable final Reader aReader,
                                                                    @WillClose @Nullable final Writer aWriter,
                                                                    @Nonnegative final long nLimit)
  {
    try
    {
      return copyReaderToWriter (aReader,
                                 aWriter,
                                 new char [DEFAULT_BUFSIZE],
                                 (MutableLong) null,
                                 Long.valueOf (nLimit));
    }
    finally
    {
      close (aWriter);
    }
  }

  /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriter (@WillClose @Nullable final Reader aReader,
                                             @WillNotClose @Nullable final Writer aWriter)
  {
    return copyReaderToWriter (aReader, aWriter, new char [DEFAULT_BUFSIZE], (MutableLong) null, (Long) null);
  }

  /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aCopyCharCount
   *        An optional mutable long object that will receive the total number
   *        of copied characters. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriter (@WillClose @Nullable final Reader aReader,
                                             @WillNotClose @Nullable final Writer aWriter,
                                             @Nullable final MutableLong aCopyCharCount)
  {
    return copyReaderToWriter (aReader, aWriter, new char [DEFAULT_BUFSIZE], aCopyCharCount, (Long) null);
  }

  /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriter (@WillClose @Nullable final Reader aReader,
                                             @WillNotClose @Nullable final Writer aWriter,
                                             @Nonnull final char [] aBuffer)
  {
    return copyReaderToWriter (aReader, aWriter, aBuffer, (MutableLong) null, (Long) null);
  }

  /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param nLimit
   *        The maximum number of chars to be copied to the writer. Must be &ge;
   *        0.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriterWithLimit (@WillClose @Nullable final Reader aReader,
                                                      @WillNotClose @Nullable final Writer aWriter,
                                                      final long nLimit)
  {
    return copyReaderToWriter (aReader, aWriter, new char [DEFAULT_BUFSIZE], (MutableLong) null, Long.valueOf (nLimit));
  }

  @Nonnegative
  private static long _copyReaderToWriter (@Nonnull final Reader aReader,
                                           @Nonnull final Writer aWriter,
                                           @Nonnull final char [] aBuffer) throws IOException
  {
    long nTotalCharsWritten = 0;
    int nCharsRead;
    while ((nCharsRead = aReader.read (aBuffer, 0, aBuffer.length)) > -1)
    {
      aWriter.write (aBuffer, 0, nCharsRead);
      nTotalCharsWritten += nCharsRead;
    }
    return nTotalCharsWritten;
  }

  @Nonnegative
  private static long _copyReaderToWriterWithLimit (@Nonnull final Reader aReader,
                                                    @Nonnull final Writer aWriter,
                                                    @Nonnull final char [] aBuffer,
                                                    @Nonnegative final long nLimit) throws IOException
  {
    long nRest = nLimit;
    long nTotalCharsWritten = 0;
    while (true)
    {
      // if nRest is smaller than aBuffer.length, which is an int, it is safe to
      // cast nRest also to an int!
      final int nCharsToRead = nRest >= aBuffer.length ? aBuffer.length : (int) nRest;
      if (nCharsToRead == 0)
        break;
      final int nCharsRead = aReader.read (aBuffer, 0, nCharsToRead);
      if (nCharsRead == -1)
      {
        // EOF
        break;
      }
      if (nCharsRead > 0)
      {
        // At least one byte read
        aWriter.write (aBuffer, 0, nCharsRead);
        nTotalCharsWritten += nCharsRead;
        nRest -= nCharsRead;
      }
    }
    return nTotalCharsWritten;
  }

  /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @param aCopyCharCount
   *        An optional mutable long object that will receive the total number
   *        of copied characters. Note: and optional old value is overwritten!
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriter (@WillClose @Nullable final Reader aReader,
                                             @WillNotClose @Nullable final Writer aWriter,
                                             @Nonnull @Nonempty final char [] aBuffer,
                                             @Nullable final MutableLong aCopyCharCount)
  {
    return copyReaderToWriter (aReader, aWriter, aBuffer, aCopyCharCount, (Long) null);
  }

  /**
   * Pass the content of the given reader to the given writer. The reader is
   * automatically closed, whereas the writer stays open!
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>. Automatically
   *        closed!
   * @param aWriter
   *        The writer to write to. May be <code>null</code>. Not automatically
   *        closed!
   * @param aBuffer
   *        The buffer to use. May not be <code>null</code>.
   * @param aCopyCharCount
   *        An optional mutable long object that will receive the total number
   *        of copied characters. Note: and optional old value is overwritten!
   * @param aLimit
   *        An optional maximum number of chars to copied from the reader to the
   *        writer. May be <code>null</code> to indicate no limit, meaning all
   *        chars are copied.
   * @return <code>{@link ESuccess#SUCCESS}</code> if copying took place,
   *         <code>{@link ESuccess#FAILURE}</code> otherwise
   */
  @Nonnull
  public static ESuccess copyReaderToWriter (@WillClose @Nullable final Reader aReader,
                                             @WillNotClose @Nullable final Writer aWriter,
                                             @Nonnull @Nonempty final char [] aBuffer,
                                             @Nullable final MutableLong aCopyCharCount,
                                             @Nullable final Long aLimit)
  {
    ValueEnforcer.notEmpty (aBuffer, "Buffer"); //$NON-NLS-1$
    if (aLimit != null && aLimit.longValue () < 0)
      throw new IllegalArgumentException ("Limit may not be negative!"); //$NON-NLS-1$

    try
    {
      if (aReader != null && aWriter != null)
      {
        // both streams are not null
        final long nTotalCharsCopied = aLimit == null ? _copyReaderToWriter (aReader, aWriter, aBuffer)
                                                      : _copyReaderToWriterWithLimit (aReader,
                                                                                      aWriter,
                                                                                      aBuffer,
                                                                                      aLimit.longValue ());

        // Add to statistics
        s_aCharSizeHdl.addSize (nTotalCharsCopied);

        // Remember number of copied characters
        if (aCopyCharCount != null)
          aCopyCharCount.set (nTotalCharsCopied);
        return ESuccess.SUCCESS;
      }
    }
    catch (final IOException ex)
    {
      if (!isKnownEOFException (ex))
        s_aLogger.error ("Failed to copy from reader to writer", ex instanceof IMockException ? null : ex); //$NON-NLS-1$
    }
    finally
    {
      // Ensure reader is closed, even if writer is null
      close (aReader);
    }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public static NonBlockingStringWriter getCopy (@Nonnull @WillClose final Reader aReader)
  {
    final NonBlockingStringWriter aWriter = new NonBlockingStringWriter (DEFAULT_BUFSIZE);
    copyReaderToWriterAndCloseWriter (aReader, aWriter);
    return aWriter;
  }

  @Nonnull
  public static NonBlockingStringWriter getCopyWithLimit (@Nonnull @WillClose final Reader aReader,
                                                          @Nonnegative final long nLimit)
  {
    final NonBlockingStringWriter aWriter = new NonBlockingStringWriter (DEFAULT_BUFSIZE);
    copyReaderToWriterWithLimitAndCloseWriter (aReader, aWriter, nLimit);
    return aWriter;
  }

  /**
   * Read all characters from the passed reader into a char array.
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>.
   * @return The character array or <code>null</code> if the reader is
   *         <code>null</code>.
   */
  @Nullable
  public static char [] getAllCharacters (@Nullable @WillClose final Reader aReader)
  {
    if (aReader == null)
      return null;

    return getCopy (aReader).getAsCharArray ();
  }

  /**
   * Read all characters from the passed reader into a String.
   * 
   * @param aReader
   *        The reader to read from. May be <code>null</code>.
   * @return The character array or <code>null</code> if the reader is
   *         <code>null</code>.
   */
  @Nullable
  public static String getAllCharactersAsString (@Nullable @WillClose final Reader aReader)
  {
    if (aReader == null)
      return null;

    return getCopy (aReader).getAsString ();
  }

  /**
   * Get the content of the passed Spring resource as one big string in the
   * passed character set.
   * 
   * @param aISP
   *        The resource to read. May not be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the resolved input stream is <code>null</code>
   *         , the content otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  @Deprecated
  public static List <String> readStreamLines (@Nullable final IInputStreamProvider aISP,
                                               @Nonnull @Nonempty final String sCharset)
  {
    return readStreamLines (aISP, sCharset, 0, CGlobal.ILLEGAL_UINT);
  }

  /**
   * Get the content of the passed Spring resource as one big string in the
   * passed character set.
   * 
   * @param aISP
   *        The resource to read. May not be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the resolved input stream is <code>null</code>
   *         , the content otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  public static List <String> readStreamLines (@Nullable final IInputStreamProvider aISP,
                                               @Nonnull final Charset aCharset)
  {
    return readStreamLines (aISP, aCharset, 0, CGlobal.ILLEGAL_UINT);
  }

  /**
   * Get the content of the passed Spring resource as one big string in the
   * passed character set.
   * 
   * @param aISP
   *        The resource to read. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @return <code>null</code> if the resolved input stream is <code>null</code>
   *         , the content otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  @Deprecated
  public static List <String> readStreamLines (@Nullable final IInputStreamProvider aISP,
                                               @Nonnull @Nonempty final String sCharset,
                                               @Nonnegative final int nLinesToSkip,
                                               final int nLinesToRead)
  {
    if (aISP == null)
      return null;

    return readStreamLines (aISP.getInputStream (), sCharset, nLinesToSkip, nLinesToRead);
  }

  /**
   * Get the content of the passed Spring resource as one big string in the
   * passed character set.
   * 
   * @param aISP
   *        The resource to read. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @return <code>null</code> if the resolved input stream is <code>null</code>
   *         , the content otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  public static List <String> readStreamLines (@Nullable final IInputStreamProvider aISP,
                                               @Nonnull final Charset aCharset,
                                               @Nonnegative final int nLinesToSkip,
                                               final int nLinesToRead)
  {
    if (aISP == null)
      return null;

    return readStreamLines (aISP.getInputStream (), aCharset, nLinesToSkip, nLinesToRead);
  }

  /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the input stream is <code>null</code>, the
   *         content lines otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  @Deprecated
  public static List <String> readStreamLines (@WillClose @Nullable final InputStream aIS,
                                               @Nonnull @Nonempty final String sCharset)
  {
    return readStreamLines (aIS, sCharset, 0, CGlobal.ILLEGAL_UINT);
  }

  /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @return <code>null</code> if the input stream is <code>null</code>, the
   *         content lines otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  public static List <String> readStreamLines (@WillClose @Nullable final InputStream aIS,
                                               @Nonnull @Nonempty final Charset aCharset)
  {
    return readStreamLines (aIS, aCharset, 0, CGlobal.ILLEGAL_UINT);
  }

  /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aTargetList
   *        The list to be filled with the lines. May not be <code>null</code>.
   */
  @Deprecated
  public static void readStreamLines (@WillClose @Nullable final InputStream aIS,
                                      @Nonnull @Nonempty final String sCharset,
                                      @Nonnull final List <String> aTargetList)
  {
    if (aIS != null)
      readStreamLines (aIS, sCharset, 0, CGlobal.ILLEGAL_UINT, new INonThrowingRunnableWithParameter <String> ()
      {
        @Override
        public void run (final String sLine)
        {
          aTargetList.add (sLine);
        }
      });
  }

  /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aTargetList
   *        The list to be filled with the lines. May not be <code>null</code>.
   */
  public static void readStreamLines (@WillClose @Nullable final InputStream aIS,
                                      @Nonnull final Charset aCharset,
                                      @Nonnull final List <String> aTargetList)
  {
    if (aIS != null)
      readStreamLines (aIS, aCharset, 0, CGlobal.ILLEGAL_UINT, new INonThrowingRunnableWithParameter <String> ()
      {
        @Override
        public void run (final String sLine)
        {
          aTargetList.add (sLine);
        }
      });
  }

  /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @return <code>null</code> if the input stream is <code>null</code>, the
   *         content lines otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  @Deprecated
  public static List <String> readStreamLines (@WillClose @Nullable final InputStream aIS,
                                               @Nonnull @Nonempty final String sCharset,
                                               @Nonnegative final int nLinesToSkip,
                                               final int nLinesToRead)
  {
    if (aIS == null)
      return null;

    // Read stream and collect all read lines in a list
    final List <String> ret = new ArrayList <String> ();
    readStreamLines (aIS, sCharset, nLinesToSkip, nLinesToRead, new INonThrowingRunnableWithParameter <String> ()
    {
      @Override
      public void run (final String sLine)
      {
        ret.add (sLine);
      }
    });
    return ret;
  }

  /**
   * Get the content of the passed stream as a list of lines in the passed
   * character set.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @return <code>null</code> if the input stream is <code>null</code>, the
   *         content lines otherwise.
   */
  @Nullable
  @ReturnsMutableCopy
  public static List <String> readStreamLines (@WillClose @Nullable final InputStream aIS,
                                               @Nonnull final Charset aCharset,
                                               @Nonnegative final int nLinesToSkip,
                                               final int nLinesToRead)
  {
    if (aIS == null)
      return null;

    // Read stream and collect all read lines in a list
    final List <String> ret = new ArrayList <String> ();
    readStreamLines (aIS, aCharset, nLinesToSkip, nLinesToRead, new INonThrowingRunnableWithParameter <String> ()
    {
      @Override
      public void run (final String sLine)
      {
        ret.add (sLine);
      }
    });
    return ret;
  }

  /**
   * Read the complete content of the passed stream and pass each line
   * separately to the passed callback.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aLineCallback
   *        The callback that is invoked for all read lines. Each passed line
   *        does NOT contain the line delimiter!
   */
  @Deprecated
  public static void readStreamLines (@WillClose @Nullable final InputStream aIS,
                                      @Nonnull @Nonempty final String sCharset,
                                      @Nonnull final INonThrowingRunnableWithParameter <String> aLineCallback)
  {
    readStreamLines (aIS, sCharset, 0, CGlobal.ILLEGAL_UINT, aLineCallback);
  }

  /**
   * Read the complete content of the passed stream and pass each line
   * separately to the passed callback.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @param aLineCallback
   *        The callback that is invoked for all read lines. Each passed line
   *        does NOT contain the line delimiter!
   */
  public static void readStreamLines (@WillClose @Nullable final InputStream aIS,
                                      @Nonnull @Nonempty final Charset aCharset,
                                      @Nonnull final INonThrowingRunnableWithParameter <String> aLineCallback)
  {
    readStreamLines (aIS, aCharset, 0, CGlobal.ILLEGAL_UINT, aLineCallback);
  }

  private static void _readFromReader (final int nLinesToSkip,
                                       final int nLinesToRead,
                                       final INonThrowingRunnableWithParameter <String> aLineCallback,
                                       final boolean bReadAllLines,
                                       final NonBlockingBufferedReader aBR) throws IOException
  {
    // Skip all requested lines
    String sLine;
    for (int i = 0; i < nLinesToSkip; ++i)
    {
      sLine = aBR.readLine ();
      if (sLine == null)
        break;
    }

    if (bReadAllLines)
    {
      // Read all lines
      while (true)
      {
        sLine = aBR.readLine ();
        if (sLine == null)
          break;
        aLineCallback.run (sLine);
      }
    }
    else
    {
      // Read only a certain amount of lines
      int nRead = 0;
      while (true)
      {
        sLine = aBR.readLine ();
        if (sLine == null)
          break;
        aLineCallback.run (sLine);
        ++nRead;
        if (nRead >= nLinesToRead)
          break;
      }
    }
  }

  /**
   * Read the content of the passed stream line by line and invoking a callback
   * on all matching lines.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @param aLineCallback
   *        The callback that is invoked for all read lines. Each passed line
   *        does NOT contain the line delimiter! Note: it is not invoked for
   *        skipped lines!
   */
  public static void readStreamLines (@WillClose @Nullable final InputStream aIS,
                                      @Nonnull @Nonempty final Charset aCharset,
                                      @Nonnegative final int nLinesToSkip,
                                      final int nLinesToRead,
                                      @Nonnull final INonThrowingRunnableWithParameter <String> aLineCallback)
  {
    ValueEnforcer.notNull (aCharset, "Charset"); //$NON-NLS-1$
    ValueEnforcer.isGE0 (nLinesToSkip, "LinesToSkip"); //$NON-NLS-1$
    final boolean bReadAllLines = nLinesToRead == CGlobal.ILLEGAL_UINT;
    if (nLinesToRead < 0 && !bReadAllLines)
      throw new IllegalArgumentException ("Line count may not be that negative: " + nLinesToRead); //$NON-NLS-1$
    ValueEnforcer.notNull (aLineCallback, "LineCallback"); //$NON-NLS-1$

    if (aIS != null)
      try
      {
        // Start the action only if there is something to read
        if (bReadAllLines || nLinesToRead > 0)
        {
          NonBlockingBufferedReader aBR = null;
          try
          {
            // read with the passed charset
            aBR = new NonBlockingBufferedReader (createReader (aIS, aCharset));
            _readFromReader (nLinesToSkip, nLinesToRead, aLineCallback, bReadAllLines, aBR);
          }
          catch (final IOException ex)
          {
            if (!isKnownEOFException (ex))
              s_aLogger.error ("Failed to read from input stream", ex instanceof IMockException ? null : ex); //$NON-NLS-1$
          }
          finally
          {
            // Close buffered reader
            close (aBR);
          }
        }
      }
      finally
      {
        // Close input stream in case something went wrong with the buffered
        // reader.
        close (aIS);
      }
  }

  /**
   * Read the content of the passed stream line by line and invoking a callback
   * on all matching lines.
   * 
   * @param aIS
   *        The input stream to read from. May be <code>null</code>.
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @param nLinesToSkip
   *        The 0-based index of the first line to read. Pass in 0 to indicate
   *        to read everything.
   * @param nLinesToRead
   *        The number of lines to read. Pass in {@link CGlobal#ILLEGAL_UINT} to
   *        indicate that all lines should be read. If the number passed here
   *        exceeds the number of lines in the file, nothing happens.
   * @param aLineCallback
   *        The callback that is invoked for all read lines. Each passed line
   *        does NOT contain the line delimiter! Note: it is not invoked for
   *        skipped lines!
   */
  @Deprecated
  public static void readStreamLines (@WillClose @Nullable final InputStream aIS,
                                      @Nonnull @Nonempty final String sCharset,
                                      @Nonnegative final int nLinesToSkip,
                                      final int nLinesToRead,
                                      @Nonnull final INonThrowingRunnableWithParameter <String> aLineCallback)
  {
    ValueEnforcer.notNull (sCharset, "Charset"); //$NON-NLS-1$
    ValueEnforcer.isGE0 (nLinesToSkip, "LinesToSkip"); //$NON-NLS-1$
    final boolean bReadAllLines = nLinesToRead == CGlobal.ILLEGAL_UINT;
    if (nLinesToRead < 0 && !bReadAllLines)
      throw new IllegalArgumentException ("Line count may not be that negative: " + nLinesToRead); //$NON-NLS-1$
    ValueEnforcer.notNull (aLineCallback, "LineCallback"); //$NON-NLS-1$

    if (aIS != null)
      try
      {
        // Start the action only if there is something to read
        if (bReadAllLines || nLinesToRead > 0)
        {
          NonBlockingBufferedReader aBR = null;
          try
          {
            // read with the passed charset
            aBR = new NonBlockingBufferedReader (createReader (aIS, sCharset));
            _readFromReader (nLinesToSkip, nLinesToRead, aLineCallback, bReadAllLines, aBR);
          }
          catch (final IOException ex)
          {
            if (!isKnownEOFException (ex))
              s_aLogger.error ("Failed to read from input stream", ex instanceof IMockException ? null : ex); //$NON-NLS-1$
          }
          finally
          {
            // Close buffered reader
            close (aBR);
          }
        }
      }
      finally
      {
        // Close input stream in case something went wrong with the buffered
        // reader.
        close (aIS);
      }
  }

  /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param aBuf
   *        The byte array from which is to be written. May not be
   *        <code>null</code>.
   * @param nOfs
   *        The 0-based index to the first byte in the array to be written. May
   *        not be &lt; 0.
   * @param nLen
   *        The non-negative amount of bytes to be written. May not be &lt; 0.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeStream (@WillClose @Nonnull final OutputStream aOS,
                                      @Nonnull final byte [] aBuf,
                                      @Nonnegative final int nOfs,
                                      @Nonnegative final int nLen)
  {
    ValueEnforcer.notNull (aOS, "OutputStream"); //$NON-NLS-1$
    ValueEnforcer.isArrayOfsLen (aBuf, nOfs, nLen);

    try
    {
      aOS.write (aBuf, nOfs, nLen);
      aOS.flush ();
      return ESuccess.SUCCESS;
    }
    catch (final IOException ex)
    {
      if (!isKnownEOFException (ex))
      {
        s_aLogger.error ("Failed to write to output stream", ex instanceof IMockException ? null : ex); //$NON-NLS-1$
      }
      return ESuccess.FAILURE;
    }
    finally
    {
      close (aOS);
    }
  }

  /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param aBuf
   *        The byte array to be written. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeStream (@WillClose @Nonnull final OutputStream aOS, @Nonnull final byte [] aBuf)
  {
    return writeStream (aOS, aBuf, 0, aBuf.length);
  }

  /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param sContent
   *        The string to be written. May not be <code>null</code>.
   * @param sCharset
   *        The charset to be used, to convert the String to a byte array.
   * @return {@link ESuccess}
   */
  @Nonnull
  @Deprecated
  public static ESuccess writeStream (@WillClose @Nonnull final OutputStream aOS,
                                      @Nonnull final String sContent,
                                      @Nonnull @Nonempty final String sCharset)
  {
    ValueEnforcer.notNull (sContent, "Content"); //$NON-NLS-1$
    ValueEnforcer.notEmpty (sCharset, "Charset"); //$NON-NLS-1$

    return writeStream (aOS, CharsetManager.getAsBytes (sContent, sCharset));
  }

  /**
   * Write bytes to an {@link OutputStream}.
   * 
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>. Is
   *        closed independent of error or success.
   * @param sContent
   *        The string to be written. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used, to convert the String to a byte array.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess writeStream (@WillClose @Nonnull final OutputStream aOS,
                                      @Nonnull final String sContent,
                                      @Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (sContent, "Content"); //$NON-NLS-1$
    ValueEnforcer.notNull (aCharset, "Charset"); //$NON-NLS-1$

    return writeStream (aOS, CharsetManager.getAsBytes (sContent, aCharset));
  }

  @Nonnull
  public static NonBlockingStringReader createReader (@Nonnull final String sText)
  {
    return new NonBlockingStringReader (sText);
  }

  @Nonnull
  public static NonBlockingStringReader createReader (@Nonnull final char [] aChars)
  {
    return new NonBlockingStringReader (aChars);
  }

  @Nullable
  @Deprecated
  public static InputStreamReader createReader (@Nullable final InputStream aIS, @Nonnull final String sCharset)
  {
    try
    {
      return aIS == null ? null : new InputStreamReader (aIS, sCharset);
    }
    catch (final UnsupportedEncodingException ex)
    {
      throw new IllegalArgumentException ("Failed to create Reader for charset '" + sCharset + "'", ex); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  @Nullable
  public static InputStreamReader createReader (@Nullable final InputStream aIS, @Nonnull final Charset aCharset)
  {
    return aIS == null ? null : new InputStreamReader (aIS, aCharset);
  }

  @Nullable
  @Deprecated
  public static OutputStreamWriter createWriter (@Nullable final OutputStream aOS, @Nonnull final String sCharset)
  {
    try
    {
      return aOS == null ? null : new OutputStreamWriter (aOS, sCharset);
    }
    catch (final UnsupportedEncodingException ex)
    {
      throw new IllegalArgumentException ("Failed to create Writer for charset '" + sCharset + "'", ex); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  @Nullable
  public static OutputStreamWriter createWriter (@Nullable final OutputStream aOS, @Nonnull final Charset aCharset)
  {
    return aOS == null ? null : new OutputStreamWriter (aOS, aCharset);
  }

  /**
   * Fully skip the passed amounts in the input stream. Only forward skipping is
   * possible!
   * 
   * @param aIS
   *        The input stream to skip in.
   * @param nBytesToSkip
   *        The number of bytes to skip. Must be &ge; 0.
   * @throws IOException
   *         In case something goes wrong internally
   */
  public static void skipFully (@Nonnull final InputStream aIS, @Nonnegative final long nBytesToSkip) throws IOException
  {
    ValueEnforcer.notNull (aIS, "InputStream"); //$NON-NLS-1$
    ValueEnforcer.isGE0 (nBytesToSkip, "BytesToSkip"); //$NON-NLS-1$

    long nRemaining = nBytesToSkip;
    while (nRemaining > 0)
    {
      // May only return a partial skip
      final long nSkipped = aIS.skip (nRemaining);
      if (nSkipped == 0)
      {
        // Check if we're at the end of the file or not
        // -> blocking read!
        if (aIS.read () == -1)
        {
          throw new EOFException ("Failed to skip a total of " + //$NON-NLS-1$
                                  nBytesToSkip +
                                  " bytes on input stream. Only skipped " + //$NON-NLS-1$
                                  (nBytesToSkip - nRemaining) +
                                  " bytes so far!"); //$NON-NLS-1$
        }
        nRemaining--;
      }
      else
      {
        // Skipped at least one char
        nRemaining -= nSkipped;
      }
    }
  }

  /**
   * Read the whole buffer from the input stream.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   * @param aBuffer
   *        The buffer to read from. May not be <code>null</code>.
   * @throws IOException
   *         In case reading fails
   */
  public static void readFully (@Nonnull final InputStream aIS, @Nonnull final byte [] aBuffer) throws IOException
  {
    readFully (aIS, aBuffer, 0, aBuffer.length);
  }

  /**
   * Read the whole buffer from the input stream.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   * @param aBuffer
   *        The buffer to read from. May not be <code>null</code>.
   * @param nOfs
   *        The offset into the destination buffer to use. May not be &lt; 0.
   * @param nLen
   *        The number of bytes to read into the destination buffer to use. May
   *        not be &lt; 0.
   * @return The number of read bytes
   * @throws IOException
   *         In case reading fails
   */
  @Nonnegative
  public static int readFully (@Nonnull final InputStream aIS,
                               @Nonnull final byte [] aBuffer,
                               @Nonnegative final int nOfs,
                               @Nonnegative final int nLen) throws IOException
  {
    ValueEnforcer.notNull (aIS, "InputStream"); //$NON-NLS-1$
    ValueEnforcer.isArrayOfsLen (aBuffer, nOfs, nLen);

    int nTotalBytesRead = 0;
    while (nTotalBytesRead < nLen)
    {
      final int nBytesRead = aIS.read (aBuffer, nOfs + nTotalBytesRead, nLen - nTotalBytesRead);
      if (nBytesRead < 0)
        throw new EOFException ("Failed to read a total of " + //$NON-NLS-1$
                                nLen +
                                " bytes from input stream. Only read " + //$NON-NLS-1$
                                nTotalBytesRead +
                                " bytes so far."); //$NON-NLS-1$
      nTotalBytesRead += nBytesRead;
    }
    return nTotalBytesRead;
  }

  public static boolean isBuffered (@Nullable final InputStream aIS)
  {
    return aIS instanceof BufferedInputStream ||
           aIS instanceof NonBlockingBufferedInputStream ||
           aIS instanceof ByteArrayInputStream ||
           aIS instanceof NonBlockingByteArrayInputStream ||
           aIS instanceof ByteBufferInputStream ||
           aIS instanceof WrappedInputStream && isBuffered (((WrappedInputStream) aIS).getWrappedInputStream ());
  }

  @Nullable
  public static InputStream getBuffered (@Nullable final InputStream aIS)
  {
    return aIS == null || isBuffered (aIS) ? aIS : new NonBlockingBufferedInputStream (aIS);
  }

  public static boolean isBuffered (@Nullable final OutputStream aOS)
  {
    return aOS instanceof BufferedOutputStream ||
           aOS instanceof NonBlockingBufferedOutputStream ||
           aOS instanceof ByteArrayOutputStream ||
           aOS instanceof NonBlockingByteArrayOutputStream ||
           aOS instanceof ByteBufferOutputStream ||
           aOS instanceof WrappedOutputStream && isBuffered (((WrappedOutputStream) aOS).getWrappedOutputStream ());
  }

  @Nullable
  public static OutputStream getBuffered (@Nullable final OutputStream aOS)
  {
    return aOS == null || isBuffered (aOS) ? aOS : new NonBlockingBufferedOutputStream (aOS);
  }

  public static boolean isBuffered (@Nullable final Reader aReader)
  {
    return aReader instanceof BufferedReader ||
           aReader instanceof NonBlockingBufferedReader ||
           aReader instanceof StringReader ||
           aReader instanceof NonBlockingStringReader ||
           aReader instanceof WrappedReader && isBuffered (((WrappedReader) aReader).getWrappedReader ());
  }

  @Nullable
  public static Reader getBuffered (@Nullable final Reader aReader)
  {
    return aReader == null || isBuffered (aReader) ? aReader : new NonBlockingBufferedReader (aReader);
  }

  public static boolean isBuffered (@Nullable final Writer aWriter)
  {
    return aWriter instanceof BufferedWriter ||
           aWriter instanceof NonBlockingBufferedWriter ||
           aWriter instanceof StringWriter ||
           aWriter instanceof NonBlockingStringWriter ||
           aWriter instanceof WrappedWriter && isBuffered (((WrappedWriter) aWriter).getWrappedWriter ());
  }

  @Nullable
  public static Writer getBuffered (@Nullable final Writer aWriter)
  {
    return aWriter == null || isBuffered (aWriter) ? aWriter : new NonBlockingBufferedWriter (aWriter);
  }
}
