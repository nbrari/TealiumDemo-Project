//Test n.3
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.HoverPage;

public class HoverTest extends BaseTest {

    @Test
    public void checkUnderlineOnWishlistHover() {
        HomePage homePage = new HomePage(driver);
        homePage.goToLoginPage();
        new LoginPage(driver).login(registeredEmail, password);

        HoverPage hoverPage = new HoverPage(driver);
        hoverPage.navigateToWomanCategory();

        String before = hoverPage.getTextDecorationBeforeHover();
        String after = hoverPage.getTextDecorationAfterHover();

        System.out.println("Text-decoration before hover: " + before);
        System.out.println("Text-decoration after  hover: " + after);

        Assert.assertNotEquals(before, after, "Hover did not visually underline the wishlist link.");
        Assert.assertEquals(after, "underline", "Expected 'underline' on hover, but got: " + after);
    }
}
