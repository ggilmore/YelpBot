package ZulipCommunicators

/**
 * Created by gmgilmore on 3/23/15.
 */


import MessageClasses.ZulipInboundMessage

import scalaj.http.Http
object ZulipMessageSender extends App {

  val BOT_NAME = "yelpbot-bot@students.hackerschool.com"

  private val ZULIP_ADDR_MESSAGES = "https://api.zulip.com/v1/messages"

  private val ZULIP_ADDR_REGISTER ="https://api.zulip.com/v1/register"

  private val ZULIP_ADDR_EVENTS = "https://api.zulip.com/v1/events"

//  println(Http("https://api.zulip.com/v1/messages").auth("yelpbot-bot@students.hackerschool.com", "NK3ilaGgyV9TxCikfEBbJVmBUKzNYKMx").postForm(Seq("type" -> "private", "to" -> "ggilmore@mit.edu", "content" ->"I come not, friends, to steal away your hearts." )).asString)

  def sendToZulip(addr:String = ZULIP_ADDR_MESSAGES, authName:String = BOT_NAME,
                  authPW:String = SecretKeys.ZULIP_BOT_KEY, isPrivate:Boolean = false,
                  target:String, subject:String = "", content:String):ZulipInboundMessage ={
    if (isPrivate) ZulipInboundMessage(Http(addr).auth(authName, authPW).postForm(Seq("type" -> "private", "to" -> target, "content" ->content )).asString)
    else ZulipInboundMessage(Http(addr).auth(authName, authPW).postForm(Seq("type" -> "strean", "to" -> target,  "subject" -> subject,  "content" ->content )).asString)
  }



////  println(sendToZulip(target="lyn.nagara@gmail.com", content = "test", isPrivate = true))
////  println(registerQueue)
//  println(getLatestEvents())


}