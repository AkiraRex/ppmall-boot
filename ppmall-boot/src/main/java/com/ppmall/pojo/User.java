package com.ppmall.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oauth2.domain.UserDetails;

public class User implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1410229794166263351L;

	private Integer id;

	private String username;

	private String password;

	private String email;

	private String phone;

	private String question;

	private String answer;

	private Integer role;

	private String wechatOpenid;

	private Date createTime;

	private Date updateTime;

	public User(Integer id, String username, String password, String email, String phone, String question,
			String answer, Integer role, String wechatOpenid, Date createTime, Date updateTime) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.question = question;
		this.answer = answer;
		this.role = role;
		this.wechatOpenid = wechatOpenid;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public User() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question == null ? null : question.trim();
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer == null ? null : answer.trim();
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getWechatOpenid() {
		return wechatOpenid;
	}

	public void setWechatOpenid(String wechatOpenid) {
		this.wechatOpenid = wechatOpenid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@JsonIgnore
	@Override
	public boolean isLock() {
		// TODO Auto-generated method stub
		return false;
	}

//	@JsonIgnore
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		List<GrantedAuthority> authorities = new ArrayList<>();
//		if (role == Const.Role.ROLE_ADMIN) 
//			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//		else
//			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//		return authorities;
//	}
//	
//	@JsonIgnore
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@JsonIgnore
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@JsonIgnore
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@JsonIgnore
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return true;
//	}
}