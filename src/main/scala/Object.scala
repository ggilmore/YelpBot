/**
 * Created by gmgilmore on 3/23/15.
 */


import scalaj.http.{HttpResponse, Http, Token}
import spray.json._
import DefaultJsonProtocol._
import scala.sys.process._
object Junk extends App {




  """-d "type=private" -d  "to=ggilmore@mit.edu" -d "content=I come not, friends, to steal away your hearts." """

  val ZULIP_ADDR_MESSAGES = "https://api.zulip.com/v1/messages"

  val BOT_NAME = "yelpbot-bot@students.hackerschool.com"

  val ZULIP_ADDR_REGISTER ="https://api.zulip.com/v1/register"

  val ZULIP_ADDR_EVENTS = "https://api.zulip.com/v1/events"




//  println(Http("https://api.zulip.com/v1/messages").auth("yelpbot-bot@students.hackerschool.com", "NK3ilaGgyV9TxCikfEBbJVmBUKzNYKMx").postForm(Seq("type" -> "private", "to" -> "ggilmore@mit.edu", "content" ->"I come not, friends, to steal away your hearts." )).asString)
//

  def sendToZulip(addr:String = ZULIP_ADDR_MESSAGES, authName:String = BOT_NAME, authPW:String = SecretKeys.ZULIP_BOT_KEY, isPrivate:Boolean = false, target:String, subject:String = "", content:String):HttpResponse[String] ={
    if (isPrivate) Http(addr).auth(authName, authPW).postForm(Seq("type" -> "private", "to" -> target, "content" ->content )).asString
    else Http(addr).auth(authName, authPW).postForm(Seq("type" -> "strean", "to" -> target,  "subject" -> subject,  "content" ->content )).asString
  }

  def registerQueue:HttpResponse[String] = Http(ZULIP_ADDR_REGISTER).auth(BOT_NAME, SecretKeys.ZULIP_BOT_KEY).postData(s"""event_types=["message"]""").asString

//  def getLatestEvents:HttpResponse[String] = Http(ZULIP_ADDR_EVENTS).auth(BOT_NAME, SecretKeys.ZULIP_BOT_KEY).params()

//  println(registerQueue)


}