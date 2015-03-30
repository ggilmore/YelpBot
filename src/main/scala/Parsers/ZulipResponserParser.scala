package Parsers

import MessageClasses._
import spray.json._
import DefaultJsonProtocol._

import scala.collection.Map._

/**
 * Created by gmgilmore on 3/24/15.
 */


import MessageClasses.ProcessedZulipInboundMessageJSONS._
sealed trait ParsingFailure
object ZulipResponserParser extends App {

  case class ParsingError(errText:String) extends ParsingFailure


  def parseUserMessageResponse(msg:String):Either[ParsingFailure, MessageSendingJsonProtocolsResult] = {
    val map = msg.parseJson.asJsObject.fields
    try {
      List("msg", "result", "id") flatMap (map get _) match {
        case JsString(message) :: JsString("success") :: JsNumber(id) :: Nil => Right(MessageSendingSuccessfulJson(message, "success", id.toInt))
        case JsString(message) :: JsString("error") :: JsNumber(id) :: Nil => Right(MessageSendingSuccessfulJson(message, "error", id.toInt))
        case _ => Left(ParsingError(s"""Something was wrong with this map that we got: ${map}"""))
      }
    } catch {
      case e: DeserializationException => Left(ParsingError(map.toString))
    }
  }

  // Parse queue message responses
  def parseQueueMessageResponse(msg:String):Either[ParsingFailure, QueueMessageJsonProtocolsResult] = {
    val map = msg.parseJson.asJsObject.fields
    try {
      List("msg", "result", "last_event_id", "events") flatMap (map get _) match {
        case JsString(message) :: JsString("success") :: JsNumber(last_event_id) :: JsArray(events) :: Nil => Right(QueueMessageJson(msg = message, result = "success", last_event_id = last_event_id.toInt, events = processEvents(events)))
        case JsString(message) :: JsString("error") :: _ => Right(QueueRequestErrorJson(msg = message, result = "error"))
        case _ => Left(ParsingError( s"""Something was wrong with this map that we got: ${map}"""))
      }
    } catch {
      case e: DeserializationException => Left(ParsingError(map.toString))
    }
  }

  def processEvents(events:Seq[JsValue]):Seq[UserRequestMessageJson] = {
    def processEvents0(events:Seq[JsValue], processedEvents:Seq[UserRequestMessageJson]):Seq[UserRequestMessageJson] = {
      if (events.nonEmpty) processEvent(events.head) match {
        case Right(json) => processEvents0(events.tail, processedEvents :+ json)
        case Left(error) => processEvents0(events.tail, processedEvents)
      }
      else processedEvents
    }
    processEvents0(events, Seq())
  }

  def processEvent(event:JsValue):Either[ParsingFailure,UserRequestMessageJson] = {
    val map = event.asJsObject.fields
    List("message") flatMap (map get _) match {
      case msg :: _ => processMessage(msg)
      case Nil => Left(ParsingError(s"""Something was wrong with this map that we got: ${map}"""))
    }
  }

  def processMessage(msg:JsValue):Either[ParsingFailure,UserRequestMessageJson] = {
    val map = msg.asJsObject.fields
    List("id", "type", "subject", "sender_email", "content") flatMap (map get _) match {
      case JsNumber(id) :: JsString(t) :: JsString(subject) :: JsString(sender_email) :: JsString(content) :: Nil =>
        Right(UserRequestMessageJson(id = id.toString, `type` = t, subject = subject, sender_email = sender_email, content = content))
      case _ => throw new Exception // Anything else, throw exception
    }
  }



//  println("""{
//            |  "result": "success",
//            |  "queue_id": "1426862239:343005",
//            |  "msg": "",
//            |  "events": [{
//            |    "flags": [],
//            |    "message": {
//            |      "subject": "",
//            |      "timestamp": 1427229261,
//            |      "sender_email": "lyn.nagara@gmail.com",
//            |      "sender_id": 7635,
//            |      "content_type": "text/x-markdown",
//            |      "subject_links": [],
//            |      "sender_full_name": "Lyn Nagara (SP1'15)",
//            |      "display_recipient": [{
//            |        "email": "lyn.nagara@gmail.com",
//            |        "full_name": "Lyn Nagara (SP1'15)",
//            |        "domain": "students.hackerschool.com",
//            |        "id": 7635,
//            |        "short_name": "lyn.nagara",
//            |        "is_mirror_dummy": false
//            |      }, {
//            |        "email": "yelpbot-bot@students.hackerschool.com",
//            |        "full_name": "YelpBot",
//            |        "domain": "students.hackerschool.com",
//            |        "id": 7822,
//            |        "short_name": "yelpbot-bot",
//            |        "is_mirror_dummy": false
//            |      }],
//            |      "sender_short_name": "lyn.nagara",
//            |      "id": 37115712,
//            |      "client": "website",
//            |      "gravatar_hash": "5298bd30a2032597f9256c8ca889aec2",
//            |      "content": "hi",
//            |      "sender_domain": "students.hackerschool.com",
//            |      "recipient_id": 40186,
//            |      "type": "private",
//            |      "avatar_url": "https://secure.gravatar.com/avatar/5298bd30a2032597f9256c8ca889aec2?d=identicon"
//            |    },
//            |    "type": "message",
//            |    "id": 0
//            |  }]
//            |}""".stripMargin.parseJson.asJsObject.fields)
 // println(parseQueueMessageResponse("""{"msg":"Invalid authorization header for basic auth","result":"error"}"""))

  println(parseQueueMessageResponse("""{"msg": "", "result": "success", "last_event_id": 12345678, "events":[
    {"flags":[],
    "message":{
      "content_type":"text\/x-markdown",
      "avatar_url":"https:\/\/secure.gravatar.com\/avatar\/5298bd30a2032597f9256c8ca889aec2?d=identicon",
      "timestamp":1427229261,"display_recipient":[
        {"domain":"students.hackerschool.com",
        "short_name":"lyn.nagara","id":7635,"is_mirror_dummy":false,"full_name":"Lyn Nagara (SP1'15)",
        "email":"lyn.nagara@gmail.com"},{"domain":"students.hackerschool.com","short_name":"yelpbot-bot",
        "email":"yelpbot-bot@students.hackerschool.com","is_mirror_dummy":false,"full_name":"YelpBot","id":7822}
      ],
      "sender_id":7635,
      "sender_full_name":"Lyn Nagara (SP1'15)","sender_domain":"students.hackerschool.com",
    "content":"hi","gravatar_hash":"5298bd30a2032597f9256c8ca889aec2","recipient_id":40186,"client":"website",
    "sender_email":"lyn.nagara@gmail.com","subject_links":[],"subject":"","type":"private","id":37115712,
    "sender_short_name":"lyn.nagara"},"type":"message","id":0}]}"""))

}
