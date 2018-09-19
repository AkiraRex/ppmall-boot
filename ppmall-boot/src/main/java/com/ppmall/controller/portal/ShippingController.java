package com.ppmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.config.shiro.annotation.RequiredLogin;
import com.ppmall.pojo.Shipping;
import com.ppmall.pojo.User;
import com.ppmall.service.IShippingService;

@Controller
@RequestMapping("/shipping")
@RequiredLogin
public class ShippingController {

	@Autowired
	private IShippingService iShippingService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getShippingList(HttpSession session,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.getShippingList(user.getId(), pageNum, pageSize);
	}
	
	@RequestMapping(value = "/getDefault", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getDefaultShipping(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.getDefaultShipping(user.getId());
	}
	
	@RequestMapping(value = "/setDefault", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse setDefaultShipping(HttpSession session, int shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.setDefaultShipping(user.getId(),shippingId);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse addShipping(HttpSession session, Shipping shipping) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.addShipping(user.getId(), shipping);
	}
	
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse selectShipping(HttpSession session, Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.selectShipping(shippingId);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse saveShipping(HttpSession session, Shipping shipping) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.saveShipping(shipping,user.getId());
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse deleteShipping(HttpSession session, Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.deleteShipping(shippingId);
	}

}
