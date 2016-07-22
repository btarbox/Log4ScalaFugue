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
import org.jfugue.Player

/** Specifies the association between log messages and musical instruments */

//  THIS ENTIRE CLASS IS LIKELY DEPRECATED AS WE CANT GET SUBCUT TO WORK *******
object Configuration {

//  bind[Player]  toSingle new Player(true, true)
//  bind[List[MessageMap]] identifiedBy  'instrumentMessages toSingle
//    List[MessageMap] (MessageMap("stream create", "LOW_TOM", 1),
//                      MessageMap("PumpReservations.reserveStreamBandwidth", "ACOUSTIC_SNARE", 2),
//                      MessageMap("exception getting pump", "HAND_CLAP", 3),
//                      MessageMap("ContentAwarePumpSelector.useExistingAffinity", "HI_BONGO", 4),
//                      MessageMap("NgnEventisPlayListRtspFlowHandler", "COWBELL", 5),
//                      MessageMap("GenericDAO", "LOW_CONGA", 6),
//                      MessageMap("StreamFacade.createStream", "TAMBOURINE", 7),
//                      MessageMap("CdnDAO.updatePlaycount", "MARACAS", 8),
//                      MessageMap("StreamFacade.destroyStream", "HI_WOOD_BLOCK", 9)
//    )
//
//  bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log.4/server.log"
//  bind[String] identifiedBy 'dateFormat  toSingle  "yyyy-MM-dd HH:mm:ss,SSS"
//  bind[MessageProcessor]  toSingle new MessageProcessor(1)
//  bind[SimpleDataGetter] toSingle  new FileDataGetter
//
//  //bind[SimpleDataGetter] toSingle  new MongoDataGetter
//  //bind[String] identifiedBy 'timeColumn toSingle "timestamp"
//  //bind[String] identifiedBy 'msgColumn toSingle "msg"
//
//  bind[SoundBuilder] toSingle  new RhythmSoundBuilder
}
