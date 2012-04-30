<<<<<<< HEAD
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
import com.mongodb.casbah.Imports._
import org.scala_tools.subcut.inject.BindingModule

class MongoDataGetter() (override implicit val bindingModule: BindingModule) extends FileDataGetter  {
  lazy val timeColumn     = injectOptional[String]('timeColumn).getOrElse("time")
  lazy val msgColumn      = injectOptional[String]('msgColumn).getOrElse("msg")
  lazy val databaseName   = injectOptional[String]('databaseName).getOrElse("logs")
  lazy val collectionName = injectOptional[String]('collectionName).getOrElse("thisLog")
  lazy val conn = MongoConnection() (databaseName)(collectionName)

  override def run() {
    try {
      for { x <- conn.find().sort(MongoDBObject("timeColumn" -> "1")); thisTime = x.get(timeColumn); if(thisTime != null)} {
        val line = x.get(timeColumn).toString
        messageProcessor ! x.get(msgColumn)
        delayProcessing(line)
      }
    }finally {
      //conn.underlying.close()
    }
  }
=======
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
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import org.scala_tools.subcut.inject.BindingModule

class MongoDataGetter() (override implicit val bindingModule: BindingModule) extends FileDataGetter  {
  lazy val timeColumn     = injectOptional[String]('timeColumn).getOrElse("time")
  lazy val msgColumn      = injectOptional[String]('msgColumn).getOrElse("msg")
  lazy val databaseName   = injectOptional[String]('databaseName).getOrElse("logs")
  lazy val collectionName = injectOptional[String]('collectionName).getOrElse("thisLog")
  lazy val conn = MongoConnection() (databaseName)(collectionName)

  override def run() {
    try {
      for { x <- conn; thisTime = x.get(timeColumn); if(thisTime != null)} {
        val line = x.get(timeColumn).toString
        messageProcessor ! x.get(msgColumn)
        delayProcessing(line)
      }
    }finally {
      //conn.underlying.close()
    }
  }
>>>>>>> 9ba3984a904b185111d264c47e680333f68b44ac
}