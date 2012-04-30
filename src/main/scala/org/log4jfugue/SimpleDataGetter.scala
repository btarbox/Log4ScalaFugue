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
import org.scala_tools.subcut.inject.{AutoInjectable, BindingModule, Injectable}

/** Base class that provides hard coded set of messages */
class SimpleDataGetter() extends Thread with AutoInjectable {
  val messageProcessor = injectOptional[MessageProcessor].getOrElse(new MessageProcessor)
  val sampleData       = injectOptional[List[String]].getOrElse(List("stream create\n", "stream create\n", "other message\n", "stream delete\n"))
  var keepRunning = true

  override def run() {
    println("sample data is " + sampleData)
    for(msg:String <- sampleData) {
      messageProcessor ! msg
    }
    Thread.sleep(1000)
    messageProcessor ! "exit"
  }
}

