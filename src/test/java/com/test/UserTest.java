package com.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.validator.PublicClassValidator;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bawei.entity.User;
import com.qiyachao.util.DateUtil;
import com.qiyachao.util.RandomUtil;
import com.qiyachao.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-redis.xml")
public class UserTest {

	@Resource
	private RedisTemplate redisTemplate;

	//使用JDK系列化方式保存5万个user随机对象到Redis，并计算耗时
	@Test
	public void TestJDK() {
		//创建list对象
		List<User> list = new ArrayList<User>();
		//遍历五万次
		for (int i = 1; i <= 50000; i++) {
			//随机生成名字
			String name = StringUtil.randomChineseString(3);
			//随机生成性别
			String[] str = {"男","女"};
			String sex = str[RandomUtil.random(0, 1)];
			//随机生成手机号
			String phone = "13"+RandomUtil.randomNumber(9);
			//随机生成生日
			Calendar c = Calendar.getInstance();
			c.set(1954,0, 01);
			Calendar c1 = Calendar.getInstance();
			c1.set(2001, 11, 30);
			Date birthday = DateUtil.randomDate(c.getTime(), c1.getTime());
			//随机生成邮箱
			String[] e ={"@qq.com","@163.com","@sian.com","@gmail.com","@sohu.com","@hotmail.com","@foxmail.com"};
			String em = e[RandomUtil.random(0, 6)];
			String email = RandomUtil.randomEnglen(RandomUtil.random(3, 20))+em;
			//创建对象
			User user = new User(i, name, sex, phone, birthday, email);
			//存入lisi集合
			list.add(user);
		}
		ListOperations opsForList = redisTemplate.opsForList();
		//开始时间
		long start = System.currentTimeMillis();
		opsForList.leftPushAll("user_jdk", list);
		//结束时间
		long end = System.currentTimeMillis();
		System.out.println(start - end);
		System.out.println("输入完毕");
		//总耗时888
	}

	//使用JSON系列化方式保存5万个user随机对象到Redis，并计算耗时

	@Test
	public void TestJSON() {
		//创建list集合
		List<User> list = new ArrayList<User>();
		//循环五万次
		for (int i = 1; i <= 50000; i++) {
			//创建随机姓名
			String name = StringUtil.randomChineseString(3);
			//随机性别
			String[] str = {"男","女"};
			String sex = str[RandomUtil.random(0, 1)];
			//随机手机号
			String phone = "13"+RandomUtil.randomNumber(9);
			//随机出生日期
			Calendar c = Calendar.getInstance();
			c.set(1954,0, 01);
			Calendar c1 = Calendar.getInstance();
			c1.set(2001, 11, 30);
			Date birthday = DateUtil.randomDate(c.getTime(), c1.getTime());
			//随机邮箱
			String[] e ={"@qq.com","@163.com","@sian.com","@gmail.com","@sohu.com","@hotmail.com","@foxmail.com"};
			String em = e[RandomUtil.random(0, 6)];
			String email = RandomUtil.randomEnglen(RandomUtil.random(3, 20))+em;
			//创建对象
			User user = new User(i, name, sex, phone, birthday, email);
			//存入集合
			list.add(user);
		}
		ListOperations opsForList = redisTemplate.opsForList();
		//开始时间
		long start = System.currentTimeMillis();
		opsForList.leftPushAll("user_json", list);
		//结束时间
		long end = System.currentTimeMillis();
		System.out.println(start - end);
		System.out.println("输入完毕");
		//总耗时636
	}

	//使用Redis的Hash类型保存5万个user随机对象到Redis，并计算耗时（18分）
	@Test
	public void testHash() {
		//创建map集合
		Map<String, User> hashMap = new HashMap<String, User>();
		//遍历五万次
		for (int i = 1; i <= 50000; i++) {
			//生成随机姓名
			String name = StringUtil.randomChineseString(3);
			//生成随机性别
			String[] str = {"男","女"};
			String sex = str[RandomUtil.random(0, 1)];
			//生成随机手机号
			String phone = "13"+RandomUtil.randomNumber(9);
			//生成随机生日
			Calendar c = Calendar.getInstance();
			c.set(1954,0, 01);
			Calendar c1 = Calendar.getInstance();
			c1.set(2001, 11, 30);
			Date birthday = DateUtil.randomDate(c.getTime(), c1.getTime());
			//生成随机邮箱
			String[] e ={"@qq.com","@163.com","@sian.com","@gmail.com","@sohu.com","@hotmail.com","@foxmail.com"};
			String em = e[RandomUtil.random(0, 6)];
			String email = RandomUtil.randomEnglen(RandomUtil.random(3, 20))+em;
			//创建对象
			User user = new User(i, name, sex, phone, birthday, email);
			//存入map集合
			hashMap.put(i+"", user);
		}
		HashOperations opsForHash = redisTemplate.opsForHash();
		//开始时间
		long start = System.currentTimeMillis();
		opsForHash.putAll("user_hash", hashMap);
		//结束时间
		long end = System.currentTimeMillis();
		System.out.println(start-end);
		//1101
	}

}
