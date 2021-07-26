import model.Order

object Main extends App {
  println("Hello, Amigo!")

  val orderFulfiller = new OrderFulfiller

  val orderPrepper = new OrderPrepperSim(orderFulfiller.completeOrder)
  orderFulfiller.orderPrepper = Some(orderPrepper)

  val courierDispatcher = new CourierDispatcherSim(orderFulfiller.availableCourier)
  orderFulfiller.courierDispatcher = Some(courierDispatcher)

  val orderSubmitter = new OrderSubmitterSim(orderFulfiller.createOrder)

  new Thread(orderFulfiller).start
  new Thread(orderPrepper).start
  new Thread(courierDispatcher).start
  new Thread(orderSubmitter).start

  Thread.sleep(20000)
  orderFulfiller.running.set(false)
  orderPrepper.running.set(false)
  courierDispatcher.running.set(false)
}
