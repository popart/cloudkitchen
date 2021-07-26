import model.Order
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

trait OrderPrepper {
  def prepareOrder(order: Order)
}

class OrderPrepperSim(val completeOrderCallback: Order => ()) extends OrderPrepper with Runnable {

  val running = new AtomicBoolean(false)

  val allOrders = new PriorityBlockingQueue[Order]

  def prepareOrder(order: Order) = {
    allOrders.put(order)
  }

  def run() = {
    running.set(true)
    while(running.get()) {
      val order = allOrders.poll
      if (order != null) {
        if (order.isReady) {
          completeOrderCallback(order)
        } else {
          allOrders.put(order)
        }
      }
    }
  }
}
