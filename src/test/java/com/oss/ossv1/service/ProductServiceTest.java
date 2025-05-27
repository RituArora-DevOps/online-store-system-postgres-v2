package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.Electronics;
import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductService class.
 * Uses Mockito to mock ProductRepository behavior and verifies business logic.
 * Covers logic validation, null handling, repository interaction, and edge cases.
 */
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository; // Mocked repository to simulate database interactions

    @InjectMocks
    private ProductService productService; // Service under test, with mocked dependencies injected

    private AutoCloseable closeable; // To clean up Mockito resources after each test

    private Product sampleProduct; // Reusable test product

    /**
     * Initializes mocks and sample data before each test method.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // Initializes @Mock annotations
        sampleProduct = new Electronics(); // Create a sample product
        sampleProduct.setId(1);
        sampleProduct.setName("Sample");
        sampleProduct.setCategory("Electronics");
        sampleProduct.setPrice(99.99);
    }

    @Test
    void checkMockitoConfigFileIsVisible() {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("mockito-extensions/org.mockito.plugins.MockMaker");
        assertNotNull(in, " Mockito config file is NOT in the test classpath!");
    }


    /**
     * Tests retrieval of all products using listProducts().
     */
    @Test
    void testListProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct)); // Simulate DB result
        Iterable<Product> result = productService.listProducts();
        assertNotNull(result); // Ensure result is not null
        assertEquals("Sample", result.iterator().next().getName()); // Check expected product
    }

    /**
     * Tests fetching a product by ID when the product exists.
     */
    @Test
    void testGetProductFound() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));
        Product result = productService.getProduct(1);
        assertNotNull(result); // Product should be found
        assertEquals("Sample", result.getName());
    }

    /**
     * Tests fetching a product by ID when the product does not exist.
     */
    @Test
    void testGetProductNotFound() {
        when(productRepository.findById(2)).thenReturn(Optional.empty()); // Simulate missing product
        Product result = productService.getProduct(2);
        assertNull(result); // Should return null
    }

    /**
     * Tests product creation using createProduct().
     */
    @Test
    void testCreateProduct() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);
        Product result = productService.createProduct(sampleProduct);
        assertEquals(sampleProduct, result);
    }

    /**
     * Tests updating a product using updateProduct().
     */
    @Test
    void testUpdateProduct() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);
        Product result = productService.updateProduct(sampleProduct);
        assertEquals(sampleProduct.getPrice(), result.getPrice());
    }

    /**
     * Tests deletion of a single product by ID.
     */
    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);
        assertDoesNotThrow(() -> productService.deleteProduct(1)); // Ensure no exceptions
        verify(productRepository, times(1)).deleteById(1); // Verify method call
    }

    /**
     * Tests deletion of all products in the repository.
     */
    @Test
    void testDeleteAllProducts() {
        doNothing().when(productRepository).deleteAll();
        productService.deleteAllProducts();
        verify(productRepository, times(1)).deleteAll();
    }

    /**
     * Tests filtering products by category.
     */
    @Test
    void testGetProductsByCategory() {
        when(productRepository.findByCategory("Electronics")).thenReturn(List.of(sampleProduct));
        List<Product> result = productService.getProductsByCategory("Electronics");
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
    }

    /**
     * Tests filtering products by price range.
     */
    @Test
    void testGetProductsByPriceRange() {
        when(productRepository.findByPriceBetween(50.0, 150.0)).thenReturn(List.of(sampleProduct));
        List<Product> result = productService.getProductsByPriceRange(50.0, 150.0);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getPrice() >= 50.0 && result.get(0).getPrice() <= 150.0);
    }

    /**
     * Closes the mock environment after each test to avoid memory leaks.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
