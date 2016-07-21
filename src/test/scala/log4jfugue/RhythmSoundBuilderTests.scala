package org.log4jfugue
/*
 * Log4JFugue - Application Sonification
 * Copyright (C) 2011-2012  Brian Tarbox
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

class RhythmSoundBuilderTests extends FreeSpec with BaseAkkaSpec {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  "Test RhythmSoundBuilder" - {
    "yup, test it" in {
      println("START TESTING RHYTHM SOUND BUILDER")
      val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
      val soundBuilder = new RhythmSoundBuilder()

      val messages = List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
        MessageMap("stream delete", "snareDrum", 2),
        MessageMap("stream pause", "tomtom", 3),
        MessageMap("stream trick", "cymbal", 4),
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

      for (x <- 1 to 14 ) currentSecond(x) = x

      val rhythm = soundBuilder.buildRhythm(messages, currentSecond)
      assert(rhythm.getLayer(1).equals("1..............."))
      assert(rhythm.getLayer(2).equals("2.......2......."))
      assert(rhythm.getLayer(3).equals("3....3....3....."))
      assert(rhythm.getLayer(4).equals("4...4...4...4..."))
      assert(rhythm.getLayer(5).equals("5..5..5..5..5..."))
      assert(rhythm.getLayer(6).equals("6.6..6..6.6..6.."))
      assert(rhythm.getLayer(7).equals("7.7.7.7..7.7.7.."))
      assert(rhythm.getLayer(8).equals("8.8.8.8.8.8.8.8."))
      assert(rhythm.getLayer(9).equals("99.9.9.99.9.9.9."))
      assert(rhythm.getLayer(10).equals("AA.AA.A.AA.AA.A."))
      assert(rhythm.getLayer(11).equals("BBB.BB.BB.BB.BB."))
      assert(rhythm.getLayer(12).equals("CCC.CCC.CCC.CCC."))
      assert(rhythm.getLayer(13).equals("DDDDD.DDDD.DDDD."))
      assert(rhythm.getLayer(14).equals("EEEEEEE.EEEEEEE."))
    }
  }

//  test ("Test Rhythm SimpleSoundBuilder") {
//    val bindingModule =  new NewBindingModule({ implicit module =>
//       import module._
//       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
//    })
//    val soundBuilder = new RhythmSoundBuilder()(bindingModule)
//
//    val messages = List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
//                                     MessageMap("stream delete", "snareDrum", 2),
//                                     MessageMap("stream pause", "tomtom", 3),
//                                     MessageMap("stream trick", "cymbal", 4),
//                                     MessageMap("stream failure", "crash", 5),
//                                     MessageMap("stream pause", "tomtom2", 6),
//                                     MessageMap("stream trick", "cymbal2", 7),
//                                     MessageMap("stream failure", "crash2", 8),
//                                     MessageMap("stream failure", "crash3", 9),
//                                     MessageMap("stream failure", "crash4", 10),
//                                     MessageMap("stream failure", "crash5", 11),
//                                     MessageMap("stream failure", "crash6", 12),
//                                     MessageMap("stream failure", "crash7", 13),
//                                     MessageMap("stream failure", "crash8", 14))
//
//
//    for (x <- 1 to 14 ) currentSecond(x) = x
//
//    val rhythm = soundBuilder.buildRhythm(messages, currentSecond)
//    rhythm.getLayer(1) should be ("1...............")
//    rhythm.getLayer(2) should be ("2.......2.......")
//    rhythm.getLayer(3) should be ("3....3....3.....")
//    rhythm.getLayer(4) should be ("4...4...4...4...")
//    rhythm.getLayer(5) should be ("5..5..5..5..5...")
//    rhythm.getLayer(6) should be ("6.6..6..6.6..6..")
//    rhythm.getLayer(7) should be ("7.7.7.7..7.7.7..")
//    rhythm.getLayer(8) should be ("8.8.8.8.8.8.8.8.")
//    rhythm.getLayer(9) should be ("99.9.9.99.9.9.9.")
//    rhythm.getLayer(10) should be ("AA.AA.A.AA.AA.A.")
//    rhythm.getLayer(11) should be ("BBB.BB.BB.BB.BB.")
//    rhythm.getLayer(12) should be ("CCC.CCC.CCC.CCC.")
//    rhythm.getLayer(13) should be ("DDDDD.DDDD.DDDD.")
//    rhythm.getLayer(14) should be ("EEEEEEE.EEEEEEE.")
//  }

}