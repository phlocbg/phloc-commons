/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.types.datatype.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.types.datatype.CDataType;
import com.phloc.types.datatype.IDataType;

/**
 * Utility class for parsing a complex data type ID into a string.
 * 
 * @author Philip Helger
 */
@Immutable
public final class DataTypeParser
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final DataTypeParser s_aInstance = new DataTypeParser ();

  private DataTypeParser ()
  {}

  @Nonnull
  public static IDataType getDataTypeFromString (@Nonnull final String sID)
  {
    if (sID.startsWith (CDataType.ID_LIST_PREFIX))
    {
      // It's a list
      return new ListDataType (getDataTypeFromString (sID.substring (CDataType.ID_LIST_PREFIX.length () +
                                                                     CDataType.ID_TYPE_SEPARATOR_LENGTH)));
    }

    if (sID.startsWith (CDataType.ID_SET_PREFIX))
    {
      // It's a set
      return new SetDataType (getDataTypeFromString (sID.substring (CDataType.ID_SET_PREFIX.length () +
                                                                    CDataType.ID_TYPE_SEPARATOR_LENGTH)));
    }

    if (sID.startsWith (CDataType.ID_MAP_PREFIX))
    {
      // It's a map
      final String sRest = sID.substring (CDataType.ID_MAP_PREFIX.length () + CDataType.ID_TYPE_SEPARATOR_LENGTH);
      final IDataType aKeyDataType = getDataTypeFromString (sRest);
      final IDataType aValueDataType = getDataTypeFromString (sRest.substring (aKeyDataType.getID ().length () +
                                                                               CDataType.ID_TYPE_SEPARATOR_LENGTH));
      return new MapDataType (aKeyDataType, aValueDataType);
    }

    // Simple data type
    final int nIndex = sID.indexOf (CDataType.ID_TYPE_SEPARATOR);
    return SimpleDataTypeRegistry.getSimpleDataTypeOfID (nIndex == -1 ? sID : sID.substring (0, nIndex));
  }
}
