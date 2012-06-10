package com.phloc.commons.actor.experimental;


/**
 * An Actor superclass that provided access to a runtime helper.
 * 
 * @author BFEIGENB
 */
public abstract class AbstractTestableActor extends AbstractActor
{
  DefaultActorTest m_aActorTest;

  public AbstractTestableActor ()
  {}

  public DefaultActorTest getActorTest ()
  {
    return m_aActorTest;
  }

  public void setActorTest (final DefaultActorTest actorTest)
  {
    m_aActorTest = actorTest;
  }
}
