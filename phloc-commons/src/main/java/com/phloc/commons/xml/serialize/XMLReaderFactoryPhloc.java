package com.phloc.commons.xml.serialize;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.annotation.Nonnull;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.phloc.commons.priviledged.PrivilegedActionSystemGetProperty;

/**
 * Factory for creating an XML reader. <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em> See <a
 * href='http://www.saxproject.org'>http://www.saxproject.org</a> for further
 * information. </blockquote>
 * <p>
 * This class contains static methods for creating an XML reader from an
 * explicit class name, or based on runtime defaults:
 * </p>
 * 
 * <pre>
 * try
 * {
 *   XMLReader myReader = XMLReaderFactory.createXMLReader ();
 * }
 * catch (SAXException e)
 * {
 *   System.err.println (e.getMessage ());
 * }
 * </pre>
 * <p>
 * <strong>Note to Distributions bundled with parsers:</strong> You should
 * modify the implementation of the no-arguments <em>createXMLReader</em> to
 * handle cases where the external configuration mechanisms aren't set up. That
 * method should do its best to return a parser when one is in the class path,
 * even when nothing bound its class name to <code>org.xml.sax.driver</code> so
 * those configuration mechanisms would see it.
 * </p>
 * <p>
 * Copied from JDK 1.6.0_45 and modified by phloc to work around an error when
 * the class name was read from a JAR file and this method is invoked a second
 * time!
 * </p>
 * 
 * @since SAX 2.0
 * @author David Megginson, David Brownell
 * @version 2.0.1 (sax2r2)
 */
public final class XMLReaderFactoryPhloc
{
  /**
   * This class is duplicated for each JAXP subpackage so keep it in sync. It is
   * package private and therefore is not exposed as part of the JAXP API.
   * Security related methods that only work on J2SE 1.2 and newer.
   */
  private static final class SecuritySupport
  {
    ClassLoader getContextClassLoader () throws SecurityException
    {
      return AccessController.doPrivileged (new PrivilegedAction <ClassLoader> ()
      {
        public ClassLoader run ()
        {
          ClassLoader cl = Thread.currentThread ().getContextClassLoader ();
          if (cl == null)
            cl = ClassLoader.getSystemClassLoader ();
          return cl;
        }
      });
    }

    String getSystemProperty (final String propName)
    {
      return AccessController.doPrivileged (new PrivilegedActionSystemGetProperty (propName));
    }

    InputStream getResourceAsStream (final ClassLoader cl, final String name)
    {
      return AccessController.doPrivileged (new PrivilegedAction <InputStream> ()
      {
        public InputStream run ()
        {
          InputStream ris;
          if (cl == null)
            ris = Object.class.getResourceAsStream (name);
          else
            ris = cl.getResourceAsStream (name);
          return ris;
        }
      });
    }
  }

  /**
   * Create a new instance of a class by name. <blockquote>
   * <em>This module, both source code and documentation, is in the
   * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em> See <a
   * href='http://www.saxproject.org'>http://www.saxproject.org</a> for further
   * information. </blockquote>
   * <p>
   * This class contains a static method for creating an instance of a class
   * from an explicit class name. It tries to use the thread's context
   * ClassLoader if possible and falls back to using Class.forName(String).
   * </p>
   * <p>
   * This code is designed to compile and run on JDK version 1.1 and later
   * including versions of Java 2.
   * </p>
   * 
   * @author Edwin Goei, David Brownell
   * @version 2.0.1 (sax2r2)
   */
  private static final class NewInstance
  {
    private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";

    /**
     * Creates a new instance of the specified class name Package private so
     * this code is not exposed at the API level.
     */
    static Object newInstance (final ClassLoader classLoader, final String className) throws ClassNotFoundException,
                                                                                     IllegalAccessException,
                                                                                     InstantiationException
    {
      // make sure we have access to restricted packages
      boolean internal = false;
      if (System.getSecurityManager () != null)
        if (className != null && className.startsWith (DEFAULT_PACKAGE))
          internal = true;

      Class <?> driverClass;
      if (classLoader == null || internal)
        driverClass = Class.forName (className);
      else
        driverClass = classLoader.loadClass (className);
      return driverClass.newInstance ();
    }
  }

  /**
   * Private constructor.
   * <p>
   * This constructor prevents the class from being instantiated.
   * </p>
   */
  private XMLReaderFactoryPhloc ()
  {}

  private static final String property = "org.xml.sax.driver";
  private static SecuritySupport ss = new SecuritySupport ();

  private static boolean _jarread = false;
  private static String _jarreadClassname;

  /**
   * Attempt to create an XMLReader from system defaults. In environments which
   * can support it, the name of the XMLReader class is determined by trying
   * each these options in order, and using the first one which succeeds:</p>
   * <ul>
   * <li>If the system property <code>org.xml.sax.driver</code> has a value,
   * that is used as an XMLReader class name.</li>
   * <li>The JAR "Services API" is used to look for a class name in the
   * <em>META-INF/services/org.xml.sax.driver</em> file in jarfiles available to
   * the runtime.</li>
   * <li>SAX parser distributions are strongly encouraged to provide a default
   * XMLReader class name that will take effect only when previous options (on
   * this list) are not successful.</li>
   * </ul>
   * <p>
   * In environments such as small embedded systems, which can not support that
   * flexibility, other mechanisms to determine the default may be used.
   * </p>
   * <p>
   * Note that many Java environments allow system properties to be initialized
   * on a command line. This means that <em>in most cases</em> setting a good
   * value for that property ensures that calls to this method will succeed,
   * except when security policies intervene. This will also maximize
   * application portability to older SAX environments, with less robust
   * implementations of this method.
   * </p>
   * 
   * @return A new XMLReader.
   * @exception org.xml.sax.SAXException
   *            If no default XMLReader class can be identified and
   *            instantiated.
   * @see #createXMLReader(java.lang.String)
   */
  @Nonnull
  public static XMLReader createXMLReader () throws SAXException
  {
    String className = null;
    ClassLoader cl = ss.getContextClassLoader ();

    // 1. try the JVM-instance-wide system property
    try
    {
      className = ss.getSystemProperty (property);
    }
    catch (final RuntimeException e)
    { /* continue searching */}

    // 2. if that fails, try META-INF/services/
    if (className == null)
    {
      if (!_jarread)
      {
        _jarread = true;
        final String service = "META-INF/services/" + property;
        InputStream in;
        BufferedReader reader;

        try
        {
          if (cl != null)
          {
            in = ss.getResourceAsStream (cl, service);

            // If no provider found then try the current ClassLoader
            if (in == null)
            {
              cl = null;
              in = ss.getResourceAsStream (cl, service);
            }
          }
          else
          {
            // No Context ClassLoader, try the current ClassLoader
            in = ss.getResourceAsStream (null, service);
          }

          if (in != null)
          {
            reader = new BufferedReader (new InputStreamReader (in, "UTF8"));
            className = reader.readLine ();
            in.close ();
            // Remember the read class name
            _jarreadClassname = className;
          }
        }
        catch (final Exception e)
        {}
      }
      else
      {
        // Use the previously read class name - may be null!
        className = _jarreadClassname;
      }
    }

    // 3. Distro-specific fallback
    if (className == null)
    {
      // EXAMPLE:
      // className = "com.example.sax.XmlReader";
      // or a $JAVA_HOME/jre/lib/*properties setting...
      className = "com.sun.org.apache.xerces.internal.parsers.SAXParser";
    }

    // do we know the XMLReader implementation class yet?
    return _loadClass (cl, className);
  }

  /**
   * Attempt to create an XML reader from a class name.
   * <p>
   * Given a class name, this method attempts to load and instantiate the class
   * as an XML reader.
   * </p>
   * <p>
   * Note that this method will not be usable in environments where the caller
   * (perhaps an applet) is not permitted to load classes dynamically.
   * </p>
   * 
   * @return A new XML reader.
   * @exception org.xml.sax.SAXException
   *            If the class cannot be loaded, instantiated, and cast to
   *            XMLReader.
   * @see #createXMLReader()
   */
  @Nonnull
  public static XMLReader createXMLReader (final String className) throws SAXException
  {
    return _loadClass (ss.getContextClassLoader (), className);
  }

  @Nonnull
  private static XMLReader _loadClass (final ClassLoader loader, final String className) throws SAXException
  {
    try
    {
      return (XMLReader) NewInstance.newInstance (loader, className);
    }
    catch (final ClassNotFoundException e1)
    {
      throw new SAXException ("SAX2 driver class " + className + " not found", e1);
    }
    catch (final IllegalAccessException e2)
    {
      throw new SAXException ("SAX2 driver class " + className + " found but cannot be loaded", e2);
    }
    catch (final InstantiationException e3)
    {
      throw new SAXException ("SAX2 driver class " +
                              className +
                              " loaded but cannot be instantiated (no empty public constructor?)", e3);
    }
    catch (final ClassCastException e4)
    {
      throw new SAXException ("SAX2 driver class " + className + " does not implement XMLReader", e4);
    }
  }
}
