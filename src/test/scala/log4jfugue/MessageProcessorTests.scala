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
import org.scalatest.matchers.ShouldMatchers
import org.scala_tools.subcut.inject._
import org.scalatest.{FunSuite, SeveredStackTraces}

class MessageProcessorTests extends FunSuite with ShouldMatchers {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  implicit val bindingModule =  new NewBindingModule({ implicit module =>
     import module._
     bind[List[MessageMap]] identifiedBy  'instrumentMessages toSingle
       List[MessageMap] (MessageMap("stream create", "baseDrum", 1),
                         MessageMap("stream delete", "snareDrum", 2),
                         MessageMap("MessageParser", "cow-bell", 3),
                         MessageMap("RtspClient", "cymbal", 4)
       )
  })

  test ("Test message processor") {
    val mp = new MessageProcessor()
    mp.start()
    var currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))
    
    mp ! "stream create"
    mp ! "RtspClient"
    currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0))

    // should be back to zero
    currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))


    mp ! "RtspClient"
    mp ! "RtspClient"
    currentSecond = mp !? ("nextData") match { case e: Accumulator => e}
    currentSecond should be (Array[Int] (0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0))
    mp  ! "exit"
    Thread.sleep(1000)
  }


  //case class LogMsg(time:Int, cat:String, msg:String) {
  //  def diff(bla:LogMsg) =  new LogMsg(time - bla.time, bla.cat, bla.msg)
  //}
  import scala.io.Source.fromFile
  case class LogMsg(str: String) {
    val x = str.split(' ')
    val time = x(0).toInt
    val cat  = x(1)
  }

  test("create list of message sources and counts, i.e. find the popular messages") {
    val source = fromFile("c:/Users/gqx487/Downloads/server.log.4/server.log")
    val lines = source.getLines
    val catList = for{oneLine <- lines
                      tokens = oneLine.split(" ")
                      if(tokens.size > 4)} yield tokens(4)
    val foo = for(groupedList <- catList.toList.groupBy(x => x)) yield (groupedList._1, groupedList._2.size)
    for(sorted <- foo.toList.sortBy(_._2).reverse) {
      println(sorted._1 + ":" + sorted._2)
    }
  }

  test("messing around") {
    val source = fromFile("file.txt")
    val lines = source.getLines
    val cList = for(oneLine <- lines) yield new LogMsg(oneLine)
    for(x <- cList.toList.groupBy(k => k.cat)) {
      val groupedBy = for(logPairs <- x._2.sliding(2)) yield (logPairs(1).time - logPairs(0).time)
      println(x._1 + ":" + groupedBy.mkString(","))
    }
    source close
  }

  //def stringToTupple(oneLine:String) =  {val z = oneLine.split(' '); (z(0).toInt, z(1)) }

  test("messing around2") {
    val lines = fromFile("file.txt").getLines
    val tuppleList = for(oneLine <- lines) yield {val z = oneLine.split(' '); (z(0).toInt, z(1)) }
    for(groupedList <- tuppleList.toList.groupBy(oneTuple => oneTuple._2)) {
      val diffList = for(logPairs <- groupedList._2.sliding(2)) yield (logPairs(1)._1 - logPairs(0)._1)
      println(groupedList._1 + ":" + diffList.mkString(","))
    }
  }


/*
  test("just messing around") {
    val cList = List(LogMsg(1,"a", "bla"), LogMsg(2,"b", "bla"), LogMsg(4,"a", "bla"), LogMsg(14,"b", "bla"),
                     LogMsg(20,"a", "bla"), LogMsg(24,"b", "bla"))

    val foo = cList.groupBy(_.cat)

    val aList = List(1,2,3,3,6,8,15)
    aList.reduceLeft((a,b) => b - a)

    cList.sliding(2).map(x => x(0).time - x(1).time)
    val foo2 = foo.valuesIterator
    //foo2.reduceLeft((a:LogMsg, b:LogMsg) => new LogMsg(b.time - a.time,  b.cat,b.msg))

    cList.reduceLeft((a:LogMsg,b:LogMsg) => b.diff(a))

    val xlist = cList.groupBy(k => k.cat) //creates list of tupples, each sublist is by cat
    for(x <- xlist) {
      val groupedBy = for(logPairs <- x._2.sliding(2)) yield (logPairs(1).time - logPairs(0).time)
      //for(one <- groupedBy)println(one)
      println(x._1 + ":" + groupedBy.mkString(","))
    }
    println("end of group by")

    //cList.groupBy(k => k.cat).sliding(2).map(t => t(0).time = t(1).time).map(_.abs).max
  //  val bla = (cList, cList.tail).zipped
  //  bla.map((a,b) => b.time - a.time).max

    val dick = for(logPairs <- cList.sliding(2)) yield (logPairs(1).time - logPairs(0).time)
    for(bla <- dick) println(bla)
  }
*/
}