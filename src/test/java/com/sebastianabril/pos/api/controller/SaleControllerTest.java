package com.sebastianabril.pos.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebastianabril.pos.api.controller.dto.SaleRequest;
import com.sebastianabril.pos.api.entity.*;
import com.sebastianabril.pos.api.service.SaleService;
import java.time.LocalDate;
import java.time.LocalTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SaleController.class)
class SaleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaleService saleService;

    @Test
    public void saveTest() throws Exception {
        //given
        Integer userId = 2;
        Integer productId = 5;
        Integer quantitySold = 4;
        Double price = 6.66;

        SaleRequest saleRequest = new SaleRequest();
        saleRequest.setUserId(userId);
        saleRequest.setProductId(productId);
        saleRequest.setQuantitySold(quantitySold);

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User user = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product product = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");

        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();

        Sale sale = new Sale(null, user, product, saleRequest.getQuantitySold(), date, time, price);

        when(saleService.save(saleRequest.getUserId(), saleRequest.getProductId(), saleRequest.getQuantitySold()))
            .thenReturn(sale);

        //when y then juntos
        mockMvc
            .perform(
                post("/api/sale")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(saleRequest))
            )
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id", Matchers.is(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.product.id", Matchers.is(5)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.quantitySold", Matchers.is(4)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(6.66)));
    }
}
