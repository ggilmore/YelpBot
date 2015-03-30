import collection.mutable.Stack
import MessageClasses.ZulipOutboundMessage
import org.scalatest._

/**
 * Testing Strategy:
 *
 * constructor:
 *  all valid
 *  null arguments
 *  default values
 *
 *
 */

class ZulipOutBoundMessageTest extends FlatSpec with Matchers {

  "ZulipOutBoundMessage" should "be fine when creating a message with all parameters specified and not null" in {
    val msg = ZulipOutboundMessage(target = "Lyn", isPrivate = true, subject = "", content = "Hello.")
  }

  it  should "throw an exception when creating a message with all parameters specified besides target, which is null" in {
    intercept[IllegalArgumentException] {
      val msg = ZulipOutboundMessage(target = null, isPrivate = true, subject = "", content = "Hello.")
    }
  }

  it  should "throw an exception when creating a message with all parameters specified besides subject, which is null" in {
    intercept[IllegalArgumentException] {
      val msg = ZulipOutboundMessage(target = "Lyn" , isPrivate = true, subject = null, content = "Hello.")
    }
  }

  it  should "throw an exception when creating a message with all parameters specified besides content, which is null" in {
    intercept[IllegalArgumentException] {
      val msg = ZulipOutboundMessage(target = "Lyn" , isPrivate = true, subject = "", content = null)
    }
  }

  it should "have its value isPrivate default to false and its value subject default to the empty string" +
    "when both are not explicitly specified" in {
    val msg = ZulipOutboundMessage(target = "Lyn", content = "Hello.")
    assert(!msg.isPrivate)
    assert(msg.subject.isEmpty)
  }



}