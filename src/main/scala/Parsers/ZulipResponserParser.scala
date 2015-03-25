package Parsers

import MessageClasses._
import spray.json._
import DefaultJsonProtocol._

/**
 * Created by gmgilmore on 3/24/15.
 */

sealed trait ParsingFailure
object ZulipResponserParser extends App {

  case class ParsingError(errText:String) extends ParsingFailure

  def parseUserMessageResponse(msg:String):Either[ParsingFailure, MessageSendingJsonProtocolsResult] = {
    import MessageSendingJsonProtocols._
    try{
      Right(msg.parseJson.convertTo[MessageSendingSuccessfulJson])
    }
    catch {
      case e: DeserializationException => {
        try{
          Right(msg.parseJson.convertTo[GenericErrorJson])
        }
        catch {
          case e: DeserializationException => Left(ParsingError(e.getMessage))
        }
      }
    }

  }

}
