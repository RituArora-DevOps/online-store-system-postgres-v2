-- Create new database
IF DB_ID('OnlineStoreDB') IS NULL
BEGIN
    CREATE DATABASE OnlineStoreDB;
END
GO

-- Switch to the new database
USE OnlineStoreDB;
GO

-- Drop tables if they already exist
IF OBJECT_ID('dbo.ProductReviews', 'U') IS NOT NULL DROP TABLE dbo.ProductReviews;
IF OBJECT_ID('dbo.OrderItems', 'U') IS NOT NULL DROP TABLE dbo.OrderItems;
IF OBJECT_ID('dbo.Orders', 'U') IS NOT NULL DROP TABLE dbo.Orders;
IF OBJECT_ID('dbo.ShoppingCartItems', 'U') IS NOT NULL DROP TABLE dbo.ShoppingCartItems;
IF OBJECT_ID('dbo.Products', 'U') IS NOT NULL DROP TABLE dbo.Products;
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;

-- Users Table
CREATE TABLE dbo.Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    PasswordHash NVARCHAR(255) NOT NULL
);

-- Products Table (Single Table Inheritance)
CREATE TABLE dbo.Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Category NVARCHAR(50) NOT NULL CHECK (Category IN ('Clothing', 'Electronics', 'Grocery')),
    Price DECIMAL(10,2) NOT NULL,
    Description NVARCHAR(255),
    Stock INT NOT NULL,
    -- Optional fields for specific categories
    Color NVARCHAR(50),           -- For Clothing
    Size NVARCHAR(50),            -- For Clothing
    WarrantyMonths INT,           -- For Electronics
    ExpiryDate DATE               -- For Grocery
);

-- Shopping Cart Items
CREATE TABLE dbo.ShoppingCartItems (
    CartItemID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    FOREIGN KEY (UserID) REFERENCES dbo.Users(UserID),
    FOREIGN KEY (ProductID) REFERENCES dbo.Products(ProductID)
);

-- Orders (serves as Order History)
CREATE TABLE dbo.Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(10,2) NOT NULL,
    PaymentMethod NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES dbo.Users(UserID)
);

-- Order Items
CREATE TABLE dbo.OrderItems (
    OrderItemID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    UnitPrice DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES dbo.Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES dbo.Products(ProductID)
);

-- Product Reviews
CREATE TABLE dbo.ProductReviews (
    ReviewID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    ProductID INT NOT NULL,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(255),
    ReviewDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES dbo.Users(UserID),
    FOREIGN KEY (ProductID) REFERENCES dbo.Products(ProductID)
);
