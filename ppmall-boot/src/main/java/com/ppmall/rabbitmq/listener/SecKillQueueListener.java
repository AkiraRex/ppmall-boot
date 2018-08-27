package com.ppmall.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;



public class SecKillQueueListener implements MessageListener {

	
//	@Autowired
//	private ICartService iCartService;
//	
//	@Autowired
//	private IOrderService iOrderService;
	
	public SecKillQueueListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		

	}

}
