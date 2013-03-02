package com.phloc.commons.i18n;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorByteArray extends CodepointIteratorByteBuffer
{
  public CodepointIteratorByteArray (@Nonnull final byte [] aBytes, @Nonnull final Charset aCharset)
  {
    super (ByteBuffer.wrap (aBytes), aCharset);
  }
}
