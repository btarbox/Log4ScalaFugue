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
import scala.io.StdIn

import akka.actor.ActorSystem
import org.jfugue.Player


object L4JFCloud extends App {
  println("L4JFCloud initializing")
  private val system = ActorSystem("Log4ScalaFugueCloud")
  val fileDataGetterFile = "/Users/btarbox/projects2/Log4ScalaFugue/sample.log"
  val SocketDataGetterPort = 4445
  val player = new Player(true, true)
  val messages = List[MessageMap] (
    MessageMap("stream create", "LOW_TOM", 1),
    MessageMap("PumpReservations.reserveStreamBandwidth", "ACOUSTIC_SNARE", 2),
    MessageMap("exception getting pump", "HAND_CLAP", 3),
    MessageMap("ContentAwarePumpSelector.useExistingAffinity", "HI_BONGO", 4),
    MessageMap("NgnEventisPlayListRtspFlowHandler", "COWBELL", 5),
    MessageMap("GenericDAO", "LOW_CONGA", 6),
    MessageMap("StreamFacade.createStream", "TAMBOURINE", 7),
    MessageMap("CdnDAO.updatePlaycount", "MARACAS", 8),
    MessageMap("StreamFacade.destroyStream", "HI_WOOD_BLOCK", 9))

  val messageProcessor = system.actorOf(MessageProcessor.props(1), "MessageProcessor")
  val soundBuilder = new RhythmSoundBuilder
  val dataGetter = new FileDataGetter

  soundBuilder.start()
  dataGetter.start()

//  dataGetter.join()
//  messageProcessor ! "exit"
//  soundBuilder.keepRunning = false
//  dataGetter.stop()
  println("L4JFCloud done initializing")

  println("main app starting....")
  StdIn.readLine()  // placeholder for system to keep running
  println("main app exiting....")
}
