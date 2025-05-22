package com.oss.ossv1.config;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class CartDataInitializer {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final Random random = new Random();

    @PostConstruct
    public void seedCarts() {
        if (cartRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            List<Product> products = productRepository.findAll();

            for (int i = 0; i < users.size(); i++) {
                Cart cart = new Cart();
                cart.setUser(users.get(i));

                // Add 2 random items
                List<CartItem> items = List.of(
                        createItem(cart, products.get(random.nextInt(products.size())), random.nextInt(2) + 1),
                        createItem(cart, products.get(random.nextInt(products.size())), random.nextInt(2) + 1)
                );
                cart.setItems(items);

                cartRepository.save(cart);
            }

            System.out.println(" Seeded carts for users.");
        } else {
            System.out.println(" Carts already exist, skipping.");
        }
    }

    private CartItem createItem(Cart cart, Product product, int quantity) {
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }
}
