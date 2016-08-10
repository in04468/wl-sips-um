package salesforce

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import controllers.Logging
import models.{Contact, SalesforceException}
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json.{JsArray, JsResult, JsValue, Json}

/**
  * Created by in04468 on 29-06-2016.
  */
class SalesforceService(val salesforceDao: SalesforceDao) extends Logging{

  /**
    * Retrieves all the contacts from sales force
    *
    * @return A list of Json elements holding the contact details for retrieved
    *         customers from sales force
    */
  def getContacts : Seq[Contact] = {
    val queryStr = "select Id, FirstName, LastName, Email from Contact"
    log.debug("Querying sales force with: '" + queryStr + "'")
    var result:Seq[Contact] = Nil
    try {
      result = salesforceDao.query(queryStr).validate[Seq[Contact]].get
    } catch {
      case ex: SalesforceException => log.error("ERROR: " + ex.getMessage +"\nError Description: " + ex.getCause)
      case ex: Exception => log.error("ERROR: " + ex.getMessage +"\nError Description: " + ex.getCause)
    }
    log.debug("Got results : " + result.length)
    return result
  }

  def getContact(field: String, value:String) : Contact = {
    val queryStr = "select Id,FirstName,LastName,Email,Token__c,Activated_On__c from Contact where " + field + " = '" + value + "'"
    log.debug("Querying sales force with: '" + queryStr + "'")
    var result: Contact = null
    try {
      val lresult = salesforceDao.query(queryStr).validate[Seq[Contact]].get
      result = lresult.head
    } catch {
      case ex: SalesforceException => log.error("ERROR: " + ex.getMessage +"\nError Description: " + ex.getCause)
      case ex: Exception => log.error("ERROR: " + ex.getMessage +"\nError Description: " + ex.getCause)
    }
    log.debug("Got results : " + result)
    return result
  }

  def updateContact(id:String, password: String): Int = {
    var res: Int = 0
    val dateTimeFormat = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ssZ")
    //log.info("Date is :"+BCrypt.hashpw(password, BCrypt.gensalt()))
    val jsonReq  : JsValue = Json.obj(
      "Password__c" -> BCrypt.hashpw(password, BCrypt.gensalt()),
      "Activated_On__c" -> dateTimeFormat.format(new Date)
    )
    try {
      res = salesforceDao.update("Contact", id, jsonReq)
      log.info("Update query result " + res)
    } catch {
      case ex: SalesforceException => log.error("ERROR: " + ex.getMessage +"\nError Description: " + ex.getCause)
      res = 500
    }
    return res
  }

}
