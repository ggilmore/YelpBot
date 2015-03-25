package MessageClasses

import scalaj.http.HttpResponse

/**
 * Created by gmgilmore on 3/24/15.
 */
case class RawZulipInboundMessage(contents:HttpResponse[String], requester:ZulipInboundMessageRequester){
  require(RawZulipInboundMessage != null)
  require(requester != null)
}

sealed trait ZulipInboundMessageRequester

case object ZulipMessageSender extends ZulipInboundMessageRequester

case object ZulipMessageRequesterNewQueueID extends ZulipInboundMessageRequester

case object ZulipMessageRequesterGetMsgQueue extends ZulipInboundMessageRequester