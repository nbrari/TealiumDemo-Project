package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.FilterPage;
import pages.HomePage;
import pages.LoginPage;

public class FilterTest extends BaseTest {

    @Test
    public void checkFiltersFunctionality() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();

        new LoginPage(driver).login(registeredEmail, password);

        FilterPage filterPage = new FilterPage(driver);
        filterPage.navigateToMenCategory();

        filterPage.filterByBlackColor();
        Assert.assertTrue(filterPage.allProductsShowBlackSwatchWithBlueBorder(),
                "Not all products have black swatch with blue border.");

        filterPage.filterByPrice70AndAbove();

        Assert.assertTrue(filterPage.isProductCountCorrect(), "Product count is not 3.");
        Assert.assertTrue(filterPage.allProductPricesAreAbove70(), "Some products are under $70.");
    }
}