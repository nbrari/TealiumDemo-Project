package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.util.*;
import java.util.stream.Collectors;

public class SortPage extends BasePage {

    private By womenMenu = By.xpath("//a[text()='Women' and contains(@class, 'has-children')]");
    private By viewAllWomen = By.xpath("//a[text()='View All Women']");
    private By sortByDropdown = By.cssSelector("select[title='Sort By']");
    private By finalPrices = By.cssSelector(".category-products .item .price-box .special-price .price, " +
            ".category-products .item .price-box .regular-price .price, " +
            ".category-products .item .price-box > .price");
    private By addToWishlistButtons = By.cssSelector(".link-wishlist");

    public SortPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToWomenCategory() {
        driver.get("https://ecommerce.tealiumdemo.com");

        Actions actions = new Actions(driver);
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(womenMenu));
        actions.moveToElement(menu).perform();

        WebElement submenu = wait.until(ExpectedConditions.elementToBeClickable(viewAllWomen));
        submenu.click();
    }

    public void sortByPrice() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortByDropdown));
        Select select = new Select(dropdown);

        WebElement priceOption = select.getOptions().stream()
                .filter(opt -> opt.getText().trim().equalsIgnoreCase("Price"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("'Price' option not found in sort dropdown!"));

        String sortUrl = priceOption.getAttribute("value");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.location.href = arguments[0];", sortUrl);

        wait.until(ExpectedConditions.urlContains("order=price"));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(finalPrices));
        wait.until(driver -> {
            List<WebElement> prices = driver.findElements(finalPrices);
            if (prices.size() < 2) return false;

            try {
                List<Double> priceValues = prices.stream()
                        .map(WebElement::getText)
                        .map(text -> text.replaceAll("[$,]", "").trim())
                        .filter(text -> !text.isEmpty())
                        .map(Double::parseDouble)
                        .collect(Collectors.toList());

                for (int i = 0; i < priceValues.size() - 1; i++) {
                    if (priceValues.get(i) > priceValues.get(i + 1)) return false;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public List<Double> getDisplayedProductPrices() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(finalPrices));
        List<WebElement> priceElements = driver.findElements(finalPrices);

        for (int i = 0; i < priceElements.size(); i++) {
            System.out.println("Price [" + i + "]: " + priceElements.get(i).getText());
            System.out.println("Outer HTML: " + priceElements.get(i).getAttribute("outerHTML"));
        }

        return priceElements.stream()
                .map(WebElement::getText)
                .map(price -> price.replace("$", "").trim())
                .filter(p -> !p.isEmpty())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    public void addFirstTwoProductsToWishlist() {
        for (int i = 0; i < 2; i++) {
            try {
                List<WebElement> wishlistButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(addToWishlistButtons));

                if (i >= wishlistButtons.size()) {
                    System.out.println("Not enough wishlist buttons available for index " + i);
                    break;
                }

                WebElement button = wishlistButtons.get(i);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
                button.click();

                if (i == 0) {
                    WebElement returnLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("here")));
                    returnLink.click();
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(addToWishlistButtons));
                } else {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
                }

            } catch (StaleElementReferenceException | TimeoutException e) {
                System.out.println("Failed to add product " + (i + 1) + " to wishlist: " + e.getMessage());
            }
        }
    }

    public String getWishlistCountFromAccount() {
        try {
            By accountLabel = By.xpath("//span[@class='label'][normalize-space()='Account']");
            wait.until(ExpectedConditions.elementToBeClickable(accountLabel)).click();

            By wishlistLocator = By.xpath("//a[contains(text(), 'My Wishlist')]");
            WebElement wishlistLink = wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistLocator));

            String rawText = wishlistLink.getText().trim();
            System.out.println("Wishlist text: " + rawText);
            String count = rawText.replaceAll(".*\\((\\d+ items?)\\).*", "$1");
            return count;

        } catch (StaleElementReferenceException e) {
            System.out.println("Retrying after stale element...");
            By accountLabel = By.xpath("//span[@class='label'][normalize-space()='Account']");
            wait.until(ExpectedConditions.elementToBeClickable(accountLabel)).click();

            By wishlistLocator = By.xpath("//a[contains(text(), 'My Wishlist')]");
            WebElement wishlistLink = wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistLocator));

            String rawText = wishlistLink.getText().trim();
            System.out.println("Wishlist text (retry): " + rawText);
            String count = rawText.replaceAll(".*\\((\\d+ items?)\\).*", "$1");
            return count;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get wishlist count", e);
        }
    }
}
