package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {

        super(driver);
    }

    private final By cartItems = By.cssSelector("#cart-sidebar .item");
    private final By removeButtons = By.cssSelector("#cart-sidebar .item .remove");
    private final By emptyCartMessage = By.xpath("//*[contains(text(), 'You have no items in your shopping cart.')]");
    private final By cartIcon = By.cssSelector("a.skip-cart");

    public void openCart() {
        try {
            WebElement cartBtn = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
            cartBtn.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartItems));
        } catch (Exception e) {
            System.out.println("Failed to open cart: " + e.getMessage());
            throw e;
        }
    }

    public int getItemCount() {

        return driver.findElements(cartItems).size();
    }

    public void deleteFirstItem() {
        List<WebElement> buttons = driver.findElements(removeButtons);
        if (!buttons.isEmpty()) {
            WebElement removeButton = buttons.get(0);
            removeButton.click();
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(cartItems, buttons.size()));
        }
    }

    public boolean isCartEmpty() {

        return driver.findElements(emptyCartMessage).size() > 0;
    }
}
