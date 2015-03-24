/**
 * Created by gmgilmore on 3/24/15.
 */

import java.util.concurrent
import java.util.concurrent.LinkedBlockingQueue

import MessageClasses.{ZulipOutboundMessage, ZulipInboundMessage}

object Queues {

  private val ZulipInQueue:LinkedBlockingQueue[ZulipInboundMessage] = new LinkedBlockingQueue[ZulipInboundMessage]

  private val ZulipOutQueue:LinkedBlockingQueue[ZulipOutboundMessage] = new LinkedBlockingQueue[ZulipOutboundMessage]

  def putOnZulipInQueue(msg:ZulipInboundMessage) = ZulipInQueue.put(msg)

  def takeFromZulipInQueue:ZulipInboundMessage = ZulipInQueue.take

  def putOnZulipOutQueue(msg:ZulipOutboundMessage) = ZulipOutQueue.put(msg)

  def takeFromZulipOutQueue:ZulipOutboundMessage = ZulipOutQueue.take

}
