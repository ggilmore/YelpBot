import collection.mutable.Stack
import org.scalatest._
import Parsers._

  /**
  * Testing strategy: processMessage
  * - json with keys "id", "type", "subject", "sender_email", "content" all mapping to JsString
  * - json with other keys mixed in
  * - json with missing id
  * - json with missing type
  * - json with missing subject
  * - json with missing sender_email
  * - json with missing content
  * - json with none of the 5 keys
  * - json with id value which is not JsNumber
  * - json with "type", "subject", "sender_email", "content" values that are not JsString
  */

class ZulipResponserParserTest extends FlatSpec with Matchers {
  "processMessage" should "successfully process a msg when id, type, subject, sender_email and content are present" in {
    val msg = """{"id":1, "type":"private", "subject":"hi", "sender_email":"lyn.nagara@gmail.com", "content":"booya"}""".parseJson

    // assert(
    //   processMessage(msg) 

    // )

  }



  // "processMessage" should "be fine when creating a message with all parameters specified and not null" in {
  //   val msg = ZulipOutboundMessage(target = "Lyn", isPrivate = true, subject = "", content = "Hello.")
  // }

  // it  should "throw an exception when creating a message with all parameters specified besides target, which is null" in {
  //   intercept[IllegalArgumentException] {
  //     val msg = ZulipOutboundMessage(target = null, isPrivate = true, subject = "", content = "Hello.")
  //   }
  // }

  // it  should "throw an exception when creating a message with all parameters specified besides subject, which is null" in {
  //   intercept[IllegalArgumentException] {
  //     val msg = ZulipOutboundMessage(target = "Lyn" , isPrivate = true, subject = null, content = "Hello.")
  //   }
  // }

  // it  should "throw an exception when creating a message with all parameters specified besides content, which is null" in {
  //   intercept[IllegalArgumentException] {
  //     val msg = ZulipOutboundMessage(target = "Lyn" , isPrivate = true, subject = "", content = null)
  //   }
  // }

  // it should "have its value isPrivate default to false and its value subject default to the empty string" +
  //   "when both are not explicitly specified" in {
  //   val msg = ZulipOutboundMessage(target = "Lyn", content = "Hello.")
  //   assert(!msg.isPrivate)
  //   assert(msg.subject.isEmpty)
  // }

}