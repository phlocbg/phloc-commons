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

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Test class for class {@link BeanNameHelper}.
 * 
 * @author Philip Helger
 */
public final class BeanNameHelperTest
{
  @Test
  public void testBeanUpperCaseFirstChar ()
  {
    assertEquals (null, BeanNameHelper.beanUpperCaseFirstChar (null));
    assertEquals ("", BeanNameHelper.beanUpperCaseFirstChar (""));
    assertEquals ("A", BeanNameHelper.beanUpperCaseFirstChar ("a"));
    assertEquals ("A", BeanNameHelper.beanUpperCaseFirstChar ("A"));
    assertEquals ("Super", BeanNameHelper.beanUpperCaseFirstChar ("super"));
    assertEquals ("Super", BeanNameHelper.beanUpperCaseFirstChar ("Super"));
    assertEquals ("aVersion", BeanNameHelper.beanUpperCaseFirstChar ("aVersion"));
    assertEquals ("AVersion", BeanNameHelper.beanUpperCaseFirstChar ("AVersion"));
  }

  @Test
  public void testBeanLowerCaseFirstChar ()
  {
    assertEquals (null, BeanNameHelper.beanLowerCaseFirstChar (null));
    assertEquals ("", BeanNameHelper.beanLowerCaseFirstChar (""));
    assertEquals ("a", BeanNameHelper.beanLowerCaseFirstChar ("a"));
    assertEquals ("a", BeanNameHelper.beanLowerCaseFirstChar ("A"));
    assertEquals ("super", BeanNameHelper.beanLowerCaseFirstChar ("super"));
    assertEquals ("super", BeanNameHelper.beanLowerCaseFirstChar ("Super"));
    assertEquals ("aVersion", BeanNameHelper.beanLowerCaseFirstChar ("aVersion"));
    assertEquals ("AVersion", BeanNameHelper.beanLowerCaseFirstChar ("AVersion"));
  }

  @Test
  public void testBeanCamelCaseName ()
  {
    assertEquals ("", BeanNameHelper.beanCamelCaseName (""));
    assertEquals ("A", BeanNameHelper.beanCamelCaseName ("a"));
    assertEquals ("A", BeanNameHelper.beanCamelCaseName ("A"));
    assertEquals ("Super", BeanNameHelper.beanCamelCaseName ("super"));
    assertEquals ("Super", BeanNameHelper.beanCamelCaseName ("Super"));
    assertEquals ("aVersion", BeanNameHelper.beanCamelCaseName ("aVersion"));
    assertEquals ("AVersion", BeanNameHelper.beanCamelCaseName ("AVersion"));

    assertEquals ("ComPhloc", BeanNameHelper.beanCamelCaseName ("com.phloc"));
    assertEquals ("ComPhloc", BeanNameHelper.beanCamelCaseName ("com_phloc"));
    assertEquals ("ComPhlocTest", BeanNameHelper.beanCamelCaseName ("com.phloc.test"));
    assertEquals ("ComPhlocTest", BeanNameHelper.beanCamelCaseName ("com_phloc_test"));
    assertEquals ("ComPhlocTest", BeanNameHelper.beanCamelCaseName ("com.phloc_test"));
    assertEquals ("ComPhlocTest", BeanNameHelper.beanCamelCaseName ("com_phloc.test"));
    assertEquals ("ComPhloc_Test", BeanNameHelper.beanCamelCaseName ("Com.Phloc$Test"));
    assertEquals ("Comphloc_Test", BeanNameHelper.beanCamelCaseName ("Comphloc$Test"));
  }

  @Test
  public void testGetGetterName ()
  {
    assertEquals ("get", BeanNameHelper.getGetterName (""));
    assertEquals ("getA", BeanNameHelper.getGetterName ("a"));
    assertEquals ("getA", BeanNameHelper.getGetterName ("A"));
    assertEquals ("getAny", BeanNameHelper.getGetterName ("any"));
    assertEquals ("getAnyCase", BeanNameHelper.getGetterName ("anyCase"));
  }

  @Test
  public void testGetIsGetterName ()
  {
    assertEquals ("is", BeanNameHelper.getIsGetterName (""));
    assertEquals ("isA", BeanNameHelper.getIsGetterName ("a"));
    assertEquals ("isA", BeanNameHelper.getIsGetterName ("A"));
    assertEquals ("isAny", BeanNameHelper.getIsGetterName ("any"));
    assertEquals ("isAnyCase", BeanNameHelper.getIsGetterName ("anyCase"));
  }

  @Test
  public void testGetSetterName ()
  {
    assertEquals ("set", BeanNameHelper.getSetterName (""));
    assertEquals ("setA", BeanNameHelper.getSetterName ("a"));
    assertEquals ("setA", BeanNameHelper.getSetterName ("A"));
    assertEquals ("setAny", BeanNameHelper.getSetterName ("any"));
    assertEquals ("setAnyCase", BeanNameHelper.getSetterName ("anyCase"));
  }
}
