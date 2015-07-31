/*
 * Copyright 2014 DataGenerator Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package SocialNetwork_Example.scala

import Helpers.StringHelper._
import NodeData.NodeDataStub
import SocialNetwork_Example.scala.UserType.UserType

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * A user stub is input used to create a user, and comprises of user type plus optional metadata
 * representing parameters of that user that we want to override with specified values.
 * Any non-specified parameters are expected to be populated with default or random values.
 */
class UserStub protected() extends NodeDataStub[UserType, User, UserTypes, UserStub]() {
  var dataType: UserType = UserType.PublicUser
  @BeanProperty var geographicalLocation: Option[(Double, Double)] = None
  @BeanProperty var isSecret: Option[Boolean] = None

  def this(userType: UserType.UserType, geographicalLocation: (Double, Double), isSecret: Boolean) = {
    this()
    this.dataType = userType
    this.geographicalLocation = Option(geographicalLocation)
    this.isSecret = Option(isSecret)
  }

  def this(userType: UserType.UserType) = {
    this()
    this.dataType = userType
  }

  override def displayableElements: ArrayBuffer[String] = {
    val elements = mutable.ArrayBuffer[String](displayableDataId)
    if (geographicalLocation.nonEmpty) {
      elements += s"Lat=${geographicalLocation.get._1}"
      elements += s"Long=${geographicalLocation.get._2}"
    }
    if (isSecret.nonEmpty && isSecret.get) elements += s"IsSecret=True"
    elements
  }

  override def simplifiedDisplayableElements: Iterable[String] = {
    Iterable[String](displayableDataId)
  }

  /**
   * Used for testing graph isomorphism.
   * @return String uniquely representing this graph's structure
   */
  override def getStructuralMD5: String = {
    new String(s"${dataType.name},${displayableElements.mkString(",")}".md5)
  }

  override def defaultDisplayableDataId: String = dataType.name
}

object UserStub {
  def getStubWithRandomParams(userType: UserType.UserType): UserStub = {
    new UserStub(userType, SocialNetworkUtilities.getRandomGeographicalLocation, SocialNetworkUtilities.getRandomIsSecret)
  }
}
