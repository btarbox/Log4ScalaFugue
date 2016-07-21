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
import scala.concurrent.Await

import org.jfugue.Player
import akka.actor._
import akka.pattern.ask
import scala.concurrent.duration._

/** This is the base trait that the various SoundBuilders extend.
 *  It captures the logic of getting the accumulator data from the MessageProcessor
 */
trait SoundBuilder extends Thread  {
  // implicit val bindingModule: BindingModule
  lazy val messageProcessor = L4JFCloud.messageProcessor
  lazy val player           = L4JFCloud.player
  lazy val messages         = L4JFCloud.messages
  type Accumulator = Array[Int]
  var keepRunning = true
  val timeout = 4L
  implicit val askTime = akka.util.Timeout(timeout, SECONDS)

  override def run {
    println("constructed messages as:" + messages)
    while(keepRunning) {
      val currentSecondFuture = messageProcessor ?  MessageProcessor.NextData
      val currentSecond = Await.result(currentSecondFuture, 2 seconds) match {
        case e: Accumulator => e
      }

      //val currentSecond = messageProcessor ? ("nextData") match { case e: Accumulator => e}
      buildAndPlayMusic(messages, currentSecond)
    }
  }

  def buildAndPlayMusic(messages: List[MessageMap], currentSecond: Accumulator)
}
