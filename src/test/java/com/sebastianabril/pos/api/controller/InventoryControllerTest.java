package com.sebastianabril.pos.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebastianabril.pos.api.controller.dto.AddProductToInventoryRequest;
import com.sebastianabril.pos.api.controller.dto.TransferProductRequest;
import com.sebastianabril.pos.api.entity.*;
import com.sebastianabril.pos.api.service.InventoryMovementsService;
import com.sebastianabril.pos.api.service.InventoryService;
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

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private InventoryMovementsService inventoryMovementsService;

    @Test
    public void addProductToInventoryTest() throws Exception {
        //given
        Integer userId = 5;
        Integer productId = 4;
        Integer quantity = 20;

        AddProductToInventoryRequest request = new AddProductToInventoryRequest();
        request.setUserId(userId);
        request.setProductId(productId);
        request.setQuantity(quantity);

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminUser = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        Inventory inventoryDb = new Inventory(userId, adminUser, productTest, 15);

        when(inventoryService.addProductToInventory(request.getUserId(), request.getProductId(), request.getQuantity()))
            .thenReturn(inventoryDb);

        //when y then juntos

        mockMvc
            .perform(
                post("/inventory/add-product-to-inventory")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(5)));
    }

    @Test
    public void transferProductTest() throws Exception {
        //given
        Integer originUserId = 1;
        Integer destinyUserId = 2;
        Integer productId = 6;
        Integer quantityTransferred = 3;

        TransferProductRequest request = new TransferProductRequest();
        request.setOriginUserId(originUserId);
        request.setDestinyUserId(destinyUserId);
        request.setProductId(productId);
        request.setQuantityTransferred(quantityTransferred);

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User originUser = new User(originUserId, "Sebastian", "Abril", "sebasAbril@gmail.com", "12345", adminRole);
        User destinyUser = new User(destinyUserId, "Hell", "Abril", "HellsitoAbril@gmail.com", "12345", adminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        InventoryMovement inventoryMovement = new InventoryMovement(
            4,
            originUser,
            destinyUser,
            productTest,
            quantityTransferred,
            date,
            time
        );

        when(inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantityTransferred))
            .thenReturn(inventoryMovement);

        //then - when

        mockMvc
            .perform(
                post("/inventory/transfer-product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.originUser.name", Matchers.is("Sebastian")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.destinyUser.name", Matchers.is("Hell")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.originUser.name", Matchers.is("Sebastian")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.product.id", Matchers.is(6)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.quantityTransferred", Matchers.is(3)));
    }
}
