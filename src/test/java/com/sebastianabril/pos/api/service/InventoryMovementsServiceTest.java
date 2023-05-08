package com.sebastianabril.pos.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sebastianabril.pos.api.entity.*;
import com.sebastianabril.pos.api.repository.InventoryMovementsRepository;
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

@ExtendWith(MockitoExtension.class)
class InventoryMovementsServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMovementsRepository inventoryMovementsRepository;

    @InjectMocks
    private InventoryMovementsService inventoryMovementsService;

    @Captor
    private ArgumentCaptor<InventoryMovement> inventoryMovementCaptor;

    @Test
    public void testTransferProductOk() {
        //given
        Integer originUserId = 1;
        Integer destinyUserId = 2;
        Integer productId = 4;
        Integer quantity = 25;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminOriginUser = new User(originUserId, "Pepito", "Gonzales", "pepitoG@gmail.com", "12345", adminRole);
        User adminDestinyUser = new User(
            destinyUserId,
            "Sebastian",
            "Abril",
            "sebasAbril@gmail.com",
            "12345",
            adminRole
        );
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        Inventory originUserInventory = new Inventory(33, adminOriginUser, productTest, 50);
        Inventory destinyUserInventory = new Inventory(33, adminDestinyUser, productTest, 10);

        when(userRepository.findById(originUserId)).thenReturn(Optional.of(adminOriginUser));
        when(userRepository.findById(destinyUserId)).thenReturn(Optional.of(adminDestinyUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productTest));
        when(inventoryRepository.findByUserAndProduct(adminOriginUser, productTest))
            .thenReturn(Optional.of(originUserInventory));
        when(inventoryRepository.findByUserAndProduct(adminDestinyUser, productTest))
            .thenReturn(Optional.of(destinyUserInventory));

        //when
        inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantity);
        //then
        verify(inventoryMovementsRepository, times(1)).save(inventoryMovementCaptor.capture());

        InventoryMovement inventoryMovement = inventoryMovementCaptor.getValue();

        assertNull(inventoryMovement.getId());
        assertEquals(inventoryMovement.getOriginUser().getId(), originUserId);
        assertEquals(inventoryMovement.getDestinyUser().getId(), destinyUserId);
        assertEquals(inventoryMovement.getProduct().getId(), productId);
        assertEquals(inventoryMovement.getQuantityTransferred(), quantity);
    }

    @Test
    public void testTransferProductWhenOriginUserDoesNotExist() {
        //given
        Integer originUserId = 11;
        Integer destinyUserId = 2;
        Integer productId = 4;
        Integer quantity = 25;

        when(userRepository.findById(originUserId)).thenReturn(Optional.empty());

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantity);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "Origin user not found");
    }

    @Test
    public void testTransferProductWhenDestinyUserDoesNotExist() {
        //given
        Integer originUserId = 11;
        Integer destinyUserId = 2;
        Integer productId = 4;
        Integer quantity = 25;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminOriginUser = new User(originUserId, "Sebastian", "Abril", "sebasAbril@gmail.com", "12345", adminRole);

        when(userRepository.findById(originUserId)).thenReturn(Optional.of(adminOriginUser));
        when(userRepository.findById(destinyUserId)).thenReturn(Optional.empty());

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantity);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "Destiny user not found");
    }

    @Test
    public void testTransferProductWhenProductDoesNotExist() {
        //given
        Integer originUserId = 11;
        Integer destinyUserId = 2;
        Integer productId = 4;
        Integer quantity = 25;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminOriginUser = new User(originUserId, "Sebastian", "Abril", "sebasAbril@gmail.com", "12345", adminRole);
        User adminDestinyUser = new User(destinyUserId, "Hell", "Abril", "HellsitoAbril@gmail.com", "12345", adminRole);

        when(userRepository.findById(originUserId)).thenReturn(Optional.of(adminOriginUser));
        when(userRepository.findById(destinyUserId)).thenReturn(Optional.of(adminDestinyUser));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantity);
            }
        );

        //then
        Assertions.assertEquals(thrown.getMessage(), "Product not found");
    }

    @Test
    public void testTransferProductWhenOriginUserDoesNotHaveInventory() {
        //given
        Integer originUserId = 11;
        Integer destinyUserId = 2;
        Integer productId = 4;
        Integer quantity = 25;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminOriginUser = new User(originUserId, "Sebastian", "Abril", "sebasAbril@gmail.com", "12345", adminRole);
        User adminDestinyUser = new User(destinyUserId, "Hell", "Abril", "HellsitoAbril@gmail.com", "12345", adminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");

        when(userRepository.findById(originUserId)).thenReturn(Optional.of(adminOriginUser));
        when(userRepository.findById(destinyUserId)).thenReturn(Optional.of(adminDestinyUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productTest));
        when(inventoryRepository.findByUserAndProduct(adminOriginUser, productTest)).thenReturn(Optional.empty());

        //then
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantity);
            }
        );

        //when
        Assertions.assertEquals(thrown.getMessage(), "The inventory for the origin user does not exist");
    }

    @Test
    public void testTransferProductWhenOriginUserDoesNotHaveQuantity() {
        //given
        Integer originUserId = 11;
        Integer destinyUserId = 2;
        Integer productId = 4;
        Integer quantity = 25;

        Role adminRole = new Role(1, "Admin", "Admin Role");
        User adminOriginUser = new User(originUserId, "Sebastian", "Abril", "sebasAbril@gmail.com", "12345", adminRole);
        User adminDestinyUser = new User(destinyUserId, "Hell", "Abril", "HellsitoAbril@gmail.com", "12345", adminRole);
        Product productTest = new Product(productId, "Correas", "Correas de cuero", 50000.00, "04");
        Inventory originUserInventory = new Inventory(33, adminOriginUser, productTest, 10);

        when(userRepository.findById(originUserId)).thenReturn(Optional.of(adminOriginUser));
        when(userRepository.findById(destinyUserId)).thenReturn(Optional.of(adminDestinyUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productTest));
        when(inventoryRepository.findByUserAndProduct(adminOriginUser, productTest))
            .thenReturn(Optional.of(originUserInventory));

        //then
        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                inventoryMovementsService.transferProduct(originUserId, destinyUserId, productId, quantity);
            }
        );

        //when
        Assertions.assertEquals(thrown.getMessage(), "The origin user does not have that quantity, try less");
    }
}
