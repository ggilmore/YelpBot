package MessageClasses

/**
 * Created by gmgilmore on 3/24/15.
 */
case class ZulipOutboundMessage(target:String, isPrivate:Boolean = false, subject:String ="", content:String){
  require(target != null)
  require(isPrivate != null)
  require(subject != null)
  require(content != null)
}
