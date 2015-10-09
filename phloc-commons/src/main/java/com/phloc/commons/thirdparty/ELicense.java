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
package com.phloc.commons.thirdparty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.version.Version;

/**
 * Represents a bunch of Open Source licenses regularly used.
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 */
public enum ELicense implements ILicense
{
  // Apache
  APACHE1 ("apache1", "Apache License", new Version (1, 0), "http://www.apache.org/licenses/LICENSE-1.0.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  APACHE2 ("apache2", "Apache License", new Version (2, 0), "http://www.apache.org/licenses/LICENSE-2.0.txt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // Mozilla
  MPL10 ("mpl10", "MOZILLA PUBLIC LICENSE", new Version (1, 0), "http://www.mozilla.org/MPL/1.0/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  MPL11 ("mpl11", "MOZILLA PUBLIC LICENSE", new Version (1, 1), "http://www.mozilla.org/MPL/1.1/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  MPL20 ("mpl20", "MOZILLA PUBLIC LICENSE VERSION 2.0", new Version (2, 0), "http://www.mozilla.org/MPL/2.0/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // BSD/MIT
  BSD ("bsd", "The BSD License", null, "http://www.opensource.org/licenses/bsd-license.php"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  MIT ("mit", "MIT License", null, "http://www.opensource.org/licenses/mit-license.php"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // GPL
  GPL20 ("gpl20", "GNU GENERAL PUBLIC LICENSE", new Version (2, 0), "http://www.gnu.org/licenses/old-licenses/gpl-2.0.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  GPL30 ("gpl30", "GNU GENERAL PUBLIC LICENSE", new Version (3, 0), "http://www.gnu.org/licenses/gpl.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // LGPL
  LGPL20 ("lgpl20", "GNU LIBRARY GENERAL PUBLIC LICENSE", new Version (2, 0), "http://www.gnu.org/licenses/old-licenses/lgpl-2.0.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  LGPL21 ("lgpl21", "GNU LESSER GENERAL PUBLIC LICENSE", new Version (2, 1), "http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  LGPL30 ("lgpl30", "GNU LESSER GENERAL PUBLIC LICENSE", new Version (3, 0), "http://www.gnu.org/licenses/lgpl.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // Eclipse Public License
  EPL10 ("epl10", "Eclipse Public License", new Version (1, 0), "http://www.eclipse.org/legal/epl-v10.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // EUPL
  EUPL11 ("eupl11", "The European Union Public License", new Version (1, 1), "http://joinup.ec.europa.eu/software/page/eupl"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  EUPL12 ("eupl12", "The European Union Public License", new Version (1, 1), "http://joinup.ec.europa.eu/software/page/eupl"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  // Creative commons
  CCBY10 ("ccby10", "Creative Commons Attributation", new Version (1, 0), "http://creativecommons.org/licenses/by/1.0/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  CCBY20 ("ccby20", "Creative Commons Attributation", new Version (2, 0), "http://creativecommons.org/licenses/by/2.0/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  CCBY25 ("ccby25", "Creative Commons Attributation", new Version (2, 5), "http://creativecommons.org/licenses/by/2.5/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  CCBY30 ("ccby30", "Creative Commons Attributation", new Version (3, 0), "http://creativecommons.org/licenses/by/3.0/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  CCBYSA30 ("ccby-sa30", "Creative Commons Attributation-ShareAlike", new Version (3, 0), "http://creativecommons.org/licenses/by-sa/3.0/"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  CDDL1 ("CDDL1", "Common Development and Distribution License", new Version (1), "https://glassfish.dev.java.net/public/CDDLv1.0.html"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  // Public domain
  PUBLIC_DOMAIN ("pd", "Public Domain", null, null); //$NON-NLS-1$ //$NON-NLS-2$

  private final String m_sID;
  private final String m_sName;
  private final Version m_aVersion;
  private final String m_sURL;

  /**
   * Create a custom license.
   * 
   * @param sID
   *        The ID of the license.
   * @param sName
   *        The name of the license.
   * @param aVersion
   *        The version of the license.
   * @param sURL
   *        The URL of the license.
   */
  private ELicense (@Nonnull @Nonempty final String sID,
                    @Nonnull @Nonempty final String sName,
                    @Nullable final Version aVersion,
                    @Nullable final String sURL)
  {
    this.m_sID = sID;
    this.m_sName = sName;
    this.m_aVersion = aVersion;
    this.m_sURL = sURL;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getID ()
  {
    return this.m_sID;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return this.m_sName;
  }

  @Override
  @Nullable
  public Version getVersion ()
  {
    return this.m_aVersion;
  }

  @Override
  @Nullable
  public String getURL ()
  {
    return this.m_sURL;
  }

  @Nullable
  public static ELicense getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (ELicense.class, sID);
  }
}
