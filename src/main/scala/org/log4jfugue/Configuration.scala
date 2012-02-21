package org.log4jfugue
import org.jfugue.Player
import org.scala_tools.subcut.inject._

/** Specifies the association between log messages and musical instruments */
object Configuration extends NewBindingModule({ implicit module =>
  //implicit val bindingModule = module
  import module._

  bind[Player]  toSingle new Player(true, true)
  bind[List[MessageMap]] identifiedBy  'instrumentMessages toSingle
    List[MessageMap] (MessageMap("stream create", "LOW_TOM", 1),
                      MessageMap("PumpReservations.reserveStreamBandwidth", "ACOUSTIC_SNARE", 2),
                      MessageMap("exception getting pump", "HAND_CLAP", 3),
                      MessageMap("ContentAwarePumpSelector.useExistingAffinity", "HI_BONGO", 4),
                      MessageMap("NgnEventisPlayListRtspFlowHandler", "COWBELL", 5),
                      MessageMap("GenericDAO", "LOW_CONGA", 6),
                      MessageMap("StreamFacade.createStream", "TAMBOURINE", 7),
                      MessageMap("CdnDAO.updatePlaycount", "MARACAS", 8),
                      MessageMap("StreamFacade.destroyStream", "HI_WOOD_BLOCK", 9)
    )

  bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log.4/server.log"
  bind[String] identifiedBy 'dateFormat  toSingle  "yyyy-MM-dd HH:mm:ss,SSS"
  bind[MessageProcessor]  toSingle new MessageProcessor
  bind[SimpleDataGetter] toSingle  new FileDataGetter

  //bind[SimpleDataGetter] toSingle  new MongoDataGetter
  //bind[String] identifiedBy 'timeColumn toSingle "timestamp"
  //bind[String] identifiedBy 'msgColumn toSingle "msg"
  
  bind[SoundBuilder] toSingle  new RhythmSoundBuilder
})
