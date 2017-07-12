import com.nuanyou.cms.NuanyouMerchantApplication;
import com.nuanyou.cms.service.BdUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Byron on 2017/7/12.
 */

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = NuanyouMerchantApplication.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
public class TestCase {
    @Autowired
    private BdUserService bdUserService;

    @Test
    public void likeName() {
        bdUserService.findAllCountry();
    }
}
