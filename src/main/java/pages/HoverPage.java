package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HoverPage extends BasePage {

    private By womanMenu = By.xpath("//a[text()='Women' and contains(@class, 'has-children')]");
    private By viewAllWomen = By.xpath("//a[text()='View All Women']");
    private By wishlistLink = By.cssSelector(".category-products .item:first-child .link-wishlist");

    public HoverPage(WebDriver driver) {

        super(driver);
    }

    public void navigateToWomanCategory() {
        Actions actions = new Actions(driver);
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(womanMenu));
        actions.moveToElement(menu).perform();

        WebElement submenu = wait.until(ExpectedConditions.visibilityOfElementLocated(viewAllWomen));
        actions.moveToElement(submenu).click().perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistLink));
    }

    public String getTextDecorationBeforeHover() {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistLink));
        return link.getCssValue("text-decoration-line");
    }

    public String getTextDecorationAfterHover() {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
        new Actions(driver).moveToElement(link).pause(300).perform();
        return link.getCssValue("text-decoration-line");
    }
}
