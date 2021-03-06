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
package com.phloc.commons.messagedigest;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CharsetManager;

/**
 * Base class for creating a cryptographic hash value. Don't mix it up with the
 * {@link com.phloc.commons.hash.HashCodeGenerator} which is used to generate
 * hash values for Java objects.
 * 
 * @author Philip Helger
 */
public abstract class AbstractMessageDigestGenerator implements IMessageDigestGenerator
{
  @Nonnull
  @Deprecated
  public final IMessageDigestGenerator update (@Nonnull final String aValue, @Nonnull @Nonempty final String sCharset)
  {
    ValueEnforcer.notNull (aValue, "Value");
    ValueEnforcer.notEmpty (sCharset, "Charset");

    return update (CharsetManager.getAsBytes (aValue, sCharset));
  }

  @Nonnull
  public final IMessageDigestGenerator update (@Nonnull final String aValue, @Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (aValue, "Value");
    ValueEnforcer.notNull (aCharset, "Charset");

    return update (CharsetManager.getAsBytes (aValue, aCharset));
  }

  @Nonnull
  public final IMessageDigestGenerator update (@Nonnull final byte [] aValue)
  {
    ValueEnforcer.notNull (aValue, "Value");

    return update (aValue, 0, aValue.length);
  }

  public final long getDigestLong ()
  {
    return MessageDigestGeneratorHelper.getLongFromDigest (getDigest ());
  }

  @Nonnull
  public final String getDigestHexString ()
  {
    return MessageDigestGeneratorHelper.getHexValueFromDigest (getDigest ());
  }
}
