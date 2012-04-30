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
import io.Source
import org.scalatest.{FunSuite, SeveredStackTraces}

class FileDataGetterTest extends FunSuite with ShouldMatchers {
  type Accumulator = Array[Int]
  val currentSecond = Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  test ("Test SimpleSoundBuilder") {
    val bindingModule =  new NewBindingModule({ implicit module =>
       import module._
       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0)
       bind[String] identifiedBy 'dateFormat  toSingle  "yyyy-MM-dd HH:mm:ss,SSS"
    })
    val fdg =  new FileDataGetter()(bindingModule)

    val firstTime = "2012-04-30 12:00:00,000"
    val before = System.currentTimeMillis()
    fdg.delayProcessing(firstTime)
    val after = System.currentTimeMillis()
    (after - before) should be <= (20L)

    val before2 = System.currentTimeMillis()
    fdg.delayProcessing(firstTime)
    val after2 = System.currentTimeMillis()
    (after2 - before2) should be <= (20L)

    val delayTime = "2012-04-30 12:00:00,100"
    val before3 = System.currentTimeMillis()
    fdg.delayProcessing(delayTime)
    val after3 = System.currentTimeMillis()
    (after3 - before3) should be >= (100L)
  }

}