package unit

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.Results

/**
  * Created by in04468 on 26-07-2016.
  */
abstract class UnitTestsSpec extends PlaySpec with MockitoSugar with Results {
  def sampleContacts = Json.parse("[{\"attributes\":{\"type\":\"Contact\",\"url\":\"/services/data/v31.0/sobjects/Contact/00358000007nP9kAAE\"},\"Id\":\"00358000007nP9kAAE\",\"FirstName\":\"Rose\",\"LastName\":\"Gonzalez\",\"Email\":\"rose@edge.com\"},{\"attributes\":{\"type\":\"Contact\",\"url\":\"/services/data/v31.0/sobjects/Contact/00358000007nP9lAAE\"},\"Id\":\"00358000007nP9lAAE\",\"FirstName\":\"Sean\",\"LastName\":\"Forbes\",\"Email\":\"sean@edge.com\"}]")
  def sampleoAuthToken = "{\"access_token\":\"00D58000000PA6v!AQgAQACi0JD3QFxOrMi1_CwIxd.G7hl3_OmjyoCWtX_D8cmeJQyaH7df3QzayQbYIJGc._MaoJcng2DgeSCMcQuZ7cJepBzs\",\"instance_url\":\"https://eu6.salesforce.com\",\"id\":\"https://login.salesforce.com/id/00D58000000PA6vEAG/00558000001IkYXAA0\",\"token_type\":\"Bearer\",\"issued_at\":\"1472566995708\",\"signature\":\"o+0WetiBNxetLTiX6kdy+MI7VC3sPkLMHCrjN1/rFFk=\"}"
}
