package pers.cy.iris.commons.cache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
	}

	@Test
	public void rpush() {
		String key = "rpush";

		long size = jedis.llen(key);
		for(int i=0;i<size;i++){
			jedis.lpop(key);
		}

		size = jedis.rpush(key, "bar", "rpush");
		Assert.assertEquals(2, size);
		for(int i=0;i<size;i++){
			jedis.lpop(key);
		}
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
