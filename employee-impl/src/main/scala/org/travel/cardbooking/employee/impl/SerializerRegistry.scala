package org.travel.cardbooking.employee.impl

import com.lightbend.lagom.scaladsl.playjson.{ JsonSerializer, JsonSerializerRegistry }

import scala.collection.immutable
import com.travel.cardbooking.employee.api.ServiceMessages

object SerializerRegistry extends JsonSerializerRegistry with ServiceMessages {

  override def serializers: immutable.Seq[JsonSerializer[_]] = List()
}
