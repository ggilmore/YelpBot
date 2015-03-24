package ZulipCommunicators

/**
 * Created by gmgilmore on 3/23/15.
 */

import Queues._
import MessageClasses.{ZulipOutboundMessage, ZulipInboundMessage}

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
        Queues.putOnZulipInQueue(sendToZulip(message = msg))
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
                  authPW:String = SecretKeys.ZULIP_BOT_KEY, message:ZulipOutboundMessage):ZulipInboundMessage ={
    if (message.isPrivate) ZulipInboundMessage(Http(addr).auth(authName, authPW).postForm(Seq("type" -> "private", "to" -> message.target, "content" ->message.content )).asString)
    else ZulipInboundMessage(Http(addr).auth(authName, authPW).postForm(Seq("type" -> "strean", "to" -> message.target,  "subject" -> message.subject,  "content" ->message.content )).asString)
  }

////  println(sendToZulip(target="lyn.nagara@gmail.com", content = "test", isPrivate = true))
////  println(registerQueue)
//  println(getLatestEvents())


}