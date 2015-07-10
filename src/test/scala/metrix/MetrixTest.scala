package metrix

import scala.scalajs.js
import utest._

object MetrixTest extends TestSuite {

  Metrix.main()

  def tests = TestSuite {
    'Scrolling {
      val maxScrolled = DataLayer.get[Double]("metrics.maxScrolled")
      assert(maxScrolled == 0d)
      assert(DataLayer.get[Double]("metrics.maxViewed") == 100d)
    }
  }
}