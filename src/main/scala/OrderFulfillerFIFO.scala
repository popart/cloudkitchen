import model.{Courier, Order}
import util.OrderStats
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class OrderFulfillerFIFO extends Runnable {
  val running = new AtomicBoolean(false)
  var orderPrepper: Option[OrderPrepper] = None
  var courierDispatcher: Option[CourierDispatcher] = None
  val orderQueue = new ArrayBlockingQueue[Order](1000)
  val courierQueue = new ArrayBlockingQueue[Courier](1000)

  def createOrder(order: Order) = {
    order.createdAt = System.currentTimeMillis
    orderPrepper match {
      case Some(prepper) => prepper.prepareOrder(order)
      case None => ()
    }
    courierDispatcher match {
      case Some(dispatcher) => dispatcher.dispatchCourier("")
      case None => ()
    }
  }

  def completeOrder(order: Order) = {
    orderQueue.add(order)
  }

  def availableCourier(courier: Courier) = {
    courierQueue.add(courier)
  }

  def run() = {
    running.set(true)
    var courier: Courier = null
    var order: Order = null
    while(running.get()) {
      if (courier == null) courier = courierQueue.poll
      if (order == null) order = orderQueue.poll

      if (courier != null && order != null) {
        val pickupTime = System.currentTimeMillis
        val stats = OrderStats.addStats(pickupTime, order, courier)

        println("delivering order to courier")
        println(order)
        println(courier)
        println(s"pickupTime: $pickupTime, orderWaitTime: ${stats._1}, courierWaitTime: ${stats._2}")

        courier = null
        order = null
      }
    }
  }
}
