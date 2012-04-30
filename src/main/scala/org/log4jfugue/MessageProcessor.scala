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
import actors.Actor
import org.scala_tools.subcut.inject.{BindingModule, Injectable, AutoInjectable}

/** Holds the association between log messages and instruments */
case class MessageMap (logMessage: String, instrumentName: String, midiVoice: Int)

/**
 * The only class to manipulate the message accumulator.  In response to Actor
 * messages from the DataGetter this class filters and increments message counts.
 * It is constructed with an implicit SubCut binding module.
 */
class MessageProcessor() extends Actor with AutoInjectable {
  val messages = injectOptional[List[MessageMap]] ('instrumentMessages).getOrElse(Nil)
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

  def act() {
    loop {
      react {
        case "exit"  => {println("MessageProcessor Exiting"); exit()}
        case "nextData" =>
          reply(currentSecond.clone())
          for (x <- 1 to 15 ) currentSecond(x) = 0
        case msg:String =>
           //messages.filter(msg contains _.logMessage).foreach(m => currentSecond(m.midiVoice) += 1)
           for(m <- messages; if msg contains m.logMessage) currentSecond(m.midiVoice) += 1
        case _ => println("unexpected message type")
      }
    }
  }
}