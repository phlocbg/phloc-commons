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
package com.phloc.commons.xml.ls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.resource.URLResource;

/**
 * Test class for class {@link SimpleLSResourceResolver}.
 * 
 * @author philip
 */
public final class SimpleLSResourceResolverTest
{
  @Test
  public void testDoStandardResourceResolving () throws URISyntaxException, IOException
  {
    IReadableResource aRes;

    // Using URLs as the base
    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("", "http://www.phloc.com");
    assertTrue (aRes instanceof URLResource);
    assertEquals ("http://www.phloc.com", aRes.getPath ());

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("dir/file.txt", "http://www.phloc.com");
    assertTrue (aRes instanceof URLResource);
    assertEquals ("http://www.phloc.com/dir/file.txt", aRes.getPath ());

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("../dir/file.txt", "http://www.phloc.com");
    assertTrue (aRes instanceof URLResource);
    assertEquals ("http://www.phloc.com/../dir/file.txt", aRes.getPath ());

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("../dir/file.txt", "http://www.phloc.com/abc/");
    assertTrue (aRes instanceof URLResource);
    assertEquals ("http://www.phloc.com/dir/file.txt", aRes.getPath ());

    // system ID is a fixed URL
    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("http://www.example.org/file.txt",
                                                                 "http://www.phloc.com/abc/");
    assertTrue (aRes instanceof URLResource);
    assertEquals ("http://www.example.org/file.txt", aRes.getPath ());

    // Using files as the basis
    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("dir/include.xml", "pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("phloc-commons/dir/include.xml").getPath ()));

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("dir/include.xml", "abc/pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("phloc-commons/abc/dir/include.xml").getPath ()));

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("../dir/include.xml", "pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("dir/include.xml").getPath ()));

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("../dir/include.xml", "abc/pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("phloc-commons/dir/include.xml").getPath ()));

    // system ID has a protocol prefix
    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("file:dir/include.xml", "pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("phloc-commons/dir/include.xml").getPath ()));

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("file:dir/include.xml", "abc/pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("phloc-commons/abc/dir/include.xml").getPath ()));

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("file:../dir/include.xml", "pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("dir/include.xml").getPath ()));

    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("file:../dir/include.xml", "abc/pom.xml");
    assertTrue (aRes instanceof FileSystemResource);
    assertTrue (aRes.getPath (), aRes.getPath ().endsWith (new File ("phloc-commons/dir/include.xml").getPath ()));

    // system ID is a fixed URL
    aRes = SimpleLSResourceResolver.doStandardResourceResolving ("http://www.example.org/file.txt", "abc/pom.xml");
    assertTrue (aRes instanceof URLResource);
    assertEquals ("http://www.example.org/file.txt", aRes.getPath ());
  }
}
