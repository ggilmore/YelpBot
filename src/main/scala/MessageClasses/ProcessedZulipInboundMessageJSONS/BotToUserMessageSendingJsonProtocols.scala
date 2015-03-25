package MessageClasses.ProcessedZulipInboundMessageJSONS

import spray.json.DefaultJsonProtocol

/**
 * Created by gmgilmore on 3/25/15.
 */

object BotToUserMessageSendingJsonProtocols extends DefaultJsonProtocol {
  implicit val msgSendingSuccFormat = jsonFormat3(MessageSendingSuccessfulJson)
  implicit val errorFormat = jsonFormat2(GenericErrorJson)
}

trait MessageSendingJsonProtocolsResult

case class MessageSendingSuccessfulJson(msg:String, result: String, id: Int) extends MessageSendingJsonProtocolsResult