package org.log4jfugue
import org.scala_tools.subcut.inject.{BindingModule, Injectable}

/** Base class that provides hard coded set of messages */
class SimpleDataGetter() (implicit val bindingModule: BindingModule) extends Thread with Injectable {
  val messageProcessor = injectIfBound[MessageProcessor] {new MessageProcessor}
   override def run() {
     val sampleData = List("stream create\n", "stream create\n", "other message\n", "stream delete\n")
     while(true) {
       for(msg:String <- sampleData) {
          messageProcessor ! msg
          Thread.sleep(1000)
       }
     }
  }
}

