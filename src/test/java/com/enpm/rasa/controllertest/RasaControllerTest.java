package com.enpm.rasa.controllertest;


import com.enpm.rasa.controller.AuthenticationController;
import com.enpm.rasa.controller.RasaController;
import com.enpm.rasa.dto.*;
import com.enpm.rasa.model.CardDetails;
import com.enpm.rasa.model.Role;
import com.enpm.rasa.model.User;
import com.enpm.rasa.repository.*;
import com.enpm.rasa.services.AuthenticationService;
import com.enpm.rasa.services.RasaService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest
public class RasaControllerTest {

    @MockBean
    private RasaService rasaService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private WishListRepository wishListRepository;

    @Autowired
    private RasaController rasaController;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private InventoryRepository inventoryRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private CartRepository cartRepository;


    @Test
    public void testGetCardDetails_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        List<CardDetails> cardDetails = new ArrayList<>();
        cardDetails.add(new CardDetails());

        // Mock HttpServletRequest
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userObj));
        when(cardRepository.findByUser(userObj.getUserId())).thenReturn(cardDetails);

        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(Map.of("status", 1, "cardDetails", cardDetails), HttpStatus.OK);
        when(rasaService.getCardDetails(anyString())).thenReturn(mockResponseEntity);

        // Act
        ResponseEntity<Object> response = rasaController.getCardDetails(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check responseBody only if response has body
        if (response.hasBody()) {
            Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();
            assertEquals(1, responseBody.get("status"));
            assertEquals(cardDetails, responseBody.get("cardDetails"));
        }
    }

    @Test
    public void testAddCardDetails_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        CardDetailsRequestDTO cardDetails = new CardDetailsRequestDTO();
        when(rasaService.addCardDetails("test@example.com", cardDetails)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.addCardDetails(request, cardDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddProductToWishlist_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        when(rasaService.addProductToWishlist("test@example.com", 1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.addProductToWishlist(request, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveProductFromWishlist_Success() {
        // Arrange

        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        when(rasaService.removeProductFromWishlist("test@example.com", 1)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.removeProductFromWishlist(request, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCheckIfProductIsInWishlist_Success() {
        // Arrange

        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        when(rasaService.checkIfProductIsInWishlist("test@example.com", 1)).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.checkIfProductIsInWishlist(request, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testGetWishList_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        when(rasaService.getWishlist(userEmail)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getWishList(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetProductById_Success() {
        // Arrange
        int productId = 123;
        when(rasaService.getProductById(productId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetProductsByName_Success() {
        // Arrange
        String productName = "TestProduct";
        when(rasaService.getProductsByName(productName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getProductsByName(productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetProductsByTag_Success() {
        // Arrange
        String tagName = "TestTag";
        when(rasaService.getProductsByTag(tagName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getProductsByTag(tagName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFilterProducts_Success() {
        // Arrange
        FilterProductsRequestDTO filters = new FilterProductsRequestDTO();
        when(rasaService.filterProducts(filters)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.filterProducts(filters);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddTag_AdminUser_Success() {
        // Arrange
        AddTag tagData = new AddTag();
        String userEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.addTag(tagData)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.addTag(request, tagData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddTag_NonAdminUser_Error() {
        // Arrange
        AddTag tagData = new AddTag();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.addTag(request, tagData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Error! Please try again", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetTags_Success() {
        // Arrange
        AddTag tagData = new AddTag();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.getTags()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getTags(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCart_Success() {
        // Arrange
        String userEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.getCart(userEmail)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getcart(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCart_Success() {
        // Arrange

        AddToCart productData = new AddToCart();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.addToCart(userEmail, productData)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.addToCart(request, productData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart_Success() {
        // Arrange
        int cartId = 123;
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.removeFromCart(cartId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.removeFromCart(request, cartId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testBuyNow_Success() {
        // Arrange
        BuyNowDTO buyNowDTO = new BuyNowDTO();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.buyNow(userEmail, buyNowDTO)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.buyNow(request, buyNowDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEditUser_Success() {
        // Arrange
        EditUserRequestDTO userDetails = new EditUserRequestDTO();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.editUser(userEmail, userDetails)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.editUser(request, userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrderHistory_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.getUserOrderHistory(userEmail)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getOrderHistory(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetUserOrderHistory_Success() {
        // Arrange
        UserOrderHistoryDTO userOrderHistoryDTO = new UserOrderHistoryDTO();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);
        when(rasaService.getUserOrderHistory(userEmail)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getUserOrderHistory(request, userOrderHistoryDTO);

        // Assert
        assertNull(response);
    }

    @Test
    public void testGetOrderItems_Success() {
        // Arrange
        int orderId = 123;
        when(rasaService.getOrderItems(orderId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getOrderHistory(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddProduct_AdminUser_Success() {
        // Arrange
        AddProduct productData = new AddProduct();
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.addProduct(productData)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.addProduct(request, productData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddProduct_NonAdminUser_Error() {
        // Arrange
        AddProduct productData = new AddProduct();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.addProduct(request, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can add product!", responseBody.get("errorMsg"));
    }

    @Test
    public void testDeleteProduct_AdminUser_Success() {
        // Arrange
        int productId = 123;
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.deleteProduct(productId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.deleteProduct(request, productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct_NonAdminUser_Error() {
        // Arrange
        int productId = 123;
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.deleteProduct(request, productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can delete product!", responseBody.get("errorMsg"));
    }

    @Test
    public void testEditProduct_AdminUser_Success() {
        // Arrange
        int productId = 123;
        AddProduct productData = new AddProduct();
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.editProduct(productId, productData)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.editProduct(request, productId, productData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEditProduct_NonAdminUser_Error() {
        // Arrange
        int productId = 123;
        AddProduct productData = new AddProduct();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.editProduct(request, productId, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can edit product!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllProducts_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getAllProducts()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getAllProducts(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllProducts_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getAllProducts(request);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllOrders_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getAllOrders()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getAllOrders(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllOrders_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getAllOrders(request);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can see all orders!", responseBody.get("errorMsg"));
    }

    @Test
    public void testChangeOrderStatus_AdminUser_Success() {
        // Arrange
        int orderId = 123;
        ChangeOrderStatusRequest orderStatus = new ChangeOrderStatusRequest();
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.changeOrderStatus(orderId, orderStatus)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.changeOrderStatus(request, orderId, orderStatus);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testChangeOrderStatus_NonAdminUser_Error() {
        // Arrange
        int orderId = 123;
        ChangeOrderStatusRequest orderStatus = new ChangeOrderStatusRequest();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.changeOrderStatus(request, orderId, orderStatus);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can change order status!", responseBody.get("errorMsg"));
    }

    @Test
    public void testDeleteUser_AdminUser_Success() {
        // Arrange
        int userId = 123;
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.deleteUser(userId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.deleteUser(request, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUser_NonAdminUser_Error() {
        // Arrange
        int userId = 123;
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.deleteUser(request, userId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can delete user!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllUsers_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getAllUsers()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getAllUsers(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllUsers_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getAllUsers(request);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can delete user!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetInventory_AdminUser_Success() {
        // Arrange
        InventoryDTO inventoryDTO = new InventoryDTO();
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getInventory(inventoryDTO.getTagName())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getInventory(request, inventoryDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetInventory_NonAdminUser_Error() {
        // Arrange
        InventoryDTO inventoryDTO = new InventoryDTO();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getInventory(request, inventoryDTO);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access inventory!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetInventoryByCategory_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getInventoryByCategory()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getInventoryByCategory(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetInventoryByCategory_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getInventoryByCategory(request);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access inventory!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetTotalSales_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getTotalSales()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getTotalSales(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTotalSales_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getTotalSales(request);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access inventory!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllProductNames_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getAllProductNames()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getAllProductNames(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllProductNames_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getAllProductNames(request);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetYOYSales_AdminUser_Success() {
        // Arrange
        YOYSalesRequestDTO salesRequestData = new YOYSalesRequestDTO();
        String adminEmail = "admin@gmail.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getYOYSales(salesRequestData.getYear(), salesRequestData.getProductName())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getYOYSales(request, salesRequestData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetYOYSales_NonAdminUser_Error() {
        // Arrange
        YOYSalesRequestDTO salesRequestData = new YOYSalesRequestDTO();
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getYOYSales(request, salesRequestData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetRevenueByFilterType_AdminUser_Success() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        String filterType = "filter";
        String productName = "product";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> adminEmail);
        when(rasaService.getRevenueByProduct(filterType, productName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getRevenueByFilterType(request, filterType, productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetRevenueByFilterType_NonAdminUser_Error() {
        // Arrange
        String userEmail = "test@example.com";
        String filterType = "filter";
        String productName = "product";
        User userObj = new User();
        userObj.setUserId(1);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setUserPrincipal(() -> userEmail);

        // Act
        ResponseEntity<Object> response = rasaController.getRevenueByFilterType(request, filterType, productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Only admin can access!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetProductsForHome_Success() {
        // Arrange
        when(rasaService.getProductsForHome()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = rasaController.getProductsForHome();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
