import model.Courier
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

trait CourierDispatcher {
  def dispatchCourier
}

class CourierDispatcherSim(val availableCourierCallback: Courier => ()) extends CourierDispatcher with Runnable {

  val running = new AtomicBoolean(false)
  val allCouriers = new PriorityBlockingQueue[Courier]

  def dispatchCourier = {
    allCouriers.put(new Courier)
  }

  def run() = {
    running.set(true)
    while(running.get()) {
      val courier = allCouriers.poll
      if (courier != null) {
        if (courier.isReady) {
          availableCourierCallback(courier)
        } else {
          allCouriers.put(courier)
        }
      }
    }
  }
}
