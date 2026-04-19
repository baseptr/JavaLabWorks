import config.ServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        new AnnotationConfigApplicationContext(ServerConfig.class);
        Thread.currentThread().join();
    }
}
