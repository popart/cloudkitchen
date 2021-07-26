package model

class Order(val id: String, val name: String, val prepTime: Int) extends Comparable[Order] {
  var createdAt: Long = 0

  def readyAt: Long = createdAt + (prepTime * 1000)
  def isReady: Boolean = System.currentTimeMillis >= readyAt

  override def compareTo(otherOrder: Order): Int = {
    (this.readyAt - otherOrder.readyAt).toInt
  }
  override def toString = s"(id: $id, name: $name, prepTime: $prepTime, createdAt: $createdAt)"
}
