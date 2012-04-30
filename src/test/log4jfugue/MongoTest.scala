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
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

class MongoTest extends FunSuite with ShouldMatchers {

  test("mongo test") {
    implicit val bindingModule =  new NewBindingModule({ implicit module =>
       //implicit val bindingModule = module
       import module._
       bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log.4/server.log"
       bind[String] identifiedBy 'dateFormat  toSingle  "yyyy-MM-dd HH:mm:ss,SSS"
       bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    })

    val mongo = new MongoDataGetter()

    removeOldData(mongo)
    loadSomeData(mongo)
    find(mongo)
  }

  def removeOldData(mongo: MongoDataGetter) = {
    for ( x <- mongo.conn.find()) {
     mongo.conn.remove(x)
    }
  }

  def loadSomeData(mongo: MongoDataGetter) = {
    for(x <- 1 to 20) {
    val newObj = MongoDBObject("timestamp" -> x.toString,
                               "category"  -> "general",
                               "msg"       -> "bla bla bla")
    mongo.conn += newObj
  }
  }

  def find(mongo: MongoDataGetter) = {
    for { x <- mongo.conn.find().sort(MongoDBObject("timeColumn" -> "1")); thisTime = x.get("timestamp"); if(thisTime != null)} {
      println(x)
      println(thisTime)
    }
  }
}