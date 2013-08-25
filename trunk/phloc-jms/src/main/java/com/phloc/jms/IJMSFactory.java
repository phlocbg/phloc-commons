package com.phloc.jms;

import javax.jms.Connection;
import javax.jms.JMSException;

public interface IJMSFactory
{
  Connection createConnection () throws JMSException;

  Connection createConnection (boolean bStartConnection) throws JMSException;

  void shutdown ();
}
