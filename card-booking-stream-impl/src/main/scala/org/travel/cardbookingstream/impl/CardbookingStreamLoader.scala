package org.travel.cardbookingstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.travel.cardbookingstream.api.CardbookingStreamService
import org.travel.cardbooking.api.CardbookingService
import com.softwaremill.macwire._

class CardbookingStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CardbookingStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CardbookingStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CardbookingStreamService])
}

abstract class CardbookingStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[CardbookingStreamService](wire[CardbookingStreamServiceImpl])

  // Bind the CardbookingService client
  lazy val cardbookingService = serviceClient.implement[CardbookingService]
}
