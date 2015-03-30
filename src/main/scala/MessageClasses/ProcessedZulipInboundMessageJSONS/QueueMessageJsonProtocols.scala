package MessageClasses.ProcessedZulipInboundMessageJSONS

import spray.json.DefaultJsonProtocol

/**
 * Created by gmgilmore on 3/25/15.
 */

sealed trait QueueMessageJsonProtocolsResult

case class QueueMessageJson(result:String, msg:String, events:Seq[UserRequestMessageJson], last_event_id:Int) extends QueueMessageJsonProtocolsResult

// case class QueueMessageErrorJson(result:String, msg:String, queue_id:Int=-1) extends QueueMessageJsonProtocolsResult

case class UserRequestMessageJson(sender_email:String, content:String, subject:String, `type`:String, id:String) extends QueueMessageJsonProtocolsResult

case class QueueRequestErrorJson(result:String, msg:String) extends QueueMessageJsonProtocolsResult

case object NoParsedMessages extends QueueMessageJsonProtocolsResult
//
//object QueueMessageJsonProtocols extends DefaultJsonProtocol {
//  implicit val messageFormat = jsonFormat5(UserRequestMessageJson)
//  implicit val queueMessageJsonFormat = jsonFormat3(QueueMessageJson)
//  implicit val queueRequestErrorJson = jsonFormat3(QueueRequestErrorJson)}
