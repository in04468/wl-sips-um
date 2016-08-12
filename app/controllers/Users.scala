package controllers

import play.api.cache.CacheApi
import play.api.libs.json._
import play.api.mvc._
import models.{Attributes, Contact, User}
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

  /** Retrieves the user for the given id as JSON */
  def user(id: String) = HasToken(parse.empty) { token => userId => implicit request =>
    Ok(Json.toJson(findOneById(id)))
  }

  /** Creates a user from the given JSON */
  def createUser() = HasToken(parse.json) { token => userId => implicit request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    NotImplemented
  }

  /** Updates the user for the given id from the JSON body */
  def updateUser(id: String) = HasToken(parse.json) { token => userId => implicit request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    NotImplemented
  }

  /** Deletes a user for the given id */
  def deleteUser(id: String) = HasToken(parse.empty) { token => userId => implicit request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    NotImplemented
  }

  def retrieveUser(token: String) = Action {
    //Verify the token with salesforce and fet the user details
    val contact: Contact = salesforceService.getContact("Token__c", token)
    //log.info("Retrived user with contact.activatedOn: "+contact.activatedOn)
    if (contact != null && contact.activatedOn == None ) {
      Ok(Json.toJson(contact))
    } else {
      Ok("")
    }
  }

  def activateUser() = Action(parse.json) { request => {
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

  def findOneById(id: String): Option[User] = {
    val contact: Contact = salesforceService.getContact("Id", id)
    if (contact != null) {
      Some(User(contact.id, contact.email.getOrElse("dummy"), contact.password.getOrElse("dummy"), contact.firstName + " " + contact.lastName, None))
    } else {
      None
    }
    // TODO: find the corresponding user
    //

  }
}
