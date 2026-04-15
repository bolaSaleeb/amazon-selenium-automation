# Amazon Selenium Automation Project (Java + Maven)

## 📌 Overview
This project is a Selenium WebDriver automation script written in Java using Maven.  
It simulates a real-world e-commerce user journey on Amazon Egypt (amazon.eg), including product browsing, filtering, cart validation, checkout flow, address creation, and subtotal verification.

The goal of this project is to practice real-world automation testing scenarios using dynamic web elements and end-to-end workflows.

---

## ⚙️ Technologies Used
- Java 15+
- Selenium WebDriver 4.x
- ChromeDriver
- Maven
- WebDriverWait (Explicit Waits)
- JavaScript Executor

---

## 🚀 Test Flow Summary

### 1. Launch Application
- Opens Chrome browser
- Navigates to Amazon Egypt
- Maximizes the window

---

### 2. Product Navigation
- Opens hamburger menu
- Navigates to:
  - See All Categories
  - Video Games
  - All Video Games

---

### 3. Apply Filters
- Applies:
  - Free Shipping filter
  - New condition filter
- Sorts products by:
  - Price (High → Low)

---

### 4. Product Selection Logic
- Iterates through product listing pages
- Extracts product prices
- Selects products under **15,000 EGP**
- Navigates across pages until matching products are found

---

### 5. Add to Cart
- Adds valid products to cart
- Stores:
  - Product WebElements
  - Product titles for validation

---

### 6. Cart Validation
- Retrieves cart items
- Compares:
  - Selected products vs cart products
- Ensures:
  - Same number of items
  - Same product titles

If mismatch occurs:
```java
throw new RuntimeException("Chosen Products are not the same as products in cart");
```

---

### 7. Checkout Process
- Proceeds to checkout page
- Logs into Amazon account using:
  - Email
  - Password

---

### 8. Delivery Address Handling
- Selects Cash on Delivery option (if available)
- Adds a **new delivery address** during checkout:
  - Full name
  - Phone number
  - Street address
  - Building details
  - City and district (with auto-suggestions)

---

### 9. Subtotal Validation
- Extracts subtotal from:
  - Cart page
  - Checkout summary page
- Cleans currency formatting:
  - Removes "EGP"
  - Removes commas and special characters
- Converts value to `double`
- Compares both values for consistency

```java
if(subtotalValue != subtotal) {
    throw new RuntimeException("Total items value does not match subtotal!");
}
```

---

## ⚠️ Important Preconditions Before Running the Project

### 🛒 Cart State
- The Amazon account **must have an empty shopping cart** before execution.
- This ensures only the automation script controls item selection.

---

### 📍 Delivery Address Requirements
- The account **must already contain at least one saved address**
- During execution, the script will:
  - Navigate to checkout
  - Add a **new delivery address**
  - Use it for order flow validation

---

### 🔐 Account Requirements
- Valid Amazon.eg account required
- Must support:
  - Checkout access
  - Address creation during checkout
  - Cash on Delivery option (if available)

---

### ⚠️ Notes
- This project is for **automation testing practice only**
- It is not intended for real order placement
- Amazon UI changes may break locators over time

---

## 🧠 Key Automation Concepts Used
- Selenium locators (XPath, CSS, ID)
- Explicit waits (WebDriverWait)
- JavaScript Executor clicks
- Pagination handling
- Dynamic element handling
- Data extraction & validation
- Exception handling
- String parsing for currency values

---

## 📂 Project Structure (Recommended)

```
src
 └── main
      └── java
           └── AmazonTest.java
pom.xml
README.md
```

---

## 🔧 How to Run

### 1. Clone the Repository
```bash
git clone https://github.com/bolaSaleeb/amazon-selenium-automation.git
```

### 2. Open the Project
- Open the project in **IntelliJ IDEA** or **Eclipse**
- Make sure it is detected as a **Maven project**
- Wait for dependencies to download

### 3. Install Dependencies
Ensure `pom.xml` contains Selenium dependency:

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.21.0</version>
</dependency>
```

### 4. Run the Project

#### Option 1: Run directly
- Open `AmazonTest.java`
- Click Run (Java Application)

#### Option 2: Run using Maven
```bash
mvn clean test
```

---

## ⚠️ Important Notes
- Do NOT hardcode real credentials in production projects
- ChromeDriver version must match installed Chrome browser
- Use explicit waits instead of Thread.sleep for stability

---

## 📌 Author
Automation testing project built to practice real-world Selenium WebDriver workflows for QA engineering skill development.
