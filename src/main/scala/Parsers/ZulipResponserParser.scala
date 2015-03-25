package Parsers

import spray.json._
import DefaultJsonProtocol._

/**
 * Created by gmgilmore on 3/24/15.
 */
object ZulipResponserParser extends App {

  case class Message(id:Int, message:String)

  case class Color(name: String, testArray: List[Int], messages: List[Message], green: Int, blue: Int)

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val messageFormat = jsonFormat2(Message)
    implicit val colorFormat = jsonFormat5(Color)
  }

  import MyJsonProtocol._


  val colString = """{ "name": "testArray", "testArray": [1,2,3,4,5], "messages": [{"id":123, "message":"test", "crap": "crap", "morecrap": "morecrap", "array":[[1,2,3],[1,2]] }], "test": [1,2,3,4,5], "test2": "test2" , "green":99, "blue":98}"""
  println(colString.parseJson.convertTo[Color])


}
