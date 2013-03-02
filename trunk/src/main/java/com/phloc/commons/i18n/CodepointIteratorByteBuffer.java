package com.phloc.commons.i18n;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorByteBuffer extends CodepointIteratorCharBuffer
{
  public CodepointIteratorByteBuffer (@Nonnull final ByteBuffer aBytes, @Nonnull final Charset aCharset)
  {
    super (aCharset.decode (aBytes));
  }
}
