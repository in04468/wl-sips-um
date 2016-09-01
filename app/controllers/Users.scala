package controllers

import play.api.cache.CacheApi
import play.api.libs.json._
import play.api.mvc._
import models.{Contact, User}
import salesforce.SalesforceService

/** Example controller; see conf/routes for the the mapping to routes */
class Users(val cache: CacheApi, salesforceService: SalesforceService) extends Controller with Security with Logging{

  /** Retrieves a logged in user if the authentication token is valid.
    *
    * If the token is invalid, [[HasToken]] does not invoke this function.
    *
    * @return The user in JSON format.
    */
  def authUser() = HasToken(parse.empty) { token => userId => implicit request =>
    Ok(Json.toJson(findOneById(userId)))
  }

  def retrieveUser(token: String) = Action {
    //Verify the token with salesforce and fet the user details
    val contact: Contact = salesforceService.getContactByToken(token)
    //log.info("Retrived user with contact.activatedOn: "+contact.activatedOn)
    if (contact != null && contact.activatedOn == None ) {
      Ok(Json.toJson(contact))
    } else {
      Ok("")
    }
  }

  def setUserPassword() = Action(parse.json) { request => {
    log.info("Got: " + (request.body \ "id").as[String])
    var success: Boolean = false
    val res = salesforceService.updateContact((request.body \ "id").as[String], (request.body \ "password").as[String])
    if (res == 204) {
      success = true
    } else {
      success = false
    }
    Ok(Json.obj("success" -> success))
    }
  }

  def requestPasswdReset(email: String) = Action {
    var success: Boolean = false
    val contact: Contact = salesforceService.getContactByEmail(email)
    if (contact != null) {
      val res = salesforceService.updateContactToken(contact.id, java.util.UUID.randomUUID.toString)
      if (res == 204) {
        success = true
      } else {
        success = false
      }
    }
    Ok(Json.obj("success" -> success))
  }

  def findOneById(id: String): Option[User] = {
    val contact: Contact = salesforceService.getContact("Id", id)
    if (contact != null) {
      Some(User(contact.id, contact.email.getOrElse("dummy"), contact.password.getOrElse("dummy"), contact.firstName + " " + contact.lastName, None))
    } else {
      None
    }
  }

  def getContactById(id:String) = Action {
    val contact = salesforceService.getContactById(id)
    if (contact != None && contact.get.accountId != None) {
      Ok(Json.obj("contact" -> contact, "account" -> salesforceService.getAccountById(contact.get.accountId.get).get))
    } else {
      Ok(Json.obj("contact" -> contact))
    }
  }

}
