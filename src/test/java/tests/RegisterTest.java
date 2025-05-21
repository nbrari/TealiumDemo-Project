//Test n.1
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RegisterPage;

public class RegisterTest extends BaseTest {

    @Test
    public void registerSuccessfullyAndLogout() {
        HomePage homePage = new HomePage(driver);
        homePage.goToRegisterPage();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillRegistrationForm("Nadia", "Bbbb", registeredEmail, password, password);

        String successMsg = registerPage.getSuccessMessage();
        Assert.assertTrue(successMsg.toLowerCase().contains("thank you") || successMsg.toLowerCase().contains("registered"),
                "Expected success message not found. Message: " + successMsg);

        homePage.logoutFromAccount();
    }
}