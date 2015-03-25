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

  def parseResponse[T](protocol:DefaultJsonProtocol, candidates:Seq[T], msg:String) = {

    import protocol._
    def tryConversion(candidates:Seq[T], errText:String = ""):Either[ParsingFailure, T] = {
      if (candidates.isEmpty) Left(ParsingError(errText))
      else {
        try {
          val head = candidates.head
          Right(msg.parseJson.convertTo[head.type])
        }
        catch {
          case e: DeserializationException => tryConversion(candidates.drop(1), e.getMessage)
        }

      }
    }
    tryConversion(candidates)
  }

  val thingy:Seq[MessageSendingJsonProtocolsResult] =  Seq(GenericErrorJson:MessageSendingJsonProtocolsResult,MessageSendingSuccessfulJson: MessageSendingJsonProtocolsResult)
  println(parseResponse[MessageSendingJsonProtocolsResult](MessageSendingJsonProtocols, Vector(GenericErrorJson,MessageSendingSuccessfulJson):Vector[MessageSendingSuccessfulJson], ""))
  // parseResponse()

}
