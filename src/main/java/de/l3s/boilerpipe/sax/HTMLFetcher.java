/**
 * Copyright (C) 2013 Christian Kohlschütter (ckkohl79@gmail.com)
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
package de.l3s.boilerpipe.sax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.ContentType;

/**
 * A very simple HTTP/HTML fetcher, really just for demo purposes.
 * 
 * @author Christian Kohlschütter
 */
public class HTMLFetcher {
	private HTMLFetcher() {
	}

	private static final Pattern PAT_CHARSET = Pattern
			.compile("charset=([^; ]+)$");
    private static final Pattern PAT_CHARSET_REX = Pattern
            .compile("charset=\"?([^; ]+)\"");

	/**
	 * Fetches the document at the given URL, using {@link URLConnection}.
	 * 
	 * @param url
	 * @return the document at the given URL
	 * @throws IOException
	 */
	public static HTMLDocument fetch(final URL url) throws IOException {
        return fetch(url.toString());

        //return fetchHelper(url);

	}


    public static HTMLDocument fetch(final String url) throws IOException {
        //DefaultHttpClient httpclient = new DefaultHttpClient();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url.toString());

        HttpResponse response = httpclient.execute(request);
        HttpEntity  entity= response.getEntity();
        entity.getContentType();
        //System.out.println("Response Code: " +
                //response.getStatusLine().getStatusCode());
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = Charset.forName("gb2312");
        }
        System.out.println(charset);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(entity.getContent(), charset));


        StringBuilder builder = new StringBuilder();
        String aux = "";
        Charset cs = Charset.forName("gb2312");
        boolean charsetFlag = false;
        while ((aux = rd.readLine()) != null) {
            if (aux != null && !charsetFlag && aux.contains("http-equiv")) {
                Matcher m = PAT_CHARSET_REX.matcher(aux);
                if (m.find()) {
                    final String cName = m.group(1);
                    charsetFlag = true;
                    try {
                        cs = Charset.forName(cName);
                        break;
                    } catch (UnsupportedCharsetException e) {
                        // keep default
                    }
                }
            }
            //builder.append(aux);
            //System.out.println(builder.toString());
        }

        HttpGet request2 = new HttpGet(url.toString());

        HttpResponse response2 = httpclient.execute(request2);
        HttpEntity  entity2= response2.getEntity();
        contentType = ContentType.getOrDefault(entity2);
        charset = contentType.getCharset();
        if(charset == null)
            charset = cs;
        BufferedReader rd2 = new BufferedReader(
                new InputStreamReader(entity2.getContent(), charset));
        while ((aux = rd2.readLine()) != null) {
            builder.append(aux);
            //System.out.println(builder.toString());
        }

        String text = builder.toString();
        System.out.println(text);
        rd.close();
        rd2.close();
        return new HTMLDocument(text, cs);
    }

    public static HTMLDocument fetchHelper(final URL url) throws IOException {
        final URLConnection conn = url.openConnection();
        //conn.setRequestProperty("User-Agent",
        //"Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.104 Safari/537.36");
        //conn.setRequestProperty("Cookie","wapparam=web2wap; vt=4");
        final String ct = conn.getContentType();

        if (ct == null
                || !(ct.equals("text/html") || ct.startsWith("text/html;"))) {
            //throw new IOException("Unsupported content type: "+ct+ url);
            System.err.println("WARN: unsupported Content-type: "
                    + ct + url);
        }

        Charset cs = Charset.forName("UTF8");
        if (ct != null) {
            Matcher m = PAT_CHARSET_REX.matcher(ct);
            if (m.find()) {
                final String charset = m.group(1);
                try {
                    cs = Charset.forName(charset);
                } catch (UnsupportedCharsetException e) {
                    // keep default
                }
            }
        }

        InputStream in = conn.getInputStream();

        final String encoding = conn.getContentEncoding();
        if (encoding != null) {
            if ("gzip".equalsIgnoreCase(encoding)) {
                in = new GZIPInputStream(in);
            } else {
                System.err.println("WARN: unsupported Content-Encoding: "
                        + encoding);
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int r;
        while ((r = in.read(buf)) != -1) {
            bos.write(buf, 0, r);
        }
        in.close();

        final byte[] data = bos.toByteArray();

        return new HTMLDocument(data, cs);
    }


}
