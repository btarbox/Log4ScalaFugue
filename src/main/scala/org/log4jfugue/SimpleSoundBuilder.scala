package org.log4jfugue
import org.jfugue._
import org.scala_tools.subcut.inject.{BindingModule, Injectable}

class SimpleSoundBuilder()(implicit val bindingModule: BindingModule)extends Thread with Injectable with SoundBuilder {
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
