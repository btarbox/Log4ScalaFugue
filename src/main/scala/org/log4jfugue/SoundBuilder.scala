package org.log4jfugue
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
