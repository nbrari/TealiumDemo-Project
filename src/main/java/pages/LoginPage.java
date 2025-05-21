package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private By emailInput = By.id("email");
    private By passwordInput = By.id("pass");
    private By loginButton = By.id("send2");

    public LoginPage(WebDriver driver) {

        super(driver);
    }

    public void login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }

        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);

        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginBtn);

        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }
}


