package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app.properties")
public class TomcatConfig {

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private int port;

    @Value("${server.webappDir}")
    private String webappDir;

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getWebappDir() { return webappDir; }
}
