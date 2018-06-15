package org.travel.cardbooking.employee.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.travel.cardbooking.employee.api.{ Employee, EmployeeEnvilope, EmployeeService, EmployeesEnvilope }

class EmployeeServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends EmployeeService {

  override def employeesList: ServiceCall[NotUsed, EmployeesEnvilope] = ServiceCall { _ => ??? }

  override def getEmployee(id: String): ServiceCall[NotUsed, EmployeeEnvilope] = ServiceCall { _ =>
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

  override def upsertEmployee: ServiceCall[Employee, EmployeeEnvilope] = ServiceCall { employee =>
    ???
  }
}
