import mx.parrot.commonapis.CommonApisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonApisApplication.class)
public class CommonApisApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(CommonApisApplicationTest.class, args);
	}

	@Test
	public void contextLoads() {
	}

}
