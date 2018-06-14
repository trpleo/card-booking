package org.travel.cardbooking.employee.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.travel.cardbooking.employee.api.EmployeeService

class EmployeeServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends EmployeeService {

  override def employeesList: ServiceCall[NotUsed, Employees] = ServiceCall { _ => ??? }

  override def getEmployee(id: String): ServiceCall[NotUsed, OptionEmployee] = ServiceCall { _ =>
    //      Try(persistentEntityRegistry
    //        .refFor[EmployeeEntity](id)
    //        .withAskTimeout(FiniteDuration(1000l, TimeUnit.MILLISECONDS))
    //        .ask(id)) match {
    //        case Success(f) => f
    //        case Failure(t) => Future.successful(t.getMessage)
    //      }
    //    persistentEntityRegistry
    //      .refFor[EmployeeEntity](id)
    //      .withAskTimeout(FiniteDuration(1000l, TimeUnit.MILLISECONDS))
    //      .ask(id)
    ???
  }

  override def upsertEmployee: ServiceCall[Employee, Employee] = ServiceCall { employee =>
    ???
  }
}
