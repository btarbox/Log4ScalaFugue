package org.log4jfugue
/*
 * Log4JFugue - Application Sonification
 * Copyright (C) 2011-2012  Brian Tarbox
 *
 * http://www.log4jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
import org.jfugue.Player
import org.scala_tools.subcut.inject.{BindingModule, Injectable}

/** This is the base trait that the various SoundBuilders extend.
 *  It captures the logic of getting the accumulator data from the MessageProcessor
 */
trait SoundBuilder extends Thread with Injectable {
  implicit val bindingModule: BindingModule
  lazy val messageProcessor = injectOptional[MessageProcessor].getOrElse(new MessageProcessor)
  lazy val player           = injectOptional[Player].getOrElse( new DummyPlayer)
  lazy val messages         = injectOptional[List[MessageMap]] ('instrumentMessages).getOrElse(Nil)
  type Accumulator = Array[Int]
  var keepRunning = true

  override def run {
    println("constructed messages as:" + messages)
    while(keepRunning) {
      val currentSecond = messageProcessor !? ("nextData") match { case e: Accumulator => e}
      buildAndPlayMusic(messages, currentSecond)
    }
  }

  def buildAndPlayMusic(messages: List[MessageMap], currentSecond: Accumulator)
}
