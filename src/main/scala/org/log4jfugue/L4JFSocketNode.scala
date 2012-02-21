package org.log4jfugue
import org.scala_tools.subcut.inject.{Injectable, BindingModule}
import java.net.{SocketException, Socket}
import java.io.{IOException, EOFException, BufferedInputStream, ObjectInputStream}
import org.apache.log4j.spi._

/**
 *  This class is based on the log4j class SocketNode and we would have just
 *  extended it but the key member variables were defined as package private
 *  and so were not accessible to derived classes.
 *
 *  Read {@link LoggingEvent} objects sent from a remote client using
 *  Sockets (TCP).
 */
class L4JFSocketNode(socket: Socket)(override implicit val bindingModule: BindingModule) extends Runnable with Injectable {
  val messageProcessor = injectOptional[MessageProcessor].getOrElse(new MessageProcessor)
  
  override def run() {
    try {
      val ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream))
      while(true) {
        val event = ois.readObject() match {
          case x:LoggingEvent => messageProcessor ! x.getMessage()
          case _              => println("msg type error")
        }
      }
    } catch {
      case e:EOFException    => println("got EOF exception")
      case e:SocketException => println("got Socket exception")
      case e:IOException     => println("got IOException exception")
      case _                 => println("other exception")
    }
  }
}