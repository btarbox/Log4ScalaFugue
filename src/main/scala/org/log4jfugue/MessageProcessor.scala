package org.log4jfugue
/*
 * Log4JFugue - Application Sonification
 * Copyright (C) 2011-2016  Brian Tarbox
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
import akka.actor._

/** Holds the association between log messages and instruments */
case class MessageMap (logMessage: String, instrumentName: String, midiVoice: Int)

object MessageProcessor {
  case class ExitMsg(msg: String)
  case object NextData
  case class Msg(msg: String)

  def props(id: Int): Props = Props(new MessageProcessor(1))
}

/**
 * The only class to manipulate the message accumulator.  In response to Actor
 * messages from the DataGetter this class filters and increments message counts.
 */
class MessageProcessor(id: Int) extends Actor with ActorLogging {
  import MessageProcessor._
  val messages = L4JFCloud.messages
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

  override def receive:Receive = {
    case ExitMsg => {
      println("Message Processor got ExitMsg")
      System.exit(1)
    }

    case NextData => {
      println("Message Processor got NextData")
      sender() ! currentSecond.clone()
      for (x <- 1 to 15) currentSecond(x) = 0
    }

    case Msg(msg) => {
      println("Message Processor got Msg")
      for (m <- messages; if msg contains m.logMessage) currentSecond(m.midiVoice) += 1
    }

    case x: Any => println(s"Message Processor got unexpected message ${x}")
  }

}