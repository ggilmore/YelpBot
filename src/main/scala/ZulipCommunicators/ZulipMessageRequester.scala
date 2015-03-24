package ZulipCommunicators
import scalaj.http.Http
import ZulipMessageSender.BOT_NAME

import MessageClasses.ZulipInboundMessage

/**
 * Created by gmgilmore on 3/24/15.
 */
object ZulipMessageRequester {

  private val ZULIP_ADDR_REGISTER ="https://api.zulip.com/v1/register"

  private val ZULIP_ADDR_EVENTS = "https://api.zulip.com/v1/events"


  def registerQueue:ZulipInboundMessage= ZulipInboundMessage(Http(ZULIP_ADDR_REGISTER).auth(BOT_NAME, SecretKeys.ZULIP_BOT_KEY).postData(s"""event_types=["message"]""").asString)

  def getLatestEvents(queueID:String = SecretKeys.ZULIP_BOT_KEY, eventQueueID:String = SecretKeys.QUEUE_ID, lastEventID: String = "0") :ZulipInboundMessage =
    ZulipInboundMessage(Http(ZULIP_ADDR_EVENTS).auth(BOT_NAME, SecretKeys.ZULIP_BOT_KEY).params(Seq(("queue_id",eventQueueID), ("last_event_id",lastEventID))).asString)


}
