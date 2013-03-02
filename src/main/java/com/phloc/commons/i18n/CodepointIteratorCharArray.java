package com.phloc.commons.i18n;

import javax.annotation.Nonnull;

/**
 * @author Apache Abdera
 */
public class CodepointIteratorCharArray extends AbstractCodepointIterator
{
  private final char [] m_aBuffer;

  public CodepointIteratorCharArray (@Nonnull final char [] aBuffer)
  {
    this (aBuffer, 0, aBuffer.length);
  }

  public CodepointIteratorCharArray (@Nonnull final char [] aBuffer, final int nOfs, final int nLen)
  {
    super (nOfs, Math.min (aBuffer.length - nOfs, nLen));
    m_aBuffer = aBuffer;
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
