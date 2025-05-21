package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private By accountLink = By.cssSelector("a[href$='/customer/account/']");
    private By registerLink = By.xpath("//a[contains(text(),'Register')]");
    private By loginLink = By.xpath("//a[contains(text(),'Log In')]");
    private By logoutLink = By.xpath("//a[contains(text(),'Log Out')]");
    private By loggedInUser = By.cssSelector("p.hello strong");

    public HomePage(WebDriver driver) {

        super(driver);
    }

    public void goToRegisterPage() {
        wait.until(ExpectedConditions.elementToBeClickable(accountLink)).click();
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }

    public void goToLoginPage() {
        wait.until(ExpectedConditions.elementToBeClickable(accountLink)).click();
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public String getLoggedInUserName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInUser)).getText();
    }

    public void logoutFromAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(accountLink)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }
}
