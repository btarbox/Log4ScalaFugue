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
import org.jfugue._

class SimpleSoundBuilder()extends Thread with SoundBuilder {
  def buildAndPlayMusic(messages: List[MessageMap], currentSecond: Accumulator) = {
    player.play(buildMusicString(messages, currentSecond))
  }

  def buildMusicString(messages: List[MessageMap], currentSecond: Array[Int]) : String = {
    val buffer = new StringBuffer()
    messages.filter(x => currentSecond(x.midiVoice) > 0).foreach{m =>
      buffer.append("V" + m.midiVoice + " I[" + m.instrumentName + "]" + buildTempoString(m.midiVoice, currentSecond(m.midiVoice)))
    }
    println("built a music string:" + buffer)
    buffer.toString
  }

  def buildTempoString(voiceNumber: Int, count: Int): String = {
    val note = limitedString("A6/" + 1.0 / count, 8)
    val tempoBuffer = new StringBuffer()
    for (i <- 1 to count) tempoBuffer.append(note + " ")
    tempoBuffer.toString
  }

  def limitedString(str:String, maxLength:Int) = {
    val len = if(str.length > maxLength) maxLength else str.length()
    str.substring(0, len)
  }
}
