package Parsers

import MessageClasses._
import spray.json._
import DefaultJsonProtocol._

import scala.collection.Map._
import scalaj.http.HttpResponse

/**
 * Created by gmgilmore on 3/24/15.
 */


import MessageClasses.ProcessedZulipInboundMessageJSONS._
sealed trait ParsingFailure
object ZulipResponserParser extends App {

  case class ParsingError(errText:String) extends ParsingFailure


  def getMessageBodyFromResponse(response:HttpResponse[String]):String = response.body





  /**
   * @param "msg" is an instance of String, that represents the json response when a message is sent to a user or stream, 
   * that maps "msg" and "result" to JsStrings, and "id" to JsNumber
   * @return Either MessageSendingJsonProtocolsResult if the parsing of "msg", "result" and "id" are successful, 
   * or ParsingFailure if there was some kind of issue, such as if "msg", or "result" or "id" are missing
   */

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


  /**
   * @param "msg" is an instance of String, that represents the json received from a get queue messages API call, 
   * that maps "msg" and "result" to JsStrings, "last_event_id" to JsNumber, and "events" to JsArray
   * @return Either QueueMessageJsonProtocolsResult if the parsing of "msg" was successful, 
   * or ParsingFailure if there was some kind of issue, such as if "result", or "events" is missing
   */

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


  /**
   * @param events an instance of Seq[JsValue]
   * @return a Tuple (List[ParsingFailure], List[UserRequestMessageJson]) where List[ParsingFailure] represents the list of
   * events that have failed to parse, and List[UserRequestMessageJson] is the list of successfully parsed events
   */

  def processEvents(events:Seq[JsValue]):(List[ParsingFailure], List[UserRequestMessageJson]) = {
    def processEvents0(events:Seq[JsValue], processedEvents:Seq[Either[ParsingFailure, UserRequestMessageJson]]):Seq[Either[ParsingFailure, UserRequestMessageJson]] = {
      if (events.nonEmpty) {
        processEvents0(events.tail, processedEvents :+ processEvent(events.head))
      }
      else processedEvents
    }
    split[ParsingFailure, UserRequestMessageJson](processEvents0(events, Seq()).toList)
  }


  def split[E, V](results: List[Either[E, V]]): (List[E], List[V]) = {
    def loop(listE: List[E], listV: List[V], rest: List[Either[E, V]]): (List[E], List[V]) = rest match {
      case Nil => (listE, listV)
      case Right(v) :: xs => loop(listE, listV :+ v, xs)
      case Left(e) :: xs => loop(listE :+ e, listV, xs)
    }
    loop(List(), List(), results)
  }


  /**
   * @param "event" an instance of JsValue with at least the String -> JsValue pair: "message"
   * @return a UserRequestMessageJson if the parsing of "event" was successful, or ParsingFailure if there was some kind
   * of issue when parsing the "event", such as if "message" is missing/incorrectly formatted as described by processedMessage
   */

  def processEvent(event:JsValue):Either[ParsingFailure,UserRequestMessageJson] = {
    val map = event.asJsObject.fields
    List("message") flatMap (map get _) match {
      case msg :: _ => processMessage(msg)
      case Nil => Left(ParsingError(s"""Something was wrong with this map that we got: ${map}"""))
    }
  }

  /**
   * @param msg an instance of JsValue with at least String -> JsString pairs: type, subject,
   *            sender_email, and content, whose values all map to strings, and String -> JsNumber pair: id
   * @return a UserRequestMessageJson if the parsing of "msg" was successful, or ParsingFailure if there was some kind
   *         of issue when parsing the "msg", such as if one of above stated keys is missing
   */
  def processMessage(msg:JsValue):Either[ParsingFailure,UserRequestMessageJson] = {
    val map = msg.asJsObject.fields
    List("id", "type", "subject", "sender_email", "content") flatMap (map get _) match {
      case JsNumber(id) :: JsString(t) :: JsString(subject) :: JsString(sender_email) :: JsString(content) :: Nil =>
        Right(UserRequestMessageJson(id = id.toString, `type` = t, subject = subject, sender_email = sender_email, content = content))
      case _ => Left(ParsingError(s"""Something was wrong with this map that we got: ${map}"""))
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

  println(parseQueueMessageResponse("""{"msg": "", "result": "success", "last_event_id": 12345678, 
    "events":[
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
