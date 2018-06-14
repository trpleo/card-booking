package org.travel.cardbooking.employee.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
//import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{ LagomApplication, LagomApplicationContext, LagomApplicationLoader }
import com.travel.cardbooking.employee.api.EmployeeService

class EmployeeLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication = new EmployeeApplication(context) {
    override def serviceLocator: ServiceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication = ???
  //    new EmployeeApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[EmployeeService])
}