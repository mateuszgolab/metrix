package metrix

import scala.scalajs.js
import utest._

object MetrixTest extends TestSuite {

  def tests = TestSuite {
    'Scrolling {
      val scroll = Scroll()
      println(scroll)
      // import upickle.default._
      // println(write(scroll))
      DataLayer.pushMetrics(scroll)
      val maxScrolled = DataLayer.get[Double]("metrics.scrolled")
      // println(maxScrolled)
      assert(maxScrolled == 0d)
      assert(DataLayer.get[Double]("metrics.viewed") == 100d)
    }
  }
}