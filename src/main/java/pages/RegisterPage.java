package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

public class RegisterPage extends BasePage {

    private By firstName = By.id("firstname");
    private By lastName = By.id("lastname");
    private By email = By.id("email_address");
    private By password = By.id("password");
    private By confirmPassword = By.id("confirmation");
    private By registerButton = By.xpath("//button[@title='Register']");
    private By successMessage = By.cssSelector("li.success-msg span");

    public RegisterPage(WebDriver driver) {

        super(driver);
    }

    public void fillRegistrationForm(String fname, String lname, String emailAddr, String pwd, String confirmPwd) {
        driver.findElement(firstName).sendKeys(fname);
        driver.findElement(lastName).sendKeys(lname);
        driver.findElement(email).sendKeys(emailAddr);
        driver.findElement(password).sendKeys(pwd);
        driver.findElement(confirmPassword).sendKeys(confirmPwd);

        WebElement registerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(registerButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerBtn);

        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }
}

