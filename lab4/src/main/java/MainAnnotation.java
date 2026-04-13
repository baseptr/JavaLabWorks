import config.TomcatConfig;
import config.WebConfig;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class MainAnnotation {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext configCtx = new AnnotationConfigApplicationContext(TomcatConfig.class);
        TomcatConfig config = configCtx.getBean(TomcatConfig.class);

        Tomcat tomcat = new Tomcat();

        Connector connector = new Connector();
        connector.setPort(config.getPort());
        tomcat.setConnector(connector);

        String baseDir = System.getProperty("java.io.tmpdir");
        tomcat.setBaseDir(baseDir);

        Context context = tomcat.addContext("", new File(config.getWebappDir()).getAbsolutePath());

        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(WebConfig.class);
        springContext.setServletContext(context.getServletContext());
        springContext.refresh();

        Tomcat.addServlet(context, "dispatcher", new DispatcherServlet(springContext));
        context.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();
        System.out.println("Tomcat started on " + config.getHost() + ":" + config.getPort());

        configCtx.close();
        Thread.currentThread().join();

        tomcat.stop();
        springContext.close();
    }
}
