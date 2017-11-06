package pers.cy.iris.commons.cache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pers.cy.iris.commons.cache.impl.jedis.standalone.JedisProxy;

import static org.testng.Assert.assertEquals;

/**
 * @Author:cy
 * @Date:Created in  17/10/31
 * @Destription:
 */
public class RedisCacheServiceTest {

	protected CacheService jedis;

	@BeforeClass
	public void before()
	{
		ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:cache/spring-cache.xml");
		jedis = (CacheService)ac.getBean("redisCacheService");

		//测试单机redis打开下面一行
		jedis = (CacheService)new JedisProxy().bind(jedis);
	}

	@Test
	public void rpush() {
		long size = jedis.rpush("rpush", "bar", "rpush");
		Assert.assertEquals(2, size);
	}

	@Test
	public void srem() {
		jedis.sadd("foo", "a");
		jedis.sadd("foo", "b");

		long status = jedis.srem("foo", "bar");

		assertEquals(0, status);

		status = jedis.srem("foo", "a");
		assertEquals(1, status);

	}



	@Test
	public void scard() {
		jedis.sadd("foo", "a");
		jedis.sadd("foo", "b");

		long card = jedis.scard("foo");

		assertEquals(2, card);

		card = jedis.scard("bar");
		assertEquals(0, card);

	}
}
