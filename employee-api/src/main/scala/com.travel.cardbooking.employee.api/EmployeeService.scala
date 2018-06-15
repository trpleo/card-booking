package com.travel.cardbooking.employee.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.deser.MessageSerializer
import com.lightbend.lagom.scaladsl.api.transport.Method.{ GET, POST }
import com.lightbend.lagom.scaladsl.api.{ Service, ServiceCall }
import com.travel.cardbooking.employee.api.v100.{ Employee, EmployeeEnvilope, EmployeesEnvilope }

trait EmployeeService extends Service {

  def employeesList: ServiceCall[NotUsed, EmployeesEnvilope]
  def getEmployee(id: String): ServiceCall[NotUsed, EmployeeEnvilope]
  def upsertEmployee: ServiceCall[Employee, EmployeeEnvilope]

  override final def descriptor = {
    import Service._

    implicit val s0 = new StrictMessageSerializerGeneric[Employee](Employee)
    implicit val s1 = new StrictMessageSerializerGeneric[EmployeeEnvilope](EmployeeEnvilope)
    implicit val s2 = new StrictMessageSerializerGeneric[EmployeesEnvilope](EmployeesEnvilope)

    val requestSerializer: MessageSerializer[Employee, _] = new StrictMessageSerializerGeneric[Employee](Employee)
    val x = requestSerializer.serializerForRequest.serialize(Employee())

    named("employee-srv")
      .withCalls(
        restCall(GET, "/employee/all", employeesList),
        restCall(GET, "/employee/:id", getEmployee _),
        restCall(POST, "/employee/", upsertEmployee))
  }
}