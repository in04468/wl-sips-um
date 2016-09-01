package unit

import org.mockito.Matchers._
import org.mockito.Mockito._
import play.api.Configuration
import play.api.cache.CacheApi
import play.api.libs.json.{JsArray, Json}
import play.core.server.Server
import play.api.routing.sird._
import play.api.mvc._
import play.api.test._
import salesforce.SalesforceDao

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by in04468 on 28-07-2016.
  */
class SalesforceDaoTest extends UnitTestsSpec {
  "SalesforceDao#generateOAuthToken" should {
    "return OAuthToken object in success scenario" in {
      val mockCacheApi = mock[CacheApi]
      val mockConfiguration = mock[Configuration]
      when(mockConfiguration.getString("salesforce.oauth.url")) thenReturn Some("/services/oauth2/token")
      when(mockConfiguration.getString("salesforce.oauth.consumer-key")) thenReturn Some("key")
      when(mockConfiguration.getString("salesforce.oauth.consumer-secret")) thenReturn Some("secret")
      when(mockConfiguration.getString("salesforce.user.email")) thenReturn Some("MyUser")
      when(mockConfiguration.getString("salesforce.user.password")) thenReturn Some("passwd")
      Server.withRouter() {
        case POST(p"/services/oauth2/token" ? q"grant_type=$grantType"
          & q"client_id=$clientId" & q"client_secret=$clientSecret"
          & q"username=$userName" & q"password=$password") => Action {
            Results.Ok(sampleoAuthToken)
        }
      } { implicit port =>
        WsTestClient.withClient { client =>
          val result = new SalesforceDao (mockCacheApi, client, mockConfiguration).generateOAuthToken
          result.get.signature mustBe "o+0WetiBNxetLTiX6kdy+MI7VC3sPkLMHCrjN1/rFFk="
          result.get.access_token mustBe "00D58000000PA6v!AQgAQACi0JD3QFxOrMi1_CwIxd.G7hl3_OmjyoCWtX_D8cmeJQyaH7df3QzayQbYIJGc._MaoJcng2DgeSCMcQuZ7cJepBzs"
        }
      }
    }
  }
}
