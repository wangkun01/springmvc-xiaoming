import com.xiaoming.spring.framework.context.XMApplicationContext;
import org.junit.Test;

/**
 * @program: springmvcProjects
 * @description:
 * @author: xiaoming
 * @create: 2020-01-09 23:49
 */
public class XMIOCDemo {


    @Test
    public void testIOC(){
        XMApplicationContext context = new XMApplicationContext("classpath:/application.properties");

    }

}