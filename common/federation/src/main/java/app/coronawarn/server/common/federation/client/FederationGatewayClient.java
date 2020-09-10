/*-
 * ---license-start
 * Corona-Warn-App
 * ---
 * Copyright (C) 2020 SAP SE and all other contributors
 * ---
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
 * ---license-end
 */

package app.coronawarn.server.common.federation.client;

import app.coronawarn.server.common.federation.client.download.BatchDownloadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Declarative web service client for the Federation Gateway API.
 *
 * <p>Any application that wants to uses it must make sure the required configuration
 * beans in this module are registered (scan root package of the module). There is also a constraint imposed on
 * application properties, such that values for the following structure must be declared:
 * <li> federation-gateway.base-url
 * <li> federation-gateway.ssl.key-store-path
 * <li> federation-gateway.ssl.key-store-pass
 * <li> federation-gateway.ssl.certificate-type
 */
@FeignClient(name = "federation-server", url = "${federation-gateway.base-url}", primary = false)
public interface FederationGatewayClient {

  @GetMapping(value = "/diagnosiskeys/download/{date}",
      headers = {"Accept=application/protobuf; version=1.0", "X-SSL-Client-SHA256=abcd", "X-SSL-Client-DN=C=PL"})
  BatchDownloadResponse getDiagnosisKeys(@PathVariable("date") String date);

  @GetMapping(value = "/diagnosiskeys/download/{date}",
      headers = {"Accept=application/protobuf; version=1.0", "X-SSL-Client-SHA256=abcd", "X-SSL-Client-DN=C=PL"})
  BatchDownloadResponse getDiagnosisKeys(@RequestHeader("batchTag") String batchTag,
      @PathVariable("date") String date);

  /**
   * HTTP POST request federation gateway endpoint /diagnosiskyes/upload.
   * @param raw Payload body. This property contains a raw byte array with the encoded protobuf DiagnosisKeyBatch.
   * @param accept HTTP Header Accept.
   * @param shaClient HTTP Header X-SSL-Client-SHA256.
   * @param dnClient HTTP Header X-SSL-Client-DN.
   * @param batchTag Unique batchTag to be identified by EFGS.
   * @param batchSignature Batch Signature as per PKCS#7 spec using Authorized Signing Certificate.
   */
  @PostMapping(value = "/diagnosiskeys/upload",
      consumes = "application/protobuf; version=1.0")
  String postBatchUpload(
      byte[] raw,
      @RequestHeader("Accept") String accept,
      @RequestHeader("X-SSL-Client-SHA256") String shaClient,
      @RequestHeader("X-SSL-Client-DN") String dnClient,
      @RequestHeader("batchTag") String batchTag,
      @RequestHeader("batchSignature") String batchSignature);
}
