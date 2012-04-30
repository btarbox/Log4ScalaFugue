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
import java.net.ServerSocket
import java.lang.Thread

class SocketDataGetter()(override implicit val bindingModule: BindingModule) extends SimpleDataGetter {
  val port = injectOptional[Int]('port).getOrElse(4445)

  override def run() {
    try {
      val serverSocket = new ServerSocket(port)
      val socket = serverSocket.accept()
      val thread = new Thread(new L4JFSocketNode(socket))
      thread.start()  // listen for log4j connection
      thread.join()
    }catch {
      case _ => println("got exception")
    }
  }
}