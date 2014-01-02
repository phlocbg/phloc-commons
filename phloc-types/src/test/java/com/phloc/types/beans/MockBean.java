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
package com.phloc.types.beans;

import com.phloc.commons.annotations.UsedViaReflection;

@UsedViaReflection
final class MockBean
{
  private int m_nX = 0;
  private String m_sYY = "";
  private boolean m_bBoolean = false;
  private byte m_nByte = 0;
  private char m_cChar = 0;
  private double m_dDouble = 0;
  private float m_fFloat = 0;
  private int m_nInt = 0;
  private long m_nLong = 0;
  private short m_nShort = 0;

  public int getX ()
  {
    return m_nX;
  }

  public void setX (final int nX)
  {
    m_nX = nX;
  }

  public String getYY ()
  {
    return m_sYY;
  }

  public void setYY (final String sYY)
  {
    m_sYY = sYY;
  }

  public boolean getBoolean ()
  {
    return m_bBoolean;
  }

  public void setBoolean (final boolean aValue)
  {
    m_bBoolean = aValue;
  }

  public byte getByte ()
  {
    return m_nByte;
  }

  public void setByte (final byte aValue)
  {
    m_nByte = aValue;
  }

  public char getChar ()
  {
    return m_cChar;
  }

  public void setChar (final char aValue)
  {
    m_cChar = aValue;
  }

  public double getDouble ()
  {
    return m_dDouble;
  }

  public void setDouble (final double aValue)
  {
    m_dDouble = aValue;
  }

  public float getFloat ()
  {
    return m_fFloat;
  }

  public void setFloat (final float aValue)
  {
    m_fFloat = aValue;
  }

  public int getInt ()
  {
    return m_nInt;
  }

  public void setInt (final int aValue)
  {
    m_nInt = aValue;
  }

  public long getLong ()
  {
    return m_nLong;
  }

  public void setLong (final long aValue)
  {
    m_nLong = aValue;
  }

  public short getShort ()
  {
    return m_nShort;
  }

  public void setShort (final short aValue)
  {
    m_nShort = aValue;
  }
}