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
import org.jfugue._
import java.lang.StringBuffer

/**
 * Builds a set of rhythm layers, one per instrument, which are then combined
 * into a JFugue Pattern that is played.
 */
class RhythmSoundBuilder()extends Thread with SoundBuilder {
  var bla = 1
  
  def buildAndPlayMusic(messages: List[MessageMap], currentSecond: Accumulator) = {
    for (x <- 1 to 15 )print(currentSecond(x) + ",")
    println(" ")
    val rhythm = buildRhythm(messages, currentSecond)
    val pattern: PatternInterface = rhythm.getPattern
    pattern.repeat(1)
    println("play iteration " + bla + " " + pattern.toString())
    bla = bla + 1
    player.play(pattern)  // blocks for one second
  }

  def buildRhythm(messages: List[MessageMap], currentSecond: Accumulator) : Rhythm = {
    val rhythm = createInstrumentAliases()
    messages.filter(x => currentSecond(x.midiVoice) > 0).foreach{m =>
      val layerString = buildLayerString(m.midiVoice, currentSecond(m.midiVoice))
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
    println(buf.toString)
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