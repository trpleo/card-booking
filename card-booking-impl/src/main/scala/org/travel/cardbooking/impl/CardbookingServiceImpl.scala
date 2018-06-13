package org.travel.cardbooking.impl

import org.travel.cardbooking.api
import org.travel.cardbooking.api.{ CardbookingService }
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{ EventStreamElement, PersistentEntityRegistry }

/**
 * Implementation of the CardbookingService.
 */
class CardbookingServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends CardbookingService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the card-booking entity for the given ID.
    val ref = persistentEntityRegistry.refFor[CardbookingEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the card-booking entity for the given ID.
    val ref = persistentEntityRegistry.refFor[CardbookingEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }

  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(CardbookingEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[CardbookingEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
