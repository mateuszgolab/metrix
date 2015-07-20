package metrix

import org.scalatest._
import selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.chrome.ChromeDriver
import com.typesafe.config.ConfigFactory

trait WebFunctionalTest extends FlatSpec with Matchers with GivenWhenThen with WebBrowser
    with BeforeAndAfterAll {
  val config = ConfigFactory.load()
  val port = config.getInt("http.port")
  val baseUrl = s"http://localhost:$port/"
  java.lang.System.setProperty("webdriver.chrome.driver", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
  implicit val webDriver: WebDriver = //new HtmlUnitDriver << no JS!
    // new PhantomJSDriver
    new ChromeDriver
    // new org.openqa.selenium.firefox.FirefoxDriver
  // override def afterAll = webDriver.quit()
}

class ScrollingSpec extends WebFunctionalTest {

  def dataLayer[T](key: String): T =
    executeScript(s"(function() { return ddlHelper.get('$key'); })();").asInstanceOf[T]

  "scrolltracking" should "detect when page fits into the viewport" in {
    Given("A page that is smaller than the screen size")
    go to (baseUrl + "page/small.html")
    // println(pageSource.substring(0, 300))
    When("scrolltracking records metrics")
    // let scrolltracking kick off and fire first metrics
    Then("on page load, scroll is 0%")
    val ms: Double = dataLayer[Double]("metrics.scrolled")
    println("ms: "+ms)
    // println(executeScript("ddlHelper;"))
    ms should be (0.0d)
    And("it says entire screen has been viewed")
    val mv = dataLayer[Double]("metrics.viewed")
    println(s"mv: $mv")
    // dom.document.documentElement.clientHeight
    val height = executeScript(s"(function() { return document.documentElement.clientHeight; })();").asInstanceOf[Double]
    println(s"height: $height")
    mv should be (100d)
  }
}