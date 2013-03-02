package com.phloc.commons.i18n;

import java.nio.CharBuffer;

import javax.annotation.Nonnull;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorCharBuffer extends AbstractCodepointIterator
{
  private final char [] m_aBuffer;

  public CodepointIteratorCharBuffer (@Nonnull final CharBuffer aBuffer)
  {
    super (aBuffer.position (), aBuffer.limit ());
    m_aBuffer = aBuffer.array ();
  }

  @Override
  protected char get ()
  {
    return m_nPosition < m_nLimit ? m_aBuffer[m_nPosition++] : (char) -1;
  }

  @Override
  protected char get (final int index)
  {
    if (index < 0 || index >= m_nLimit)
      throw new ArrayIndexOutOfBoundsException (index);
    return m_aBuffer[index];
  }
}
