package com.enpm.rasa.servicetest;

import com.enpm.rasa.dto.*;
import com.enpm.rasa.model.*;
import com.enpm.rasa.repository.*;
import com.enpm.rasa.services.RasaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RasaServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private WishListRepository wishListRepository;

    @Autowired
    private RasaService rasaService;

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

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        tagRepository.deleteAll(); // Clear the product_tag table before each test case
    }

    @Test
    public void testGetCardDetails_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        List<CardDetails> cardDetails = new ArrayList<>();
        cardDetails.add(new CardDetails());
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userObj));
        when(cardRepository.findByUser(userObj.getUserId())).thenReturn(cardDetails);

        // Act
        ResponseEntity<Object> response = rasaService.getCardDetails(userEmail);

        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(cardDetails, responseBody.get("cardDetails"));
    }

    @Test
    public void testGetCardDetails_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.getCardDetails(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetCardDetails_NoCardDetailsFound() {
        // Arrange
        String userEmail = "test@example.com";
        User userObj = new User();
        userObj.setUserId(1);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userObj));
        when(cardRepository.findByUser(userObj.getUserId())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = rasaService.getCardDetails(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1,  responseBody.get("status"));
        assertEquals(new HashMap<>(), responseBody.get("cardDetails"));
    }

    @Test
    public void testAddProductToWishlist_Success() {
        // Arrange
        String userEmail = "test@example.com";
        int productId = 1;
        User user = new User();
        Product product = new Product();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(productRepository.findByProductId(productId)).thenReturn(product);

        // Act
        ResponseEntity<Object> response = rasaService.addProductToWishlist(userEmail, productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(wishListRepository, times(1)).save(any(Wishlist.class));
    }

    @Test
    public void testAddProductToWishlist_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        int productId = 1;
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.addProductToWishlist(userEmail, productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertTrue(responseBody.containsKey("errorMsg"));
    }

    @Test
    public void testRemoveProductFromWishlist_Success() {
        // Arrange
        String userEmail = "test@example.com";
        int wishlistId = 1;

        // Act
        ResponseEntity<Object> response = rasaService.removeProductFromWishlist(userEmail, wishlistId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(wishListRepository, times(1)).deleteById(wishlistId);
    }

    @Test
    public void testRemoveProductFromWishlist_Exception() {
        // Arrange
        String userEmail = "test@example.com";
        int wishlistId = 1;
        doThrow(new RuntimeException("Database error")).when(wishListRepository).deleteById(wishlistId);

        // Act
        ResponseEntity<Object> response = rasaService.removeProductFromWishlist(userEmail, wishlistId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(wishListRepository, times(1)).deleteById(wishlistId);
    }

    @Test
    public void testCheckIfProductIsInWishlist_ProductPresent() {
        // Arrange
        String userEmail = "test@example.com";
        int productId = 1;
        User user = new User();
        Product product = new Product();
        product.setProductId(productId);
        List<Wishlist> wishlists = new ArrayList<>();
        Wishlist wishlist = new Wishlist();
        wishlist.setProductId(product);
        wishlists.add(wishlist);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(productRepository.findByProductId(productId)).thenReturn(product);
        when(wishListRepository.findByUserId(user)).thenReturn(wishlists);

        // Act
        ResponseEntity<Object> response = rasaService.checkIfProductIsInWishlist(userEmail, productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertTrue((Boolean) responseBody.get("isPresent"));
    }

    @Test
    public void testCheckIfProductIsInWishlist_ProductNotPresent() {
        // Arrange
        String userEmail = "test@example.com";
        int productId = 1;
        User user = new User();
        Product product = new Product();
        product.setProductId(2);
        List<Wishlist> wishlists = new ArrayList<>();
        Wishlist wishlist = new Wishlist();
        wishlist.setProductId(product);
        wishlists.add(wishlist);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(productRepository.findByProductId(productId)).thenReturn(product);
        when(wishListRepository.findByUserId(user)).thenReturn(wishlists);

        // Act
        ResponseEntity<Object> response = rasaService.checkIfProductIsInWishlist(userEmail, productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertFalse((Boolean) responseBody.get("isPresent"));
    }

    @Test
    public void testCheckIfProductIsInWishlist_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        int productId = 1;
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.checkIfProductIsInWishlist(userEmail, productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found!", responseBody.get("errorMsg"));
    }

    @Test
    public void testAddCardDetails_Success() {
        // Arrange
        String userEmail = "test@example.com";
        CardDetailsRequestDTO newCardDetails = new CardDetailsRequestDTO();
        newCardDetails.setPaymentId("1234");
        newCardDetails.setPaymentMethod(PaymentMethod.CREDITCARD);
        newCardDetails.setName("John Doe");
        User user = new User();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Object> response = rasaService.addCardDetails(userEmail, newCardDetails);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertNotNull(responseBody.get("card"));
        CardDetails savedCardDetails = (CardDetails) responseBody.get("card");
        assertEquals(newCardDetails.getPaymentId(), savedCardDetails.getPaymentId());
        assertEquals(newCardDetails.getPaymentMethod(), savedCardDetails.getPaymentMethod());
        assertEquals(newCardDetails.getName(), savedCardDetails.getName());
        assertEquals(user, savedCardDetails.getUserId());
        verify(cardRepository, times(1)).save(any(CardDetails.class));
    }

    @Test
    public void testAddCardDetails_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        CardDetailsRequestDTO newCardDetails = new CardDetailsRequestDTO();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.addCardDetails(userEmail, newCardDetails);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found!", responseBody.get("errorMsg"));
        verify(cardRepository, never()).save(any(CardDetails.class));
    }

    @Test
    public void testAddTag_Success() {
        // Arrange
        String tagName = "Test Tag";
        String imageUrl = "https://example.com/test-tag.png";
        AddTag tagData = new AddTag(tagName, imageUrl);

        // Act
        ResponseEntity<Object> response = rasaService.addTag(tagData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        ProductTag savedTag = (ProductTag) responseBody.get("tag");
        assertEquals(tagName, savedTag.getTagName());
        assertEquals(imageUrl, savedTag.getImageUrl());
        verify(tagRepository, times(1)).save(any(ProductTag.class));
    }

    @Test
    public void testGetTags_Success() {
        // Arrange
        List<ProductTag> tags = new ArrayList<>();
        tags.add(new ProductTag(1, "Tag 1", "https://example.com/tag1.png"));
        tags.add(new ProductTag(2, "Tag 2", "https://example.com/tag2.png"));
        when(tagRepository.findAll()).thenReturn(tags);

        // Act
        ResponseEntity<Object> response = rasaService.getTags();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<List<String>> tagNames = (List<List<String>>) responseBody.get("tags");
        assertEquals(2, tagNames.size());
        assertEquals(Arrays.asList("Tag 1", "https://example.com/tag1.png"), tagNames.get(0));
        assertEquals(Arrays.asList("Tag 2", "https://example.com/tag2.png"), tagNames.get(1));
    }

    @Test
    public void testGetTags_NoTags() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getTags();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<List<String>> tagNames = (List<List<String>>) responseBody.get("tags");
        assertTrue(tagNames.isEmpty());
    }

    @Test
    public void testGetTags_Exception() {
        // Arrange
        when(tagRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getTags();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetWishlist_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);

        Product product1 = new Product();
        product1.setProductId(1);
        product1.setName("Product 1");
        product1.setImageUrl("https://example.com/product1.jpg");

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setName("Product 2");
        product2.setImageUrl("https://example.com/product2.jpg");

        Wishlist wishlist1 = new Wishlist();
        wishlist1.setWishlistId(1);
        wishlist1.setProductId(product1);
        wishlist1.setUserId(user);

        Wishlist wishlist2 = new Wishlist();
        wishlist2.setWishlistId(2);
        wishlist2.setProductId(product2);
        wishlist2.setUserId(user);

        List<Wishlist> wishlists = Arrays.asList(wishlist1, wishlist2);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(wishListRepository.findAll()).thenReturn(wishlists);

        // Act
        ResponseEntity<Object> response = rasaService.getWishlist(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<WishListDTO> wishlistDTOs = (List<WishListDTO>) responseBody.get("wishlist");
        assertEquals(2, wishlistDTOs.size());

        WishListDTO dto1 = wishlistDTOs.get(0);
        assertEquals(1, dto1.getProductId());
        assertEquals("Product 1", dto1.getProductName());
        assertEquals("https://example.com/product1.jpg", dto1.getProductImage());
        assertEquals(1, dto1.getWishlistId());

        WishListDTO dto2 = wishlistDTOs.get(1);
        assertEquals(2, dto2.getProductId());
        assertEquals("Product 2", dto2.getProductName());
        assertEquals("https://example.com/product2.jpg", dto2.getProductImage());
        assertEquals(2, dto2.getWishlistId());
    }

    @Test
    public void testGetWishlist_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.getWishlist(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found!", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetWishlist_NoWishlists() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(wishListRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getWishlist(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<WishListDTO> wishlistDTOs = (List<WishListDTO>) responseBody.get("wishlist");
        assertTrue(wishlistDTOs.isEmpty());
    }

    @Test
    public void testAddProduct_Success() {
        // Arrange
        AddProduct productData = new AddProduct();
        productData.setTagName("Test Tag");
        productData.setName("Test Product");
        productData.setDescription("This is a test product");
        productData.setPrice(100L);
        productData.setQuantity(10L);
        productData.setImageUrl("https://example.com/test-product.jpg");

        ProductTag tag = new ProductTag();
        tag.setTagName("Test Tag");

        when(tagRepository.findByTagName("Test Tag")).thenReturn(tag);

        // Act
        ResponseEntity<Object> response = rasaService.addProduct(productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    public void testAddProduct_NullProductData() {
        // Arrange
        AddProduct productData = null;

        // Act
        ResponseEntity<Object> response = rasaService.addProduct(productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertTrue(responseBody.containsKey("errorMsg"));
        verify(productRepository, never()).save(any(Product.class));
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    public void testDeleteProduct_Success() {
        // Arrange
        int productId = 1;

        // Act
        ResponseEntity<Object> response = rasaService.deleteProduct(productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        // Arrange
        int productId = 1;
        doThrow(new EmptyResultDataAccessException("Product not found", 1)).when(productRepository).deleteById(productId);

        // Act
        ResponseEntity<Object> response = rasaService.deleteProduct(productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Product not found", responseBody.get("errorMsg"));
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_Exception() {
        // Arrange
        int productId = 1;
        doThrow(new RuntimeException("Database error")).when(productRepository).deleteById(productId);

        // Act
        ResponseEntity<Object> response = rasaService.deleteProduct(productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testEditProduct_Success() {
        // Arrange
        int productId = 1;
        AddProduct productData = new AddProduct();
        productData.setTagName("Test Tag");
        productData.setName("Updated Product Name");
        productData.setDescription("Updated product description");
        productData.setPrice(200L);

        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setName("Old Product Name");
        existingProduct.setDescription("Old product description");
        existingProduct.setPrice(100L);

        ProductTag tag = new ProductTag();
        tag.setTagName("Test Tag");

        when(productRepository.findByProductId(productId)).thenReturn(existingProduct);
        when(tagRepository.findByTagName("Test Tag")).thenReturn(tag);

        // Act
        ResponseEntity<Object> response = rasaService.editProduct(productId, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(productRepository, times(1)).save(existingProduct);
        assertEquals(productData.getName(), existingProduct.getName());
        assertEquals(productData.getDescription(), existingProduct.getDescription());
        assertEquals(productData.getPrice(), existingProduct.getPrice());
        assertEquals(tag, existingProduct.getTag());
    }

    @Test
    public void testEditProduct_ProductNotFound() {
        // Arrange
        int productId = 1;
        AddProduct productData = new AddProduct();
        productData.setTagName("Test Tag");
        productData.setName("Updated Product Name");
        productData.setDescription("Updated product description");
        productData.setPrice(200L);

        when(productRepository.findByProductId(productId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = rasaService.editProduct(productId, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertTrue(responseBody.containsKey("errorMsg"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testGetAllOrders_Success() {
        // Arrange
        List<Order> allOrders = new ArrayList<>();

        // Pending order
        User user1 = new User();
        user1.setUserId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        Order pendingOrder = new Order();
        pendingOrder.setOrderId(1);
        pendingOrder.setUserId(user1);
        pendingOrder.setOrderDate(new Date());
        pendingOrder.setTotalAmount(100L);
        pendingOrder.setPaymentMethod(PaymentMethod.CREDITCARD);
        pendingOrder.setOrderStatus("PENDING");
        allOrders.add(pendingOrder);

        // Fulfilled order
        User user2 = new User();
        user2.setUserId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        Order fulfilledOrder = new Order();
        fulfilledOrder.setOrderId(2);
        fulfilledOrder.setUserId(user2);
        fulfilledOrder.setOrderDate(new Date());
        fulfilledOrder.setTotalAmount(200L);
        fulfilledOrder.setPaymentMethod(PaymentMethod.PAYPAL);
        fulfilledOrder.setOrderStatus("FULFILLED");
        allOrders.add(fulfilledOrder);

        // Cancelled order
        User user3 = new User();
        user3.setUserId(3);
        user3.setFirstName("Bob");
        user3.setLastName("Johnson");
        Order cancelledOrder = new Order();
        cancelledOrder.setOrderId(3);
        cancelledOrder.setUserId(user3);
        cancelledOrder.setOrderDate(new Date());
        cancelledOrder.setTotalAmount(150L);
        cancelledOrder.setPaymentMethod(PaymentMethod.CASH);
        cancelledOrder.setOrderStatus("CANCELLED");
        allOrders.add(cancelledOrder);

        when(orderRepository.findAll()).thenReturn(allOrders);

        // Act
        ResponseEntity<Object> response = rasaService.getAllOrders();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<OrderResponseDTO> pendingOrders = (List<OrderResponseDTO>) responseBody.get("pendingorders");
        List<OrderResponseDTO> fulfilledOrders = (List<OrderResponseDTO>) responseBody.get("fulfilledorders");
        List<OrderResponseDTO> cancelledOrders = (List<OrderResponseDTO>) responseBody.get("cancelledOrders");
        assertEquals(1, pendingOrders.size());
        assertEquals(1, fulfilledOrders.size());
        assertEquals(1, cancelledOrders.size());
        // Add more assertions for the order details if needed
    }

    @Test
    public void testGetAllOrders_NoOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getAllOrders();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<OrderResponseDTO> pendingOrders = (List<OrderResponseDTO>) responseBody.get("pendingorders");
        List<OrderResponseDTO> fulfilledOrders = (List<OrderResponseDTO>) responseBody.get("fulfilledorders");
        List<OrderResponseDTO> cancelledOrders = (List<OrderResponseDTO>) responseBody.get("cancelledOrders");
        assertTrue(pendingOrders.isEmpty());
        assertTrue(fulfilledOrders.isEmpty());
        assertTrue(cancelledOrders.isEmpty());
    }

    @Test
    public void testGetAllOrders_Exception() {
        // Arrange
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getAllOrders();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testChangeOrderStatus_Success() {
        // Arrange
        int orderId = 1;
        String newOrderStatus = "FULFILLED";
        ChangeOrderStatusRequest orderStatusRequest = new ChangeOrderStatusRequest();
        orderStatusRequest.setOrderStatus(newOrderStatus);

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus("PENDING");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Object> response = rasaService.changeOrderStatus(orderId, orderStatusRequest);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(newOrderStatus, order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testChangeOrderStatus_OrderNotFound() {
        // Arrange
        int orderId = 1;
        String newOrderStatus = "FULFILLED";
        ChangeOrderStatusRequest orderStatusRequest = new ChangeOrderStatusRequest();
        orderStatusRequest.setOrderStatus(newOrderStatus);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.changeOrderStatus(orderId, orderStatusRequest);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Order not found", responseBody.get("errorMsg"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testChangeOrderStatus_Exception() {
        // Arrange
        int orderId = 1;
        String newOrderStatus = "FULFILLED";
        ChangeOrderStatusRequest orderStatusRequest = new ChangeOrderStatusRequest();
        orderStatusRequest.setOrderStatus(newOrderStatus);

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus("PENDING");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doThrow(new RuntimeException("Database error")).when(orderRepository).save(any(Order.class));

        // Act
        ResponseEntity<Object> response = rasaService.changeOrderStatus(orderId, orderStatusRequest);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        int userId = 1;

        // Act
        ResponseEntity<Object> response = rasaService.deleteUser(userId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        int userId = 1;
        doThrow(new EmptyResultDataAccessException("User not found", 1)).when(userRepository).deleteById(userId);

        // Act
        ResponseEntity<Object> response = rasaService.deleteUser(userId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_Exception() {
        // Arrange
        int userId = 1;
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(userId);

        // Act
        ResponseEntity<Object> response = rasaService.deleteUser(userId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testEditUser_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setFirstName("John");
        user.setLastName("Doe");

        EditUserRequestDTO editUserRequest = new EditUserRequestDTO();
        editUserRequest.setFirstName("Jane");
        editUserRequest.setLastName("Smith");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Object> response = rasaService.editUser(userEmail, editUserRequest);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testEditUser_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        EditUserRequestDTO editUserRequest = new EditUserRequestDTO();
        editUserRequest.setFirstName("Jane");
        editUserRequest.setLastName("Smith");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.editUser(userEmail, editUserRequest);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testEditUser_Exception() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setFirstName("John");
        user.setLastName("Doe");

        EditUserRequestDTO editUserRequest = new EditUserRequestDTO();
        editUserRequest.setFirstName("Jane");
        editUserRequest.setLastName("Smith");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Database error")).when(userRepository).save(any(User.class));

        // Act
        ResponseEntity<Object> response = rasaService.editUser(userEmail, editUserRequest);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserOrderHistory_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);
        user.setFirstName("John");
        user.setLastName("Doe");

        Order pendingOrder = new Order();
        pendingOrder.setOrderId(1);
        pendingOrder.setUserId(user);
        pendingOrder.setOrderDate(new Date());
        pendingOrder.setTotalAmount(100L);
        pendingOrder.setPaymentMethod(PaymentMethod.CREDITCARD);
        pendingOrder.setOrderStatus("PENDING");

        Order fulfilledOrder = new Order();
        fulfilledOrder.setOrderId(2);
        fulfilledOrder.setUserId(user);
        fulfilledOrder.setOrderDate(new Date());
        fulfilledOrder.setTotalAmount(200L);
        fulfilledOrder.setPaymentMethod(PaymentMethod.PAYPAL);
        fulfilledOrder.setOrderStatus("FULFILLED");

        List<Order> orders = Arrays.asList(pendingOrder, fulfilledOrder);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        ResponseEntity<Object> response = rasaService.getUserOrderHistory(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<OrderResponseDTO> pendingOrders = (List<OrderResponseDTO>) responseBody.get("pendingorders");
        List<OrderResponseDTO> fulfilledOrders = (List<OrderResponseDTO>) responseBody.get("fulfilledorders");
        assertEquals(1, pendingOrders.size());
        assertEquals(1, fulfilledOrders.size());
        // Add more assertions for the order details if needed
    }

    @Test
    public void testGetUserOrderHistory_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.getUserOrderHistory(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetOrderItems_Success() {
        // Arrange
        int orderId = 1;
        Order order = new Order();
        order.setOrderId(orderId);

        Product product1 = new Product();
        product1.setProductId(1);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setName("Product 2");

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrderItemId(1);
        orderItem1.setOrderId(order);
        orderItem1.setProductId(product1);
        orderItem1.setQuantity(2L);
        orderItem1.setUnitPrice(50L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderItemId(2);
        orderItem2.setOrderId(order);
        orderItem2.setProductId(product2);
        orderItem2.setQuantity(3L);
        orderItem2.setUnitPrice(75L);

        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAll()).thenReturn(orderItems);

        // Act
        ResponseEntity<Object> response = rasaService.getOrderItems(orderId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<OrderItemResponseDTO> orderedItems = (List<OrderItemResponseDTO>) responseBody.get("orderItems");
        assertEquals(2, orderedItems.size());
        // Add more assertions for the order item details if needed
    }

    @Test
    public void testGetOrderItems_OrderNotFound() {
        // Arrange
        int orderId = 1;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.getOrderItems(orderId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Order not found", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllUsers_Success() {
        // Arrange
        List<User> users = new ArrayList<>();
        User user1 = User.builder()
                .email("user1@example.com")
                .password("abc123")
                .firstName("John")
                .lastName("Doe")
                .userId(1)
                .build();

        users.add(user1);

        User user2 = User.builder()
                .email("user2@example.com")
                .password("abc123")
                .firstName("John")
                .lastName("Doe")
                .userId(2)
                .build();

        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        ResponseEntity<Object> response = rasaService.getAllUsers();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<UsersResponseDTO> usersDTOList = (List<UsersResponseDTO>) responseBody.get("users");
        assertEquals(2, usersDTOList.size());
        // Add more assertions for user details if needed
    }

    @Test
    public void testGetAllUsers_NoUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getAllUsers();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<UsersResponseDTO> usersDTOList = (List<UsersResponseDTO>) responseBody.get("users");
        assertTrue(usersDTOList.isEmpty());
    }

    @Test
    public void testGetAllUsers_Exception() {
        // Arrange
        when(userRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getAllUsers();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetProductById_Success() {
        // Arrange
        int productId = 1;
        Product product = new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, null);
        when(productRepository.findByProductId(productId)).thenReturn(product);

        // Act
        ResponseEntity<Object> response = rasaService.getProductById(productId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(product, responseBody.get("product"));
    }

    @Test
    public void testGetProductsByName_Success() {
        // Arrange
        String productName = "Product";
        ProductTag tag1 = new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg");
        ProductTag tag2 = new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag1));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag2));
        products.add(new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, tag1));
        when(productRepository.findByProductName(productName)).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.getProductsByName(productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(products, responseBody.get("products"));
        assertEquals(Arrays.asList("Tag 1", "Tag 2"), responseBody.get("tags"));
        assertEquals(100L, responseBody.get("minPrice"));
        assertEquals(150L, responseBody.get("maxPrice"));
    }

    @Test
    public void testGetProductsByName_NoProducts() {
        // Arrange
        String productName = "Product";
        when(productRepository.findByProductName(productName)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getProductsByName(productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertTrue(((List<?>) responseBody.get("products")).isEmpty());
        assertTrue(((List<?>) responseBody.get("tags")).isEmpty());
        assertEquals(Long.MAX_VALUE, responseBody.get("minPrice"));
        assertEquals(Long.MIN_VALUE, responseBody.get("maxPrice"));
    }

    @Test
    public void testGetProductsByName_Exception() {
        // Arrange
        String productName = "Product";
        when(productRepository.findByProductName(productName)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getProductsByName(productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetProductsByTag_Success() {
        // Arrange
        String tagName = "Tag 1";
        ProductTag tag = new ProductTag(1, tagName, "https://example.com/tag1.jpg");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag));

        when(tagRepository.findByTagName(tagName)).thenReturn(tag);
        when(productRepository.findByTagId(tag.getTagId())).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.getProductsByTag(tagName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(products, responseBody.get("products"));
    }

    @Test
    public void testGetProductsByTag_TagNotFound() {
        // Arrange
        String tagName = "Tag 1";
        when(tagRepository.findByTagName(tagName)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = rasaService.getProductsByTag(tagName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertTrue(responseBody.containsKey("errorMsg"));
    }

    @Test
    public void testGetProductsByTag_Exception() {
        // Arrange
        String tagName = "Tag 1";
        when(tagRepository.findByTagName(tagName)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getProductsByTag(tagName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testRemoveFromCart_Success() {
        // Arrange
        int cartId = 1;

        // Act
        ResponseEntity<Object> response = rasaService.removeFromCart(cartId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(cartRepository, times(1)).deleteById(cartId);
    }

    @Test
    public void testRemoveFromCart_Exception() {
        // Arrange
        int cartId = 1;
        doThrow(new RuntimeException("Database error")).when(cartRepository).deleteById(cartId);

        // Act
        ResponseEntity<Object> response = rasaService.removeFromCart(cartId);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(cartRepository, times(1)).deleteById(cartId);
    }

    @Test
    public void testAddToCart_Success() {
        // Arrange
        String userEmail = "test@example.com";
        AddToCart productData = new AddToCart();
        productData.setProductId(1);
        productData.setQuantity(2L);

        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);

        Product product = new Product();
        product.setProductId(1);
        product.setName("Product 1");
        product.setPrice(100L);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(productRepository.findByProductId(productData.getProductId())).thenReturn(product);

        // Act
        ResponseEntity<Object> response = rasaService.addToCart(userEmail, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddToCart_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        AddToCart productData = new AddToCart();
        productData.setProductId(1);
        productData.setQuantity(2L);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.addToCart(userEmail, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testAddToCart_Exception() {
        // Arrange
        String userEmail = "test@example.com";
        AddToCart productData = new AddToCart();
        productData.setProductId(1);
        productData.setQuantity(2L);

        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);

        Product product = new Product();
        product.setProductId(1);
        product.setName("Product 1");
        product.setPrice(100L);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(productRepository.findByProductId(productData.getProductId())).thenReturn(product);
        doThrow(new RuntimeException("Database error")).when(cartRepository).save(any(Cart.class));

        // Act
        ResponseEntity<Object> response = rasaService.addToCart(userEmail, productData);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testFilterProducts_Success() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg")));
        products.add(new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));

        FilterProductsRequestDTO filters = new FilterProductsRequestDTO();
        filters.setSearchString("product");
        filters.setTags(Arrays.asList("Tag 1"));
        filters.setMinPrice(110L);
        filters.setMaxPrice(140L);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.filterProducts(filters);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<Product> filteredProducts = (List<Product>) responseBody.get("filteredProducts");
        assertEquals(1, filteredProducts.size());
        assertEquals(3, filteredProducts.get(0).getProductId());
    }

    @Test
    public void testFilterProducts_NoFilters() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg")));
        products.add(new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));

        FilterProductsRequestDTO filters = new FilterProductsRequestDTO();

        when(productRepository.findAll()).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.filterProducts(filters);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
    }

    @Test
    public void testFilterProducts_NoMatchingProducts() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg")));
        products.add(new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));

        FilterProductsRequestDTO filters = new FilterProductsRequestDTO();
        filters.setSearchString("nonexistent");
        filters.setTags(Arrays.asList("Tag 3"));
        filters.setMinPrice(200L);
        filters.setMaxPrice(300L);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.filterProducts(filters);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<Product> filteredProducts = (List<Product>) responseBody.get("filteredProducts");
        assertFalse(filteredProducts.isEmpty());
    }

    @Test
    public void testFilterProducts_Exception() {
        // Arrange
        FilterProductsRequestDTO filters = new FilterProductsRequestDTO();
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.filterProducts(filters);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetCart_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);

        ProductTag tag1 = new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg");
        ProductTag tag2 = new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg");

        Product product1 = new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag1);
        Product product2 = new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag2);

        Cart cart1 = new Cart(1, user, product1, 2L, 200L);
        Cart cart2 = new Cart(2, user, product2, 3L, 450L);

        List<Cart> cartItems = Arrays.asList(cart1, cart2);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user)).thenReturn(cartItems);

        // Act
        ResponseEntity<Object> response = rasaService.getCart(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<GetCartDTO> cartDTOs = (List<GetCartDTO>) responseBody.get("cartItems");
        assertEquals(2, cartDTOs.size());
        long totalAmount = (long) responseBody.get("totalAmount");
        assertEquals(650L, totalAmount);
        // Add more assertions for cart item details if needed
    }

    @Test
    public void testGetCart_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.getCart(userEmail);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
    }

    @Test
    public void testBuyNow_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setUserId(1);
        user.setEmail(userEmail);

        ProductTag tag1 = new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg");
        ProductTag tag2 = new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg");

        Product product1 = new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag1);
        Product product2 = new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag2);

        Cart cart1 = new Cart(1, user, product1, 2L, 200L);
        Cart cart2 = new Cart(2, user, product2, 3L, 450L);

        List<Cart> cartItems = Arrays.asList(cart1, cart2);

        Inventory inventory1 = new Inventory(1, 10L, product1);
        Inventory inventory2 = new Inventory(2, 5L, product2);

        BuyNowDTO buyNowDTO = new BuyNowDTO();
        buyNowDTO.setPaymentMethod(PaymentMethod.CREDITCARD);

        Long totalAmount = 650L;

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(cartRepository.findTotalAmount(user.getUserId())).thenReturn(totalAmount);
        when(cartRepository.findByUserId(user)).thenReturn(cartItems);
        when(inventoryRepository.findByProduct(product1)).thenReturn(inventory1);
        when(inventoryRepository.findByProduct(product2)).thenReturn(inventory2);

        // Act
        ResponseEntity<Object> response = rasaService.buyNow(userEmail, buyNowDTO);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertNotNull(responseBody.get("orderId"));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
//        verify(inventoryRepository, times(2)).save(any(Inventory.class));
        verify(cartRepository, times(1)).deleteByUser(user.getUserId());
    }

    @Test
    public void testBuyNow_UserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        BuyNowDTO buyNowDTO = new BuyNowDTO();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = rasaService.buyNow(userEmail, buyNowDTO);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("User not found", responseBody.get("errorMsg"));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderItemRepository, never()).save(any(OrderItem.class));
        verify(inventoryRepository, never()).save(any(Inventory.class));
        verify(cartRepository, never()).deleteByUser(anyInt());
    }

    @Test
    public void testGetInventory_Success() {
        // Arrange
        String tagName = "Tag 1";
        ProductTag tag = new ProductTag(1, tagName, "https://example.com/tag1.jpg");

        Product product1 = new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag);
        Product product2 = new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag);

        Inventory inventory1 = new Inventory(1, 10L, product1);
        Inventory inventory2 = new Inventory(2, 5L, product2);

        List<Inventory> inventoryList = Arrays.asList(inventory1, inventory2);

        when(tagRepository.findByTagName(tagName)).thenReturn(tag);
        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        // Act
        ResponseEntity<Object> response = rasaService.getInventory(tagName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<InventoryResponseDTO> inventoryResponse = (List<InventoryResponseDTO>) responseBody.get("inventory");
        assertEquals(2, inventoryResponse.size());
        // Add more assertions for inventory item details if needed
    }

    @Test
    public void testGetInventoryByCategory_Success() {
        // Arrange
        ProductTag tag1 = new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg");
        ProductTag tag2 = new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg");

        Product product1 = new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag1);
        Product product2 = new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag2);
        Product product3 = new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, tag1);

        Inventory inventory1 = new Inventory(1, 10L, product1);
        Inventory inventory2 = new Inventory(2, 5L, product2);
        Inventory inventory3 = new Inventory(3, 8L, product3);

        List<Inventory> inventoryList = Arrays.asList(inventory1, inventory2, inventory3);

        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        // Act
        ResponseEntity<Object> response = rasaService.getInventoryByCategory();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<HashMap<String, String>> inventoryResponse = (List<HashMap<String, String>>) responseBody.get("inventory");
        assertEquals(2, inventoryResponse.size());
        Set<String> tags = (Set<String>) responseBody.get("tags");
        assertEquals(2, tags.size());
        assertTrue(tags.contains("Tag 1"));
        assertTrue(tags.contains("Tag 2"));
        // Add more assertions for inventory item details if needed
    }

    @Test
    public void testGetInventoryByCategory_Exception() {
        // Arrange
        when(inventoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getInventoryByCategory();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllProducts_Success() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg")));
        products.add(new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg")));

        when(productRepository.findAll()).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.getAllProducts();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(products, responseBody.get("products"));
        assertEquals(Arrays.asList("Tag 1", "Tag 2"), responseBody.get("tags"));
        assertEquals(100L, responseBody.get("minPrice"));
        assertEquals(150L, responseBody.get("maxPrice"));
    }

    @Test
    public void testGetAllProducts_NoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getAllProducts();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertTrue(((List<?>) responseBody.get("products")).isEmpty());
        assertTrue(((List<?>) responseBody.get("tags")).isEmpty());
        assertEquals(Long.MAX_VALUE, responseBody.get("minPrice"));
        assertEquals(Long.MIN_VALUE, responseBody.get("maxPrice"));
    }

    @Test
    public void testGetAllProducts_Exception() {
        // Arrange
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getAllProducts();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetAllProductNames_Success() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, null));
        products.add(new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, null));
        products.add(new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, null));

        when(productRepository.findAll()).thenReturn(products);

        // Act
        ResponseEntity<Object> response = rasaService.getAllProductNames();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(Arrays.asList("Product 1", "Product 2", "Product 3"), responseBody.get("productNames"));
    }

    @Test
    public void testGetAllProductNames_NoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getAllProductNames();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertTrue(((List<?>) responseBody.get("productNames")).isEmpty());
    }

    @Test
    public void testGetAllProductNames_Exception() {
        // Arrange
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getAllProductNames();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testGetYOYSales_Success() throws ParseException {
        // Arrange
        int year = 2023;
        String productName = "Product 1";

        Order order1 = new Order();
        order1.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));
        order1.setOrderStatus("FULFILLED");

        Order order2 = new Order();
        order2.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-06-01"));
        order2.setOrderStatus("FULFILLED");

        List<Order> orders = Arrays.asList(order1, order2);

        Product product = new Product();
        product.setName(productName);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrderId(order1);
        orderItem1.setProductId(product);
        orderItem1.setQuantity(5L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderId(order2);
        orderItem2.setProductId(product);
        orderItem2.setQuantity(3L);

        when(orderRepository.findByYear(year)).thenReturn(orders);
        when(orderItemRepository.findByOrderId(order1)).thenReturn(Collections.singletonList(orderItem1));
        when(orderItemRepository.findByOrderId(order2)).thenReturn(Collections.singletonList(orderItem2));

        // Act
        ResponseEntity<Object> response = rasaService.getYOYSales(year, productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        Map<Integer, Integer> yoySales = (Map<Integer, Integer>) responseBody.get("sales");
        assertEquals(5, yoySales.get(year).intValue());
        assertEquals(3, yoySales.get(year - 1).intValue());
        assertEquals(0, yoySales.get(year - 2).intValue());
        assertEquals(0, yoySales.get(year - 3).intValue());
        assertEquals(0, yoySales.get(year - 4).intValue());
        assertEquals(year, responseBody.get("year"));
        assertEquals(productName, responseBody.get("productName"));
    }

    @Test
    public void testGetYOYSales_NoOrders() {
        // Arrange
        int year = 2023;
        String productName = "Product 1";
        when(orderRepository.findByYear(year)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getYOYSales(year, productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        Map<Integer, Integer> yoySales = (Map<Integer, Integer>) responseBody.get("sales");
        assertEquals(0, yoySales.get(year).intValue());
        assertEquals(0, yoySales.get(year - 1).intValue());
        assertEquals(0, yoySales.get(year - 2).intValue());
        assertEquals(0, yoySales.get(year - 3).intValue());
        assertEquals(0, yoySales.get(year - 4).intValue());
        assertEquals(year, responseBody.get("year"));
        assertEquals(productName, responseBody.get("productName"));
    }

    @Test
    public void testGetRevenueByProduct_Success() {
        // Arrange
        String filterType = "week";
        String productName = "Product 1";

        Order order1 = new Order();
        order1.setOrderDate(new Date());
        order1.setOrderStatus("FULFILLED");

        Order order2 = new Order();
        order2.setOrderDate(new Date());
        order2.setOrderStatus("FULFILLED");

        List<Order> orders = Arrays.asList(order1, order2);

        Product product = new Product();
        product.setName(productName);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrderId(order1);
        orderItem1.setProductId(product);
        orderItem1.setQuantity(2L);
        orderItem1.setUnitPrice(100L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderId(order2);
        orderItem2.setProductId(product);
        orderItem2.setQuantity(3L);
        orderItem2.setUnitPrice(150L);

        when(orderRepository.findOrdersInLast8Weeks()).thenReturn(orders);
        when(orderItemRepository.findByOrderId(order1)).thenReturn(Collections.singletonList(orderItem1));
        when(orderItemRepository.findByOrderId(order2)).thenReturn(Collections.singletonList(orderItem2));

        // Act
        ResponseEntity<Object> response = rasaService.getRevenueByProduct(filterType, productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(900L, responseBody.get("revenue"));
        assertEquals(filterType, responseBody.get("filterType"));
        assertEquals(productName, responseBody.get("productName"));
    }

    @Test
    public void testGetRevenueByProduct_NoOrders() {
        // Arrange
        String filterType = "week";
        String productName = "Product 1";
        when(orderRepository.findOrdersInLast8Weeks()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getRevenueByProduct(filterType, productName);
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(0L, responseBody.get("revenue"));
        assertEquals(filterType, responseBody.get("filterType"));
        assertEquals(productName, responseBody.get("productName"));
    }

    @Test
    public void testGetProductsForHome_Success() {
        // Arrange
        ProductTag tag1 = new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg");
        ProductTag tag2 = new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg");

        Product product1 = new Product(1, "Product 1", "Description 1", "https://example.com/product1.jpg", 100L, tag1);
        Product product2 = new Product(2, "Product 2", "Description 2", "https://example.com/product2.jpg", 150L, tag1);
        Product product3 = new Product(3, "Product 3", "Description 3", "https://example.com/product3.jpg", 120L, tag2);
        Product product4 = new Product(4, "Product 4", "Description 4", "https://example.com/product4.jpg", 180L, tag2);

        List<ProductTag> tags = Arrays.asList(tag1, tag2);
        List<Product> products1 = Arrays.asList(product1, product2);
        List<Product> products2 = Arrays.asList(product3, product4);

        when(tagRepository.findAll()).thenReturn(tags);
        when(productRepository.findByTagId(tag1.getTagId())).thenReturn(products1);
        when(productRepository.findByTagId(tag2.getTagId())).thenReturn(products2);

        // Act
        ResponseEntity<Object> response = rasaService.getProductsForHome();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<Product> mixProducts = (List<Product>) responseBody.get("products");
        assertEquals(0, mixProducts.size());
        assertFalse(mixProducts.contains(product2));
        assertFalse(mixProducts.contains(product3));
    }

    @Test
    public void testGetProductsForHome_NoProducts() {
        // Arrange
        ProductTag tag1 = new ProductTag(1, "Tag 1", "https://example.com/tag1.jpg");
        ProductTag tag2 = new ProductTag(2, "Tag 2", "https://example.com/tag2.jpg");

        List<ProductTag> tags = Arrays.asList(tag1, tag2);
        List<Product> emptyList = Collections.emptyList();

        when(tagRepository.findAll()).thenReturn(tags);
        when(productRepository.findByTagId(tag1.getTagId())).thenReturn(emptyList);
        when(productRepository.findByTagId(tag2.getTagId())).thenReturn(emptyList);

        // Act
        ResponseEntity<Object> response = rasaService.getProductsForHome();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        List<Product> mixProducts = (List<Product>) responseBody.get("products");
        assertTrue(mixProducts.isEmpty());
    }

    @Test
    public void testGetTotalSales_Success() {
        // Arrange
        List<Order> orders = new ArrayList<>();

        Order order1 = new Order();
        order1.setOrderId(1);
        order1.setTotalAmount(100L);

        Order order2 = new Order();
        order2.setOrderId(2);
        order2.setTotalAmount(200L);

        orders.add(order1);
        orders.add(order2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrderItemId(1);
        orderItem1.setOrderId(order1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderItemId(2);
        orderItem2.setOrderId(order1);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setOrderItemId(3);
        orderItem3.setOrderId(order2);

        List<OrderItem> orderItems1 = Arrays.asList(orderItem1, orderItem2);
        List<OrderItem> orderItems2 = Collections.singletonList(orderItem3);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderItemRepository.findByOrderId(order1)).thenReturn(orderItems1);
        when(orderItemRepository.findByOrderId(order2)).thenReturn(orderItems2);

        // Act
        ResponseEntity<Object> response = rasaService.getTotalSales();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(300L, responseBody.get("totalAmount"));
        assertEquals(2L, responseBody.get("totalOrders"));
        assertEquals(3L, responseBody.get("totalProductsSold"));
    }

    @Test
    public void testGetTotalSales_NoOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = rasaService.getTotalSales();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
        assertEquals(0L, responseBody.get("totalAmount"));
        assertEquals(0L, responseBody.get("totalOrders"));
        assertEquals(0L, responseBody.get("totalProductsSold"));
    }

    @Test
    public void testGetTotalSales_Exception() {
        // Arrange
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Object> response = rasaService.getTotalSales();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(2, responseBody.get("status"));
        assertEquals("Database error", responseBody.get("errorMsg"));
    }

    @Test
    public void testCreateDummyData_Success() {
        // Act
        ResponseEntity<Object> response = rasaService.createDummyData();
        Map<Object, Object> responseBody = (Map<Object, Object>) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBody.get("status"));
    }

}