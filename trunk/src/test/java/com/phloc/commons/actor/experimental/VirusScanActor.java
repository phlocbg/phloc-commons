package com.phloc.commons.actor.experimental;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An actor that does a simplified "Virus" (actually files withmultiple text
 * lines) scan of a directory tree.
 * 
 * @author BFEIGENB
 */
public class VirusScanActor extends AbstractTestableActor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (VirusScanActor.class);

  @Override
  protected void runBody ()
  {
    // s_aLogger.trace("TestActor:%s runBody: %s", getName(), this);
    DefaultActorTest.sleeper (1);
    final DefaultActorMessage m = new DefaultActorMessage ("init", null);
    getManager ().send (m, null, this);
  }

  @Override
  protected void loopBody (final IActorMessage aMessage)
  {
    try
    {
      // s_aLogger.trace("TestActor:%s loopBody %s: %s", getName(), m, this);
      DefaultActorTest.sleeper (1);
      final String sSubject = aMessage.getSubject ();
      final DefaultActorManager aManager = getManager ();
      if ("scanDir".equals (sSubject))
      {
        final String dir = (String) aMessage.getData ();
        final File [] list = new File (dir).listFiles ();
        s_aLogger.trace ("Scanning directory %s...", dir);
        if (aManager.getCategorySize (getCategory ()) < 50)
        {
          createVirusScanActor (aManager);
        }
        for (final File file : list)
        {
          if (file.isDirectory ())
          {
            final DefaultActorMessage dm = new DefaultActorMessage ("scanDir", file.getCanonicalPath ());
            aManager.send (dm, this, this.getClass ().getSimpleName ());
          }
          else
            if (file.isFile () && file.getName ().toLowerCase ().endsWith (".txt"))
            {
              final DefaultActorMessage dm = new DefaultActorMessage ("scanFile", file.getCanonicalPath ());
              aManager.send (dm, this, getCategoryName ());
            }
        }
      }
      else
        if ("scanFile".equals (sSubject))
        {
          final String file = (String) aMessage.getData ();
          final File xfile = new File (file);
          final String xpath = xfile.getCanonicalPath ();
          s_aLogger.trace ("Scaning file %s...", xpath);
          byte [] ba = readBinaryFileContents (xfile);
          // TODO: check contents here is real app
          DefaultActorTest.sleeper (1);
          // s_aLogger.trace("**** Size %s:  %d", xpath, ba.length);
          int count = 0;
          for (final byte b : ba)
          {
            if (b == (byte) '\n')
            {
              count++;
            }
          }
          if (count > 10)
          {
            final DefaultActorMessage dm = new DefaultActorMessage ("virusFound",
                                                                    new Object [] { xpath, "more than 10 returns found" });
            aManager.send (dm, this, getCategoryName ());
          }
          ba = null;
        }
        else
          if ("virusFound".equals (sSubject))
          {
            final Object [] params = (Object []) aMessage.getData ();
            final String file = params.length > 0 ? (String) params[0] : null;
            final String message = params.length > 1 ? (String) params[1] : null;
            if (file != null)
            {
              final File xfile = new File (file);
              final String xpath = xfile.getCanonicalPath ();
              s_aLogger.trace ("**** suspect file; %s: %s", message, xpath);
            }
          }
          else
            if ("init".equals (sSubject))
            {
              final String startPath = (String) aMessage.getData ();
              if (startPath != null)
              {
                final DefaultActorMessage dm = new DefaultActorMessage ("scanDir", startPath);
                aManager.send (dm, this, getCategoryName ());
              }
            }
            else
            {
              s_aLogger.warn ("VirusScanActor:" + getName () + " loopBody unknown subject: " + sSubject);
            }
    }
    catch (final IOException e)
    {
      s_aLogger.error ("VirusScanActor exception", e);
    }
  }

  /** Read a file. */
  public static byte [] readBinaryFileContents (final String fileName) throws IOException
  {
    return readBinaryFileContents (new File ((fileName)));
  }

  /** Read a file. */
  public static byte [] readBinaryFileContents (final File file) throws IOException
  {
    final BufferedInputStream bis = new BufferedInputStream (new FileInputStream (file));
    try
    {
      return readBinaryStreamContents (bis);
    }
    finally
    {
      bis.close ();
    }
  }

  /** Read a stream. */
  public static byte [] readBinaryStreamContents (final InputStream is) throws IOException
  {
    return streamToByteArray (is);
  }

  /** Get the bytes from an input stream. */
  public static byte [] streamToByteArray (final InputStream pis) throws IOException
  {
    InputStream is = pis;
    if (!(is instanceof BufferedInputStream))
      is = new BufferedInputStream (is);
    final ByteArrayOutputStream baos = new ByteArrayOutputStream ();
    final byte [] buffer = new byte [4 * 1024];
    for (int count = is.read (buffer); count >= 0; count = is.read (buffer))
    {
      baos.write (buffer, 0, count);
    }
    return baos.toByteArray ();
  }

  public static VirusScanActor createVirusScanActor (final IActorManager manager)
  {
    final VirusScanActor res = (VirusScanActor) manager.createAndStartActor (VirusScanActor.class, getActorName ());
    res.setCategory (getCategoryName ());
    return res;
  }

  public static String getCategoryName ()
  {
    return VirusScanActor.class.getSimpleName ();
  }

  protected static int s_nActorCount;

  protected static String getActorName ()
  {
    return getCategoryName () + '_' + s_nActorCount++;
  }
}
