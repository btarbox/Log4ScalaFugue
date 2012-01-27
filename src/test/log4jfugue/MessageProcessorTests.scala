package org.log4jfugue
import org.scalatest.matchers.ShouldMatchers
import org.scala_tools.subcut.inject._
import org.scalatest.{FunSuite, SeveredStackTraces}

class MessageProcessorTests extends FunSuite with ShouldMatchers {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  implicit val bindingModule =  new NewBindingModule({ module =>
     implicit val bindingModule = module
     import module._
     bind[List[MessageMap]] identifiedBy  'instrumentMessages toSingle
       List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
                         MessageMap("stream delete", "snareDrum", 2),
                         MessageMap("MessageParser", "cow-bell", 3),
                         MessageMap("RtspClient", "cymbal", 4)
       )
  })

  test ("Test message processor") {
    val mp = new MessageProcessor()
    mp.start()
    var currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))
    
    mp ! "stream create"
    mp ! "RtspClient"
    currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0))

    // should be back to zero
    currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))


    mp ! "RtspClient"
    mp ! "RtspClient"
    currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0))
    mp  ! "exit"
    Thread.sleep(1000)
  }



}