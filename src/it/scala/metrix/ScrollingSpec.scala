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
  // java.lang.System.setProperty("webdriver.chrome.driver", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
  implicit val webDriver: WebDriver = //new HtmlUnitDriver << no JS!
    new PhantomJSDriver
    // new ChromeDriver
    // new org.openqa.selenium.firefox.FirefoxDriver
  override def afterAll = webDriver.quit()
}

class ScrollingSpec extends WebFunctionalTest {

  def getFromDataLayer[T](key: String): T =
    executeScript(s"return ddlHelper.get('$key');").asInstanceOf[T]

  // getting a float, double, byte or short from the datalayer is
  // tricky! If you set e.g. a double that can be stored as an int
  // you'll get back an int. You have to go through a java.lang.Number
  // first to avoid class cast exception.
  def getDoubleFromDataLayer(key: String): Double =
    getFromDataLayer[java.lang.Number](key).doubleValue()

  "scrolltracking" should "detect when page fits into the viewport" in {
    Given("A page that is smaller than the screen size")
    go to (baseUrl + "page/small.html")
    When("scrolltracking records metrics")
    // let scrolltracking kick off and fire first metrics
    Then("on page load, scroll is 0%")
    val ms = getDoubleFromDataLayer("metrics.scrolled")
    ms should be (0d)
    And("it says entire screen has been viewed")
    val mv = getDoubleFromDataLayer("metrics.viewed")
    mv should be (100d)
  }
}