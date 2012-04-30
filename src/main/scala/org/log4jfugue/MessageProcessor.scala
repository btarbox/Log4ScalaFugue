package org.log4jfugue
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