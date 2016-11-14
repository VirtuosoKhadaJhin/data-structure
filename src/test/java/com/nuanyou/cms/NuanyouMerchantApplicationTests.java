package com.nuanyou.cms;

import com.nuanyou.cms.dao.TestChildDao;
import com.nuanyou.cms.dao.TestDao;
import com.nuanyou.cms.entity.TestChild;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NuanyouMerchantApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Autowired
	private TestDao testDao;
	@Autowired
	private TestChildDao testChildDao;
	@RequestMapping("test")
	@ResponseBody
	public String test() {

		List<com.nuanyou.cms.entity.Test> list=this.testDao.findAll();
		// Test test=new Test();
		//test.setId(2l);

		com.nuanyou.cms.entity.Test test=this.testDao.findOne(1L);
		TestChild testChild=this.testChildDao.findOne(1L);
		//testChild.setName("ee");
		//testChild.setTest(test);
		// test.setTestChild(testChild);

		//  testDao.save(test);
		//testChildDao.save(testChild);
		return null;
	}


}
