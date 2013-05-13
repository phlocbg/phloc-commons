package com.phloc.test.benchmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

public class TST <DATATYPE>
{
  private static class Node <DATATYPE>
  {
    // character
    private final char m_cChar;
    // left, middle, and right subtries
    private Node <DATATYPE> m_aLeft, m_aMid, m_aRight;
    // value associated with string
    private DATATYPE m_aValue;

    public Node (final char c)
    {
      m_cChar = c;
    }
  }

  // size
  private int m_nSize;
  // root of TST
  private Node <DATATYPE> m_aRoot;

  @Nonnegative
  public int size ()
  {
    return m_nSize;
  }

  public boolean contains (@Nonnull @Nonempty final String sKey)
  {
    return get (sKey) != null;
  }

  @Nullable
  public DATATYPE get (@Nonnull @Nonempty final String sKey)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key must have length >= 1");

    final Node <DATATYPE> x = _get (m_aRoot, sKey, 0);
    return x == null ? null : x.m_aValue;
  }

  // return subtrie corresponding to given key
  @Nullable
  private Node <DATATYPE> _get (@Nullable final Node <DATATYPE> aNode,
                                @Nonnull @Nonempty final String sKey,
                                @Nonnegative final int nIndex)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key must have length >= 1");

    if (aNode != null)
    {
      final char c = sKey.charAt (nIndex);
      if (c < aNode.m_cChar)
        return _get (aNode.m_aLeft, sKey, nIndex);
      if (c > aNode.m_cChar)
        return _get (aNode.m_aRight, sKey, nIndex);
      if (nIndex < sKey.length () - 1)
        return _get (aNode.m_aMid, sKey, nIndex + 1);
    }
    return aNode;
  }

  public void put (@Nonnull @Nonempty final String sKey, @Nullable final DATATYPE val)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key must have length >= 1");

    final char [] aChars = sKey.toCharArray ();
    m_aRoot = _put (m_aRoot, aChars, val, 0);
  }

  @Nonnull
  private Node <DATATYPE> _put (@Nullable final Node <DATATYPE> aNode,
                                @Nonnull final char [] s,
                                @Nullable final DATATYPE aValue,
                                @Nonnegative final int nIndex)
  {
    final char c = s[nIndex];
    Node <DATATYPE> aRealNode;
    if (aNode == null)
    {
      aRealNode = new Node <DATATYPE> (c);
      if (nIndex == s.length - 1)
        m_nSize++;
    }
    else
      aRealNode = aNode;
    if (c < aRealNode.m_cChar)
      aRealNode.m_aLeft = _put (aRealNode.m_aLeft, s, aValue, nIndex);
    else
      if (c > aRealNode.m_cChar)
        aRealNode.m_aRight = _put (aRealNode.m_aRight, s, aValue, nIndex);
      else
        if (nIndex < s.length - 1)
          aRealNode.m_aMid = _put (aRealNode.m_aMid, s, aValue, nIndex + 1);
        else
          aRealNode.m_aValue = aValue;
    return aRealNode;
  }

  @Nullable
  public String getLongestPrefixOf (@Nullable final String s)
  {
    if (StringHelper.hasNoText (s))
      return null;

    int nLength = 0;
    Node <DATATYPE> x = m_aRoot;
    int i = 0;
    while (x != null && i < s.length ())
    {
      final char c = s.charAt (i);
      if (c < x.m_cChar)
        x = x.m_aLeft;
      else
        if (c > x.m_cChar)
          x = x.m_aRight;
        else
        {
          i++;
          if (x.m_aValue != null)
            nLength = i;
          x = x.m_aMid;
        }
    }
    return s.substring (0, nLength);
  }

  @Nonnull
  public Collection <String> getAllKeys ()
  {
    final List <String> aList = new ArrayList <String> ();
    _collect (m_aRoot, "", aList);
    return aList;
  }

  // all keys in subtrie rooted at x with given prefix
  private void _collect (@Nullable final Node <DATATYPE> aNode,
                         @Nonnull final String sPrefix,
                         @Nonnull final List <String> aList)
  {
    if (aNode != null)
    {
      _collect (aNode.m_aLeft, sPrefix, aList);
      final String sPrefixC = sPrefix + aNode.m_cChar;
      if (aNode.m_aValue != null)
        aList.add (sPrefixC);
      _collect (aNode.m_aMid, sPrefixC, aList);
      _collect (aNode.m_aRight, sPrefix, aList);
    }
  }

  // all keys starting with given prefix
  @Nonnull
  public Collection <String> prefixMatch (@Nonnull @Nonempty final String sPrefix)
  {
    final List <String> aList = new ArrayList <String> ();
    final Node <DATATYPE> aNode = _get (m_aRoot, sPrefix, 0);
    if (aNode != null)
    {
      if (aNode.m_aValue != null)
        aList.add (sPrefix);
      _collect (aNode.m_aMid, sPrefix, aList);
    }
    return aList;
  }

  // return all keys matching given wilcard pattern
  @Nonnull
  public Collection <String> wildcardMatch (@Nonnull final String sPattern)
  {
    final List <String> queue = new ArrayList <String> ();
    collect (m_aRoot, "", 0, sPattern, queue);
    return queue;
  }

  public void collect (@Nullable final Node <DATATYPE> aNode,
                       @Nonnull final String sPrefix,
                       @Nonnegative final int nIndex,
                       @Nonnull final String sPattern,
                       @Nonnull final List <String> aList)
  {
    if (aNode != null)
    {
      final char c = sPattern.charAt (nIndex);
      if (c == '.' || c < aNode.m_cChar)
        collect (aNode.m_aLeft, sPrefix, nIndex, sPattern, aList);
      if (c == '.' || c == aNode.m_cChar)
      {
        if (nIndex == sPattern.length () - 1 && aNode.m_aValue != null)
          aList.add (sPrefix + aNode.m_cChar);
        if (nIndex < sPattern.length () - 1)
          collect (aNode.m_aMid, sPrefix + aNode.m_cChar, nIndex + 1, sPattern, aList);
      }
      if (c == '.' || c > aNode.m_cChar)
        collect (aNode.m_aRight, sPrefix, nIndex, sPattern, aList);
    }
  }
}
