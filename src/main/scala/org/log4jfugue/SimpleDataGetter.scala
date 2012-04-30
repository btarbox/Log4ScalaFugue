package org.log4jfugue
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

