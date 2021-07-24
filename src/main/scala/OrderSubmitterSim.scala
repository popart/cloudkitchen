import scala.util.control.Breaks._
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.core.`type`.TypeReference

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import model.Order

class OrderSubmitterSim(val orderCallback: Order => ()) extends Runnable {

  val inputFile = "dispatch_orders.json"
  val jsonContent = scala.io.Source.fromResource(inputFile).mkString

  val mapper = JsonMapper.builder()
    .addModule(DefaultScalaModule)
    .build()
  var orders: List[Order] = mapper.readValue(jsonContent, new TypeReference[List[Order]] {})

  def run() {
    var lastRunTime = System.currentTimeMillis()
    breakable {
      while (true) {
        if (System.currentTimeMillis - lastRunTime >= 1000) {
          if (orders.isEmpty) break
          submitOrder

          if (orders.isEmpty) break
          submitOrder

          lastRunTime = System.currentTimeMillis
        } else {
          Thread.sleep(1000 - (System.currentTimeMillis - lastRunTime))
        }
      }
    }
  }

  def submitOrder = {
    orderCallback(orders.head)
    orders = orders.tail
  }
}
