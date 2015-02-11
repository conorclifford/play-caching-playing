package controllers

import java.util.UUID

import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

import scala.util.Random

case class UserForm(userGuid: UUID)
case class DataValue(key: Int, value: Double)
case class DummyData(userGuid: UUID, values: Seq[DataValue])

object Application extends Controller {
  implicit val dvReads = Json.reads[DataValue]
  implicit val dvWrites = Json.writes[DataValue]
  implicit val ddReads = Json.reads[DummyData]
  implicit val ddWrites = Json.writes[DummyData]
  implicit val ufReads = Json.reads[UserForm]
  implicit val ufWrites = Json.writes[UserForm]

  def getById(id: UUID) = Action {
    Ok(Json.toJson(getExistingData(id).getOrElse(newData(id))))
  }

  def post() = Action(parse.json) { request =>
    request.body.validate[UserForm] fold(
      errors =>
        BadRequest,
      form => {
        Logger.info(s"Accepting $form")
        Ok(Json.toJson(newData(form.userGuid)))
      })
  }

  private def cacheScoreKey(user: UUID) = s"dummy-data:$user"

  private def getExistingData(user: UUID): Option[DummyData] = {
    Logger.info(s"Looking up $user in dummy data cache")

    //
    // Depending on the cache plugin being used, this can do interesting things on failures of the cache service.

    //
    // IF "play2-memcached" plugin, this times out, with a logged exception, but returns None - the default timeout is 1 second!!!! (configurable???)
    //
    // IF "play-plugin-redis" this simply propogates exception to client here... seems immediate though... wrap with a Try(...).toOption.flatten to hide.
    //

    Cache.getAs[DummyData](cacheScoreKey(user))
  }

  private def newData(user: UUID): DummyData = {
    Logger.info(s"Generating random data for $user")
    val values = 1.to(10).map(i => DataValue(i, Random.nextDouble))
    val data = DummyData(user, values)
    Cache.set(cacheScoreKey(user), data)
    data
  }
}

