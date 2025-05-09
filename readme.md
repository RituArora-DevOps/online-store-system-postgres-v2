# Online Store System
Java Programming III - Assignment Project
John Abbott College
Qi, Shao Hang and Ritu

## Project Description
This project represents our implementation of an Online Store System, developed as part of the Java Programming III course. The system demonstrates the practical application of advanced Java programming concepts, including JavaFX for the graphical user interface, Spring Boot for backend services, and database management using JPA. 

1. Three Tier Architecture
   - The presentation layer uses JavaFX for a user-friendly interface
   - The business logic layer contains all the core functionality
   - The data access layer manages database operations through Spring JPA

2. Database
   - Product information
   - Customer orders
   - User reviews
   - Payment records
--------------------------------------------------------------------------

1. Product Management
   - Created an abstract Product class with specific implementations
   - Implement (Electronics, Clothing, Grocery) class
   - Added support for product filtering and searching

2. Shopping Cart
   - Shopping cart functionality with add/remove capabilities
   - Order processing system
   - Payment simulation (Credit Card/PayPal)

3. User
   - Product review system
   - Ratings
   - Order history

1. Product Class
   The abstract Product (abstract) class serves as the foundation, with specialized subclasses:
   - Electronics: Includes warranty information
   - Clothing: Handles size and color attributes
   - Grocery: Manages expiration dates

2. Service
   - Product management
   - Shopping cart operations
   - Order processing
   - Review handling

3. Database
   Implemented tables for:
   - Products (with inheritance mapping)
   - Orders
   - Reviews
   - User accounts


Through this project, We have gained practical experience in:
- Developing complex Java applications
- Working with JavaFX for GUI development
- Implementing Spring Boot functionality
- Managing database operations
- Writing maintainable code and validations 

Project Structure Overview
online_store_system
┣ controller
┃ ┣ ProductController.java
┃ ┣ CartController.java
┃ ┣ PaymentController.java
┃ ┣ OrderController.java
┃ ┗ ReviewController.java
┣ model
┃ ┣ Product.java (abstract)
┃ ┣ Electronics.java
┃ ┣ Clothing.java
┃ ┣ Grocery.java
┃ ┣ ShoppingCart.java
┃ ┣ Payment.java
┃ ┣ OrderHistory.java
┃ ┗ ProductReview.java
┣ repository
JAVA ASSIGNMENT 2
┃ ┣ ProductRepository.java
┃ ┣ OrderRepository.java
┃ ┣ PaymentRepository.java
┃ ┗ ReviewRepository.java
┣ service
┃ ┣ ProductService.java
┃ ┣ CartService.java
┃ ┣ PaymentService.java
┃ ┣ OrderService.java
┃ ┗ ReviewService.java
┣ ui
┃ ┣ MainApp.java
┃ ┣ ProductListView.fxml
┃ ┣ CartView.fxml
┃ ┣ PaymentView.fxml
┃ ┣ OrderHistoryView.fxml
┃ ┗ ReviewView.fxml
┣ application.properties
┣ OnlineStoreSystemApplication.java