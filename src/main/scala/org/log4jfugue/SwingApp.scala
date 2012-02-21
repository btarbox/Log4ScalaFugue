package org.log4jfugue

import scala.swing._
import scala.swing.event._
import org.scala_tools.subcut.inject.{BindingModule, Injectable}
import java.io.File

// dead simple log picker wrapper
object SwingApp extends SimpleGUIApplication {
  def top = new MainFrame {
    title = "Reactive Swing App"
    val button = new Button { text = "Click me" }
    val label = new Label {   text = "No button clicks registered"  }
    val logPickButton = new Button { text = "Choose file" }
    val logPicker = new FileChooser(null)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += button
      contents += label
      contents += logPickButton
      border = Swing.EmptyBorder(30, 30, 10, 30)
    }

    listenTo(button)
    var nClicks = 0
    reactions += {
      case ButtonClicked(b) =>
      nClicks += 1
      label.text = "Number of button clicks: "+ nClicks
    }

    listenTo(logPickButton)
    reactions += {
      case ButtonClicked(b) =>
      println("got button press")
      val result = logPicker.showOpenDialog(label)
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