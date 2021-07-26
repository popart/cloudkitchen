import model.Order
import util.OrderStats

object Main extends App {
  println("--------- beginning FIFO simulation ------------")
  val orderFulfillerFIFO = new OrderFulfillerFIFO

  var orderPrepper = new OrderPrepperSim(orderFulfillerFIFO.completeOrder)
  orderFulfillerFIFO.orderPrepper = Some(orderPrepper)

  var courierDispatcher = new CourierDispatcherSim(orderFulfillerFIFO.availableCourier)
  orderFulfillerFIFO.courierDispatcher = Some(courierDispatcher)

  var orderSubmitter = new OrderSubmitterSim(orderFulfillerFIFO.createOrder)

  new Thread(orderFulfillerFIFO).start
  new Thread(orderPrepper).start
  new Thread(courierDispatcher).start
  new Thread(orderSubmitter).start

  Thread.sleep(30000)
  orderFulfillerFIFO.running.set(false)
  orderPrepper.running.set(false)
  courierDispatcher.running.set(false)

  val avgFIFOStats = OrderStats.getAverageStats

  println("--------- beginning MATCHED simulation ------------")

  OrderStats.clearStats
  val orderFulfillerMatched = new OrderFulfillerMatched

  orderPrepper = new OrderPrepperSim(orderFulfillerMatched.completeOrder)
  orderFulfillerMatched.orderPrepper = Some(orderPrepper)

  courierDispatcher = new CourierDispatcherSim(orderFulfillerMatched.availableCourier)
  orderFulfillerMatched.courierDispatcher = Some(courierDispatcher)

  orderSubmitter = new OrderSubmitterSim(orderFulfillerMatched.createOrder)

  new Thread(orderFulfillerMatched).start
  new Thread(orderPrepper).start
  new Thread(courierDispatcher).start
  new Thread(orderSubmitter).start

  Thread.sleep(30000)
  orderFulfillerMatched.running.set(false)
  orderPrepper.running.set(false)
  courierDispatcher.running.set(false)

  val avgMatchedStats = OrderStats.getAverageStats

  println("--------- END simulation ------------")

  println(s"FIFO avg order wait time: ${avgFIFOStats._1}, avg courier wait time: ${avgFIFOStats._2}")
  println(s"Matched avg order wait time: ${avgMatchedStats._1}, avg courier wait time: ${avgMatchedStats._2}")
}
