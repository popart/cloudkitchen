package model

import scala.util.Random

class Courier(val orderId: String = "") extends Comparable[Courier] {
  val prepTime = new Random().nextInt(12) + 3
  var requestedAt: Long = System.currentTimeMillis

  def readyAt: Long = requestedAt + (prepTime * 1000)
  def isReady: Boolean = System.currentTimeMillis >= readyAt

  override def compareTo(otherCourier: Courier) = {
    (this.readyAt - otherCourier.readyAt).toInt
  }
  override def toString = s"(requestedAt: $requestedAt, prepTime: $prepTime, readyAt: $readyAt)"
}
