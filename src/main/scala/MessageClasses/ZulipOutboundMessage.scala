package MessageClasses

/**
 * Created by gmgilmore on 3/24/15.
 */
case class ZulipOutboundMessage(target:String, isPrivate:Boolean = false, subject:String ="", content:String)
