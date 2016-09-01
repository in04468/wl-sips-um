package unit

import scala.concurrent.Future
import org.mockito.Mockito._
import play.api.cache.CacheApi
import play.api.test._
import play.api.mvc._
import controllers.Salesforce
import models.Contact
import play.api.libs.json.JsValue
import salesforce.SalesforceService
import play.api.test.Helpers._

/**
  * Created by in04468 on 27-07-2016.
  */
class SalesforceControllerTest extends UnitTestsSpec {
  "SalesforceController#getContacts" should {
    "should generate contacts in success scenario" in {
      val mockCacheApi = mock[CacheApi]
      val mockSalesforceService = mock[SalesforceService]
      when(mockSalesforceService.getContacts) thenReturn sampleContacts.as[Seq[Contact]]
      val controller = new Salesforce(mockCacheApi, mockSalesforceService)
      val result: Future[Result] = controller.getContacts().apply(FakeRequest())
      val json : JsValue = contentAsJson(result)

      //println((json.head  \ "FirstName").as[String])
      status(result) mustEqual OK
      contentType(result) mustBe Some("application/json")
      (json.head  \ "FirstName").as[String] mustBe "Rose"
    }

    "should generate empty list in the failure scenario" in {
      val mockCacheApi = mock[CacheApi]
      val mockSalesforceService = mock[SalesforceService]
      when(mockSalesforceService.getContacts) thenReturn Nil
      val controller = new Salesforce(mockCacheApi, mockSalesforceService)
      val result: Future[Result] = controller.getContacts().apply(FakeRequest())
      val json : String = contentAsString(result)

      status(result) mustEqual OK
      contentType(result) mustBe Some("application/json")
      json mustBe "[]"
    }
  }
}
