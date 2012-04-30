package org.log4jfugue
import java.text.{ParseException, SimpleDateFormat}
import org.scala_tools.subcut.inject.{BindingModule, Injectable, AutoInjectable}
import io.Source

/**
 * Gets messages from an existing log file; reads timestamps so as to play back the
 * messages in the same time sequence that they were originally written in.
 */
class FileDataGetter() (override implicit val bindingModule: BindingModule) extends SimpleDataGetter {
  lazy val dateFormat = injectOptional[String]('dateFormat).getOrElse("yyXXXXXXyy/MM/dd HH:mm:ss.SSS")
  lazy val fileName   = injectOptional[String]('logFileName).getOrElse("no-file-bound")
  println("fileName:" + fileName + ", format:" + dateFormat)
  val sdf = new SimpleDateFormat(dateFormat)
  private var lastTime: Option[Long] = None

  override def run() {
    val source = Source.fromFile(fileName)
    try {
      source getLines() foreach {line =>
        messageProcessor ! line
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
      case bad => println("some other exception: " + bad)
    }
  }

  def max(time1: Long, time2: Option[Long]) : Option[Long] = {
    time2 match {
      case None => Some(time1)
      case _ => if(time1 > time2.get) Some(time1) else time2
    }
  }
}