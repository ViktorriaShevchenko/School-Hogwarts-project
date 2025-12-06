package ru.hogwarts.school.restTemplateTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SteamControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:";

    @Test
    void getSumParallelIntegrationTest() {
        ResponseEntity<Long> response = restTemplate.getForEntity(
                BASE_URL + port + "/stream/sum",
                Long.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500000500000L, response.getBody());
    }

    @Test
    void getSumFormulaIntegrationTest() {
        ResponseEntity<Long> response = restTemplate.getForEntity(
                BASE_URL + port + "/stream/sum-formula",
                Long.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500000500000L, response.getBody());
    }

    @Test
    void bothSumMethodsReturnSameResultTest() {
        ResponseEntity<Long> parallelResponse = restTemplate.getForEntity(
                BASE_URL + port + "/stream/sum",
                Long.class
        );

        ResponseEntity<Long> formulaResponse = restTemplate.getForEntity(
                BASE_URL + port + "/stream/sum-formula",
                Long.class
        );

        assertEquals(parallelResponse.getBody(), formulaResponse.getBody());
        assertEquals(500000500000L, parallelResponse.getBody());
    }


}
