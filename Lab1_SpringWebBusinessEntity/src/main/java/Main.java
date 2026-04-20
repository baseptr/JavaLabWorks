import config.ServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        new AnnotationConfigApplicationContext(ServerConfig.class);
        System.out.println("""
                App started at http://localhost:8080
                Docs json http://localhost:8080/v3/api-docs
                Swagger http://localhost:8080/swagger-ui/index.html
                """);
        Thread.currentThread().join();
    }
}
