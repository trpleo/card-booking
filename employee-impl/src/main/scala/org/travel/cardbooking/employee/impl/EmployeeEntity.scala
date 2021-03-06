package org.travel.cardbooking.employee.impl

import com.lightbend.lagom.scaladsl.persistence.{ AggregateEvent, AggregateEventTag, PersistentEntity }
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType

sealed trait EmployeeCommand[R] extends ReplyType[R]

sealed trait EmployeeEvent extends AggregateEvent[EmployeeEvent] {
  def aggregateTag: AggregateEventTag[EmployeeEvent] = EmployeeEvent.Tag
}

object EmployeeEvent {
  val Tag = AggregateEventTag[EmployeeEvent]
}

case class EmployeeState(message: String, timestamp: String)

class EmployeeEntity extends PersistentEntity {

  override type Command = EmployeeCommand[_]
  override type Event = EmployeeEvent
  override type State = Option[EmployeeState]

  override def initialState: Option[EmployeeState] = None

  override def behavior: Behavior = {
    case None =>
      ???
    case Some(s0) =>
      ???
  }
}
