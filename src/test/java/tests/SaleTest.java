package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.SalePage;

import java.util.List;

public class SaleTest extends BaseTest {

    @Test
    public void verifySaleProductStyles() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();

        new LoginPage(driver).login(registeredEmail, password);

        SalePage salePage = new SalePage(driver);
        salePage.navigateToSalePage();

        List<String> failures = salePage.validateSaleProductStyles();

        for (String msg : failures) {
            System.out.println(msg);
        }

        Assert.assertTrue(failures.isEmpty(), "One or more sale products failed the style check.");
    }
}
