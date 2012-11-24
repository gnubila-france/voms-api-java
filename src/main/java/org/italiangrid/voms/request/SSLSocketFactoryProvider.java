/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2012.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.italiangrid.voms.request;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.ac.impl.DefaultVOMSValidator;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.X509Credential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

/**
 * Provider for a SSL socket factory configured using CAnL. 
 * 
 * 
 * @author valerioventuri
 *
 */
public class SSLSocketFactoryProvider {

  private X509Credential credential;
  private X509CertChainValidatorExt validator;
  
  
  public SSLSocketFactoryProvider(X509Credential credential, X509CertChainValidatorExt validator){
	  this.credential = credential;
	  this.validator = validator;
	  
  }
  public SSLSocketFactoryProvider(X509Credential credential) {
    this(credential, CertificateValidatorBuilder.buildCertificateValidator(DefaultVOMSValidator.DEFAULT_TRUST_ANCHORS_DIR,
    		null, 60000L));
  }
  
  /**
   * Get the SSL socket factory.
   * 
   * @return the {@link SSLSocketFactory} object
   */
  public SSLSocketFactory getSSLSockectFactory() {

    SSLContext context = null;

    try {

      context = SSLContext.getInstance("SSLv3");

    } catch (NoSuchAlgorithmException e) {

      throw new VOMSError("No SSLv3 algorithm, cannot instanciate SSLContext", e);
    }

    KeyManager[] keyManagers = new KeyManager[] {credential.getKeyManager()}; 

    X509TrustManager trustManager = SocketFactoryCreator.getSSLTrustManager(validator);

    TrustManager[] trustManagers = new TrustManager[] {trustManager};

    SecureRandom secureRandom = null;

    try {

      secureRandom = SecureRandom.getInstance("SHA1PRNG");

    } catch (NoSuchAlgorithmException e) {

      throw new VOMSError("No algorithm SHA1PRNG, unable to initialize SSL context", e);
    }

    try {

      context.init(keyManagers, trustManagers, secureRandom);

    } catch (KeyManagementException e) {

      throw new VOMSError("No algorithm SHA1PRNG, unable to initialize SSL context", e);
    }

    return context.getSocketFactory();
  }

}
