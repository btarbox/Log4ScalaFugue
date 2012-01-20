package org.log4jfugue
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class PathToEnlightenmentTest extends FunSuite {
  override def nestedSuites = List(new FugueTests,
                                   new MessageProcessorTests,
                                   new SimpleSoundBuilderTest,
                                   new RhythmSoundBuilderTests)
}