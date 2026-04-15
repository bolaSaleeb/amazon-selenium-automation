import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmazonTest {
	
	public static void main(String[] args) {

		//Initializing the web driver
		WebDriver driver = new ChromeDriver();

		//Navigating to Amazon.eg
		driver.get("https://www.amazon.eg/");
		driver.manage().window().maximize();

		try { Thread.sleep(1000); } catch (Exception e) {}

		// 1. Click "All"
		driver.findElement(By.id("nav-hamburger-menu")).click();

		try { Thread.sleep(1000); } catch (Exception e) {}

		// 2. Click "See all"
		driver.findElement(By.cssSelector("a[aria-label='See All Categories']")).click();

		try { Thread.sleep(1000); } catch (Exception e) {}

		// 3. Click "Video Games"
		driver.findElement(By.xpath("//div[text()='Video Games']")).click();

		try { Thread.sleep(1000); } catch (Exception e) {}
		
		// 4. All Video Games
		WebElement videoGames = driver.findElement(By.xpath("//a[contains(.,'All Video Games')]"));

		((JavascriptExecutor) driver)
				.executeScript("arguments[0].click();", videoGames);
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Click "Free Shipping"
		WebElement freeShipping = wait.until(
				ExpectedConditions.elementToBeClickable(
						By.xpath("//a[.//span[text()='Free Shipping']]")
				)
		);

		freeShipping.click();
		
		// Click "New"
		WebElement newFilter = driver.findElement(
				By.xpath("//a[.//span[text()='New']]")
		);

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", newFilter);
		
		WebElement dropdown = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.id("s-result-sort-select"))
		);

		Select select = new Select(dropdown);
		select.selectByValue("price-desc-rank");
		
		try { Thread.sleep(1000); } catch (Exception e) {}
		
		// Navigating to the page that has products under 15,000
		boolean found = false;
		
		while(true) {
			try { Thread.sleep(1000); } catch (Exception e) {}
			
			List<WebElement> prices = driver.findElements(By.cssSelector(".a-price-whole"));
			
			for (WebElement priceElement : prices) {

				String priceText = priceElement.getText()
						.replace(",", "")
						.trim();
				
				if (priceText.isEmpty()) continue;

				try {
					int price = Integer.parseInt(priceText);

					if (price < 15000) {
						found = true;
						break;
					}

				} catch (NumberFormatException e) {
					// ignore 
				}
			}

			if(found)
				break;
			else {
				WebElement nextButton = wait.until(
						ExpectedConditions.elementToBeClickable(
								By.xpath("//a[contains(@class,'s-pagination-next')]")
						)
				);

				nextButton.click();
			}
		}

		//Finding products that are less than 15,000
		List<WebElement> products = driver.findElements(
				By.cssSelector("div[data-component-type='s-search-result']")
		);
		
		List<WebElement> chosenProducts = new ArrayList<>();
		List<String> chosenProductsTitles = new ArrayList<>();
		
		for (WebElement product : products) {
			try {
				WebElement priceElement = product.findElement(
						By.cssSelector(".a-price-whole")
				);

				String cleaned = priceElement.getText()
						.replace(",", "")
						.trim();

				double price = Double.parseDouble(cleaned);
				
				if(price< 15000) {					
					try {
						WebElement addToCart = product.findElement(
								By.cssSelector("button[name='submit.addToCart']")
						);

						if (addToCart.isDisplayed() && addToCart.isEnabled()) {
							//Making sure the price is under 15,000
							addToCart.click();
							chosenProducts.add(product);
							
							String title = "";
						   
							List<WebElement> titleElements = product.findElements(
									By.cssSelector("span.a-truncate-cut")
							);

							if (!titleElements.isEmpty()) {
								title = titleElements.get(0).getText();
							} else {
								title = product.findElement(
										By.cssSelector("span.sc-product-title")
								).getText();
							} 
							
							chosenProductsTitles.add(title);
						}

					} catch (Exception e) {
						// ignore products without Add to Cart
					}
				}
			}catch (Exception e) { }
					    
			try { Thread.sleep(500); } catch (Exception e) {}
		}
		    
		try { Thread.sleep(1000); } catch (Exception e) {}

		//Going to cart to proceed
		WebElement cartButton = driver.findElement(By.id("nav-cart"));

		((JavascriptExecutor) driver)
				.executeScript("arguments[0].click();", cartButton);

		//Checking if products added to cart are the right ones
		List<WebElement> cartItems = driver.findElements(
				By.cssSelector("div.sc-list-item[data-asin]")
		);
		
		HashMap<String, String> productTitles = new HashMap<>();
		
		for (WebElement item : cartItems) {
			String title = "";
			
			List<WebElement> titleElements = item.findElements(
					By.cssSelector("span.a-truncate-cut")
			);

			if (!titleElements.isEmpty()) {
				title = titleElements.get(0).getText();
			} else {
				title = item.findElement(
						By.cssSelector("span.sc-product-title")
				).getText();
			}
			productTitles.put(title, title);
		}

		//Making sure all the products in shopping cart are the ones chosen
		boolean sizeIdentical = false;
		boolean productsIdentical = true;
		
		if(productTitles.size() == chosenProducts.size())
			sizeIdentical = true;
		
		System.out.println("chosen products' size:  "+ chosenProducts.size());
		System.out.println("product titles' size:  "+ productTitles.size());
		
		System.out.println("Size identical:  "+ sizeIdentical);
		
		for(String productTitle: chosenProductsTitles) {
			if(!productTitles.containsKey(productTitle)) {
				productsIdentical = false;
				break;
			}
		}
		
		//If products are not the same, throw and error
		if(!productsIdentical || !sizeIdentical)
			throw new RuntimeException("Chosen Products are not the same as products in cart");
		
		
		//Getting subtotal
		WebElement subtotalElement = driver.findElement(By.cssSelector(".sc-price"));

		String subtotalText = subtotalElement.getText();

		double subtotal = Double.parseDouble(
		        subtotalText.replace("EGP", "")
		                    .replace(",", "")
		                    .trim()
		);

		
		try { Thread.sleep(1000); } catch (Exception e) {}
		
		//Proceeding with the order
		WebElement checkoutBtn = driver.findElement(By.name("proceedToRetailCheckout"));

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);
		
		//Entering Email
		WebElement emailField = wait.until(
		    ExpectedConditions.visibilityOfElementLocated(By.id("ap_email_login"))
		);
		
		emailField.clear();
		emailField.sendKeys("problemsolving85@gmail.com");
		
		//Clicking the continue button
		WebElement continueBtn = wait.until(
			    ExpectedConditions.elementToBeClickable(By.id("continue"))
		);
		continueBtn.click();
		
		//Entering password
		WebElement passwordField = wait.until(
			    ExpectedConditions.visibilityOfElementLocated(By.id("ap_password"))
		);
		passwordField.sendKeys("seleniumTest_99");

		WebElement signInBtn = wait.until(
		    ExpectedConditions.elementToBeClickable(By.id("signInSubmit"))
		);
		signInBtn.click();
		
		
		//Checking if Cash on delivery is enabled
		WebElement codOption = wait.until(ExpectedConditions.presenceOfElementLocated(
			    By.xpath("//input[@type='radio' and contains(@value,'COD')]")
			));

		if (codOption.isEnabled()) {
			codOption.click();
		}
		
		
		//Adding a new delivery address

		By locator = By.xpath("//a[contains(@aria-label,'Change delivery address')]");

		WebElement changeButton = wait.until(ExpectedConditions.elementToBeClickable(locator));

		changeButton.click();

		WebElement addAddressBtn = wait.until(
		        ExpectedConditions.elementToBeClickable(
		            By.id("add-new-address-desktop-sasp-tango-link")
		        )
		);

		addAddressBtn.click();

		WebElement fullName = wait.until(
		        ExpectedConditions.visibilityOfElementLocated(
		            By.id("address-ui-widgets-enterAddressFullName")
		        )
		);

		fullName.clear();
		fullName.sendKeys("Bola Aziz");

		WebElement phoneField = wait.until(
		        ExpectedConditions.visibilityOfElementLocated(
		            By.id("address-ui-widgets-enterAddressPhoneNumber")
		        )
		);

		phoneField.clear();
		phoneField.sendKeys("01012345678");

		WebElement addressLine1 = wait.until(
		        ExpectedConditions.visibilityOfElementLocated(
		            By.id("address-ui-widgets-enterAddressLine1")
		        )
		);

		addressLine1.clear();
		addressLine1.sendKeys("Talaat Harb Street 10");

		WebElement buildingField = wait.until(
		        ExpectedConditions.visibilityOfElementLocated(
		            By.id("address-ui-widgets-enter-building-name-or-number")
		        )
		);

		buildingField.clear();
		buildingField.sendKeys("Building 116, Apartment 6");
		
		
		//Providing City/Area

		WebElement cityField = wait.until(
		        ExpectedConditions.visibilityOfElementLocated(
		            By.id("address-ui-widgets-enterAddressCity")
		        )
		);

		cityField.clear();
		cityField.sendKeys("New Cairo City");
		
		try { Thread.sleep(1500); } catch (Exception e) {}
		
		// 3. Get all suggestions
        List<WebElement> suggestions = driver.findElements(
                By.cssSelector("li.autoOp")
        );

        // 4. Click first suggestion
        if (suggestions.size() > 0) {
            suggestions.get(0).click();
        } else {
            throw new RuntimeException("No city suggestions found");
        }

		
		
		
		//choosing district

		WebElement districtInput = wait.until(
		        ExpectedConditions.elementToBeClickable(
		                By.id("address-ui-widgets-enterAddressDistrictOrCounty")
		        )
		);

		districtInput.click();
		districtInput.clear();
		districtInput.sendKeys("1st Settlement");

		wait.until(ExpectedConditions.visibilityOfElementLocated(
		        By.cssSelector("ul[role='listbox']")
		));

		suggestions = wait.until(
		        ExpectedConditions.presenceOfAllElementsLocatedBy(
		                By.cssSelector("li.autoOp")
		        )
		);

		if (!suggestions.isEmpty()) {
		    suggestions.get(0).click();
		}

		try { Thread.sleep(1000); } catch (Exception e) {}

		By btnLocator = By.cssSelector("input[data-testid='bottom-continue-button']");

		WebElement btn = wait.until(
		        ExpectedConditions.presenceOfElementLocated(btnLocator)
		);

		((JavascriptExecutor) driver)
		        .executeScript("arguments[0].click();", btn);

		try { Thread.sleep(10000); } catch (Exception e) {}
		
		
		//Making sure that subtotal before proceeding is same as when checking out with the order
		
        WebElement hiddenInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("subtotal-line-type")
                )
        );

        WebElement container = hiddenInput.findElement(
                By.xpath("./ancestor::div[contains(@class,'order-summary-line-definition')]")
        );

        subtotalElement = container.findElement(By.tagName("span"));

        subtotalText = subtotalElement.getText();
        System.out.println("Raw subtotal: " + subtotalText);

        String clean = subtotalText
                .replace("EGP", "")
                .replace(",", "")
                .replace("\u00A0", "")
                .trim();

        double subtotalValue = Double.parseDouble(clean);

        System.out.println("Subtotal as double: " + subtotalValue);
		
		if(subtotalValue != subtotal) {
			throw new RuntimeException("Total items value does not match subtotal!");
		}else {
			System.out.println("Subtotal matches");
		}
		
		try { Thread.sleep(2000); } catch (Exception e) {}

        driver.quit();

	}

}