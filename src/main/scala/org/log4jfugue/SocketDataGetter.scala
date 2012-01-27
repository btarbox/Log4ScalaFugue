package org.log4jfugue
import org.scala_tools.subcut.inject.{BindingModule, Injectable, AutoInjectable}
import java.net.ServerSocket
import java.lang.Thread

class SocketDataGetter()(override implicit val bindingModule: BindingModule) extends SimpleDataGetter {
  val port = injectIfBound[Int]('port){4445}

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