import com.nuanyou.cms.NuanyouMerchantApplication;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;

/**
 * Created by Byron on 2017/7/12.
 */

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = NuanyouMerchantApplication.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
public class TestCase {

    @Autowired
    private JavaMailSender javaMailSender;

    private static  VelocityEngine velocityEngine;

    static {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.INPUT_ENCODING, "utf-8");
        velocityEngine.setProperty(VelocityEngine.OUTPUT_ENCODING, "utf-8");
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    @Test
    public void sendSimpleMail() throws MessagingException {
        Template template = velocityEngine.getTemplate("templates/showEmail.vm");
        VelocityContext htmlContent = new VelocityContext();
        htmlContent.put("content", "您预订的泰国正宗古法抓龙筋，泰国正宗按摩（90分钟）");
        htmlContent.put("codeImg", "http://dev.img.91nuanyou.com/bg-code/th.png");
        htmlContent.put("orderId", "5106528460506996");
        htmlContent.put("goodCount", "2");
        StringWriter sw = new StringWriter();
        template.merge(htmlContent, sw);
        String text = sw.toString();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("斌给你发个邮件，我测试看看。");//标题
        helper.setFrom("zarabin@126.com");
        helper.setTo("zong.liu@91nuanyou.com");
        helper.setValidateAddresses(true);
        helper.setText(text, true);//正文
        javaMailSender.send(message);
        System.out.println("发送邮件成功");
    }
}
