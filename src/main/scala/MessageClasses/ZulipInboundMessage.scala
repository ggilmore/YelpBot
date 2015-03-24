package MessageClasses

import scalaj.http.HttpResponse

/**
 * Created by gmgilmore on 3/24/15.
 */
case class ZulipInboundMessage(contents:HttpResponse[String])
