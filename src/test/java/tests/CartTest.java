//Test n.8
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;

public class CartTest extends BaseTest {
    private CartPage cartPage;

    @Test
    public void testEmptyShoppingCart() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(registeredEmail, password);

        cartPage = new CartPage(driver);
        cartPage.openCart();

        int initialCount = cartPage.getItemCount();
        while (cartPage.getItemCount() > 0) {
            cartPage.deleteFirstItem();
            int newCount = cartPage.getItemCount();
            Assert.assertTrue(newCount < initialCount, "Item count should decrease after deletion.");
            initialCount = newCount;
        }

        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should show empty message after removing all items.");
    }
}

