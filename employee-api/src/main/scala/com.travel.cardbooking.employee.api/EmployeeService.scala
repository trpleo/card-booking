package com.travel.cardbooking.employee.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method.GET
import com.lightbend.lagom.scaladsl.api.{ Service, ServiceCall }

trait EmployeeService extends Service with ServiceMessages {

  def employeesList: ServiceCall[NotUsed, Employees]
  def getEmployee(id: String): ServiceCall[NotUsed, OptionEmployee]

  override final def descriptor = {
    import Service._

    named("employee-srv")
      .withCalls(
        restCall(GET, "/employee/all", employeesList),
        restCall(GET, "/employee/:id", getEmployee _))
  }
}