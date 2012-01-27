package org.log4jfugue
import org.jfugue._
import java.lang.StringBuffer
import org.scala_tools.subcut.inject.{AutoInjectable, BindingModule, Injectable}

/**
 * Builds a set of rhythm layers, one per instrument, which are then combined
 * into a JFugue Pattern that is played.
 */
class RhythmSoundBuilder()extends Thread with AutoInjectable with SoundBuilder {
  def buildAndPlayMusic(messages: List[MessageMap], currentSecond: Accumulator) = {
    val rhythm = buildRhythm(messages, currentSecond)
    val pattern = rhythm.getPattern
    pattern.repeat(1)
    player.play(pattern)  // blocks for one second
  }

  def buildRhythm(messages: List[MessageMap], currentSecond: Accumulator) : Rhythm = {
    val rhythm = createInstrumentAliases()
    messages.filter(x => currentSecond(x.midiVoice) > 0).foreach{m =>
      val layerString = buildLayerString(m.midiVoice, currentSecond(m.midiVoice))
      println("build layer string " + layerString)
      rhythm.setLayer(m.midiVoice, layerString)
    }
    rhythm.setLayer(15, "_._._._._._._.__") //creates a background beat so that user always hears something
    rhythm
  }

  def buildLayerString(layer: Int,  count:Int) : String = {
    val silentNote = "."
    val beatsPerMeasure = 16
    val beatsBetweenNotes : Double = 1.0 * beatsPerMeasure / count
    val buf = new StringBuffer((new StringBuilder(silentNote) * beatsPerMeasure))
    for(x <- 0 until count) buf.setCharAt(((x * beatsBetweenNotes).toInt), hexChar(layer))
    buf.toString
  }

  def createInstrumentAliases() : Rhythm = {
    val rhythm = new Rhythm()
    messages.foreach{m => rhythm.addSubstitution(hexChar(m.midiVoice), "[" + m.instrumentName + "]s") }
    rhythm.addSubstitution('.', "Rs");                    // empty notes matter too!
    rhythm.addSubstitution('_', "[ACOUSTIC_BASS_DRUM]s"); // to hold the beat
    rhythm
  }

  def hexChar(voice:Int) : Char = {
    val zero = '0'.toInt
    if(voice <10) (voice + '0').toChar else ('A' + (voice-10)).toChar
  }
}