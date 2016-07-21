package org.log4jfugue
/*
 * Log4JFugue - Application Sonification
 * Copyright (C) 2011-2016  Brian Tarbox
 *
 * http://www.log4jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
import scala.concurrent.Await
import akka.actor.ActorSystem
import org.jfugue.Player
import org.scalatest._
import akka.pattern.ask
import scala.concurrent.duration._
import org.log4jfugue.MessageProcessor.Msg

class SimpleSoundBuilderTest extends FreeSpec with BaseAkkaSpec {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

  "Test SimpleSoundBuilder" - {
    "yup, test it" in {
      println("START TESTING SOUND BUILDER")
      val soundBuilder =  new SimpleSoundBuilder()
      val currentSecond = Array[Int](0,0,0,0,0,0,0,0)
      currentSecond(1) = 2
      currentSecond(2) = 3
      val messages = List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
        MessageMap("stream delete", "snareDrum", 2),
        MessageMap("stream pause", "tomtom", 3),
        MessageMap("stream trick", "cymbal", 4),
        MessageMap("stream failure", "crash", 5))
      val playStr = soundBuilder.buildMusicString(messages, currentSecond)
      assert(playCount(playStr,"baseDrum") == 2)
      assert(playCount(playStr,"snareDrum") == 3)

      currentSecond(3) = 4
      currentSecond(4) = 6
      currentSecond(5) = 8
      currentSecond(6) = 16
      val playStr2 = soundBuilder.buildMusicString(messages, currentSecond)
      assert(playCount(playStr2,"tomtom") == 4)
      assert(playCount(playStr2,"cymbal") == 6)
      assert(playCount(playStr2,"crash") == 8)

      println("passed sound builder tests")
    }
  }
//      test ("Test SimpleSoundBuilder") {
//    val bindingModule =  new NewBindingModule({ implicit module =>
//       import module._
//       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0)
//    })
//    val soundBuilder =  new SimpleSoundBuilder()(bindingModule)
//
//    currentSecond(1) = 2
//    currentSecond(2) = 3
//    val messages = List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
//                                     MessageMap("stream delete", "snareDrum", 2),
//                                     MessageMap("stream pause", "tomtom", 3),
//                                     MessageMap("stream trick", "cymbal", 4),
//                                     MessageMap("stream failure", "crash", 5))
//    val playStr = soundBuilder.buildMusicString(messages, currentSecond)
//    playCount(playStr, "baseDrum") should be (2)
//    playCount(playStr, "snareDrum") should be (3)
//
//    currentSecond(3) = 4
//    currentSecond(4) = 6
//    currentSecond(5) = 8
//    currentSecond(6) = 16
//    val playStr2 = soundBuilder.buildMusicString(messages, currentSecond)
//    playCount(playStr2, "tomtom") should be (4)
//    playCount(playStr2, "cymbal") should be (6)
//    playCount(playStr2, "crash") should be (8)
//  }

  def playCount(playStr: String, instrument: String):Int = {
    val start = playStr.indexOf(instrument)
    val stop  = playStr.indexOf(" I", start)
    println("start:" + start + " stop:" + stop)
    playStr.substring(start, if(stop == -1) playStr.length else stop).count(_ == '/')
  }
}