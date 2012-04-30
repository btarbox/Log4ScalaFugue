package org.log4jfugue
import org.scala_tools.subcut.inject.{BindingModule, Injectable}
import org.jfugue.{Rhythm, Pattern, Player}

class DummyPlayer() extends Player {
   override def play(musicString: String) : Unit = {
    println("pretend to play " + musicString)
    Thread.sleep(1000)
  }
//  override def play(pattern: Pattern) : Unit = {
//   //println("pretend to play " + pattern.getMusicString)
//   Thread.sleep(1000)
// }

  override def play(rhythm: Rhythm) : Unit = {
   //println("pretend to play " + pattern.getMusicString)
   Thread.sleep(1000)
 }}