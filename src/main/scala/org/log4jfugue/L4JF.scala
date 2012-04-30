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
import org.scala_tools.subcut.inject.{BindingModule, Injectable, AutoInjectable}

object L4JF {
  def main(args: Array[String]) {
    implicit val bindingModule = Configuration  // makes subcut magic work
    val l4jf = new L4JF
  }
}

class L4JF (implicit val bindingModule: BindingModule)extends Thread with Injectable {
   lazy val messageProcessor = injectOptional[MessageProcessor].getOrElse(new MessageProcessor())
   lazy val soundBuilder     = injectOptional[SoundBuilder].getOrElse(new SimpleSoundBuilder())
   lazy val dataGetter       = injectOptional[SimpleDataGetter].getOrElse(new SimpleDataGetter())

  soundBuilder.start()
  messageProcessor.start()
  dataGetter.start()

  dataGetter.join()
  messageProcessor ! "exit"
  soundBuilder.keepRunning = false
  dataGetter.stop()
}