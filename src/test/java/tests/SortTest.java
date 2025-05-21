//Test n.6
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SortPage;
import pages.HomePage;
import pages.LoginPage;

import java.util.List;

public class SortTest extends BaseTest {

    @Test
    public void checkSortingByPrice() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(registeredEmail, password);

        SortPage sortPage = new SortPage(driver);
        sortPage.navigateToWomenCategory();
        sortPage.sortByPrice();

        List<Double> actualPrices = sortPage.getDisplayedProductPrices();

        for (int i = 0; i < actualPrices.size(); i++) {
            System.out.println("Price [" + i + "]: " + actualPrices.get(i));
        }

        for (int i = 1; i < actualPrices.size(); i++) {
            Assert.assertTrue(
                    actualPrices.get(i) >= actualPrices.get(i - 1),
                    "Products are not sorted by price in ascending order at index " + i +
                            ": " + actualPrices.get(i - 1) + " > " + actualPrices.get(i)
            );
        }

        System.out.println("Products are sorted by price in ascending order.");

        sortPage.addFirstTwoProductsToWishlist();

        String wishlistCount = sortPage.getWishlistCountFromAccount();
        Assert.assertEquals(wishlistCount, "2 items", "Wishlist count is incorrect.");

    }
}
