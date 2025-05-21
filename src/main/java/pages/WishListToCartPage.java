package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class WishListToCartPage extends BasePage {

    private final By accountLabel = By.xpath("//span[@class='label'][normalize-space()='Account']");
    private final By wishlistLink = By.xpath("//a[contains(text(), 'My Wishlist')]");
    private final By editButtons = By.cssSelector("a.link-edit");
    private final By sizeSmall = By.cssSelector("li.option-s a");

    private final By addToCartButton = By.cssSelector("div.add-to-cart-buttons button.btn-cart");
    private final By wishlistAddToCartButtons = By.cssSelector("button.btn-cart");

    private final By continueShoppingButton = By.xpath("//span[text()='Continue Shopping']/..");
    private final By cartIcon = By.cssSelector("a.skip-link.skip-cart");
    private final By qtyInput = By.cssSelector("input.qty.cart-item-quantity.input-text");
    private final By qtyUpdateButton = By.cssSelector("button.quantity-button");

    public WishListToCartPage(WebDriver driver) {
        super(driver);
    }

    public void goToWishlist() {
        wait.until(ExpectedConditions.elementToBeClickable(accountLabel)).click();
        wait.until(ExpectedConditions.elementToBeClickable(wishlistLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(editButtons));
    }

    public int getWishlistItemCount() {
        List<WebElement> editButtonsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(editButtons));
        return editButtonsList.size();
    }

    public void handleWishlistItem(int index, String[] colors, String size) {
        try {
            List<WebElement> editList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(editButtons));

            if (index >= editList.size()) {
                throw new RuntimeException("Wishlist has only " + editList.size() + " items. Cannot access index " + index);
            }

            WebElement editButton = editList.get(index);

            if (!editButton.isEnabled() || editButton.getAttribute("class").contains("disabled")) {
                System.out.println("No configuration needed for item " + (index + 1));
                List<WebElement> addButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(wishlistAddToCartButtons));

                if (index >= addButtons.size()) {
                    throw new RuntimeException("'Add to Cart' buttons list has only " + addButtons.size() + " items. Cannot access index " + index);
                }

                WebElement addBtn = addButtons.get(index);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addBtn);
                wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
                System.out.println("Clicked 'Add to Cart' directly from wishlist for item " + (index + 1));
            } else {
                configureWishlistItem(index, colors, size);
            }

        } catch (Exception e) {
            System.out.println("Failed to handle wishlist item " + (index + 1) + ": " + e.getMessage());
            throw new RuntimeException("Wishlist item handling failed", e);
        }
    }

    public void configureWishlistItem(int index, String[] colors, String size) {
        try {
            List<WebElement> edits = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(editButtons));
            WebElement editLink = edits.get(index);
            String href = editLink.getAttribute("href");

            driver.navigate().to(href);
            wait.until(ExpectedConditions.urlContains("/configure/"));
            System.out.println("Navigated to product config for item " + (index + 1));

            for (String color : colors) {
                String selector = "li.option-" + color + " a";
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));

                if (!elements.isEmpty()) {
                    WebElement element = elements.get(0);
                    if (element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        System.out.println("Clicked color option: " + color);
                        break;
                    } else {
                        System.out.println("Element found but not clickable: " + color);
                    }
                } else {
                    System.out.println("No element found for color: " + color);
                }
            }

            if (size.equalsIgnoreCase("s")) {
                wait.until(ExpectedConditions.elementToBeClickable(sizeSmall)).click();
                System.out.println("Selected size: S");
            }

            WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(addToCartButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addButton);
            wait.until(ExpectedConditions.visibilityOf(addButton));

            try {
                wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
                System.out.println("Clicked 'Add to Cart' using regular click.");
            } catch (Exception e) {
                System.out.println("Normal click failed, trying JavaScript click...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
                System.out.println("Clicked 'Add to Cart' using JavaScript.");
            }

        } catch (Exception e) {
            System.out.println("Failed to configure item " + (index + 1) + ": " + e.getMessage());
            throw new RuntimeException("Add to cart failed", e);
        }
    }

    public void clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
    }

    public void goToCart() {

        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }

    public void updateQuantityToTwo() {
        WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput));

        qty.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        qty.sendKeys("2");

        WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(qtyUpdateButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled')", updateBtn);
        updateBtn.click();

        By spinner = By.cssSelector(".loading-mask");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        wait.until(driver -> qty.getAttribute("value").equals("2"));
    }

    public boolean verifyTotalPrice() {
        By spinner = By.cssSelector(".loading-mask");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

        return wait.until(driver -> {
            List<WebElement> items = driver.findElements(By.cssSelector("#cart-sidebar .item"));
            double calculatedTotal = 0.0;

            for (WebElement item : items) {
                try {
                    String priceText = item.findElement(By.cssSelector(".price")).getText().replace("$", "").replace(",", "").trim();
                    double price = Double.parseDouble(priceText);

                    String qtyText = item.findElement(By.cssSelector("input.qty.cart-item-quantity")).getAttribute("value").trim();
                    int qty = Integer.parseInt(qtyText);

                    calculatedTotal += price * qty;
                } catch (Exception e) {
                    return false;
                }
            }

            WebElement totalElement = driver.findElement(By.cssSelector(".block-content .price"));
            String displayedText = totalElement.getText().replace("$", "").replace(",", "").trim();
            double displayedTotal = Double.parseDouble(displayedText);

            System.out.println("Expected Total: $" + calculatedTotal + " | Displayed: $" + displayedTotal);

            return Math.abs(calculatedTotal - displayedTotal) < 0.01;
        });
    }
}

