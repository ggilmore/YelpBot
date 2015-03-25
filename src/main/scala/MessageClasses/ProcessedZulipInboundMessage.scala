package MessageClasses

/**
 * Created by gmgilmore on 3/24/15.
 */

import spray.json._
import DefaultJsonProtocol._

sealed trait ProcessedZulipInboundMessage {
  val contents:String
  val headers:String
  val statusCode:String
}

//case class QueueMessage(contents:String, headers: String, statusCode: String, 
//  target:String, subject:String="", isPrivate:Boolean=false, id:String) extends ProcessedZulipInboundMessage

case class QueueMessageJson(result:String, msg:String, events:Array[UserRequestMessageJson])

case class UserRequestMessageJson(sender_email:String, content:String, subject:String, `type`:String, id:String)

case class GenericErrorJson(result:String, msg:String) extends MessageSendingJsonProtocolsResult

case class QueueRequestErrorJson(result:String, msg:String, queue_id:String)

object QueueMessageJsonProtocols extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat5(UserRequestMessageJson)
  implicit val queueMessageJsonFormat = jsonFormat3(QueueMessageJson)
  implicit val queueRequestErrorJson = jsonFormat3(QueueRequestErrorJson)

}

case class RequestNewQueueIDSuccJson(msg:String, last_event_id: Int, result:String, queue_id:String)

object RequestQueueIDJsonProtocols extends DefaultJsonProtocol {
  implicit val queueIDSuccFormat = jsonFormat4(RequestNewQueueIDSuccJson)
  implicit val errorFormat = jsonFormat2(GenericErrorJson)
}


//case class MessageSendingSuccessful(contents:String, headers: String, statusCode: String,
//  message:String, result:Boolean, id:String) extends ProcessedZulipInboundMessage

sealed trait MessageSendingJsonProtocolsResult

case class MessageSendingSuccessfulJson(msg:String, result: String, id: Int) extends MessageSendingJsonProtocolsResult

object MessageSendingJsonProtocols extends DefaultJsonProtocol {
  implicit val msgSendingSuccFormat = jsonFormat3(MessageSendingSuccessfulJson)
  implicit val errorFormat = jsonFormat2(GenericErrorJson)
}
//
//case class RequestEventQueue(contents:String, headers: String, statusCode: String,
//  message:String, lastEventId:String, result:Boolean, queueId:String) extends ProcessedZulipInboundMessage





