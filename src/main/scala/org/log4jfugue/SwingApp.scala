package org.log4jfugue

import scala.swing._
import scala.swing.event._
import org.scala_tools.subcut.inject.{BindingModule, Injectable}
import java.io.File

// dead simple log picker wrapper
object SwingApp extends SimpleGUIApplication {
  def top = new MainFrame {
    title = "Log File Picker"
    val logPickButton = new Button { text = "Pick a Log file to process" }
    val exitButton = new Button { text = "Exit" }
    val logPicker = new FileChooser() { title = "Pick a log file to process" }

    contents = new BoxPanel(Orientation.Vertical) {
      contents += logPickButton
      contents += exitButton
      border = Swing.EmptyBorder(30, 30, 10, 30)
    }

    listenTo(exitButton)
    reactions += { case ButtonClicked(b) => exit()}

    listenTo(logPickButton)
    reactions += {
      case ButtonClicked(b) =>
      val result = logPicker.showOpenDialog(logPickButton)
      if(result == FileChooser.Result.Approve) { processLog(logPicker.selectedFile) }
    }
  }

  def processLog(selectedFile : File) {
    println(Some(selectedFile))
    implicit val bindingModule = Configuration
    bindingModule.modifyBindings {
      thisBinding => // override default bindings...
        thisBinding.bind[String] identifiedBy 'logFileName toSingle selectedFile.getAbsolutePath
        val l4jf = new L4JF()(thisBinding)
    }
  }

}