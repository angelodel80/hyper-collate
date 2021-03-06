package nl.knaw.huygens.hypercollate.api;

/*
 * #%L
 * hyper-collate-api
 * =======
 * Copyright (C) 2017 - 2019 Huygens ING (KNAW)
 * =======
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
 * #L%
 */

import javax.ws.rs.core.MediaType;

public class UTF8MediaType {
  private static final String CHARSET_UTF8 = "; charset=UTF-8";
  public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN + CHARSET_UTF8;
  public static final String TEXT_XML = MediaType.TEXT_XML + CHARSET_UTF8;
  public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + CHARSET_UTF8;

}
