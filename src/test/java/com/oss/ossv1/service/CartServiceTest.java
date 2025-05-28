package com.oss.ossv1.service;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.oss.ossv1.data.entity.Cart;
import com.oss.ossv1.data.entity.CartItem;
import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.CartRepository;
import com.oss.ossv1.data.repository.ProductRepository;

public class CartServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrCreateCart_createsNewCart() {
        Integer userId = 1;
        when(cartRepository.findByUserId(userId)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart cart = cartService.getOrCreateCart(userId);

        assertNotNull(cart);
        assertEquals(userId, cart.getUser().getId());
    }

    @Test
    void testGetOrCreateCart_existingCartReturned() {
        Integer userId = 2;
        Cart existingCart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("test");
        user.setEmail("test@email.com");
        user.setPasswordHash("hashed");
        existingCart.setUser(user);
        existingCart.setItems(new ArrayList<>());
        when(cartRepository.findByUserId(userId)).thenReturn(existingCart);

        Cart cart = cartService.getOrCreateCart(userId);

        assertNotNull(cart);
        assertEquals(userId, cart.getUser().getId());
    }

    @Test
    void testAddToCart_NewProduct_Success() {
        Integer userId = 3;
        Integer productId = 101;
        Product product = new Product() {
            @Override public String displayInfo() { return "info"; }
        };
        product.setId(productId);
        product.setPrice(10.0);

        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("name");
        user.setEmail("email");
        user.setPasswordHash("hash");
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.addToCart(userId, productId, 2);
        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    void testAddToCart_ExistingProduct_IncreaseQuantity() {
        Integer userId = 4;
        Integer productId = 202;
        Product product = new Product() {
            @Override public String displayInfo() { return "info"; }
        };
        product.setId(productId);
        product.setPrice(20.0);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);

        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("mail");
        user.setPasswordHash("pass");
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        item.setCart(cart);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.addToCart(userId, productId, 2);
        assertEquals(3, result.getItems().get(0).getQuantity());
    }

    @Test
    void testRemoveFromCart_Success() {
        Integer userId = 5;
        Integer productId = 300;
        Product product = new Product() {
            @Override public String displayInfo() { return "info"; }
        };
        product.setId(productId);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);

        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("mail");
        user.setPasswordHash("pass");
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        item.setCart(cart);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.removeFromCart(userId, productId);
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void testUpdateCartItemQuantity_Success() {
        Integer userId = 6;
        Integer productId = 400;
        Product product = new Product() {
            @Override public String displayInfo() { return "info"; }
        };
        product.setId(productId);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);

        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("mail");
        user.setPasswordHash("pass");
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        item.setCart(cart);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.updateCartItemQuantity(userId, productId, 5);
        assertEquals(5, result.getItems().get(0).getQuantity());
    }

    @Test
    void testClearCart_Success() {
        Integer userId = 7;
        Product product = new Product() {
            @Override public String displayInfo() { return "info"; }
        };
        product.setId(500);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);

        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("mail");
        user.setPasswordHash("pass");
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        item.setCart(cart);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);

        cartService.clearCart(userId);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetCartTotal_Success() {
        Integer userId = 8;
        Product product = new Product() {
            @Override public String displayInfo() { return "info"; }
        };
        product.setId(600);
        product.setPrice(25.0);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("mail");
        user.setPasswordHash("pass");
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        item.setCart(cart);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);

        double total = cartService.getCartTotal(userId);
        assertEquals(50.0, total);
    }
}
