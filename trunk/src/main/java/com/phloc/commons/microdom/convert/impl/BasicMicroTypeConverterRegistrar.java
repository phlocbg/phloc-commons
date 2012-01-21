/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.microdom.convert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.state.EEnabled;
import com.phloc.commons.state.EFinish;
import com.phloc.commons.state.EInterrupt;
import com.phloc.commons.state.ELeftRight;
import com.phloc.commons.state.EMandatory;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ETopBottom;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.state.EValidity;

@Immutable
@IsSPIImplementation
public final class BasicMicroTypeConverterRegistrar implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    // String converter
    aRegistry.registerMicroElementTypeConverter (String.class, StringMicroTypeConverter.getInstance ());

    // Other base type based on the String converter
    aRegistry.registerMicroElementTypeConverter (AtomicBoolean.class,
                                                 new StringBasedMicroTypeConverter (AtomicBoolean.class));
    aRegistry.registerMicroElementTypeConverter (AtomicInteger.class,
                                                 new StringBasedMicroTypeConverter (AtomicInteger.class));
    aRegistry.registerMicroElementTypeConverter (AtomicLong.class, new StringBasedMicroTypeConverter (AtomicLong.class));
    aRegistry.registerMicroElementTypeConverter (Boolean.class, new StringBasedMicroTypeConverter (Boolean.class));
    aRegistry.registerMicroElementTypeConverter (Byte.class, new StringBasedMicroTypeConverter (Byte.class));
    aRegistry.registerMicroElementTypeConverter (Character.class, new StringBasedMicroTypeConverter (Character.class));
    aRegistry.registerMicroElementTypeConverter (Double.class, new StringBasedMicroTypeConverter (Double.class));
    aRegistry.registerMicroElementTypeConverter (Float.class, new StringBasedMicroTypeConverter (Float.class));
    aRegistry.registerMicroElementTypeConverter (Integer.class, new StringBasedMicroTypeConverter (Integer.class));
    aRegistry.registerMicroElementTypeConverter (Long.class, new StringBasedMicroTypeConverter (Long.class));
    aRegistry.registerMicroElementTypeConverter (Short.class, new StringBasedMicroTypeConverter (Short.class));
    aRegistry.registerMicroElementTypeConverter (BigDecimal.class, new StringBasedMicroTypeConverter (BigDecimal.class));
    aRegistry.registerMicroElementTypeConverter (BigInteger.class, new StringBasedMicroTypeConverter (BigInteger.class));
    aRegistry.registerMicroElementTypeConverter (StringBuffer.class,
                                                 new StringBasedMicroTypeConverter (StringBuffer.class));
    aRegistry.registerMicroElementTypeConverter (StringBuilder.class,
                                                 new StringBasedMicroTypeConverter (StringBuilder.class));
    aRegistry.registerMicroElementTypeConverter (byte [].class, new StringBasedMicroTypeConverter (byte [].class));

    // State enums
    aRegistry.registerMicroElementTypeConverter (EChange.class, new StringBasedMicroTypeConverter (EChange.class));
    aRegistry.registerMicroElementTypeConverter (EContinue.class, new StringBasedMicroTypeConverter (EContinue.class));
    aRegistry.registerMicroElementTypeConverter (EEnabled.class, new StringBasedMicroTypeConverter (EEnabled.class));
    aRegistry.registerMicroElementTypeConverter (EFinish.class, new StringBasedMicroTypeConverter (EFinish.class));
    aRegistry.registerMicroElementTypeConverter (EInterrupt.class, new StringBasedMicroTypeConverter (EInterrupt.class));
    aRegistry.registerMicroElementTypeConverter (ELeftRight.class, new StringBasedMicroTypeConverter (ELeftRight.class));
    aRegistry.registerMicroElementTypeConverter (EMandatory.class, new StringBasedMicroTypeConverter (EMandatory.class));
    aRegistry.registerMicroElementTypeConverter (ESuccess.class, new StringBasedMicroTypeConverter (ESuccess.class));
    aRegistry.registerMicroElementTypeConverter (ETopBottom.class, new StringBasedMicroTypeConverter (ETopBottom.class));
    aRegistry.registerMicroElementTypeConverter (ETriState.class, new StringBasedMicroTypeConverter (ETriState.class));
    aRegistry.registerMicroElementTypeConverter (EValidity.class, new StringBasedMicroTypeConverter (EValidity.class));
  }
}
