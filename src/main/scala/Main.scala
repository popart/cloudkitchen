import model.Order

object Main extends App {
  println("Hello, Amigo!")

  def orderCallback(order: Order) = {
    println(order)
  }
  val orderSubmitter = new OrderSubmitterSim(orderCallback)
  new Thread(orderSubmitter).start
}
