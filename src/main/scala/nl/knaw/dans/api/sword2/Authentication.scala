/*******************************************************************************
  * Copyright 2015 DANS - Data Archiving and Networked Services
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *   http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  ******************************************************************************/
package nl.knaw.dans.api.sword2

import org.swordapp.server.{AuthCredentials, SwordAuthException, SwordError}

object Authentication {
  @throws(classOf[SwordError])
  @throws(classOf[SwordAuthException])
  def checkAuthentication(auth: AuthCredentials) {
    if (auth.getOnBehalfOf != null && !(auth.getOnBehalfOf == "")) {
      throw new SwordError("http://purl.org/net/sword/error/MediationNotAllowed")
    }
    // temporary short-circuit
    if (!(auth.getUsername == SwordProps("user")) || !(auth.getPassword == SwordProps("password"))) {
      throw new SwordAuthException
    }
  }
}