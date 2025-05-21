package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class SalePage extends BasePage {

    private final By saleMenu = By.xpath("//a[text()='Sale' and contains(@class, 'has-children')]");
    private final By viewAllSale = By.xpath("//a[text()='View All Sale']");
    private final By saleProducts = By.cssSelector(".category-products .item");

    public SalePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToSalePage() {
        Actions actions = new Actions(driver);
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(saleMenu));
        actions.moveToElement(menu).perform();

        WebElement submenu = wait.until(ExpectedConditions.visibilityOfElementLocated(viewAllSale));
        actions.moveToElement(submenu).click().perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(saleProducts));
    }

    public List<String> validateSaleProductStyles() {
        List<WebElement> products = driver.findElements(saleProducts);
        List<String> failures = new ArrayList<>();

        if (products.isEmpty()) {
            failures.add("No sale products found on the page.");
            return failures;
        }

        for (WebElement product : products) {
            try {
                scrollAndWaitForPrices(product);

                WebElement oldPrice = product.findElement(By.cssSelector(".old-price .price"));
                WebElement newPrice = product.findElement(By.cssSelector(".special-price .price"));

                if (!isOldPriceStyledCorrectly(oldPrice) || !isNewPriceStyledCorrectly(newPrice)) {
                    failures.add("Style check failed:\n" +
                            "Old Price - Color: " + oldPrice.getCssValue("color") + ", Decoration: " + oldPrice.getCssValue("text-decoration-line") + "\n" +
                            "New Price - Color: " + newPrice.getCssValue("color") + ", Decoration: " + newPrice.getCssValue("text-decoration-line"));
                }

            } catch (NoSuchElementException e) {
                failures.add("Missing price elements in product: " + product.getText());
            }
        }

        return failures;
    }

    private void scrollAndWaitForPrices(WebElement product) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", product);
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(product, By.cssSelector(".old-price .price")));
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(product, By.cssSelector(".special-price .price")));
    }

    private boolean isOldPriceStyledCorrectly(WebElement oldPrice) {
        String color = oldPrice.getCssValue("color");
        String decoration = oldPrice.getCssValue("text-decoration-line");

        boolean colorIsGray = color.contains("160") || color.contains("a0a0a0");
        boolean isStrikethrough = decoration.equalsIgnoreCase("line-through");

        return colorIsGray && isStrikethrough;
    }

    private boolean isNewPriceStyledCorrectly(WebElement newPrice) {
        String color = newPrice.getCssValue("color");
        String decoration = newPrice.getCssValue("text-decoration-line");

        boolean colorIsBlue = color.contains("51") && color.contains("153") && color.contains("204");
        boolean isNotStrikethrough = !decoration.equalsIgnoreCase("line-through");

        return colorIsBlue && isNotStrikethrough;
    }
}
