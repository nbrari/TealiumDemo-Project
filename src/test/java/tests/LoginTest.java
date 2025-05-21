//Test n.2
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void loginAndLogout() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(registeredEmail, password);

        String username = homePage.getLoggedInUserName();
        Assert.assertTrue(username.toLowerCase().contains("nadia"),
                "Logged-in username not correct or missing. Found: " + username);

        homePage.logoutFromAccount();
    }
}
