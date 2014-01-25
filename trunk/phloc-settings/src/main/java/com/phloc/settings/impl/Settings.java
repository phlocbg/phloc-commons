/**
 * Copyright (C) 2013-2014 phloc systems
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
package com.phloc.settings.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettings;

/**
 * The default implementation of the {@link ISettings} object.
 * 
 * @author philip
 */
public class Settings implements ISettings
{
  private final String m_sName;
  private final Map <String, Object> m_aMap = new HashMap <String, Object> ();

  public Settings (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("The passed name is empty");
    m_sName = sName;
  }

  @Nonnull
  @Nonempty
  public final String getName ()
  {
    return m_sName;
  }

  @Nonnegative
  public int size ()
  {
    return m_aMap.size ();
  }

  public boolean isEmpty ()
  {
    return m_aMap.isEmpty ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <String> getAllFieldNames ()
  {
    return ContainerHelper.newSet (m_aMap.keySet ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, Object> getAllEntries ()
  {
    return ContainerHelper.newMap (m_aMap);
  }

  public final boolean containsField (@Nullable final String sFieldName)
  {
    return m_aMap.containsKey (sFieldName);
  }

  @Nullable
  public Object getValue (@Nullable final String sFieldName)
  {
    return m_aMap.get (sFieldName);
  }

  @Nullable
  public <DATATYPE> DATATYPE getTypedValue (@Nullable final String sFieldName, @Nonnull final Class <DATATYPE> aDstClass)
  {
    return getTypedValue (sFieldName, aDstClass, null);
  }

  @Nullable
  public <DATATYPE> DATATYPE getTypedValue (@Nullable final String sFieldName,
                                            @Nonnull final Class <DATATYPE> aDstClass,
                                            @Nullable final DATATYPE aDefaultValue)
  {
    final Object aValue = getValue (sFieldName);
    if (aValue == null)
      return aDefaultValue;
    return TypeConverter.convertIfNecessary (aValue, aDstClass);
  }

  public boolean getBooleanValue (@Nullable final String sFieldName, final boolean bDefault)
  {
    final Boolean aValue = getTypedValue (sFieldName, Boolean.class, null);
    return aValue == null ? bDefault : aValue.booleanValue ();
  }

  public int getIntValue (@Nullable final String sFieldName, final int nDefault)
  {
    final Integer aValue = getTypedValue (sFieldName, Integer.class, null);
    return aValue == null ? nDefault : aValue.intValue ();
  }

  public long getLongValue (@Nullable final String sFieldName, final long nDefault)
  {
    final Long aValue = getTypedValue (sFieldName, Long.class, null);
    return aValue == null ? nDefault : aValue.longValue ();
  }

  @Nullable
  public BigInteger getBigIntegerValue (@Nullable final String sFieldName)
  {
    return getBigIntegerValue (sFieldName, null);
  }

  @Nullable
  public BigInteger getBigIntegerValue (@Nullable final String sFieldName, @Nullable final BigInteger aDefault)
  {
    return getTypedValue (sFieldName, BigInteger.class, aDefault);
  }

  public float getFloatValue (@Nullable final String sFieldName, final float fDefault)
  {
    final Float aValue = getTypedValue (sFieldName, Float.class, null);
    return aValue == null ? fDefault : aValue.floatValue ();
  }

  public double getDoubleValue (@Nullable final String sFieldName, final double dDefault)
  {
    final Double aValue = getTypedValue (sFieldName, Double.class, null);
    return aValue == null ? dDefault : aValue.doubleValue ();
  }

  @Nullable
  public BigDecimal getBigDecimalValue (@Nullable final String sFieldName)
  {
    return getBigDecimalValue (sFieldName, null);
  }

  @Nullable
  public BigDecimal getBigDecimalValue (@Nullable final String sFieldName, @Nullable final BigDecimal aDefault)
  {
    return getTypedValue (sFieldName, BigDecimal.class, aDefault);
  }

  @Nullable
  public String getStringValue (@Nullable final String sFieldName)
  {
    return getStringValue (sFieldName, null);
  }

  @Nullable
  public String getStringValue (@Nullable final String sFieldName, @Nullable final String sDefault)
  {
    return getTypedValue (sFieldName, String.class, sDefault);
  }

  @Nullable
  public LocalDate getDateValue (@Nullable final String sFieldName)
  {
    return getDateValue (sFieldName, null);
  }

  @Nullable
  public LocalDate getDateValue (@Nullable final String sFieldName, @Nullable final LocalDate aDefault)
  {
    return getTypedValue (sFieldName, LocalDate.class, aDefault);
  }

  @Nullable
  public LocalTime getTimeValue (@Nullable final String sFieldName)
  {
    return getTimeValue (sFieldName, null);
  }

  @Nullable
  public LocalTime getTimeValue (@Nullable final String sFieldName, @Nullable final LocalTime aDefault)
  {
    return getTypedValue (sFieldName, LocalTime.class, aDefault);
  }

  @Nullable
  public DateTime getDateTimeValue (@Nullable final String sFieldName)
  {
    return getDateTimeValue (sFieldName, null);
  }

  @Nullable
  public DateTime getDateTimeValue (@Nullable final String sFieldName, @Nullable final DateTime aDefault)
  {
    return getTypedValue (sFieldName, DateTime.class, aDefault);
  }

  @Nullable
  public ISettings getSettingsValue (@Nullable final String sFieldName)
  {
    return getTypedValue (sFieldName, ISettings.class, null);
  }

  public void restoreValue (@Nonnull @Nonempty final String sFieldName, @Nonnull final Object aNewValue)
  {
    if (StringHelper.hasNoText (sFieldName))
      throw new IllegalArgumentException ("Empty field name");
    if (aNewValue == null)
      throw new NullPointerException ("newValue");

    m_aMap.put (sFieldName, aNewValue);
  }

  @Nonnull
  public EChange setValues (@Nonnull final IReadonlySettings aOtherSettings)
  {
    if (aOtherSettings == null)
      throw new NullPointerException ("otherSettings");

    EChange eChange = EChange.UNCHANGED;
    for (final String sFieldName : aOtherSettings.getAllFieldNames ())
      eChange = eChange.or (setValue (sFieldName, aOtherSettings.getValue (sFieldName)));
    return eChange;
  }

  @Nonnull
  public EChange removeValue (@Nullable final String sFieldName)
  {
    return EChange.valueOf (m_aMap.remove (sFieldName) != null);
  }

  @Nonnull
  public EChange clear ()
  {
    if (m_aMap.isEmpty ())
      return EChange.UNCHANGED;
    m_aMap.clear ();
    return EChange.CHANGED;
  }

  /**
   * Protected method that is invoked after a setting changed.
   * 
   * @param sFieldName
   *        The changed field name. Neither <code>null</code> nor empty.
   * @param aOldValue
   *        The old value. May be <code>null</code>.
   * @param aNewValue
   *        The new value. May be <code>null</code>.
   */
  @OverrideOnDemand
  protected void onAfterSettingsChanged (@Nonnull @Nonempty final String sFieldName,
                                         @Nullable final Object aOldValue,
                                         @Nullable final Object aNewValue)
  {}

  @Nonnull
  public EChange setValue (@Nonnull @Nonempty final String sFieldName, @Nullable final Object aNewValue)
  {
    if (StringHelper.hasNoText (sFieldName))
      throw new IllegalArgumentException ("Empty field name");

    // Get the old value
    final Object aOldValue = getValue (sFieldName);
    if (EqualsUtils.equals (aOldValue, aNewValue))
      return EChange.UNCHANGED;

    // Value changed -> trigger update
    m_aMap.put (sFieldName, aNewValue);
    onAfterSettingsChanged (sFieldName, aOldValue, aNewValue);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setValue (@Nonnull @Nonempty final String sFieldName, final boolean bValue)
  {
    return setValue (sFieldName, Boolean.valueOf (bValue));
  }

  @Nonnull
  public EChange setValue (@Nonnull @Nonempty final String sFieldName, final int nValue)
  {
    return setValue (sFieldName, Integer.valueOf (nValue));
  }

  @Nonnull
  public EChange setValue (@Nonnull @Nonempty final String sFieldName, final long nValue)
  {
    return setValue (sFieldName, Long.valueOf (nValue));
  }

  @Nonnull
  public EChange setValue (@Nonnull @Nonempty final String sFieldName, final float fValue)
  {
    return setValue (sFieldName, Float.valueOf (fValue));
  }

  @Nonnull
  public EChange setValue (@Nonnull @Nonempty final String sFieldName, final double dValue)
  {
    return setValue (sFieldName, Double.valueOf (dValue));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final Settings rhs = (Settings) o;
    return m_sName.equals (rhs.m_sName) && EqualsUtils.equals (m_aMap, rhs.m_aMap);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sName).append (m_aMap).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("name", m_sName).append ("map", m_aMap).toString ();
  }
}
