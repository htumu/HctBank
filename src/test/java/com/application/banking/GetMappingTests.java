package com.application.banking;

import com.application.banking.service.IBankService;
import com.application.banking.service.ServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMappingTests {

    private IBankService iBankService;

    /*public GetMappingTests(ServiceImpl service) {
        this.iBankService = iBankService;
    }*/

    @Test
    void testGetTransactions(){
        Object transId = iBankService.getTransactionResponses(206033467494l, 80623126l);
        assertEquals(67336433,transId);

    }
}
