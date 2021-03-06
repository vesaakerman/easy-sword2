/**
 * Copyright (C) 2015-2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.api.sword2

import java.io.File
import java.util.regex.Pattern

import scala.util.{Failure, Success}

class ResolveFetchItemsSpec extends Sword2Fixture with BagStoreFixture {

  val INPUT_BASEDIR               = new File("src/test/resources/input")
  val SIMPLE_SEQUENCE_A           = new File(INPUT_BASEDIR, "bag-sequence/a")
  val SIMPLE_SEQUENCE_B           = new File(INPUT_BASEDIR, "bag-sequence/b")
  val SIMPLE_SEQUENCE_C           = new File(INPUT_BASEDIR, "bag-sequence/c")
  val REQUIRED_FILE_MISSING       = new File(INPUT_BASEDIR, "bag-sequence/missing-required-file")
  val FETCH_ITEM_FILE_MISSING     = new File(INPUT_BASEDIR, "bag-sequence/file-missing-in-fetch-text")
  val INCORRECT_CHECKSUM          = new File(INPUT_BASEDIR, "bag-sequence/incorrect-checksum")
  val NONEXISTENT_FETCH_ITEM_PATH = new File(INPUT_BASEDIR, "bag-sequence/nonexistent-fetchtext-path")
  val FETCH_ITEM_ALREADY_IN_BAG   = new File(INPUT_BASEDIR, "bag-sequence/fetch-item-already-in-bag")
  val URL_OUTSIDE_BAGSTORE_BAG    = new File(INPUT_BASEDIR, "url-outside-bagstore-bag")
  val INVALID_URL_BAG             = new File(INPUT_BASEDIR, "invalid-url-bag")
  val NOT_ALLOWED_URL_BAG         = new File(INPUT_BASEDIR, "not-allowed-url-bag")
  val NO_DATA_BAG                 = new File(INPUT_BASEDIR, "empty-bag")
  val urlPattern = Pattern.compile("^https?://.*")

  "resolveFetchItems" should "result in a Success with a valid bag without a fetch.txt" in {
    copyToTargetBagDir(SIMPLE_SEQUENCE_A)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Success[_]]
  }

  it should "result in a Success with a valid bag with a fetch.txt"  in {
    copyToTargetBagDir(SIMPLE_SEQUENCE_B)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Success[_]]
  }

  it should "result in a Success with another valid bag with a fetch.txt"  in {
    copyToTargetBagDir(SIMPLE_SEQUENCE_C)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Success[_]]
  }

  it should "result in a Failure when a required file is missing"  in {
    copyToTargetBagDir(REQUIRED_FILE_MISSING)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Failure[_]]
  }

  it should "result in a Failure when a file is missing in the fetch.txt"  in {
    copyToTargetBagDir(FETCH_ITEM_FILE_MISSING)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Failure[_]]
  }

  it should "result in a Failure when a file checksum is incorrect"  in {
    copyToTargetBagDir(INCORRECT_CHECKSUM)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Failure[_]]
  }

  it should "result in a Failure when there is a nonexistent path in the fetch.txt"  in {
    copyToTargetBagDir(NONEXISTENT_FETCH_ITEM_PATH)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Failure[_]]
  }

  it should "result in a Failure when a file in the fetch.txt is already in the bag"  in {
    copyToTargetBagDir(FETCH_ITEM_ALREADY_IN_BAG)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Failure[_]]
  }

// TODO: PROPERLY MOCK OUT THE HTTP CALL
//  it should "result in a Success with a valid fetch.txt url referring outside the bagstore"  in {
//    copyToTargetBagDir(URL_OUTSIDE_BAGSTORE_BAG)
//    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
//    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Success[_]]
//  }

  it should "result in a Failure with a syntactically invalid url in the fetch.txt"  in {
    copyToTargetBagDir(INVALID_URL_BAG)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Failure[_]]
  }

  it should "result in a Failure with a not allowed url in the fetch.txt"  in {
    copyToTargetBagDir(NOT_ALLOWED_URL_BAG)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Failure[_]]
  }

  it should "result in a Failure with an empty bag"  in {
    copyToTargetBagDir(NO_DATA_BAG)
    DepositHandler.checkFetchItemUrls(targetBagDir, urlPattern) shouldBe a[Success[_]]
    DepositHandler.checkBagVirtualValidity(targetBagDir) shouldBe a[Failure[_]]
  }

  it should "result in a Failure when the bag-store base-dir doesn't exist"  in {
    copyToTargetBagDir(SIMPLE_SEQUENCE_A)
    implicit val baseDir = new File("non/existent/dir")
    DepositHandler.checkBagStoreBaseDir() shouldBe a[Failure[_]]
  }
}

