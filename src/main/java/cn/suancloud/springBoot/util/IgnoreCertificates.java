package cn.suancloud.springBoot.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class IgnoreCertificates {

  /**
   * 该方法用于忽略https证书认证
   * @throws Exception
   */
  public static void ignoreCertificates() throws Exception {
    trustAllHttpsCertificates();
    HostnameVerifier hv = new HostnameVerifier() {
      public boolean verify(String urlHostName, SSLSession session) {
//        System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
        return true;
      }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
  }

  private static void trustAllHttpsCertificates() throws Exception {
    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
    javax.net.ssl.TrustManager tm = new miTM();
    trustAllCerts[0] = tm;
    javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
            .getInstance("SSL");
    sc.init(null, trustAllCerts, null);
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
            .getSocketFactory());
  }

  static class miTM implements javax.net.ssl.TrustManager,
          javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    public boolean isServerTrusted(
            java.security.cert.X509Certificate[] certs) {
      return true;
    }

    public boolean isClientTrusted(
            java.security.cert.X509Certificate[] certs) {
      return true;
    }

    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
      return;
    }

    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
      return;
    }
  }
}
