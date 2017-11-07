package pers.cy.iris.commons.cache.impl.jedis.cluster;

import pers.cy.iris.commons.cache.CacheService;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Set;

/**
 * @Author:cy
 * @Date:Created in  17/10/27
 * @Destription: redis集群缓存的实现
 */
public class JedisClusterCacheService implements CacheService {

	private JedisCluster jedisCluster;

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	/**
	 * 追加到现有键对应列表中
	 *
	 * @param key      键
	 * @param value    值
	 * @param maxCount 列表最大长度，如超过最大长度，将移除最早的元素
	 */
	@Override
	public void rpush(String key, String value, int maxCount) {
		if (jedisCluster.llen(key) >= maxCount) {
			jedisCluster.lpop(key);
		}
		jedisCluster.rpush(key, value);
	}

	/**
	 * 追加到现有键对应列表中
	 *
	 * @param key      键
	 * @param value    值
	 * @param maxCount 列表最大长度，如超过最大长度，将移除最早的元素
	 */
	@Override
	public void rpush(byte[] key, byte[] value, int maxCount) {
		if (jedisCluster.llen(key) >= maxCount) {
			jedisCluster.lpop(key);
		}
		jedisCluster.rpush(key, value);
	}

	/**
	 * 取出对应键中相应范围内的值
	 *
	 * @param key  键
	 * @param from 开始索引
	 * @param to   结束索引
	 * @return 值的列表
	 */
	@Override
	public List<String> range(String key, int from, int to) {
		return jedisCluster.lrange(key, from, to);
	}

	/**
	 * 取出对应键中相应范围内的值
	 *
	 * @param key  键
	 * @param from 开始索引
	 * @param to   结束索引
	 * @return 值的列表
	 */
	@Override
	public List<byte[]> range(byte[] key, int from, int to) {
		return jedisCluster.lrange(key, from, to);
	}

	/**
	 * 增加键值对
	 *
	 * @param key   键
	 * @param value 值
	 */
	@Override
	public void put(String key, String value) {
		jedisCluster.set(key,value);
	}

	/**
	 * 获取对应键的值
	 *
	 * @param key 键
	 * @return 值
	 */
	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	/**
	 * 获取对应键的值
	 *
	 * @param key 键
	 * @return 值
	 */
	@Override
	public byte[] get(byte[] key) {
		return jedisCluster.get(key);
	}

	/**
	 * 原子性的将对应键的值增加1
	 *
	 * @param key 键
	 * @return 自增完成的值
	 */
	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	/**
	 * 原子性的将对应键的值增加指定值
	 *
	 * @param key   键
	 * @param count 值
	 * @return 自增完成的值
	 */
	@Override
	public Long incrBy(String key, long count) {
		return jedisCluster.incrBy(key,count);
	}

	/**
	 * 原子性的将对应键的值减去1
	 *
	 * @param key 键
	 * @return 自增完成的值
	 */
	@Override
	public Long decr(String key) {
		return jedisCluster.decr(key);
	}

	/**
	 * 原子性的将对应键的值减去指定值
	 *
	 * @param key   键
	 * @param count 值
	 * @return 自增完成的值
	 */
	@Override
	public Long decrBy(String key, long count) {
		return jedisCluster.decrBy(key,count);
	}

	/**
	 * 删除对应的键值对
	 *
	 * @param key 键
	 */
	@Override
	public void delete(String key) {
		jedisCluster.del(key);
	}

	/**
	 * 设置
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	@Override
	public void set(String key, String value) {
		jedisCluster.set(key,value);
	}

	/**
	 * 设置有效期键值
	 *
	 * @param key     键
	 * @param seconds 有效期
	 * @param value   值
	 * @return
	 */
	@Override
	public void setex(byte[] key, int seconds, byte[] value) {
		jedisCluster.setex(key,seconds,value);
	}

	/**
	 * 设置有效期键值
	 *
	 * @param key     键
	 * @param seconds 有效期
	 * @param value   值
	 * @return
	 */
	@Override
	public void setex(String key, int seconds, String value) {
		jedisCluster.setex(key, seconds, value);
	}

	/**
	 * 添加sorted set
	 *
	 * @param key    键
	 * @param score  分数
	 * @param member 值
	 */
	@Override
	public void zadd(String key, double score, String member) {
		jedisCluster.zadd(key, score, member);
	}

	/**
	 * 通过位置返回sorted set指定区间内的成员
	 *
	 * @param key   键
	 * @param start 其实位置
	 * @param end   结束位置
	 * @return 返回所有符合条件的成员
	 */
	@Override
	public Set<String> zrange(String key, long start, long end) {
		return jedisCluster.zrange(key, start, end);
	}

	/**
	 * 通过分数返回sorted set指定区间内的成员
	 *
	 * @param key 键
	 * @param min 最小评分
	 * @param max 最大评分
	 * @return 返回所有符合条件的成员
	 */
	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	/**
	 * 通过分数返回sorted set指定区间内的成员
	 *
	 * @param key    键
	 * @param min    最小评分
	 * @param max    最大评分
	 * @param offset 偏移位置
	 * @param count  返回数量
	 * @return 返回所有符合条件的成员
	 */
	@Override
	public Set<String> zrangeByScore(String key, double min, double max, long offset, long count) {
		return jedisCluster.zrangeByScore(key, min, max,(int)offset,(int)count);
	}

	/**
	 * 统计score在min和max之间的成员数
	 *
	 * @param key 键
	 * @param min 最小score
	 * @param max 最大score
	 * @return 符合条件的成员数
	 */
	@Override
	public Long zcount(String key, double min, double max) {
		return jedisCluster.zcount(key, min, max);
	}

	/**
	 * 统计成员数量
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Long zcard(String key) {
		return jedisCluster.zcard(key);
	}

	/**
	 * 移除sorted set中的一个或多个成员
	 *
	 * @param key    键
	 * @param member 值
	 * @return 被成功移除的成员的数量，不包括被忽略的成员
	 */
	@Override
	public Long zrem(String key, String... member) {
		return jedisCluster.zrem(key, member);
	}

	/**
	 * 获取指定成员的评分
	 *
	 * @param key    键
	 * @param member 值
	 * @return 评分，不存在则返回null
	 */
	@Override
	public Double zscore(String key, String member) {
		return jedisCluster.zscore(key, member);
	}

	/**
	 * 如果不存在则设置
	 *
	 * @param key
	 * @param value
	 * @return 是否成功
	 */
	@Override
	public Boolean setnx(String key, String value) {
		return Boolean.valueOf(jedisCluster.setnx(key, value).longValue()==1L);
	}

	/**
	 * 设置过期时间
	 *
	 * @param key     键
	 * @param seconds 毫秒
	 * @return 是否成功
	 */
	@Override
	public Boolean expire(String key, int seconds) {
		return Boolean.valueOf(jedisCluster.expire(key, seconds).longValue()==1L);
	}

	/**
	 * 获取key的剩余生存时间
	 *
	 * @param key 键
	 * @return 当 key 不存在时，返回 -2
	 * 当 key 存在但没有设置剩余生存时间时，返回 -1
	 * 否则，以秒为单位，返回 key 的剩余生存时间
	 */
	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	/**
	 * 移出并获取列表的第一个元素
	 *
	 * @param key 键
	 * @return 第一个元素
	 */
	@Override
	public String lpop(String key) {
		return jedisCluster.lpop(key);
	}

	/**
	 * 在列表中添加一个或多个值
	 *
	 * @param key   键
	 * @param value 值
	 * @return 最新列表长度
	 */
	@Override
	public Long rpush(String key, String... value) {
		return jedisCluster.rpush(key,value);
	}

	/**
	 * 获取列表长度
	 *
	 * @param key 键
	 * @return 长度
	 */
	@Override
	public Long llen(String key) {
		return jedisCluster.llen(key);
	}

	/**
	 * 从key对应list中删除count个和value相同的元素
	 *
	 * @param key
	 * @param count count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
	 *              count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
	 *              count = 0 : 移除表中所有与 value 相等的值。
	 * @param value
	 * @return 被移除的个数
	 */
	@Override
	public Long lrem(String key, long count, String value) {
		return jedisCluster.lrem(key, count, value);
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 *
	 * @param key
	 * @return
	 */
	@Override
	public String spop(String key) {
		return jedisCluster.spop(key);
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
	 *
	 * @param key
	 * @param values
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
	 */
	@Override
	public Long sadd(String key, String... values) {
		return jedisCluster.sadd(key, values);
	}

	/**
	 * 返回集合 key 的基数(集合中元素的数量)。
	 *
	 * @param key
	 * @return 当 key 不存在时，返回 0
	 */
	@Override
	public Long scard(String key) {
		return jedisCluster.scard(key);
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * 当 key 不是集合类型，返回一个错误。
	 *
	 * @param key
	 * @param values
	 * @return 被成功移除的元素的数量，不包括被忽略的元素。
	 */
	@Override
	public Long srem(String key, String... values) {
		return jedisCluster.srem(key, values);
	}

	/**
	 * 获取委托的缓存
	 *
	 * @return
	 */
	@Override
	public CacheService getDelegate() {
		return this;
	}
}
