package com.example.walletrestservice;

import com.example.walletrestservice.model.RequestDTO;
import com.example.walletrestservice.model.Wallet;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletRestServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoadTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Test
	public void loadTesting(){
		RequestDTO request = new RequestDTO(3,"DEPOSIT",100);

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Callable<ResponseEntity<String>>> taskList = new LinkedList<>();

		for (int i = 0; i < 5000; i++) {
			taskList.add(() -> restTemplate.postForEntity("http://localhost:" + port + "/api/v1/wallet", request, String.class));
		}

		try {
			executorService.invokeAll(taskList);
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.MINUTES);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Thread termination faced timeout");
		}

		ResponseEntity<Wallet> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/wallets/3", Wallet.class);

		assertNotNull(responseEntity.getBody());
		assertEquals(500300,responseEntity.getBody().getBalance());
	}

}
