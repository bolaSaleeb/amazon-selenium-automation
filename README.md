# Amazon Selenium Automation Project (Java + Maven)

## 📌 Overview
This project is a Selenium WebDriver automation script written in Java using Maven.  
It simulates a real user journey on Amazon Egypt (amazon.eg), including browsing products, applying filters, adding items to cart, verifying cart integrity, proceeding to checkout, and validating subtotal calculations.

---

## ⚙️ Technologies Used
- Java 15+
- Selenium WebDriver 4.x
- ChromeDriver
- Maven
- WebDriverWait (Explicit Waits)
- JavaScript Executor

---

## 🚀 What the Script Does

### 1. Open Amazon Egypt
- Launches Chrome browser
- Navigates to https://www.amazon.eg
- Maximizes window

---

### 2. Browse Categories
- Opens hamburger menu
- Navigates to:
  - See All Categories
  - Video Games
  - All Video Games

---

### 3. Apply Filters
- Filters products by:
  - Free Shipping
  - New condition
- Sorts products by:
  - Price (High → Low)

---

### 4. Product Search Logic
- Scans product list pages
- Identifies products under **15,000 EGP**
- Navigates pages until matching products are found

---

### 5. Add Products to Cart
- Selects products cheaper than 15,000 EGP
- Clicks "Add to Cart"
- Stores:
  - Product WebElements
  - Product titles for verification

---

### 6. Cart Validation
- Opens cart page
- Extracts all cart product titles
- Compares:
  - Selected products vs cart products
- Throws exception if mismatch detected

```java
throw new RuntimeException("Chosen Products are not the same as products in cart");
```

---

### 7. Checkout Process
- Clicks "Proceed to Checkout"
- Logs into Amazon account:
  - Email input
  - Password input

---

### 8. Delivery & Address Handling
- Selects Cash on Delivery (if available)
- Adds a new delivery address:
  - Full name
  - Phone number
  - Street address
  - Building details
  - City and district (with auto-suggestions)

---

### 9. Subtotal Verification
- Extracts subtotal from:
  - Cart page
  - Checkout summary page
- Cleans currency formatting:
  - Removes "EGP"
  - Removes commas and non-breaking spaces
- Converts to `double`

```java
double subtotalValue = Double.parseDouble(clean);
```

- Compares both values:

```java
if(subtotalValue != subtotal)
    throw new RuntimeException("Total items value does not match subtotal!");
```

---

## 🧠 Key Concepts Practiced
- Selenium locators (XPath, CSS Selectors, ID)
- Explicit waits (WebDriverWait)
- JavaScript Executor clicks
- Dynamic element handling
- Pagination handling
- Data validation between pages
- String cleaning & parsing currency
- Exception handling in automation

---

## ⚠️ Important Notes
- Amazon UI changes frequently → locators may break
- Use explicit waits instead of Thread.sleep for stability
- Do NOT hardcode credentials in real projects
- ChromeDriver version must match installed Chrome browser

---

## 🔧 Setup Instructions

### 1. Install Dependencies
Make sure Maven is installed and add Selenium dependency in `pom.xml`.

### 2. Run the Project
Run the main class:

```
AmazonTest.java
```

or via Maven:

```
mvn clean test
```

---

## 📂 Project Structure

```
src
 └── main
      └── java
           └── AmazonTest.java
pom.xml
README.md
```

---

## 📌 Author
Automation testing project built for learning Selenium WebDriver workflows and real-world e-commerce automation scenarios.
