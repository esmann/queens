/*
 * $Header: /home/juliusd/cvs/commons-ssl/src/java/org/apache/commons/httpclient/contrib/ssl/EasySSLProtocolSocketFactory.java,v 1.3 2006/11/20 21:37:35 juliusd Exp $
 * $Revision: 1.3 $
 * $Date: 2006/11/20 21:37:35 $
 * 
 * ====================================================================
 *
 *  Copyright 2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;

import org.apache.commons.ssl.HttpSecureProtocol;
import org.apache.commons.ssl.KeyMaterial;
import org.apache.commons.ssl.TrustMaterial;

/**
 * <p/> EasySSLProtocolSocketFactory can be used to creats SSL {@link Socket}s
 * that accept self-signed certificates.
 * </p>
 * <p/> This socket factory SHOULD NOT be used for productive systems due to
 * security reasons, unless it is a concious decision and you are perfectly
 * aware of security implications of accepting self-signed certificates
 * </p>
 * <p/> <p/> Example of using custom protocol socket factory for a specific
 * host:
 * 
 * <pre>
 *     Protocol easyhttps = new Protocol(&quot;https&quot;, new EasySSLProtocolSocketFactory(), 443);
 * &lt;p/&gt;
 *     HttpClient client = new HttpClient();
 *     client.getHostConfiguration().setHost(&quot;localhost&quot;, 443, easyhttps);
 *     // use relative url only
 *     GetMethod httpget = new GetMethod(&quot;/&quot;);
 *     client.executeMethod(httpget);
 * </pre>
 * 
 * </p>
 * <p/> Example of using custom protocol socket factory per default instead of
 * the standard one:
 * 
 * <pre>
 *     Protocol easyhttps = new Protocol(&quot;https&quot;, new EasySSLProtocolSocketFactory(), 443);
 *     Protocol.registerProtocol(&quot;https&quot;, easyhttps);
 * &lt;p/&gt;
 *     HttpClient client = new HttpClient();
 *     GetMethod httpget = new GetMethod(&quot;https://localhost/&quot;);
 *     client.executeMethod(httpget);
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:oleg -at- ural.ru">Oleg Kalnichevski</a> <p/> <p/>
 *         DISCLAIMER: HttpClient developers DO NOT actively support this
 *         component. The component is provided as a reference material, which
 *         may be inappropriate for use without additional customization.
 *         </p>
 */

public class MiGSSLSocketFactory extends HttpSecureProtocol {
	private String cacertFile, keyFile, certFile, conf, password;

	/**
	 * Constructor for EasySSLProtocolSocketFactory.
	 * 
	 * @throws GeneralSecurityException
	 *             GeneralSecurityException
	 * @throws IOException
	 *             IOException
	 */

	enum Conf {
		certfile, keyfile, password, migserver, cacertfile
	}

	public MiGSSLSocketFactory() throws GeneralSecurityException, IOException {

		super();
		this.loadConf();

		super.setTrustMaterial(new TrustMaterial(cacertFile));
		super.setCheckHostname(false);
		super.setCheckExpiry(false);
		super.setCheckCRL(false);
		super.setKeyMaterial(new KeyMaterial(certFile, password.toCharArray()));
	}

	private void loadConf() throws IOException {
		String f = System.getProperty("user.home");
		BufferedReader in = new BufferedReader(new FileReader(f
				+ "/.mig/miguser.conf"));
		String line;

		while ((line = in.readLine()) != null) {
			if (line.equals(""))  
				continue;
			String[] settings = line.split(" ", 2);
			
			switch (Conf.valueOf(settings[0])) {
			case certfile: // We need the pkcs12 file!				
				certFile = settings[1].replace(".pem", ".p12");
				break;
			case keyfile: // Not used...
				keyFile = settings[1];
				break;
			case cacertfile:
				cacertFile = settings[1];
				break;
			case password:
				password = settings[1];
				break;
			default:
				System.out.println("Did not recognize setting: " + line);
			}
		}

	}
}
