package org.log4jfugue
import org.scala_tools.subcut.inject.{BindingModule, Injectable}

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