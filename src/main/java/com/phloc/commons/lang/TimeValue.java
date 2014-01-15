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
package com.phloc.commons.lang;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Pair of {@link TimeUnit} and a value.
 * 
 * @author Philip Helger
 */
@Immutable
public final class TimeValue
{
  /** Special instance representing 1 second */
  public static final TimeValue SEC1 = new TimeValue (TimeUnit.SECONDS, 1);

  private final TimeUnit m_eTimeUnit;
  private final long m_nDuration;

  public TimeValue (@Nonnull final TimeUnit eTimeUnit, final long nDuration)
  {
    if (eTimeUnit == null)
      throw new NullPointerException ("timeUnit");
    m_eTimeUnit = eTimeUnit;
    m_nDuration = nDuration;
  }

  @Nonnull
  public TimeUnit getTimeUnit ()
  {
    return m_eTimeUnit;
  }

  public long getDuration ()
  {
    return m_nDuration;
  }

  public long getAsNanos ()
  {
    return m_eTimeUnit.toNanos (m_nDuration);
  }

  public long getAsMicros ()
  {
    return m_eTimeUnit.toMicros (m_nDuration);
  }

  public long getAsMillis ()
  {
    return m_eTimeUnit.toMillis (m_nDuration);
  }

  public long getAsSeconds ()
  {
    return m_eTimeUnit.toSeconds (m_nDuration);
  }

  public long getAsMinutes ()
  {
    // IFJDK5
     return getAsSeconds () / 60;
    // ELSE
//    return m_eTimeUnit.toMinutes (m_nDuration);
    // ENDIF
  }

  public long getAsHours ()
  {
    // IFJDK5
     return getAsSeconds () / 3600;
    // ELSE
//    return m_eTimeUnit.toHours (m_nDuration);
    // ENDIF
  }

  public long getAsDays ()
  {
    // IFJDK5
     return getAsSeconds () / 86400;
    // ELSE
//    return m_eTimeUnit.toDays (m_nDuration);
    // ENDIF
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof TimeValue))
      return false;
    final TimeValue rhs = (TimeValue) o;
    return m_eTimeUnit.equals (rhs.m_eTimeUnit) && m_nDuration == rhs.m_nDuration;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eTimeUnit).append (m_nDuration).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("timeUnit", m_eTimeUnit).append ("value", m_nDuration).toString ();
  }
}
