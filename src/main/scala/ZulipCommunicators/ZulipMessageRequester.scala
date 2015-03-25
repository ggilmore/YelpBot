package ZulipCommunicators

import java.net.SocketTimeoutException

import scalaj.http.Http
import ZulipMessageSender.BOT_NAME

import MessageClasses.RawZulipInboundMessage
import Queues._

/**
 * Created by gmgilmore on 3/24/15.
 */
object ZulipMessageRequester extends App {

  val BOT_NAME = "yelpbot-bot@students.hackerschool.com"

  private val ZULIP_ADDR_REGISTER ="https://api.zulip.com/v1/register"

  private val ZULIP_ADDR_EVENTS = "https://api.zulip.com/v1/events"


  private val requester:Thread = new Thread(new Runnable{
    def run():Unit = {
      while(true){
        getLatestEvents() match {
          case Some(msg) =>  Queues.putOnZulipInQueue(msg)
          case None => /*try again*/
        }
        Thread.sleep(5000)
      }
    }
  })

  requester.start

val msg = Queues.takeFromZulipInQueue
println(msg)

  def registerQueue:RawZulipInboundMessage= RawZulipInboundMessage(Http(ZULIP_ADDR_REGISTER).auth(BOT_NAME, SecretKeys.ZULIP_BOT_KEY).postData(s"""event_types=["message"]""").asString,
    MessageClasses.ZulipMessageRequesterNewQueueID)

  def getLatestEvents(botKey:String = SecretKeys.ZULIP_BOT_KEY, eventQueueID:String = SecretKeys.QUEUE_ID, lastEventID: String = "0") :Option[RawZulipInboundMessage] ={
    require(botKey != null)
    require(eventQueueID != null)
    require(lastEventID != null)

    try {
      Some(RawZulipInboundMessage(Http(ZULIP_ADDR_EVENTS).auth(BOT_NAME, SecretKeys.ZULIP_BOT_KEY).params(Seq(("queue_id", eventQueueID), ("last_event_id", lastEventID))).asString,
        MessageClasses.ZulipMessageRequesterGetMsgQueue))
    }
    catch {
      case e: SocketTimeoutException => None
    }

  }




}
