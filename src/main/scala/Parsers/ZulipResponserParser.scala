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
      List("msg", "result", "queue_id", "events") flatMap (map get _) match {
        case JsString(message) :: JsString("success") :: JsNumber(queue_id) :: Nil => Right(QueueMessageJson(msg=message, result="success", queue_id=queue_id.toInt))
        case JsString(message) :: JsString("error") :: JsNumber(queue_id) :: Nil => Right(QueueRequestErrorJson(msg=message, result= "error", queue_id=queue_id.toInt))
        case _ => Left(ParsingError(s"""Something was wrong with this map that we got: ${map}"""))
      }
    } catch {
      case e: DeserializationException => Left(ParsingError(map.toString))
    }





    // try {
    //   Right(msg.parseJson.convertTo[QueueMessageJson])
    // }
    // catch {
    //   case e: DeserializationException => {
    //     try {
    //       Right(msg.parseJson.convertTo[QueueRequestErrorJson])
    //     }
    //     catch {
    //       case e: DeserializationException => Left(ParsingError(e.getMessage))
    //     }
    //   }

    // }
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
//  println(parseQueueMessageResponse("""{"msg":"Invalid authorization header for basic auth","result":"error"}"""))

  println(parseQueueMessageResponse("""{"msg": "", "result": "success", "queue_id": 12345678, events:[1,2,3,4]}"""))
  // parseResponse()

}
