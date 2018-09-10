package com.ppmall.service.impl;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppmall.service.IClientService;
import com.ppmall.vo.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("iClientService")
public class ClientServiceImpl implements IClientService {

	@Override
	public Client createClient(Client client) {

		client.setClientId(UUID.randomUUID().toString());
		client.setClientSecret(UUID.randomUUID().toString());
		return client;
	}

	@Override
	public Client updateClient(Client client) {
		return client;
	}

	@Override
	public void deleteClient(Long clientId) {

	}

	@Override
	public Client findOne(Long clientId) {
		Client client = new Client();
		return client;
	}

	@Override
	public List<Client> findAll() {
		return new ArrayList<>();
	}

	@Override
	public Client findByClientId(String clientId) {
		Client client = new Client();
		return client;
	}

	@Override
	public Client findByClientSecret(String clientSecret) {
		Client client = new Client();
		return client;
	}
}
