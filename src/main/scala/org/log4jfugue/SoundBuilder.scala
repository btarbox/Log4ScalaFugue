package org.log4jfugue
import org.scala_tools.subcut.inject.{BindingModule, Injectable}
import org.jfugue.Player

/** This is the base trait that the various SoundBuilders extend.
 *  It captures the logic of getting the accumulator data from the MessageProcessor
 */
trait SoundBuilder extends Thread with Injectable {
  implicit val bindingModule: BindingModule
  lazy val messageProcessor = injectIfBound[MessageProcessor] { new MessageProcessor }
  lazy val player           = injectIfBound[Player] { new DummyPlayer}
  lazy val messages         = injectIfBound[List[MessageMap]] ('instrumentMessages) {Nil}
  type Accumulator = Array[Int]

  override def run {
    println("constructed messages as:" + messages)
    while(true) {
      val currentSecond = messageProcessor !? ("nextData") match { case e: Accumulator => e}
      buildAndPlayMusic(messages, currentSecond)
    }
  }

  def buildAndPlayMusic(messages: List[MessageMap], currentSecond: Accumulator)
}
