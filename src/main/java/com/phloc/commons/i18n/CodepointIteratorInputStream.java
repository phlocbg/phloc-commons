package com.phloc.commons.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorInputStream extends CodepointIteratorReadableByteChannel
{
  public CodepointIteratorInputStream (@Nonnull @WillClose final InputStream aIS, @Nonnull final Charset aCharset) throws IOException
  {
    super (Channels.newChannel (aIS), aCharset);
  }
}
