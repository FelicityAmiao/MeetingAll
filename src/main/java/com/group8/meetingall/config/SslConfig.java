package com.group8.meetingall.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod1"})
public class SslConfig {

  @Bean
  public ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
    return tomcat;
  }

  private Connector createHTTPConnector() {
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    //同时启用http（8080）、https（8077）两个端口
    connector.setScheme("http");
    connector.setSecure(false);
    connector.setPort(8080);
    connector.setRedirectPort(8077);
    return connector;
  }
}
