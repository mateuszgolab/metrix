package metrix

import scala.util.Try
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSName}
import js.Dynamic.{literal => obj}
import org.scalajs.dom

object Metrix extends js.JSApp {

  def main(): Unit = {
    initiateScrollTracking()
  }

  def initiateScrollTracking(): Unit = {
    // initiate metrics in the ddl
    val Scroll(currentScrollingPercent, currentViewedPercent) = captureScroll()
    DataLayer.push(obj(metrics =
                         obj(maxScrolled = currentScrollingPercent,
                             maxViewed = currentViewedPercent)))

    // register listener
    val recordMaxScroll = (_: Any) => {
      val Scroll(currentScrollingPercent, currentViewedPercent) = captureScroll()
      Try(DataLayer.get[Double]("metrics.maxViewed")) filter {
        max => currentViewedPercent >= max
      } foreach { _ =>
        DataLayer.push(obj(metrics =
                             obj(maxScrolled = currentScrollingPercent,
                                 maxViewed = currentViewedPercent)))
      }
    }
    val window = dom.document.defaultView
    window.addEventListener("scroll", recordMaxScroll) // can't we discard scroll up?
    window.addEventListener("resize", recordMaxScroll)
  }

  def captureScroll(): Scroll = {
    val currentScrolling = dom.document.defaultView.pageYOffset
    val pageScrollHeight = dom.document.body.scrollHeight
    val currentScrollingPercent = (currentScrolling / pageScrollHeight) * 100

    val currentDepthViewed = currentScrolling + dom.document.documentElement.clientHeight
    val currentViewedPercent = (currentDepthViewed / pageScrollHeight) * 100
    Scroll(currentScrollingPercent, currentViewedPercent)
  }

  sealed trait Metrics
  case class Scroll(depth: Double, viewed: Double) extends Metrics
}



@JSName("DataLayerHelper")
class DataLayerHelper(ddl: js.Array[js.Any]) extends js.Object {
  def get(key: String): js.Dynamic = js.native
}

object DOMGlobalScope extends js.GlobalScope {
  def dataLayer: js.Array[js.Any] = js.native
  // def window
}

object DataLayer {
  lazy val instance: js.Array[js.Any] =
    if (js.isUndefined(DOMGlobalScope.dataLayer)) {
      val ddl = new js.Array[js.Any]
      js.Dynamic.global.dataLayer = ddl
      ddl
    } else DOMGlobalScope.dataLayer

  lazy val helper: DataLayerHelper = new DataLayerHelper(instance)
  def push(json: js.Dynamic) = instance.push(json)
  def get(key: String): String = helper.get(key).asInstanceOf[String]
  def get[U](key: String): U = helper.get(key).asInstanceOf[U]
}