package com.chitkara.bfhl.service;

import com.chitkara.bfhl.config.UserProperties;
import com.chitkara.bfhl.dto.BfhlRequest;
import com.chitkara.bfhl.dto.BfhlResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BfhlServiceImplTest {

    private BfhlService bfhlService;

    @BeforeEach
    void setUp() {
        UserProperties userProperties = new UserProperties();
        userProperties.setFullName("John Doe");
        userProperties.setEmail("john@xyz.com");
        userProperties.setRollNumber("ABCD123");
        userProperties.setDateOfBirth("17-09-1999");
        bfhlService = new BfhlServiceImpl(userProperties);
    }

    @Test
    void processExampleA() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse response = bfhlService.process(request);

        assertTrue(response.isSuccess());
        assertEquals("john_doe_17091999", response.getUserId());
        assertEquals("john@xyz.com", response.getEmail());
        assertEquals("ABCD123", response.getRollNumber());
        assertEquals(List.of("1"), response.getOddNumbers());
        assertEquals(List.of("334", "4"), response.getEvenNumbers());
        assertEquals(List.of("A", "R"), response.getAlphabets());
        assertEquals(List.of("$"), response.getSpecialCharacters());
        assertEquals("339", response.getSum());
        assertEquals("Ra", response.getConcatString());
    }

    @Test
    void processExampleB() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponse response = bfhlService.process(request);

        assertTrue(response.isSuccess());
        assertEquals(List.of("5"), response.getOddNumbers());
        assertEquals(List.of("2", "4", "92"), response.getEvenNumbers());
        assertEquals(List.of("A", "Y", "B"), response.getAlphabets());
        assertEquals(List.of("&", "-", "*"), response.getSpecialCharacters());
        assertEquals("103", response.getSum());
        assertEquals("ByA", response.getConcatString());
    }

    @Test
    void processExampleC() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse response = bfhlService.process(request);

        assertTrue(response.isSuccess());
        assertEquals(List.of(), response.getOddNumbers());
        assertEquals(List.of(), response.getEvenNumbers());
        assertEquals(List.of("A", "ABCD", "DOE"), response.getAlphabets());
        assertEquals(List.of(), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("EoDdCbAa", response.getConcatString());
    }

    @Test
    void processEmptyData() {
        BfhlRequest request = new BfhlRequest(List.of());
        BfhlResponse response = bfhlService.process(request);

        assertTrue(response.isSuccess());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }
}
