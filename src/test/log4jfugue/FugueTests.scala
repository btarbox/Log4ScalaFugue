package org.log4jfugue
import org.scalatest.matchers.ShouldMatchers
import org.scala_tools.subcut.inject._
import io.Source
import org.scalatest.{FunSuite, SeveredStackTraces}

class FugueTests extends FunSuite with ShouldMatchers {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

  test ("Test file data getter") {
    implicit val bindingModule =  new NewBindingModule({ implicit module =>
       //implicit val bindingModule = module
       import module._
       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
       bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log_20110128"
       bind[String] identifiedBy 'dateFormat  toSingle  "yyyy-MM-dd HH:mm:ss,SSS"
       bind[SimpleDataGetter] toSingle  new FileDataGetter()
    })

    val messages = List[MessageMap] (MessageMap("SimpleGlobalContentFactory.get", "baseDrum", 1),
                                     MessageMap("ContentAwarePumpSelector.useExistingAffinity", "snareDrum", 2),
                                     MessageMap("ECASimpleImpl.calcDiskUsedSize", "tomtom", 3),
                                     MessageMap("StreamPOJO.createStream", "cymbal", 4),
                                     MessageMap("stream failure", "crash", 5),
                                     MessageMap("stream pause", "tomtom2", 6),
                                     MessageMap("stream trick", "cymbal2", 7),
                                     MessageMap("stream failure", "crash2", 8),
                                     MessageMap("stream failure", "crash3", 9),
                                     MessageMap("stream failure", "crash4", 10),
                                     MessageMap("stream failure", "crash5", 11),
                                     MessageMap("stream failure", "crash6", 12),
                                     MessageMap("stream failure", "crash7", 13),
                                     MessageMap("stream failure", "crash8", 14))


    val fdg = new FileDataGetter()(bindingModule)
    val source = Source.fromFile("c:/Users/gqx487/Downloads/server.log_20110128")
    try {
      source getLines() foreach {line =>
        println("got line:" + line)
        fdg.delayProcessing(line)
      }
    }finally {
      source.close()
    }
  }
}