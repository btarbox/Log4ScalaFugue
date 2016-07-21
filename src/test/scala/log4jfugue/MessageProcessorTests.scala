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
//import org.scalatest.matchers.ShouldMatchers
//import org.scala_tools.subcut.inject._
//import org.scalatest.{FunSuite, SeveredStackTraces}
// import log4jfugue.BaseAkkaSpec
import scala.concurrent.Await
import akka.actor.ActorSystem
import org.jfugue.Player
import org.scalatest._
import akka.pattern.ask
import scala.concurrent.duration._
import org.log4jfugue.MessageProcessor.Msg

class MessageProcessorTests extends FreeSpec with BaseAkkaSpec {
  val timeout = 2L
  implicit val askTime = akka.util.Timeout(timeout, SECONDS)
  override val system = ActorSystem("Log4ScalaFugueCloud")
  val messageProcessor = system.actorOf(MessageProcessor.props(1), "MessageProcessor")
  val soundBuilder = new RhythmSoundBuilder
  val dataGetter = new FileDataGetter
  val fileDataGetterFile = "foo.dat"
  val SocketDataGetterPort = 4445
  val player = new Player(true, true)
  val messages = List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
    MessageMap("stream delete", "snareDrum", 2),
    MessageMap("MessageParser", "cow-bell", 3),
    MessageMap("RtspClient", "cymbal", 4)
  )

  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
//  implicit val bindingModule =  new NewBindingModule({ implicit module =>
//     import module._
//     bind[List[MessageMap]] identifiedBy  'instrumentMessages toSingle
//       List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
//                         MessageMap("stream delete", "snareDrum", 2),
//                         MessageMap("MessageParser", "cow-bell", 3),
//                         MessageMap("RtspClient", "cymbal", 4)
//       )
//  })

  "Test Message Processor" - {
    "yup, test it" in {
      println("START TESTING")
      val currentSecondFuture = messageProcessor ? MessageProcessor.NextData
      val currentSecond = Await.result(currentSecondFuture, 2 seconds) match {
        case e: Accumulator => e
        case x: Any => println(s"got unexpected ${x}")
      }
      println(s"currentSeconds ${currentSecond}")
     // assert(currentSecond.sameElements(Array[Int] (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)))
      println("GOT PAST FIRST INCREDIBLY SIMPLE TEST")

      messageProcessor ! Msg("stream create")
      messageProcessor ! Msg("GenericDAO")
      val currentSecondFuture2 = messageProcessor ? MessageProcessor.NextData
      val currentSecond2 = Await.result(currentSecondFuture2, 2 seconds) match {
        case e: Accumulator => e
        case x: Any => println(s"got unexpected ${x}")
      }
      println(s"currentSeconds ${currentSecond2}")
    //  assert(currentSecond2.equals(Array[Int] (0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0)))

      // should be back to zero
      val currentSecondFuture3 = messageProcessor ? MessageProcessor.NextData
      val currentSecond3 = Await.result(currentSecondFuture3, 2 seconds) match {
        case e: Accumulator => e
        case x: Any => println(s"got unexpected ${x}")
      }
      println(s"currentSeconds ${currentSecond3}")
    //  assert(currentSecond3.equals(Array[Int] (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)))

      messageProcessor ! Msg("GenericDAO")
      messageProcessor ! Msg("GenericDAO")
      val currentSecondFuture4 = messageProcessor ? MessageProcessor.NextData
      val currentSecond4 = Await.result(currentSecondFuture4, 2 seconds) match {
        case e: Accumulator => e
        case x: Any => println(s"got unexpected ${x}")
      }
      println(s"currentSeconds ${currentSecond4}")
   //   assert(currentSecond4.equals(Array[Int] (0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0)))
    }
  }

  def sameElements(a1: Array[Int], a2: Array[Int]) = {

  }


//  //case class LogMsg(time:Int, cat:String, msg:String) {
//  //  def diff(bla:LogMsg) =  new LogMsg(time - bla.time, bla.cat, bla.msg)
//  //}
//  import scala.io.Source.fromFile
//  case class LogMsg(str: String) {
//    val x = str.split(' ')
//    val time = x(0).toInt
//    val cat  = x(1)
//  }
//
//  test("create list of message sources and counts, i.e. find the popular messages") {
//    val source = fromFile("c:/Users/gqx487/Downloads/server.log.4/server.log")
//    val lines = source.getLines
//    val catList = for{oneLine <- lines
//                      tokens = oneLine.split(" ")
//                      if(tokens.size > 4)} yield tokens(4)
//    val foo = for(groupedList <- catList.toList.groupBy(x => x)) yield (groupedList._1, groupedList._2.size)
//    for(sorted <- foo.toList.sortBy(_._2).reverse) {
//      println(sorted._1 + ":" + sorted._2)
//    }
//  }
//
//  test("messing around") {
//    val source = fromFile("file.txt")
//    val lines = source.getLines
//    val cList = for(oneLine <- lines) yield new LogMsg(oneLine)
//    for(x <- cList.toList.groupBy(k => k.cat)) {
//      val groupedBy = for(logPairs <- x._2.sliding(2)) yield (logPairs(1).time - logPairs(0).time)
//      println(x._1 + ":" + groupedBy.mkString(","))
//    }
//    source close
//  }
//
//  //def stringToTupple(oneLine:String) =  {val z = oneLine.split(' '); (z(0).toInt, z(1)) }
//
//  test("messing around2") {
//    val lines = fromFile("file.txt").getLines
//    val tuppleList = for(oneLine <- lines) yield {val z = oneLine.split(' '); (z(0).toInt, z(1)) }
//    for(groupedList <- tuppleList.toList.groupBy(oneTuple => oneTuple._2)) {
//      val diffList = for(logPairs <- groupedList._2.sliding(2)) yield (logPairs(1)._1 - logPairs(0)._1)
//      println(groupedList._1 + ":" + diffList.mkString(","))
//    }
//  }



}