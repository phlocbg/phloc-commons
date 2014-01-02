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
package com.phloc.types.datatype;

import javax.annotation.Nonnull;

/**
 * Utility class for data types
 * 
 * @author Philip Helger
 */
public final class DataTypeHelper
{
  private DataTypeHelper ()
  {}

  public static boolean isSimple (@Nonnull final IDataType aDataType)
  {
    return aDataType.isSimple ();
  }

  public static boolean isComplex (@Nonnull final IDataType aDataType)
  {
    return aDataType.isComplex ();
  }

  public static boolean isCollection (@Nonnull final IDataType aDataType)
  {
    return aDataType.isComplex () && aDataType instanceof ICollectionDataType;
  }

  public static boolean isList (@Nonnull final IDataType aDataType)
  {
    return isCollection (aDataType) && ((ICollectionDataType) aDataType).isList ();
  }

  public static boolean isSet (@Nonnull final IDataType aDataType)
  {
    return isCollection (aDataType) && ((ICollectionDataType) aDataType).isSet ();
  }

  public static boolean isMap (@Nonnull final IDataType aDataType)
  {
    return aDataType.isComplex () && aDataType instanceof IMapDataType;
  }
}
