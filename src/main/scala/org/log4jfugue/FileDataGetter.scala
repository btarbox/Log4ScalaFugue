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
import java.text.{ParseException, SimpleDateFormat}

import io.Source

import akka.actor._
import org.log4jfugue.MessageProcessor.Msg

/**
 * Gets messages from an existing log file; reads timestamps so as to play back the
 * messages in the same time sequence that they were originally written in.
 */
class FileDataGetter()  extends SimpleDataGetter {
  lazy val dateFormat = "yyyy-MM-dd HH:mm:ss,SSS"
  lazy val fileName   = L4JFCloud.fileDataGetterFile
  println("fileName:" + fileName + ", format:" + dateFormat)
  val sdf = new SimpleDateFormat(dateFormat)
  private var lastTime: Option[Long] = None

  override def run() {
    println(s"FileDataGetter about to open ${fileName}")
    val source = Source.fromFile(fileName)
    try {
      source getLines() foreach {line =>
        L4JFCloud.messageProcessor ! Msg(line)
        delayProcessing(line)
      }
    }finally {
      source.close()
    }
  }


  def delayProcessing(line: String) {
    try {
      val msgDate = sdf.parse(line)

     for (last <- lastTime;
          timeDiff = msgDate.getTime - last;
          if (timeDiff > 0)) {
            Thread.sleep(timeDiff)
     }
     lastTime = max(msgDate.getTime, lastTime)
    } catch  {
      case ex: NumberFormatException => println("got NFE"); Thread.sleep(1)
      case badDate: ParseException => // ignorable exception, means a multi-line message
      case x:Any => println("some other exception: " + x)
    }
  }

  def max(time1: Long, time2: Option[Long]) : Option[Long] = {
    time2 match {
      case None => Some(time1)
      case _ => if(time1 > time2.get) Some(time1) else time2
    }
  }
}
