package com.sebastianabril.pos.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sebastianabril.pos.api.entity.Inventory;
import com.sebastianabril.pos.api.entity.Product;
import com.sebastianabril.pos.api.entity.Role;
import com.sebastianabril.pos.api.entity.User;
import com.sebastianabril.pos.api.repository.InventoryRepository;
import com.sebastianabril.pos.api.repository.ProductRepository;
import com.sebastianabril.pos.api.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Captor
    private ArgumentCaptor<Inventory> inventoryCaptor;

    @Test
    public void testAddProductToInventoryWhenDoesNotExists() {
        //given
        Integer userId = 5;
        Integer productId = 4;
        Integer quantity = 5;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        User adminUser = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productTest));
        when(inventoryRepository.findByUserAndProduct(any(), any())).thenReturn(Optional.empty());

        //when
        inventoryService.addProductToInventory(userId, productId, quantity);

        //then
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());

        Inventory inventory = inventoryCaptor.getValue();

        assertNull(inventory.getId());
        assertEquals(quantity, inventory.getQuantity());
        assertEquals(inventory.getUser().getId(), adminUser.getId());
        assertEquals(inventory.getProduct().getId(), productTest.getId());
    }

    @Test
    public void testAddProductToInventoryWhenExists() {
        //given
        Integer userId = 5;
        Integer productId = 4;
        Integer quantity = 5;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminUser = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");

        Inventory inventoryDb = new Inventory(30, adminUser, productTest, 15);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productTest));
        when(inventoryRepository.findByUserAndProduct(any(), any())).thenReturn(Optional.of(inventoryDb));

        //when
        inventoryService.addProductToInventory(userId, productId, quantity);

        //then
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());

        Inventory inventory = inventoryCaptor.getValue();

        assertEquals(inventoryDb.getId(), inventory.getId());
        assertEquals(20, inventory.getQuantity());
        assertEquals(inventory.getUser().getId(), adminUser.getId());
        assertEquals(inventory.getProduct().getId(), productTest.getId());
    }

    @Test
    public void testAddProductToInventoryWhenUserDoesNotExist() {
        //given
        Integer userId = 5;
        Integer productId = 4;
        Integer quantity = 5;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when

        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryService.addProductToInventory(userId, productId, quantity);
            }
        );

        //then
        Assertions.assertEquals("user not found", thrown.getMessage());
    }

    @Test
    public void testAddProductToInventoryWhenUserIsNotAdmin() {
        //given
        Integer userId = 5;
        Integer productId = 4;
        Integer quantity = 5;

        Role notAdminRole = new Role(3, "Admin", "Admin Role");
        User notAdminUser = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", notAdminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");

        when(userRepository.findById(userId)).thenReturn(Optional.of(notAdminUser));
        //when

        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryService.addProductToInventory(userId, productId, quantity);
            }
        );
        //then

        Assertions.assertEquals(thrown.getMessage(), "The user must be an admin (userId = 1)");
    }

    @Test
    public void testAddProductToInventoryWhenProductDoesNotExist() {
        //given
        Integer userId = 5;
        Integer productId = 10;
        Integer quantity = 50;
        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminUser = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryService.addProductToInventory(userId, productId, quantity);
            }
        );
        //then

        Assertions.assertEquals(thrown.getMessage(), "productId not found");
    }

    @Test
    public void testAddProductToInventoryWhenAmountIsNegative() {
        //given
        Integer userId = 5;
        Integer productId = 4;
        Integer quantity = -25;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminUser = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productTest));

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryService.addProductToInventory(userId, productId, quantity);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "quantity must be positive number");
    }
}
/*
* 1) las personas involucradas deben existir
2) el producto involucrado debe existir
3) la cantidad que se quiere transferir debe ser positiva
4) si UD me va a mandar UD debe tener suficiente para poder enviar
5) a una persona se le disminuye y a otra e le suma
* */
