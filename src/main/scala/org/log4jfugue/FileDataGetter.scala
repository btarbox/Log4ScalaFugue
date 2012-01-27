package org.log4jfugue
import java.text.{ParseException, SimpleDateFormat}
import org.scala_tools.subcut.inject.{BindingModule, Injectable, AutoInjectable}
import io.Source

/**
 * Gets messages from an existing log file; reads timestamps so as to play back the
 * messages in the same time sequence that they were originally written in.
 */
class FileDataGetter() (override implicit val bindingModule: BindingModule) extends SimpleDataGetter {
  val fileName = injectIfBound[String] ('logFileName) {"foo"}
  val dateFormat = injectIfBound[String] ('dateFormat) {"yyyy/MM/dd HH:mm:ss.SSS"}
  val sdf = new SimpleDateFormat(dateFormat)
  private var lastTime = 0L

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
      //val timeDiff =
      if(lastTime > 0) {
        val timeDiff = msgDate.getTime - lastTime
        if(timeDiff > 0) {
          Thread.sleep(timeDiff)
          //println(line)
        }
      }
      lastTime = max(lastTime, msgDate.getTime)
    } catch  {
      case ex: NumberFormatException => println("got NFE"); Thread.sleep(1)
      case badDate: ParseException => // ignorable exception, means a multi-line message
      case bad => println("some other exception: " + bad)
    }
  }

  def max(time1: Long, time2: Long) = {if (time1 > time2) time1 else time2}
}