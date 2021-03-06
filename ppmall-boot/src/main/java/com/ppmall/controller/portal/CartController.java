package com.ppmall.controller.portal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.config.shiro.annotation.RequiredLogin;
import com.ppmall.pojo.User;
import com.ppmall.service.ICartService;
import org.apache.shiro.authz.annotation.RequiresRoles;
@Controller
@RequestMapping("/cart")
@RequiredLogin
public class CartController {

	@Autowired
	private ICartService iCartService;

	
	@RequiresRoles("ROLE_USERS")
	@RequestMapping(value = "/getCartProductCount", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<Integer> getCartCount(HttpSession session, HttpServletRequest request) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		System.out.println("/getCartProductCount" + request.getUserPrincipal());
		int userId = currentUser.getId();
		return iCartService.getCartCount(userId);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<List> getCartList(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		int userId = currentUser.getId();
		return iCartService.getCartList(userId);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse add(int productId, int count, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.addToCart(productId, count, user.getId());
	}

	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse delete(String productIds, HttpSession session) {
		String productId[] = productIds.split(",");
		int productIdsI[] = new int[productId.length];
		for (int i = 0; i < productId.length; i++) {
			productIdsI[i] = Integer.valueOf(productId[i]);
		}
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.deleteCart(productIdsI, user.getId());
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse update(int productId, int count, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(productId, user.getId(), count, 1);
	}

	@RequestMapping(value = "/select", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse select(int productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(productId, user.getId(), null, 1);
	}

	@RequestMapping(value = "/unSelect", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse unSelect(int productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(productId, user.getId(), null, 0);
	}
	
	@RequestMapping(value = "/selectAll", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse selectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(null, user.getId(), null, 1);
	}
	
	@RequestMapping(value = "/unSelectAll", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse unSelectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(null, user.getId(), null, 0);
	}
}
