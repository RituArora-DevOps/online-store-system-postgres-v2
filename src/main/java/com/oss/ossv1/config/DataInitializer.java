package com.oss.ossv1.config;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;
    private final Random random = new Random();

    private static final String[] clothingNames = {
            "T-Shirt V-Neck", "T-Shirt Round Neck", "Jeans Skinny",
            "Jeans Regular", "Hoodie", "Jacket", "Sweater"
    };
    private static final String[] sizes = {"S", "M", "L", "XL"};
    private static final String[] colors = {"Red", "Blue", "Green", "Black", "White"};

    private static final String[] electronicItems = {
            "Smartphone", "Laptop", "Bluetooth Speaker", "Monitor",
            "Headphones", "Smartwatch"
    };
    private static final String[] brands = {"AlphaTech", "NextGen", "CyberCore", "TechNova", "Pulse"};

    private static final String[] groceryItems = {
            "Milk", "Bread", "Eggs", "Bananas", "Tomatoes", "Onions", "Cheese", "Butter"
    };

    @PostConstruct
    public void seedData() {
        if (productRepository.count() > 0) return;

        for (int i = 0; i < 100; i++) {
            // Clothing
            String base = clothingNames[random.nextInt(clothingNames.length)];
            String size = sizes[random.nextInt(sizes.length)];
            String color = colors[random.nextInt(colors.length)];
            Clothing clothing = new Clothing();
            clothing.setName(base + " - " + color + " - Size " + size);
            clothing.setDescription("A comfortable " + base.toLowerCase() + " in " + color + ", size " + size);
            clothing.setPrice(round(15.0 + random.nextDouble() * 60.0));
            clothing.setCategory("clothing");
            clothing.setSize(size);
            clothing.setColor(color);
            productRepository.save(clothing);

            // Electronics
            String item = electronicItems[random.nextInt(electronicItems.length)];
            String model = brands[random.nextInt(brands.length)] + " " + (100 + random.nextInt(900)) + "X";
            Electronics electronics = new Electronics();
            electronics.setName(item + " - " + model);
            electronics.setDescription(item + " model " + model + " with latest features.");
            electronics.setPrice(round(100.0 + random.nextDouble() * 1400.0));
            electronics.setCategory("electronics");
            electronics.setWarrantyPeriod(12 + random.nextInt(24));
            productRepository.save(electronics);

            // Grocery
            String gItem = groceryItems[random.nextInt(groceryItems.length)];
            Grocery grocery = new Grocery();
            grocery.setName(gItem + " Pack");
            grocery.setDescription("Fresh " + gItem.toLowerCase() + " with expiry on " + LocalDate.now().plusDays(random.nextInt(30) + 3));
            grocery.setPrice(round(2.0 + random.nextDouble() * 13.0));
            grocery.setCategory("grocery");
            grocery.setExpiryDate(LocalDate.now().plusDays(random.nextInt(30) + 3));
            productRepository.save(grocery);
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
