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

import scala.swing._
import scala.swing.event._
import org.scala_tools.subcut.inject.{BindingModule, Injectable}
import java.io.File

// dead simple log picker wrapper
object SwingApp extends SimpleGUIApplication {
  def top = new MainFrame {
    title = "Reactive Swing App"
    val button = new Button { text = "Exit" }
    val label = new Label {   text = " "  }
    val logPickButton = new Button { text = "Choose file" }
    val logPicker = new FileChooser(null)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += logPickButton
      contents += label
      contents += button
      border = Swing.EmptyBorder(30, 30, 10, 30)
    }

    listenTo(logPickButton)
    listenTo(button)

    var nClicks = 0
    reactions += {
      case ButtonClicked(pressed: Button) =>
        println(pressed)
        if(pressed.name.equalsIgnoreCase("Exit")) {
          nClicks += 1
          //label.text = "Number of button clicks: "+ nClicks
          System.exit(0)
        }else {
          val result = logPicker.showOpenDialog(label)
          if(result == FileChooser.Result.Approve) { processLog(logPicker.selectedFile) }
        }
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