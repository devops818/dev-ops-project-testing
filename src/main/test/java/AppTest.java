import com.example.Application;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
  @Test
  public void testApp() {
    Application my App = new Application();
    String result = myApp.getStatus();
    assertEqual("OK", result);
  }
}