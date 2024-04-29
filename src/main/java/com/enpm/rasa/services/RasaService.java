package com.enpm.rasa.services;

import com.enpm.rasa.dto.*;
import com.enpm.rasa.model.*;
import com.enpm.rasa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RasaService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> getCardDetails(String userEmail){
        try{
            User userObj = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new Exception("User not found"));

            List<CardDetails>  cardDetails = cardRepository.findByUser(userObj.getUserId());
            return ResponseEntity.ok().body(Map.of("status", 1, "cardDetails", cardDetails!=null?cardDetails:new HashMap<>()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

//    public ResponseEntity<Object> editCardDetails(String userEmail, CardDetailsRequestDTO cardDetailsRequestDTO){
//        try {
//            User user = userRepository.findByEmail(userEmail)
//                    .orElseThrow(() -> new Exception("User not found!"));
//
//            List<CardDetails> cardDetails = cardRepository.findByUserId(user.getUserId());
//
//            cardDetails.setName(cardDetailsRequestDTO.getName());
//            cardDetailsRequestDTO.setPaymentId(cardDetailsRequestDTO.getPaymentId());
//            cardDetailsRequestDTO.setPaymentMethod(cardDetailsRequestDTO.getPaymentMethod());
//
//            cardRepository.save(cardDetails);
//
//            return ResponseEntity.ok().body(Map.of("status", 1));
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
//        }
//    }

    public ResponseEntity<Object> addProductToWishlist(String userEmail, int productId){
        try{
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new Exception("User not found!"));

            Product product = productRepository.findByProductId(productId);

            Wishlist wishlist = Wishlist.builder()
                    .productId(product)
                    .userId(user)
                    .build();

            wishListRepository.save(wishlist);

            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> removeProductFromWishlist(String userEmail, int wishlistId){
        try{
            wishListRepository.deleteById(wishlistId);
            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> checkIfProductIsInWishlist(String userEmail, int productId){
        try{
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new Exception("User not found!"));

            Product product = productRepository.findByProductId(productId);

            boolean isPresent = Boolean.FALSE;

            List<Wishlist> wishlists = wishListRepository.findByUserId(user);

            for(Wishlist wishlist:wishlists){
                if(wishlist.getProductId().getProductId() == productId){
                    isPresent = Boolean.TRUE;
                    break;
                }
            }

            return ResponseEntity.ok().body(Map.of("status", 1, "isPresent", isPresent));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> addCardDetails(String userEmail, CardDetailsRequestDTO newCardDetails){
        try{
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new Exception("User not found!"));

            CardDetails cardDetails = CardDetails.builder()
                    .paymentId(newCardDetails.getPaymentId())
                    .paymentMethod(newCardDetails.getPaymentMethod())
                    .userId(user)
                    .name(newCardDetails.getName())
                    .build();

            cardRepository.save(cardDetails);


            return ResponseEntity.ok().body(Map.of("status", 1, "card", cardDetails));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> addTag(AddTag tagData){
        ProductTag tag = ProductTag.builder()
                .tagName(tagData.getName())
                .imageUrl(tagData.getImageURL())
                .build();
        tagRepository.save(tag);

        return ResponseEntity.ok().body(Map.of("status", 1, "tag", tag));
    }

    public ResponseEntity<Object> getTags(){
        try{
            List<ProductTag> tags = tagRepository.findAll();
            List<List<String>> tagNames = new ArrayList<>();
            for(ProductTag tag:tags){
                List<String> tagData = new ArrayList<>();
                tagData.add(tag.getTagName());
                tagData.add(tag.getImageUrl());
                tagNames.add(tagData);
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "tags", tagNames));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getWishlist(String userEmail){
        try{
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new Exception("User not found!"));

            List<Wishlist> wishlists = wishListRepository.findAll();

            List<WishListDTO> wishlist = new ArrayList<>();
            for(Wishlist wishlist1:wishlists){
                WishListDTO wishListDTO = WishListDTO.builder()
                        .productId(wishlist1.getProductId().getProductId())
                        .productName(wishlist1.getProductId().getName())
                        .productImage(wishlist1.getProductId().getImageUrl())
                        .wishlistId(wishlist1.getWishlistId())
                        .build();
                wishlist.add(wishListDTO);
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "wishlist", wishlist));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    // Admin Services
    public ResponseEntity<Object> addProduct(AddProduct productData){
        try{
            ProductTag tag = tagRepository.findByTagName(productData.getTagName());

            Product product = Product.builder()
                    .name(productData.getName())
                    .description(productData.getDescription())
                    .price(productData.getPrice())
                    .tag(tag)
                    .imageUrl(productData.getImageUrl() != null? productData.getImageUrl() : "https://totalcomp.com/images/no-image.jpeg")
                    .build();
            productRepository.save(product);

            Inventory inventory = Inventory.builder()
                    .product(product)
                    .quantityLeft(productData.getQuantity())
                    .build();

            inventoryRepository.save(inventory);


            return ResponseEntity.ok().body(Map.of("status", 1));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> deleteProduct(int productId) {
        try {
            productRepository.deleteById(productId);
            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> editProduct(int productId, AddProduct productData){
        try{
            Product product = productRepository.findByProductId(productId);
            ProductTag tag = tagRepository.findByTagName(productData.getTagName());

            product.setDescription(productData.getDescription());
            product.setPrice(productData.getPrice());
            product.setName(productData.getName());
            product.setTag(tag);
            productRepository.save(product);

            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getAllOrders(){
        try{
            List<Order> allOrders = orderRepository.findAll();
            List<OrderResponseDTO> pendingOrders = new ArrayList<>();
            List<OrderResponseDTO> fulfilledOrders = new ArrayList<>();
            List<OrderResponseDTO> cancelledOrders = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            for (Order order : allOrders) {
                if ("PENDING".equals(order.getOrderStatus())) {
                    User user = order.getUserId();
                    OrderResponseDTO dto = new OrderResponseDTO();
                    dto.setOrderId(order.getOrderId());
                    dto.setUserId(user.getUserId());
                    dto.setFullName(user.getFullName());
                    dto.setOrderDate(sdf.format(order.getOrderDate()));
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setPaymentMethod(order.getPaymentMethod());
                    dto.setOrderStatus(order.getOrderStatus());
                    pendingOrders.add(dto);
                } else if("FULFILLED".equals(order.getOrderStatus())){
                    User user = order.getUserId();
                    OrderResponseDTO dto = new OrderResponseDTO();
                    dto.setOrderId(order.getOrderId());
                    dto.setUserId(user.getUserId());
                    dto.setFullName(user.getFullName());
                    dto.setOrderDate(sdf.format(order.getOrderDate()));
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setPaymentMethod(order.getPaymentMethod());
                    dto.setOrderStatus(order.getOrderStatus());
                    fulfilledOrders.add(dto);
                } else{
                    User user = order.getUserId();
                    OrderResponseDTO dto = new OrderResponseDTO();
                    dto.setOrderId(order.getOrderId());
                    dto.setUserId(user.getUserId());
                    dto.setFullName(user.getFullName());
                    dto.setOrderDate(sdf.format(order.getOrderDate()));
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setPaymentMethod(order.getPaymentMethod());
                    dto.setOrderStatus(order.getOrderStatus());
                    cancelledOrders.add(dto);
                }
            }

            return ResponseEntity.ok().body(Map.of("status", 1, "pendingorders", pendingOrders, "fulfilledorders", fulfilledOrders,
                    "cancelledOrders", cancelledOrders));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> changeOrderStatus(int orderId, ChangeOrderStatusRequest orderStatus){
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new Exception("Order not found"));

            order.setOrderStatus(orderStatus.getOrderStatus());
            orderRepository.save(order);
            return ResponseEntity.ok().body(Map.of("status", 1));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> deleteUser(int userId){
        try{
            userRepository.deleteById(userId);
            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> editUser(String user, EditUserRequestDTO editUser){
        try{
            User userObj = userRepository.findByEmail(user)
                    .orElseThrow(() -> new Exception("User not found"));

            userObj.setFirstName(editUser.getFirstName());
            userObj.setLastName(editUser.getLastName());

            userRepository.save(userObj);
            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getUserOrderHistory(String user){
        try{
            User userObj = userRepository.findByEmail(user)
                    .orElseThrow(() -> new Exception("User not found"));

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            List<Order> orders = orderRepository.findAll();
            List<OrderResponseDTO> pendingOrders = new ArrayList<>();
            List<OrderResponseDTO> fulfilledOrders = new ArrayList<>();
            for(Order order:orders){
                if(userObj.getUserId() == order.getUserId().getUserId()){
                    if(Objects.equals(order.getOrderStatus(), "PENDING")){
                        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                                .orderDate(sdf.format(order.getOrderDate()))
                                .orderStatus(order.getOrderStatus())
                                .paymentMethod(order.getPaymentMethod())
                                .totalAmount(order.getTotalAmount())
                                .orderId(order.getOrderId())
                                .userId(userObj.getUserId())
                                .fullName(userObj.getFullName())
                                .build();
                        pendingOrders.add(orderResponseDTO);
                    }else{
                        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                                .orderDate(sdf.format(order.getOrderDate()))
                                .orderStatus(order.getOrderStatus())
                                .paymentMethod(order.getPaymentMethod())
                                .totalAmount(order.getTotalAmount())
                                .orderId(order.getOrderId())
                                .userId(userObj.getUserId())
                                .fullName(userObj.getFullName())
                                .build();
                        fulfilledOrders.add(orderResponseDTO);
                    }

                }
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "pendingorders", pendingOrders, "fulfilledorders", fulfilledOrders));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getOrderItems(int orderId){
        try{
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new Exception("Order not found"));

            List<OrderItem> orderItems = orderItemRepository.findAll();

            List<OrderItemResponseDTO> orderedItems = new ArrayList<>();

            for(OrderItem orderItem:orderItems){
                if(orderItem.getOrderId() == order){
                    OrderItemResponseDTO orderedItem = OrderItemResponseDTO.builder()
                            .orderItemId(orderItem.getOrderItemId())
                            .orderId(order.getOrderId())
                            .product(orderItem.getProductId())
                            .quantity(orderItem.getQuantity())
                            .unitPrice(orderItem.getUnitPrice())
                            .build();
                    orderedItems.add(orderedItem);
                }
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "orderItems", orderedItems));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getAllUsers(){
        try{
            List<User> users = userRepository.findAll();
            List<UsersResponseDTO> users_list = new ArrayList<>();

            for(User user : users){
                UsersResponseDTO dto = UsersResponseDTO.builder()
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .userId(user.getUserId())
                        .build();
                users_list.add(dto);
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "users", users_list));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getProductById(int productId){
        try{
            Product product = productRepository.findByProductId(productId);
            return ResponseEntity.ok().body(Map.of("status", 1, "product", product));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getProductsByName(String productName){
        try{

            List<Product> products = productRepository.findByProductName(productName);
            Long minPrice = Long.MAX_VALUE;
            Long maxPrice = Long.MIN_VALUE;
            List<String> tags = new ArrayList<>();
            for(Product product:products){
                String tagName = product.getTag().getTagName();
                if(!tags.contains(tagName)) tags.add(tagName);
                minPrice = Math.min(minPrice, product.getPrice());
                maxPrice = Math.max(maxPrice, product.getPrice());
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "products", products, "tags", tags, "minPrice", minPrice, "maxPrice", maxPrice));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getProductsByTag(String tagName){
        try{
            ProductTag tag = tagRepository.findByTagName(tagName);
            List<Product> products = productRepository.findByTagId(tag.getTagId());
            return ResponseEntity.ok().body(Map.of("status", 1, "products", products));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> filterProducts(FilterProductsRequestDTO filters){
        try{
            List<Product> products = productRepository.findAll();
            List<Product> filteredProducts = new ArrayList<>();
            List<String> filteredTags = filters.getTags();
            if(!Objects.equals(filters.getSearchString(), "")){
                for(Product product:products){
                    if(product.getName().contains(filters.getSearchString())){
                        filteredProducts.add(product);
                    }
                }
            }

            if(filteredTags.size() >= 1 && filteredProducts.size() >= 1){
                List<Product> nameFilteredProducts = new ArrayList<>(filteredProducts);
                for(Product product:nameFilteredProducts){
                    String tag = product.getTag().getTagName();
                    if(!filteredTags.contains(tag)){
                        filteredProducts.remove(product);
                    }
                }
            }else if(filteredTags.size() >= 1){
                for(Product product:products){
                    String tag = product.getTag().getTagName();
                    if(filteredTags.contains(tag)){
                        filteredProducts.add(product);
                    }
                }
            }



            if(filters.getMinPrice() != -1 && filteredProducts.size() >= 1){
                List<Product> tagFilteredProducts = new ArrayList<>(filteredProducts);
                for(Product product:tagFilteredProducts){
                    Long price = product.getPrice();
                    if(price < filters.getMinPrice()){
                        filteredProducts.remove(product);
                    }
                }
            } else if(filters.getMinPrice() != -1){
                for(Product product:products){
                    Long price = product.getPrice();
                    if(price >= filters.getMinPrice()){
                        filteredProducts.add(product);
                    }
                }
            }

            if(filters.getMaxPrice() != -1 && filteredProducts.size() >= 1){
                List<Product> minPriceFilteredProducts = new ArrayList<>(filteredProducts);
                for(Product product:minPriceFilteredProducts){
                    Long price = product.getPrice();
                    if(price > filters.getMaxPrice()){
                        filteredProducts.remove(product);
                    }
                }
            } else if(filters.getMaxPrice() != -1){
                for(Product product:products){
                    Long price = product.getPrice();
                    if(price <= filters.getMaxPrice()){
                        filteredProducts.add(product);
                    }
                }
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "filteredProducts", filteredProducts));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> removeFromCart(int cartId){
        try{
            cartRepository.deleteById(cartId);
            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> addToCart(String userEmail, AddToCart productData){
        try{
            Product product = productRepository.findByProductId(productData.getProductId());
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Long amount = productData.getQuantity() * product.getPrice();

            Cart cart = Cart.builder()
                    .productId(product)
                    .userId(user)
                    .amount(amount)
                    .quantity(productData.getQuantity())
                    .build();

            cartRepository.save(cart);

            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }

    }

    public ResponseEntity<Object> getCart(String userEmail){
        try{
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<Cart> cartItems = cartRepository.findByUserId(user);

            List<GetCartDTO> cart = new ArrayList<>();

            long totalAmount = 0;

            for(Cart cartItem:cartItems){
                GetCartDTO getCart = GetCartDTO.builder()
                        .amount(cartItem.getAmount())
                        .quantity(cartItem.getQuantity())
                        .productName(cartItem.getProductId().getName())
                        .productId(cartItem.getProductId().getProductId())
                        .productImage(cartItem.getProductId().getImageUrl())
                        .tagName(cartItem.getProductId().getTag().getTagName())
                        .cartId(cartItem.getCartId())
                        .build();
                totalAmount += cartItem.getAmount();
                cart.add(getCart);
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "cartItems", cart, "totalAmount", totalAmount));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> buyNow(String userEmail, BuyNowDTO buyNowDTO){
        try{
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Long amount = cartRepository.findTotalAmount(user.getUserId());

            Order order = Order.builder()
                    .userId(user)
                    .totalAmount(amount)
                    .paymentMethod(buyNowDTO.getPaymentMethod())
                    .orderDate(new Date())
                    .orderStatus("PENDING")
                    .build();

            orderRepository.save(order);

            List<Cart> cartItems = cartRepository.findByUserId(user);

            int i = 0;

            while(i < cartItems.size()){
                Cart cart = cartItems.get(i);

                OrderItem orderItem = OrderItem.builder()
                        .orderId(order)
                        .productId(cart.getProductId())
                        .quantity(cart.getQuantity())
                        .unitPrice(cart.getAmount()/cart.getQuantity())
                        .build();

                orderItemRepository.save(orderItem);

                Inventory productInventory = inventoryRepository.findByProduct(cart.getProductId());
                long quantity_left = productInventory.getQuantityLeft() - cart.getQuantity();
                if(quantity_left < 0) quantity_left = 0L;
                productInventory.setQuantityLeft(quantity_left);
                i++;
            }

            cartRepository.deleteByUser(user.getUserId());

            return ResponseEntity.ok().body(Map.of("status", 1, "orderId", order.getOrderId()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }

    }

    public ResponseEntity<Object> getInventory(String tagName){
        try{
            ProductTag tag = tagRepository.findByTagName(tagName);
            List<Inventory> inventory = inventoryRepository.findAll();

            List<InventoryResponseDTO> inventoryResponse = new ArrayList<>();

            for(Inventory stock:inventory){
                if(stock.getProduct().getTag() == tag){
                    InventoryResponseDTO inventoryResponseDTO = InventoryResponseDTO.builder()
                            .productName(stock.getProduct().getName().substring(0, 3) + "...")
                            .quantityLeft(stock.getQuantityLeft())
                            .label(stock.getProduct().getName() + ", (" + stock.getQuantityLeft() + ")")
                            .build();
                    inventoryResponse.add(inventoryResponseDTO);
                }
            }

            return ResponseEntity.ok().body(Map.of("status", 1, "inventory", inventoryResponse));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getInventoryByCategory(){
        try{
            List<Inventory> inventory = inventoryRepository.findAll();

            HashMap<String, Long> inventoryStock = new HashMap<>();

            HashSet<String> tagNames = new HashSet<>();

            for(Inventory stock:inventory){
                String tagName = stock.getProduct().getTag().getTagName();
                if(tagName != null) {
                    tagNames.add(tagName);
                    if (inventoryStock.containsKey(tagName)) {
                        inventoryStock.put(tagName, (inventoryStock.get(tagName) + stock.getQuantityLeft()));
                    } else {
                        inventoryStock.put(tagName, stock.getQuantityLeft());
                    }
                }
            }

            List<HashMap<String, String>> inventoryResponse = new ArrayList<>();

            inventoryStock.forEach((key, value) -> {
                HashMap<String, String> inventoryItem = new HashMap<>();
                inventoryItem.put("category", key.substring(0, 3) + "...");
                inventoryItem.put("quantityLeft", String.valueOf(value));
                inventoryItem.put("label", key + ", (" + value + ")");
                inventoryResponse.add(inventoryItem);
            });


            return ResponseEntity.ok().body(Map.of("status", 1, "inventory", inventoryResponse, "tags", tagNames));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getAllProducts(){
        try{
            List<Product> products = productRepository.findAll();
            Long minPrice = Long.MAX_VALUE;
            Long maxPrice = Long.MIN_VALUE;
            List<String> tags = new ArrayList<>();
            for(Product product:products){
                String tagName = product.getTag().getTagName();
                if(!tags.contains(tagName)) tags.add(tagName);
                minPrice = Math.min(minPrice, product.getPrice());
                maxPrice = Math.max(maxPrice, product.getPrice());
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "products", products, "tags", tags, "minPrice", minPrice, "maxPrice", maxPrice));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getAllProductNames(){
        try{
            List<Product> products = productRepository.findAll();

            List<String> productNames = new ArrayList<>();

            for(Product product:products){
                productNames.add(product.getName());
            }

            return ResponseEntity.ok().body(Map.of("status", 1, "productNames", productNames));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getYOYSales(int year, String productName){
        try{
            List<Order> orders = orderRepository.findByYear(year);
//            long sales = 0L;
            HashMap<Integer, Integer> yoySales = new HashMap<>();
            yoySales.put(year, 0);
            yoySales.put(year-1, 0);
            yoySales.put(year-2, 0);
            yoySales.put(year-3, 0);
            yoySales.put(year-4, 0);

            SimpleDateFormat orderYearFormat = new SimpleDateFormat("yyyy");

            for(Order order:orders){
                Integer orderYear = Integer.valueOf(orderYearFormat.format(order.getOrderDate()));
                if(order.getOrderStatus().equals("FULFILLED") && yoySales.containsKey(orderYear)){
                    List<OrderItem> orderItems = orderItemRepository.findByOrderId(order);
                    for(OrderItem orderItem:orderItems){
                        if(orderItem.getProductId().getName().equals(productName)){
                            int sales = yoySales.get(orderYear);
                            sales += orderItem.getQuantity();
                            yoySales.put(orderYear, sales);
                        }
                    }
                }
            }

            return ResponseEntity.ok().body(Map.of("status", 1, "sales", yoySales, "year", year, "productName", productName));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getRevenueByProduct(String filterType, String productName){
        try{
            long revenue = 0L;
            List<Order> orders = new ArrayList<>();
            if(filterType.equals("day")) orders = orderRepository.findOrdersInLast30Days();
            if(filterType.equals("week")) orders = orderRepository.findOrdersInLast8Weeks();
            if(filterType.equals("month")) orders = orderRepository.findOrdersInLast6Months();
            if(filterType.equals("year")) orders = orderRepository.findOrdersInLast5Years();
            for (Order order : orders) {
                List<OrderItem> orderItems = orderItemRepository.findByOrderId(order);
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getProductId().getName().equals(productName)) {
                        revenue += orderItem.getQuantity() * orderItem.getUnitPrice();
                    }
                }
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "revenue", revenue, "filterType", filterType, "productName", productName));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getProductsForHome(){
        try{
            List<ProductTag> tags = tagRepository.findAll();
            List<Product> mixProducts = new ArrayList<>();
            for(ProductTag tag:tags){
                List<Product> products = productRepository.findByTagId(tag.getTagId());
                if(products.size() >= 3){
                    Collections.shuffle(products);
                    int i = 1;
                    while(i < 3){
                        mixProducts.add(products.get(i));
                        i++;
                    }
                }
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "products", mixProducts));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getTotalSales(){
        try{
            List<Order> orders = orderRepository.findAll();
            Long totalAmount = 0L;
            Long totalOrders = 0L;
            Long totalProductsSold = 0L;
            for(Order order:orders){
                totalAmount += order.getTotalAmount();
                totalOrders += 1;
                totalProductsSold += orderItemRepository.findByOrderId(order).size();
            }
            return ResponseEntity.ok().body(Map.of("status", 1, "totalAmount", totalAmount, "totalOrders", totalOrders, "totalProductsSold", totalProductsSold));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }


    public ResponseEntity<Object> createDummyData(){
        try{


            return ResponseEntity.ok().body(Map.of("status", 1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", e.getMessage()));
        }
    }

    private void generateUsers(){
        for (int i = 0; i < 20; i++) {
            String firstName = DummyData.getRandomFirstName();
            String lastName = DummyData.getRandomLastName();
            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(firstName + lastName + "@gmail.com")
                    .password(passwordEncoder.encode("abc123"))
                    .role(Role.CUSTOMER)
                    .build();

            User userObj = userRepository.save(user);
        }
    }

    private void generateProducts(){

    }


}
