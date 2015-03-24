package Queues

/**
 * Created by gmgilmore on 3/24/15.
 */

import java.util.concurrent.LinkedBlockingQueue

import MessageClasses.{RawZulipInboundMessage, ZulipOutboundMessage}

object Queues {

  private val ZulipInQueue:LinkedBlockingQueue[RawZulipInboundMessage] = new LinkedBlockingQueue[RawZulipInboundMessage]

  private val ZulipOutQueue:LinkedBlockingQueue[ZulipOutboundMessage] = new LinkedBlockingQueue[ZulipOutboundMessage]

  def putOnZulipInQueue(msg:RawZulipInboundMessage) = ZulipInQueue.put(msg)

  def takeFromZulipInQueue:RawZulipInboundMessage = ZulipInQueue.take

  def putOnZulipOutQueue(msg:ZulipOutboundMessage) = ZulipOutQueue.put(msg)

  def takeFromZulipOutQueue:ZulipOutboundMessage = ZulipOutQueue.take

}
