package MessageClasses

/**
 * Created by gmgilmore on 3/24/15.
 */
sealed trait ProcessedZulipInboundMessage {

}

case class QueueMessage(contents:String) extends ProcessedZulipInboundMessage

case class MessageSendingSuccessful(contents:String) extends ProcessedZulipInboundMessage
