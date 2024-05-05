package com.enpm.rasa.controller;

import com.enpm.rasa.dto.*;
import com.enpm.rasa.services.RasaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@CrossOrigin
public class RasaController {

    @Autowired
    private RasaService rasaService;

    @GetMapping("/payments/getCardDetails")
    public ResponseEntity<Object> getCardDetails(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        return rasaService.getCardDetails(user);
    }

//    @PostMapping("/payments/editCardDetails")
//    public ResponseEntity<Object> editCardDetails(HttpServletRequest request, @RequestBody CardDetailsRequestDTO cardDetails){
//        String user = request.getUserPrincipal().getName();
//        return rasaService.editCardDetails(user, cardDetails);
//    }

    @PostMapping("/payments/addCardDetails")
    public ResponseEntity<Object> addCardDetails(HttpServletRequest request, @RequestBody CardDetailsRequestDTO cardDetails){
        String user = request.getUserPrincipal().getName();
        return rasaService.addCardDetails(user, cardDetails);
    }

    @PostMapping("/wishlist/add/{productId}")
    public ResponseEntity<Object> addProductToWishlist(HttpServletRequest request, @PathVariable int productId){
        String userEmail = request.getUserPrincipal().getName();
        return rasaService.addProductToWishlist(userEmail, productId);
    }

    @DeleteMapping("/wishlist/remove/{wishlistId}")
    public ResponseEntity<Object> removeProductFromWishlist(HttpServletRequest request, @PathVariable int wishlistId){
        String userEmail = request.getUserPrincipal().getName();
        return rasaService.removeProductFromWishlist(userEmail, wishlistId);
    }

    @GetMapping("/wishlist/check/{productId}")
    public ResponseEntity<Object> checkIfProductIsInWishlist(HttpServletRequest request, @PathVariable int productId){
        String userEmail = request.getUserPrincipal().getName();
        return rasaService.checkIfProductIsInWishlist(userEmail, productId);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<Object> getWishList(HttpServletRequest request){
        String userEmail = request.getUserPrincipal().getName();
        return rasaService.getWishlist(userEmail);
    }

    // Called when clicking on a product
    @GetMapping("/products/getProduct/{productId}")
    public ResponseEntity<Object> getProductById(@PathVariable int productId){
        return rasaService.getProductById(productId);
    }

    @GetMapping("/products/searchProducts/{productName}")
    public ResponseEntity<Object> getProductsByName(@PathVariable String productName){
        return rasaService.getProductsByName(productName);
    }

    @GetMapping("/products/getProductsByTag/{tagName}")
    public ResponseEntity<Object> getProductsByTag(@PathVariable String tagName){
        return rasaService.getProductsByTag(tagName);
    }

    @PostMapping("/products/filter")
    public ResponseEntity<Object> filterProducts(@RequestBody FilterProductsRequestDTO filters){
        return rasaService.filterProducts(filters);
    }

    @PostMapping("/products/addTag")
    public ResponseEntity<Object> addTag(HttpServletRequest request, @RequestBody AddTag tagData){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.addTag(tagData);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Error! Please try again"));
        }
    }

    @GetMapping("/products/tags")
    public ResponseEntity<Object> getTags(HttpServletRequest request){
        return rasaService.getTags();
    }
    @GetMapping("/products/getcart")
    public ResponseEntity<Object> getcart(HttpServletRequest request) {
        String user = request.getUserPrincipal().getName();
        return rasaService.getCart(user);
    }

    @PostMapping("/products/addToCart")
    public ResponseEntity<Object> addToCart(HttpServletRequest request, @RequestBody AddToCart productData){
        String user = request.getUserPrincipal().getName();
        return rasaService.addToCart(user, productData);
    }

    @DeleteMapping("/products/removefromcart/{cartid}")
    public ResponseEntity<Object> removeFromCart(HttpServletRequest request, @PathVariable int cartid){
        String user = request.getUserPrincipal().getName();
        return rasaService.removeFromCart(cartid);
    }

    @PostMapping("/products/buyNow")
    public ResponseEntity<Object> buyNow(HttpServletRequest request, @RequestBody BuyNowDTO buyNowDTO){
        String user = request.getUserPrincipal().getName();
        return rasaService.buyNow(user, buyNowDTO);
    }

    @PostMapping("/users/editUser/")
    public ResponseEntity<Object> editUser(HttpServletRequest request, @RequestBody EditUserRequestDTO userDetails){
        String user = request.getUserPrincipal().getName();
        return rasaService.editUser(user, userDetails);
    }

    @GetMapping("/orders/getOrderHistory")
    public ResponseEntity<Object> getOrderHistory(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        return rasaService.getUserOrderHistory(user);
    }

    @PostMapping("/orders/getUserOrderHistory")
    public ResponseEntity<Object> getUserOrderHistory(HttpServletRequest request, @RequestBody  UserOrderHistoryDTO userOrderHistoryDTO){
        return rasaService.getUserOrderHistory(userOrderHistoryDTO.getEmail());
    }

    @GetMapping("/orders/getOrderItems/{orderId}")
    public ResponseEntity<Object> getOrderHistory(@PathVariable int orderId){
        return rasaService.getOrderItems(orderId);
    }

    // Admin functionalities

    // Add product
    @PostMapping("/products/addProduct")
    public ResponseEntity<Object> addProduct(HttpServletRequest request, @RequestBody AddProduct productData){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.addProduct(productData);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can add product!"));
        }
    }

    // Delete product
    @DeleteMapping("/products/deleteProduct/{productId}")
    public ResponseEntity<Object> deleteProduct(HttpServletRequest request, @PathVariable int productId){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.deleteProduct(productId);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can delete product!"));
        }
    }

    // Edit product
    @PostMapping("/products/editProduct/{productId}")
    public ResponseEntity<Object> editProduct(HttpServletRequest request, @PathVariable int productId, @RequestBody AddProduct productData){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.editProduct(productId, productData);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can edit product!"));
        }
    }

    @GetMapping("/products/all")
    public ResponseEntity<Object> getAllProducts(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getAllProducts();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access!"));
        }
    }

    // Get all orders
    @GetMapping("/orders/getAllOrders")
    public ResponseEntity<Object> getAllOrders(HttpServletRequest request) {
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getAllOrders();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can see all orders!"));
        }
    }

    // Change the order status
    @PostMapping("/orders/changeStatus/{orderId}")
    public ResponseEntity<Object> changeOrderStatus(HttpServletRequest request, @PathVariable int orderId, @RequestBody ChangeOrderStatusRequest orderStatus) {
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.changeOrderStatus(orderId, orderStatus);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can change order status!"));
        }
    }

    // Delete User
    @DeleteMapping("/users/deleteUser/{userId}")
    public ResponseEntity<Object> deleteUser(HttpServletRequest request, @PathVariable int userId){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.deleteUser(userId);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can delete user!"));
        }
    }

    // Get all users
    @GetMapping("/users/all")
    public ResponseEntity<Object> getAllUsers(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getAllUsers();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can delete user!"));
        }
    }

    @PostMapping("/inventory")
    public ResponseEntity<Object> getInventory(HttpServletRequest request, @RequestBody  InventoryDTO inventoryDTO){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getInventory(inventoryDTO.getTagName());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access inventory!"));
        }
    }

    @PostMapping("/inventory/category")
    public ResponseEntity<Object> getInventoryByCategory(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getInventoryByCategory();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access inventory!"));
        }
    }

    @GetMapping("/inventory/getSales")
    public ResponseEntity<Object> getTotalSales(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getTotalSales();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access inventory!"));
        }
    }

    @GetMapping("/products/names")
    public ResponseEntity<Object> getAllProductNames(HttpServletRequest request){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getAllProductNames();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access!"));
        }
    }

    @GetMapping("/sales/yoySales")
    public ResponseEntity<Object> getYOYSales(HttpServletRequest request, @RequestBody YOYSalesRequestDTO salesRequestData){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getYOYSales(salesRequestData.getYear(), salesRequestData.getProductName());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access!"));
        }
    }

    @GetMapping("/sales/getRevenue/{filterType}/{productName}")
    public ResponseEntity<Object> getRevenueByFilterType(HttpServletRequest request, @PathVariable String filterType, @PathVariable String productName){
        String user = request.getUserPrincipal().getName();
        if(user.equals("admin@gmail.com")){
            return rasaService.getRevenueByProduct(filterType, productName);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 2, "errorMsg", "Only admin can access!"));
        }
    }

    @GetMapping("/getproducts/home")
    public ResponseEntity<Object> getProductsForHome(){
        return rasaService.getProductsForHome();
    }

    @GetMapping("")
    public String rasaTest(){
        return "Welcome to Rasa";
    }


}
