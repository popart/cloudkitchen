import model.{Courier, Order}
import util.OrderStats
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import scala.collection.concurrent.TrieMap

class OrderFulfillerMatched extends Runnable {
  val running = new AtomicBoolean(false)
  var orderPrepper: Option[OrderPrepper] = None
  var courierDispatcher: Option[CourierDispatcher] = None
  val orderMap = new TrieMap[String, Order]
  val courierQueue = new ArrayBlockingQueue[Courier](1000)

  def createOrder(order: Order) = {
    order.createdAt = System.currentTimeMillis
    orderPrepper match {
      case Some(prepper) => prepper.prepareOrder(order)
      case None => ()
    }
    courierDispatcher match {
      case Some(dispatcher) => dispatcher.dispatchCourier(order.id)
      case None => ()
    }
  }

  def completeOrder(order: Order) = {
    orderMap += (order.id -> order)
  }

  def availableCourier(courier: Courier) = {
    courierQueue.add(courier)
  }

  def run() = {
    running.set(true)
    var courier: Courier = null

    while(running.get()) {
      if (courier == null) courier = courierQueue.poll

      if (courier != null) {
        orderMap.remove(courier.orderId) match {
          case Some(order) => {
            val pickupTime = System.currentTimeMillis
            val stats = OrderStats.addStats(pickupTime, order, courier)

            println("delivering order to courier")
            println(order)
            println(courier)
            println(s"pickupTime: $pickupTime, orderWaitTime: ${stats._1}, courierWaitTime: ${stats._2}")

          }
          case None => availableCourier(courier) // make sure this puts it at the end
        }

        courier = null
      }
    }
  }
}
