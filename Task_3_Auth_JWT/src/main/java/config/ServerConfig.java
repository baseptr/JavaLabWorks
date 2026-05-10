package config;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@PropertySource("classpath:app.properties")
public class ServerConfig {

    @Value("${server.port}")
    private int port;

    @Value("${server.host}")
    private String host;

    @Bean(initMethod = "start")
    public Tomcat tomcat() {
        AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext();
        webCtx.register(WebConfig.class, WebSecurityConfig.class);

        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setPort(port);
        tomcat.setHostname(host);

        Context tomcatCtx = tomcat.addContext("", System.getProperty("java.io.tmpdir"));
        webCtx.setServletContext(tomcatCtx.getServletContext());
        webCtx.refresh();

        Tomcat.addServlet(tomcatCtx, "dispatcher", new DispatcherServlet(webCtx));
        tomcatCtx.addServletMappingDecoded("/", "dispatcher");

        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("springSecurityFilterChain");
        filterDef.setFilter(new DelegatingFilterProxy("springSecurityFilterChain", webCtx));
        tomcatCtx.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("springSecurityFilterChain");
        filterMap.addURLPattern("/*");
        tomcatCtx.addFilterMap(filterMap);

        return tomcat;
    }
}
