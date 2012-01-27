package org.log4jfugue
import org.jfugue.Player
import org.scala_tools.subcut.inject._

/** Specifies the association between log messages and musical instruments */
object Configuration extends NewBindingModule({ module =>
  implicit val bindingModule = module
  import module._

  bind[Player]  toSingle new Player()
  //bind[Array[Int]] identifiedBy 'currentSecond toSingle Array[Int](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  bind[List[MessageMap]] identifiedBy  'instrumentMessages toSingle
    List[MessageMap] (MessageMap("stream create", "LOW_TOM", 1),
                      MessageMap("HttpService.getHttpGetResponse", "ACOUSTIC_SNARE", 2),
                      MessageMap("GlobalContentImpl.getLastPlayed", "HAND_CLAP", 3),
                      MessageMap("ContentAwarePumpSelector.useExistingAffinity", "HI_BONGO", 4),
                      MessageMap("NgnEventisPlayListRtspFlowHandler", "COWBELL", 5),
                      MessageMap("GenericDAO", "LOW_CONGA", 6),
                      MessageMap("StreamFacade", "TAMBOURINE", 7),
                      MessageMap("MessageParser", "MARACAS", 8),
                      MessageMap("RtspClient", "HI_WOOD_BLOCK", 9)
    )

  bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log_20110128"
//  bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log_20110215"
  //bind[String] identifiedBy 'logFileName toSingle "c:/Users/gqx487/Downloads/server.log.4/server.log"
  bind[String] identifiedBy 'dateFormat  toSingle  "yyyy-MM-dd HH:mm:ss,SSS"
  bind[MessageProcessor]  toSingle new MessageProcessor
  bind[SimpleDataGetter] toSingle  new FileDataGetter
  bind[SoundBuilder] toSingle  new RhythmSoundBuilder
})
