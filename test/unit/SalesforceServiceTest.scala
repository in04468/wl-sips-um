package unit

import models.{Contact, SalesforceException}
import org.mockito.Matchers.anyString
import org.mockito.Mockito._
import play.api.libs.json.JsArray
import salesforce.{SalesforceDao, SalesforceService}

/**
  * Created by in04468 on 26-07-2016.
  */
class SalesforceServiceTest extends UnitTestsSpec {
  "SalesforceService#getContacts" should {
    "return list of contacts in case of successful processing" in {
      val mockSalesforceDao = mock[SalesforceDao]
      when(mockSalesforceDao.query(anyString)) thenReturn sampleContacts.as[JsArray]
      val sfService = new SalesforceService(mockSalesforceDao)
      val result = sfService.getContacts

      result.size mustBe 2
      result.head mustBe an[Contact]
      result.last.firstName mustBe "Sean"
    }

    "return Nil in case of any exception in processing" in {
      val mockSalesforceDao = mock[SalesforceDao]
      when(mockSalesforceDao.query(anyString)) thenThrow new SalesforceException("Some reason")
      val sfService = new SalesforceService(mockSalesforceDao)
      val result = sfService.getContacts

      result mustBe Nil
    }
  }
}
