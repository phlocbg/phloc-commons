/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.io.file;

import java.io.File;
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Callback interface for {@link FileOperationManager}.
 * 
 * @author philip
 */
public interface IFileOperationCallback extends Serializable
{
  /**
   * Called upon operation success.
   * 
   * @param eOperation
   *        The operation that succeeded.
   * @param aFile1
   *        The first file worked upon. May not be <code>null</code>.
   * @param aFile2
   *        The second file worked upon. May be <code>null</code>.
   */
  void onSuccess (@Nonnull EFileIOOperation eOperation, @Nonnull File aFile1, @Nullable File aFile2);

  /**
   * Called upon operation error.
   * 
   * @param eOperation
   *        The operation that failed.
   * @param eErrorCode
   *        The error code that occurred.
   * @param aFile1
   *        The first file worked upon. May not be <code>null</code>.
   * @param aFile2
   *        The second file worked upon. May be <code>null</code>.
   * @param aException
   *        The exception that occurred. May be <code>null</code>.
   */
  void onError (@Nonnull EFileIOOperation eOperation,
                @Nonnull EFileIOErrorCode eErrorCode,
                @Nonnull File aFile1,
                @Nullable File aFile2,
                @Nullable Exception aException);
}
