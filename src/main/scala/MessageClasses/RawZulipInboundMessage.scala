package MessageClasses

import scalaj.http.HttpResponse

/**
 * Created by gmgilmore on 3/24/15.
 */
case class RawZulipInboundMessage(contents:HttpResponse[String]){
  require(RawZulipInboundMessage != null)
}