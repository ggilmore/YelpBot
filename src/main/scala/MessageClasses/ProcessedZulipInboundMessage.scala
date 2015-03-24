package MessageClasses

/**
 * Created by gmgilmore on 3/24/15.
 */
sealed trait ProcessedZulipInboundMessage {
  val contents:String
  val headers:String
  val statusCode:String
}

case class QueueMessage(contents:String, headers: String, statusCode: String, 
  target:String, subject:String="", isPrivate:Boolean=false, id:String) extends ProcessedZulipInboundMessage

case class MessageSendingSuccessful(contents:String, headers: String, statusCode: String,
  message:String, result:Boolean, id:String) extends ProcessedZulipInboundMessage

case class RequestEventQueue(contents:String, headers: String, statusCode: String,
  message:String, lastEventId:String, result:Boolean, queueId:String) extends ProcessedZulipInboundMessage
