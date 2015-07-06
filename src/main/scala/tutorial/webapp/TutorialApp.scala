package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object TutorialApp extends js.JSApp {

  def main(): Unit = {
//    println("Hello world!")
    appendPar(document.body, "Hello World")
  }

    def setupUI(): Unit = {
      jQuery("""<button type="button">Click me!</button>""")
        .click(addClickedMessage _)
        .appendTo(jQuery("body"))
      jQuery("body").append("<p>Hello World</p>")
    }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  @JSExport
  def addClickedMessage(): Unit = {
    appendPar(document.body, "You clicked the button!")
  }

  // trait RichWindow extends scala.scalajs.Window
  object DOMGlobalScope extends js.GlobalScope {
    def ddlHelper: DataLayerHelper = js.native
    def dataLayer: js.Array[js.Any] = js.native
  }

  trait DataLayerHelper extends js.Object {
    def get(key: String): String = js. native
  }

  object DataLayerHelper {
    def instance = {
      if (js.isUndefined(DOMGlobalScope.ddlHelper)) {
        installNew()
      }
      DOMGlobalScope.ddlHelper
    }

    def installNew() = js.undefined
      // js.Dynamic.global.ddlHelper =
        // js.Dynamic.newInstance(js.Dynamic.global.DataLayerHelper)(DataLayer.instance)
  }



  object DataLayer {
    import scala.scalajs.js.Dynamic.global

    // @JSExport
    // def get(key: String): String =
    //   DOMGlobalScope.ddlHelper.get(key)

    def instance: js.Array[js.Any] =
      if (js.isUndefined(DOMGlobalScope.dataLayer)) {
        val ddl = new js.Array[js.Any]
        // js.Dynamic.global.dataLayer = ddl
        ddl
      } else DOMGlobalScope.dataLayer
  }
}
