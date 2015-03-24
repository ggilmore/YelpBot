package ZulipCommunicators

/**
 * Created by gmgilmore on 3/23/15.
 */

import java.net.SocketTimeoutException

import Queues._
import MessageClasses.{ZulipOutboundMessage, RawZulipInboundMessage}

import scalaj.http.Http
object ZulipMessageSender extends App {

  val BOT_NAME = "yelpbot-bot@students.hackerschool.com"

  private val ZULIP_ADDR_MESSAGES = "https://api.zulip.com/v1/messages"

  private val ZULIP_ADDR_REGISTER ="https://api.zulip.com/v1/register"

  private val ZULIP_ADDR_EVENTS = "https://api.zulip.com/v1/events"

  private val sender:Thread = new Thread(new Runnable{
    def run():Unit = {
      while(true){
        val msg = Queues.takeFromZulipOutQueue
        sendToZulip(message = msg) match {
          case Some(message)=> Queues.putOnZulipInQueue(message)
          case None =>
        }
      }
    }
  })

  sender.start

//  Queues.putOnZulipOutQueue(ZulipOutboundMessage("lyn.nagara@gmail.com", true, "", "Hi!"))
//
//  val msg = Queues.takeFromZulipInQueue
//  println(msg)
//
//
//  Queues.putOnZulipOutQueue(ZulipOutboundMessage("lyn.nagara@gmail.com", true, "", "!!!!!!!"))

  def sendToZulip(addr:String = ZULIP_ADDR_MESSAGES, authName:String = BOT_NAME,
                  authPW:String = SecretKeys.ZULIP_BOT_KEY, message:ZulipOutboundMessage):Option[RawZulipInboundMessage] ={
    require(addr != null, "Null address")
    require(authName != null, "Null authName")
    require(authPW != null, "Null authPW")
    require(message != null, "Null message")

    if (message.isPrivate) {
      try {
        Some(RawZulipInboundMessage(Http(addr).auth(authName, authPW).postForm(Seq("type" -> "private", "to" -> message.target, "content" -> message.content)).asString))
      }
      catch {
        case e:SocketTimeoutException => None
      }
    }
    else {
      try {
        Some(RawZulipInboundMessage(Http(addr).auth(authName, authPW).postForm(Seq("type" -> "strean", "to" -> message.target, "subject" -> message.subject, "content" -> message.content)).asString))
      }
      catch {
        case e: SocketTimeoutException => None
      }
    }
  }

////  println(sendToZulip(target="lyn.nagara@gmail.com", content = "test", isPrivate = true))
////  println(registerQueue)
//  println(getLatestEvents())


}