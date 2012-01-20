package org.log4jfugue
import org.scalatest.matchers.ShouldMatchers
import org.scala_tools.subcut.inject._
import io.Source
import org.scalatest.{FunSuite, SeveredStackTraces}

class SimpleSoundBuilderTest extends FunSuite with ShouldMatchers {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  test ("Test SimpleSoundBuilder") {
    val bindingModule =  new NewBindingModule({ module =>
       implicit val bindingModule = module
       import module._
       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0)
    })
    val soundBuilder =  new SimpleSoundBuilder()(bindingModule)

    currentSecond(1) = 2
    currentSecond(2) = 3
    val messages = List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
                                     MessageMap("stream delete", "snareDrum", 2),
                                     MessageMap("stream pause", "tomtom", 3),
                                     MessageMap("stream trick", "cymbal", 4),
                                     MessageMap("stream failure", "crash", 5))
    val playStr = soundBuilder.buildMusicString(messages, currentSecond)
    playCount(playStr, "baseDrum") should be (2)
    playCount(playStr, "snareDrum") should be (3)

    currentSecond(3) = 4
    currentSecond(4) = 6
    currentSecond(5) = 8
    currentSecond(6) = 16
    val playStr2 = soundBuilder.buildMusicString(messages, currentSecond)
    playCount(playStr2, "tomtom") should be (4)
    playCount(playStr2, "cymbal") should be (6)
    playCount(playStr2, "crash") should be (8)
  }

  def playCount(playStr: String, instrument: String):Int = {
    val start = playStr.indexOf(instrument)
    val stop  = playStr.indexOf(" I", start)
    println("start:" + start + " stop:" + stop)
    playStr.substring(start, if(stop == -1) playStr.length else stop).count(_ == '/')
  }
}