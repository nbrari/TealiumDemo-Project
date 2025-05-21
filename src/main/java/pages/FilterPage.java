package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class FilterPage extends BasePage {

    private By menMenu = By.xpath("//a[text()='Men' and contains(@class, 'has-children')]");
    private By viewAllMen = By.xpath("//a[text()='View All Men']");
    private By productItems = By.cssSelector(".category-products .item");

    private By blackColorFilter = By.xpath("//a[contains(@href,'color=20') and contains(@class,'swatch-link')]");
    private By priceFilter = By.xpath("//a[contains(@href,'price=70')]");

    public FilterPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToMenCategory() {
        Actions actions = new Actions(driver);
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(menMenu));
        actions.moveToElement(menu).perform();

        WebElement submenu = wait.until(ExpectedConditions.visibilityOfElementLocated(viewAllMen));
        actions.moveToElement(submenu).click().perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(productItems));
    }

    public void filterByBlackColor() {
        wait.until(ExpectedConditions.presenceOfElementLocated(productItems));
        WebElement productBefore = driver.findElements(productItems).get(0);

        WebElement blackFilter = wait.until(ExpectedConditions.presenceOfElementLocated(blackColorFilter));
        scrollIntoView(blackFilter);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", blackFilter);

        wait.until(ExpectedConditions.stalenessOf(productBefore));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productItems));
    }

    public boolean allProductsShowBlackSwatchWithBlueBorder() {
        List<WebElement> products = driver.findElements(productItems);
        for (WebElement product : products) {
            scrollIntoView(product);
            try {
                WebElement swatch = product.findElement(By.cssSelector(".swatch-link.has-image"));
                String borderColor = swatch.getCssValue("border-top-color");
                System.out.println("Swatch border: " + borderColor);

                if (!borderColor.contains("51, 153, 204")) return false;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        }
        return true;
    }

    public void filterByPrice70AndAbove() {
        WebElement price = wait.until(ExpectedConditions.elementToBeClickable(priceFilter));
        scrollIntoView(price);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", price);

        wait.until(driver -> {
            List<WebElement> products = driver.findElements(productItems);
            if (products.size() == 0) return false;

            for (WebElement product : products) {
                try {
                    WebElement priceEl = product.findElement(By.cssSelector(".price-box .price"));
                    String priceText = priceEl.getText().replace("$", "").trim();
                    double priceVal = Double.parseDouble(priceText);
                    if (priceVal < 70.0) return false;
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        });
    }

    public boolean isProductCountCorrect() {
        List<WebElement> products = driver.findElements(productItems);
        return products.size() == 3;
    }

    public boolean allProductPricesAreAbove70() {
        List<WebElement> items = driver.findElements(productItems);

        for (WebElement product : items) {
            scrollIntoView(product);
            try {
                WebElement priceEl = product.findElement(By.cssSelector(".price-box .price"));
                String priceText = priceEl.getText().replace("$", "").trim();
                double price = Double.parseDouble(priceText);
                System.out.println("Product Price: $" + price);
                if (price < 70.00) return false;
            } catch (NoSuchElementException | NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'})", element);
            wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.and(
                            ExpectedConditions.visibilityOf(element),
                            ExpectedConditions.elementToBeClickable(element)
                    )
            ));
        } catch (StaleElementReferenceException e) {
            System.out.println("Element went stale before scrolling. Skipping scroll.");
        }
    }
}