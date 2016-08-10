package models

import play.api.libs.json._

/**
  * Created by in04468 on 27-06-2016.
  */
case class Contact (
  id: String,
  firstName: String,
  lastName: String,
  email: Option[String],
  token: String,
  activatedOn: Option[String],
  password: Option[String],
  attributes: Attributes
)

case class Attributes (
  attributeType: String,
  url: String
)

object Contact{

  import play.api.libs.functional.syntax._

  implicit val ContactFromJson: Reads[Contact] = (
    (__ \ "Id").read[String] ~
      (__ \ "FirstName").read[String] ~
      (__ \ "LastName").read[String] ~
      (__ \ "Email").readNullable[String] ~
      (__ \ "Token__c").read[String] ~
      (__ \ "Activated_On__c").readNullable[String] ~
      (__ \ "Password__c").readNullable[String] ~
      (__ \ "attributes").read[Attributes]
    )(Contact.apply _)

  implicit val ContactToJson: Writes[Contact] = (
    (__ \ "Id").write[String] ~
      (__ \ "FirstName").write[String] ~
      (__ \ "LastName").write[String] ~
      (__ \ "Email").writeNullable[String] ~
      (__ \ "Token__c").write[String] ~
      (__ \ "Activated_On__c").writeNullable[String] ~
      (__ \ "Password__c").writeNullable[String] ~
      (__ \ "attributes").write[Attributes]
    )((contact: Contact) => (
    contact.id,
    contact.firstName,
    contact.lastName,
    contact.email,
    contact.token,
    contact.activatedOn,
    contact.password,
    contact.attributes
    ))
}

object Attributes {

  import play.api.libs.functional.syntax._

  implicit val AttributesFromJson: Reads[Attributes] = (
    (__ \ "type").read[String] ~
      (__ \ "url").read[String]
    )(Attributes.apply _)

  implicit val AttributesToJson: Writes[Attributes] = (
    (__ \ "type").write[String] ~
      (__ \ "url").write[String]
    )((attributes: Attributes) => (
    attributes.attributeType,
    attributes.url
    ))
}