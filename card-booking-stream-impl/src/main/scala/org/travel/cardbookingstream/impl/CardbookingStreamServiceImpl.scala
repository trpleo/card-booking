package org.travel.cardbookingstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.travel.cardbookingstream.api.CardbookingStreamService
import org.travel.cardbooking.api.CardbookingService

import scala.concurrent.Future

/**
  * Implementation of the CardbookingStreamService.
  */
class CardbookingStreamServiceImpl(cardbookingService: CardbookingService) extends CardbookingStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(cardbookingService.hello(_).invoke()))
  }
}
