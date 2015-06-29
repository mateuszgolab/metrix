package tutorial.webapp

import org.scalatest._
import selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebDriver

class ScrollingSpec extends FlatSpec with Matchers with GivenWhenThen with WebBrowser {

  implicit val webDriver: WebDriver = new HtmlUnitDriver

  val host = "http://localhost:5000/"

  def dataLayer(key: String): String =
    executeScript(s"ddlHelper.get($key);").asInstanceOf[String]

  "scrolltracking" should "detect when page fits into the viewport" in {
    Given("A page that is smaller than the screen size")
    go to (host + "/small.html")
    When("scrolltracking records metrics")
    // let scrolltracking kick off and fire first metrics
    Then("on page load, scroll is 0%")
    dataLayer("metrics.page.small.maxScrolled") should be ("0%")
    And("it says entire screen has been viewed")
    dataLayer("metrics.page.small.maxViewed") should be ("100%")
  }
}