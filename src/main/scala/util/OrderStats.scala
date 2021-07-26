package util

import model.{Courier, Order}

object OrderStats {
  var orderWaitTimes = List[Long]()
  var courierWaitTimes = List[Long]()

  def addStats(pickupTime: Long, order: Order, courier: Courier): (Long, Long) = {
    val orderWaitTime = pickupTime - order.readyAt
    val courierWaitTime = pickupTime - courier.readyAt

    orderWaitTimes = orderWaitTime :: orderWaitTimes
    courierWaitTimes = courierWaitTime :: courierWaitTimes

    return (orderWaitTime, courierWaitTime)
  }

  def getAverageStats = { 
    (orderWaitTimes.sum / orderWaitTimes.length, courierWaitTimes.sum / courierWaitTimes.length)
  }

  def clearStats = {
    orderWaitTimes = List()
    courierWaitTimes = List()
  }
}
