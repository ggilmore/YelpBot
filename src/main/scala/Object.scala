/**
 * Created by gmgilmore on 3/23/15.
 */


import scalaj.http.{Http, Token}
import scala.sys.process._
object Junk extends App {



  """-d "type=private" -d  "to=ggilmore@mit.edu" -d "content=I come not, friends, to steal away your hearts." """


  println(Http("https://api.zulip.com/v1/messages").auth("yelpbot-bot@students.hackerschool.com", "NK3ilaGgyV9TxCikfEBbJVmBUKzNYKMx").postForm(Seq("type" -> "private", "to" -> "ggilmore@mit.edu", "content" ->"I come not, friends, to steal away your hearts." )).asString)
}