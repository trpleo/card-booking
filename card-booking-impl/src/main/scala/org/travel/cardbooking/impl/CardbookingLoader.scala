package org.travel.cardbooking.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.travel.cardbooking.api.CardbookingService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class CardbookingLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CardbookingApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CardbookingApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CardbookingService])
}

abstract class CardbookingApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
  with CassandraPersistenceComponents
  with LagomKafkaComponents
  with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[CardbookingService](wire[CardbookingServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = CardbookingSerializerRegistry

  // Register the card-booking persistent entity
  persistentEntityRegistry.register(wire[CardbookingEntity])
}
