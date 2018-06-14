package org.travel.cardbooking.employee.impl

import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{ LagomApplication, LagomApplicationContext }
import com.softwaremill.macwire.wire
import com.travel.cardbooking.employee.api.EmployeeService
import play.api.libs.ws.ahc.AhcWSComponents

abstract class EmployeeApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
  with CassandraPersistenceComponents with LagomKafkaComponents with AhcWSComponents {

  override lazy val lagomServer = serverFor[EmployeeService](wire[EmployeeServiceImpl])

  override lazy val jsonSerializerRegistry = SerializerRegistry

  persistentEntityRegistry.register(wire[EmployeeEntity])
}