//Test n.7
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.WishListToCartPage;

public class WishListToCartTest extends BaseTest {

    @Test
    public void testWishlistToCartFlow() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(registeredEmail, password);

        WishListToCartPage wishlistPage = new WishListToCartPage(driver);
        wishlistPage.goToWishlist();

        if (wishlistPage.getWishlistItemCount() == 2) {
            String[] colorsPriority = {"pink", "black", "white"};
            wishlistPage.handleWishlistItem(0, colorsPriority, "s");
            wishlistPage.clickContinueShopping();
            wishlistPage.goToWishlist();
            wishlistPage.handleWishlistItem(0, colorsPriority, "s");
        }

        wishlistPage.goToCart();
        wishlistPage.updateQuantityToTwo();

        Assert.assertTrue(wishlistPage.verifyTotalPrice(), "Total price calculation is incorrect.");
    }

}