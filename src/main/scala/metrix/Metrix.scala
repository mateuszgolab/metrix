package metrix

import scala.util.Try
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSName}
import js.Dynamic.{literal => obj}
import org.scalajs.dom

object Metrix extends js.JSApp {
  def main(): Unit = Scroll.init()
}

sealed trait Metrics
case class Scroll(scrolled: Double, viewed: Double) extends Metrics
// trait Aggregation extends Metrics {
//   def apply(): Unit
// }
// case class Max(m: Metrics) extends Aggregation {
//   def apply() = asMap(js.JSON.parse(write(m)))
// }
// Max(Scroll())

object Scroll {
  import Helpers._
  def init(): Unit = {
    // initiate metrics in the ddl
    // if we don't do it after the document is rendered, height is 0
    val window = dom.document.defaultView
    val onLoadListener = asListener {
      DataLayer.pushMetrics(apply())
    }
    // TODO: make it work with addEventListener("onload", listener)
    // window.addEventListener("onload", onLoadListener)
    window.onload = onLoadListener
    // register listener
    val recordMaxScroll = asListener {
      val scroll = apply()
      // TODO: the key should not be hardcoded
      Try(DataLayer.get[Double]("metrics.viewed")) filter {
        max => scroll.viewed >= max
      } foreach { _ =>
        DataLayer.pushMetrics(scroll)
      }
    }
    window.addEventListener("scroll", recordMaxScroll) // can't we discard scroll up?
    window.addEventListener("resize", recordMaxScroll)
  }

  def apply(): Scroll = {
    val doc = dom.document
    val currentScrolling = doc.documentElement.scrollTop
    val pageHeight = doc.body.scrollHeight

    val window = doc.defaultView
    val currentDepthViewed = currentScrolling + window.innerHeight
    Scroll(percent(currentScrolling, pageHeight),
           percent(currentDepthViewed, pageHeight))
  }
}

object Helpers {
  def asListener(f: => Unit) = (_: dom.Event) => f
  def asVerboseListener(trace: String)(f: => Unit) =
    (_: dom.Event) => { println(trace); f }
  def percent(n: Double, d: Double): Double = n / d * 100d
}

import upickle.default._

@JSName("DataLayerHelper")
class DataLayerHelper(ddl: js.Array[js.Any]) extends js.Object {
  def get(key: String): js.Dynamic = js.native
}

object DOMGlobalScope extends js.GlobalScope {
  def dataLayer: js.Array[js.Any] = js.native
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
  def pushMetrics(m: Metrics) = push(obj(metrics = js.JSON.parse(write(m))))
}