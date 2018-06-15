package com.travel.cardbooking.employee.api

import akka.util.ByteString
import com.lightbend.lagom.scaladsl.api.deser.MessageSerializer.{ NegotiatedDeserializer, NegotiatedSerializer }
import com.lightbend.lagom.scaladsl.api.deser.{ MessageSerializer, StrictMessageSerializer }
import com.lightbend.lagom.scaladsl.api.transport.MessageProtocol
import scalapb.{ GeneratedMessage, GeneratedMessageCompanion, Message }
import scalapb.json4s.JsonFormat

import scala.collection.immutable

class StrictMessageSerializerGeneric[T <: GeneratedMessage with Message[T]](value: GeneratedMessageCompanion[T])
  extends StrictMessageSerializer[T] with GenericSerializers[T] {

  override def serializerForRequest: NegotiatedSerializer[T, ByteString] =
    serializerJson

  override def deserializer(protocol: MessageProtocol): MessageSerializer.NegotiatedDeserializer[T, ByteString] =
    deserializer(value)

  override def serializerForResponse(acceptedMessageProtocols: immutable.Seq[MessageProtocol]): NegotiatedSerializer[T, ByteString] =
    negotiateResponse(acceptedMessageProtocols)
}

trait GenericSerializers[T <: GeneratedMessage with Message[T]] {

  def serializerJson: NegotiatedSerializer[T, ByteString] = {
    new NegotiatedSerializer[T, ByteString] {
      override def protocol: MessageProtocol = MessageProtocol(Some("application/json"))
      override def serialize(message: T): ByteString = {
        implicit val a = message.companion

        ByteString(JsonFormat.toJsonString(message))
      }
    }
  }

  def serializerProtobuf: NegotiatedSerializer[T, ByteString] = {
    new NegotiatedSerializer[T, ByteString] {
      override def protocol: MessageProtocol = MessageProtocol(Some("application/x-protobuf"))

      override def serialize(message: T): ByteString = {
        ByteString(message.toByteArray)
      }
    }
  }

  def deserializer(implicit T: GeneratedMessageCompanion[T]): NegotiatedDeserializer[T, ByteString] = {
    new NegotiatedDeserializer[T, ByteString] {
      override def deserialize(wire: ByteString): T = {
        JsonFormat.fromJsonString(wire.utf8String)
      }
    }
  }

  def negotiateResponse(acceptedMessageProtocols: immutable.Seq[MessageProtocol]): NegotiatedSerializer[T, ByteString] = {
    acceptedMessageProtocols match {
      case Nil => serializerJson
      case protocols =>
        protocols.collectFirst {
          case MessageProtocol(Some("application/x-protobuf"), _, _) => serializerProtobuf
          case _ => serializerJson
        }.get
    }
  }
}
