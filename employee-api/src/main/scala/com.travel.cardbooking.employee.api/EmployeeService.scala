package com.travel.cardbooking.employee.api

import com.lightbend.lagom.scaladsl.api.Service

trait EmployeeService extends Service {

  override final def descriptor = {
    import Service._
    named("employee-srv")
  }
}