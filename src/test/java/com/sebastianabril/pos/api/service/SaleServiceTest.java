package com.sebastianabril.pos.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sebastianabril.pos.api.entity.*;
import com.sebastianabril.pos.api.repository.InventoryRepository;
import com.sebastianabril.pos.api.repository.ProductRepository;
import com.sebastianabril.pos.api.repository.SaleRepository;
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

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {
    @Mock
    SaleRepository saleRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    InventoryRepository inventoryRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    SaleService saleService;

    @Captor
    private ArgumentCaptor<Sale> saleCaptor;

    @Test
    public void shouldSave() {
        //given
        Integer userId = 2;
        Integer productId = 5;
        Integer quantitySold = 3;
        Role adminRole = new Role(1, "Admin", "Admin Role");
        User user = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product product = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        Inventory inventory = new Inventory(30, user, product, 15);

        when((userRepository.findById(userId))).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(inventory));

        //when
        saleService.save(userId, productId, quantitySold);
        //then
        verify(saleRepository, times(1)).save(saleCaptor.capture());

        Sale sale = saleCaptor.getValue();

        assertEquals(sale.getUser(), user);
        assertEquals(sale.getProduct(), product);
        assertEquals(sale.getQuantitySold(), quantitySold);
    }

    @Test
    public void saveWhenUserDoesNotExist() {
        //given
        Integer userId = 2;
        Integer productId = 5;
        Integer quantitySold = 3;
        Double price = 10.5;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when

        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                saleService.save(userId, productId, quantitySold);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "The user with id " + userId + " does not exist");
    }

    @Test
    public void saveWhenProductDoesNotExist() {
        //given
        Integer userId = 2;
        Integer productId = 5;
        Integer quantitySold = 3;
        Double price = 10.5;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User user = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                saleService.save(userId, productId, quantitySold);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "The product with id " + productId + " does not exist");
    }

    @Test
    public void saveWhenQuantityIsNegativeOrZero() {
        //given
        Integer userId = 2;
        Integer productId = 5;
        Integer quantitySold = -3;
        Double price = 10.5;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User user = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product product = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                saleService.save(userId, productId, quantitySold);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "The quantity must be greater or equal than 1");
    }

    @Test
    public void saveWhenInventoryDoesNotHaveEnoughQuantityProduct() {
        //given
        Integer userId = 2;
        Integer productId = 5;
        Integer quantitySold = 3;
        Double price = 10.5;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User user = new User(userId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        Product product = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        Inventory inventory = new Inventory(30, user, product, 1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(inventory));

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                saleService.save(userId, productId, quantitySold);
            }
        );

        //then
        Assertions.assertEquals(
            thrown.getMessage(),
            "The quantity must be 0 or greater (The user does not have enough product to sell)"
        );
    }
}
