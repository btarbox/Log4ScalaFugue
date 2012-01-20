package org.log4jfugue
import org.scalatest.matchers.ShouldMatchers
import org.scala_tools.subcut.inject._
import org.scalatest.{FunSuite, SeveredStackTraces}

class RhythmSoundBuilderTests extends FunSuite with ShouldMatchers {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

  test ("Test Rhythm SimpleSoundBuilder") {
    val bindingModule =  new NewBindingModule({ module =>
       implicit val bindingModule = module
       import module._
       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    })
    val soundBuilder = new RhythmSoundBuilder()(bindingModule)

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
    rhythm.getLayer(1) should be ("1...............")
    rhythm.getLayer(2) should be ("2.......2.......")
    rhythm.getLayer(3) should be ("3....3....3.....")
    rhythm.getLayer(4) should be ("4...4...4...4...")
    rhythm.getLayer(5) should be ("5..5..5..5..5...")
    rhythm.getLayer(6) should be ("6.6..6..6.6..6..")
    rhythm.getLayer(7) should be ("7.7.7.7..7.7.7..")
    rhythm.getLayer(8) should be ("8.8.8.8.8.8.8.8.")
    rhythm.getLayer(9) should be ("99.9.9.99.9.9.9.")
    rhythm.getLayer(10) should be ("AA.AA.A.AA.AA.A.")
    rhythm.getLayer(11) should be ("BBB.BB.BB.BB.BB.")
    rhythm.getLayer(12) should be ("CCC.CCC.CCC.CCC.")
    rhythm.getLayer(13) should be ("DDDDD.DDDD.DDDD.")
    rhythm.getLayer(14) should be ("EEEEEEE.EEEEEEE.")
  }

}