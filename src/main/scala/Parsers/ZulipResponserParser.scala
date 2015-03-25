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

  def parseQueueMessageResponse(msg:String):Either[ParsingFailure, QueueMessageJsonProtocolsResult] = {
    import QueueMessageJsonProtocols._
    try {
      Right(msg.parseJson.convertTo[QueueMessageJson])
    }
    catch {
      case e: DeserializationException => {
        try {
          Right(msg.parseJson.convertTo[QueueRequestErrorJson])
        }
        catch {
          case e: DeserializationException => Left(ParsingError(e.getMessage))
        }
      }

    }
  }

  println(parseQueueMessageResponse("""{"msg":"Invalid authorization header for basic auth","result":"error"}"""))


//  def parseResponse[T](protocol:DefaultJsonProtocol, candidates:Seq[T], msg:String) = {
//
//    protocol.
//    def tryConversion(candidates:Seq[T], errText:String = ""):Either[ParsingFailure, T] = {
//      if (candidates.isEmpty) Left(ParsingError(errText))
//      else {
//        try {
//          val head = candidates.head
//          Right(msg.parseJson.convertTo[head.type])
//        }
//        catch {
//          case e: DeserializationException => tryConversion(candidates.drop(1), e.getMessage)
//        }
//
//      }
//    }
//    tryConversion(candidates)
//  }

//  val genError = GenericErrorJson("", "")
//  val msgSendSuccessfulJson = MessageSendingSuccessfulJson("","","")
  println(parseUserMessageResponse("""{"msg": "", "result": "success", "id": 12345678}"""))
  // parseResponse()

}
