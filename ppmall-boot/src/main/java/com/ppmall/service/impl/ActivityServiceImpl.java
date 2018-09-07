package com.ppmall.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.component.activity.ActivityThread;
import com.ppmall.component.activity.ActivityThreadService;
import com.ppmall.dao.ActivityMapper;
import com.ppmall.pojo.Activity;
import com.ppmall.pojo.Product;
import com.ppmall.rabbitmq.message.ActivityMessage;
import com.ppmall.rabbitmq.producer.ISecKillMessageProducer;
import com.ppmall.service.IActivityService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.RedisUtil;

@Service
public class ActivityServiceImpl implements IActivityService, InitializingBean {
	private static ConcurrentLinkedQueue<Product> queue = new ConcurrentLinkedQueue<>();// 队列

//	@Autowired
//	ActivityThreadService tpm;
//
//	@Autowired
//	private ActivityMapper activityMapper;
//
//	@Autowired
//	private ActivityThread dealSeckillThread;
//
//	@Autowired
//	private ISecKillMessageProducer iSecKillMessageProducer;
//
//	@Autowired
//	RedisUtil redisUtil;

	@Override
	public synchronized ServerResponse createOrder(ActivityMessage message) {

		return ServerResponse.createSuccessMessage("抢购失败");
	}

	@Override
	public ServerResponse listAllKillActivities(Date beginTime, Date endTime) {
		// TODO Auto-generated method stub
		return ServerResponse.createSuccess("获取成功");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		// new Thread(new DealSeckillThread(queue)).start();

	}

}
