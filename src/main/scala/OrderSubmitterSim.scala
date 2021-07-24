import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import model.Order

object OrderSubmitterSim {
  val input_file = "dispatch_orders.json"
  val json_content = scala.io.Source.fromResource(input_file).mkString

  val mapper = JsonMapper.builder()
    .addModule(DefaultScalaModule)
    .build()
  val json_data = mapper.readValue(json_content, classOf[List[Order]])
}
