package com.example.walletrestservice;

import com.example.walletrestservice.model.RequestDTO;
import com.example.walletrestservice.model.Wallet;
import com.example.walletrestservice.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletRestServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletRestServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private WalletRepository walletRepository;

	@LocalServerPort
	private int port;

	@Test
	public void testGetBalance() {
		ResponseEntity<Long> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/wallets/1", Long.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(100, responseEntity.getBody());
	}

	@Test
	public void testErrorGetBalance() {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/wallets/4", String.class);
		System.out.println(responseEntity);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}

	@Test
	public void testWithdraw() {
		RequestDTO request = new RequestDTO(2,"WITHDRAW",50);
		ResponseEntity<Wallet> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/wallet", request, Wallet.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(150, responseEntity.getBody().getBalance());
	}

	@Test
	public void testErrorWithdrawNotEnoughFunds() {
		RequestDTO request = new RequestDTO(2,"WITHDRAW",500);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/wallet", request, String.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}

	@Test
	public void testErrorWithdrawInvalidMethod() {
		RequestDTO request = new RequestDTO(2,"WITHDRAW123",500);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/wallet", request, String.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}

	@Test
	public void testErrorWithdrawNotFound() {
		RequestDTO request = new RequestDTO(5,"WITHDRAW",200);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/wallet", request, String.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}

}
