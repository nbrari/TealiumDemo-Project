package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        handleConsentPopup();
    }

    private void handleConsentPopup() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("privacy_pref_optin"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("consent_prompt_submit"))).click();
        } catch (Exception e) {
            System.out.println("Consent popup not found or already dismissed.");
        }
    }
}
