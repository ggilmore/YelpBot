package MessageClasses.ProcessedZulipInboundMessageJSONS

import spray.json.DefaultJsonProtocol

/**
 * Created by gmgilmore on 3/25/15.
 */

case class RequestNewQueueIDSuccJson(msg:String, last_event_id: Int, result:String, queue_id:String)

object RequestQueueIDJsonProtocols extends DefaultJsonProtocol {
  implicit val queueIDSuccFormat = jsonFormat4(RequestNewQueueIDSuccJson)
  implicit val errorFormat = jsonFormat2(GenericErrorJson)
}


