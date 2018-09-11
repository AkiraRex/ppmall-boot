package com.oauth2.domain;

import java.io.Serializable;
import java.util.Date;

public class AbstractDomain implements Serializable {

	private static final long serialVersionUID = 7787898374385773471L;

	/**
	 * 创建时间, 默认为当前时间值
	 */
	protected Date createTime = new Date();

	public AbstractDomain() {
	}

	public Date createTime() {
		return createTime;
	}

	/**
	 * 设置Domain的创建时间并返回实体本身. Builder设计模式的变种
	 *
	 * @param createTime
	 *            createTime
	 * @param <T>
	 *            AbstractDomain subclass
	 * @return Current domain
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractDomain> T createTime(Date createTime) {
		this.createTime = createTime;
		return (T) this;
	}

}
