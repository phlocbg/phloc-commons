/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.actor.experimental;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An ActorManager manages a set of actors. Managers route messages to actors
 * and assign threads to actors to process messages.
 * 
 * @author bfeigenb
 * @See {@link IActor}
 * @See {@link IActorMessage}
 */
public interface IActorManager
{
  /**
   * Create an actor.
   * 
   * @param clazz
   *        class of the actor. must have no argument constructor
   * @param name
   *        name to assign to the actor; must be unique for this manager
   * @return the actor
   */
  IActor createActor (Class <? extends IActor> clazz, String name);

  /**
   * Create an actor then start it.
   * 
   * @param clazz
   *        class of the actor. must have no argument constructor
   * @param name
   *        name to assign to the actor; must be unique for this manager
   * @return the actor
   */
  IActor createAndStartActor (Class <? extends IActor> clazz, String name);

  /**
   * Create an actor.
   * 
   * @param clazz
   *        class of the actor. must have no argument constructor
   * @param name
   *        name to assign to the actor; must be unique for this manager
   * @param options
   *        dependent parameters
   * @return the actor
   */
  IActor createActor (Class <? extends IActor> clazz, String name, Map <String, Object> options);

  /**
   * Create an actor then start it.
   * 
   * @param clazz
   *        class of the actor. must have no argument constructor
   * @param name
   *        name to assign to the actor; must be unique for this manager
   * @param options
   *        dependent parameters
   * @return the actor
   */
  IActor createAndStartActor (Class <? extends IActor> clazz, String name, Map <String, Object> options);

  /**
   * Start an actor.
   * 
   * @param a
   *        the actor
   */
  void startActor (IActor a);

  /**
   * Detach an actor. This actor is no longer managed by this manger and will
   * not receive and more messages.
   * 
   * @param actor
   *        the actor
   */
  void detachActor (IActor actor);

  /**
   * Send a message to an actor. The message will be processed at a later time.
   * 
   * @param message
   *        the message
   * @param from
   *        the source actor; may be null
   * @param to
   *        the target actor
   * @return number of actors that accepted the send
   */
  int send (IActorMessage message, IActor from, IActor to);

  /**
   * Send a message to a set of actors. The message will be processed at a later
   * time.
   * 
   * @param message
   *        the message
   * @param from
   *        the source actor; may be null
   * @param to
   *        the target actors
   * @return number of actors that accepted the send
   */
  int send (IActorMessage message, IActor from, IActor [] to);

  /**
   * Send a message to a set of actors. The message will be processed at a later
   * time.
   * 
   * @param message
   *        the message
   * @param from
   *        the source actor; may be null
   * @param to
   *        the target actors
   * @return number of actors that accepted the send
   */
  int send (IActorMessage message, IActor from, Collection <IActor> to);

  /**
   * Send a message to an actor in the category. The message will be processed
   * at a later time.
   * 
   * @param message
   *        the message
   * @param from
   *        the source actor; may be null
   * @param category
   *        category of the target actor
   * @return number of actors that accepted the send
   */
  int send (IActorMessage message, IActor from, String category);

  /**
   * Send a message to all actors. The message will be processed at a later
   * time.
   * 
   * @param message
   *        the message
   * @param from
   *        the source actor; may be null
   * @return number of actors that accepted the send
   */
  int broadcast (IActorMessage message, IActor from);

  /**
   * Get the categories that currently have actors.
   * 
   * @return category list
   */
  Set <String> getCategories ();

  /**
   * Initialize this manager. Should only be done once.
   */
  void initialize ();

  /**
   * Initialize this manager. Should only be done once.
   * 
   * @param options
   *        manger dependent parameters
   */
  void initialize (Map <String, Object> options);

  /**
   * Terminate this manager. Wait until all processing threads have stopped.
   */
  void terminateAndWait ();

  /**
   * Terminate this manager. Do not wait until all processing threads have
   * stopped.
   */
  void terminate ();

  /**
   * Get the count of actors.
   * 
   * @param type
   *        actor type; all if null
   * @return count of actors
   */
  int getActorCount (Class <? extends IActor> type);
}
