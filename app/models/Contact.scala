package models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import play.api.libs.json._

/**
  * Created by in04468 on 27-06-2016.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class Contact (
  id: String,
  firstName: String,
  lastName: String,
  email: Option[String],
  token: Option[String],
  accountId: Option[String],
  activatedOn: Option[String],
  password: Option[String]
)

object Contact{

  import play.api.libs.functional.syntax._

  implicit val ContactFromJson: Reads[Contact] = (
    (__ \ "Id").read[String] ~
      (__ \ "FirstName").read[String] ~
      (__ \ "LastName").read[String] ~
      (__ \ "Email").readNullable[String] ~
      (__ \ "Token__c").readNullable[String] ~
      (__ \ "AccountId").readNullable[String] ~
      (__ \ "Activated_On__c").readNullable[String] ~
      (__ \ "Password__c").readNullable[String]
    )(Contact.apply _)

  implicit val ContactToJson: Writes[Contact] = (
    (__ \ "Id").write[String] ~
      (__ \ "FirstName").write[String] ~
      (__ \ "LastName").write[String] ~
      (__ \ "Email").writeNullable[String] ~
      (__ \ "Token__c").writeNullable[String] ~
      (__ \ "AccountId").writeNullable[String] ~
      (__ \ "Activated_On__c").writeNullable[String] ~
      (__ \ "Password__c").writeNullable[String]
    )((contact: Contact) => (
    contact.id,
    contact.firstName,
    contact.lastName,
    contact.email,
    contact.token,
    contact.accountId,
    contact.activatedOn,
    contact.password
    ))
}
