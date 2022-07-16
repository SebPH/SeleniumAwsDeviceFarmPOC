import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.devicefarm.*;
import software.amazon.awssdk.services.devicefarm.model.*;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumDeviceFarm
{
    private static RemoteWebDriver driver;
    private final String URL1 = "https://the-internet.herokuapp.com/login";
    private final String URL2 = "https://www.saucedemo.com/";
    private final String userName1 = "tomsmith";
    private final String password1 = "SuperSecretPassword!";
    private final String userName2 = "standard_user";
    private final String password2 = "secret_sauce";
    SeleniumToolKit _;

    @BeforeTest
    void setUp() throws MalformedURLException {

        String myProjectARN = "arn:aws:devicefarm:us-west-2:719655382608:testgrid-project:c6171a16-a730-4bdb-a724-ec10ee2b6cfe";
        DeviceFarmClient client  = DeviceFarmClient.builder()
                .region(Region.US_WEST_2)
                .build();
        CreateTestGridUrlRequest request = CreateTestGridUrlRequest.builder()
                .expiresInSeconds(300)
                .projectArn(myProjectARN)
                .build();
        CreateTestGridUrlResponse response = client.createTestGridUrl(request);
        URL testGridUrl = new URL(response.url());
        driver = new RemoteWebDriver(testGridUrl, DesiredCapabilities.chrome());
        _ = new SeleniumToolKit(driver);
    }

    @AfterTest
    void tearDown() {

        _.quit();
    }

    @Test
    public void userLoginHerokuApp() {

        _.maximize();
        _.url(URL1);
        _.write("#username", userName1);
        _.write("#password", password1);
        _.click("radius", "classname");
        Assert.assertTrue(driver.getCurrentUrl().contains("secure"));

        System.out.println("userLoginHerokuApp with Thread Id:- "
                + Thread.currentThread().getId());
    }

    @Test
    public void logIn() {

        _.maximize();
        _.url(URL2);
        _.write("[placeholder='Username']", userName2);
        _.write("[placeholder='Password']", password2);
        _.click(".login-box .submit-button", "cssSelector");

        // validations
        _.displayed("//span[contains(text(),'Products')]", "xpath");
        _.on("https://www.saucedemo.com/inventory.html");

        System.out.println("logIn with Thread Id:- "
                + Thread.currentThread().getId());
    }

    @Test
    public void leftPaneNav() throws InterruptedException {

        // log-in
        _.maximize();
        _.url(URL2);
        _.write("[placeholder='Username']", userName2);
        _.write("[placeholder='Password']", password2);
        _.click(".login-box .submit-button", "cssSelector");

        // open-up left pane
        _.click(".bm-burger-button", "cssSelector");

        // validate
        Assert.assertTrue(_.displayed(".bm-menu", "cssSelector"));
        Thread.sleep(1500);
        Assert.assertTrue(_.displayed("//*[contains(text(),'All Items')]", "xpath"));
        Assert.assertTrue(_.displayed("//*[contains(text(),'About')]", "xpath"));
        Assert.assertTrue(_.displayed("//*[contains(text(),'Logout')]", "xpath"));
        Assert.assertTrue(_.displayed("//*[contains(text(),'Reset App State')]", "xpath"));

        // close pane
        _.click(".bm-cross-button", "cssSelector");
        Thread.sleep(1000);
        Assert.assertFalse(_.displayed(".bm-menu", "cssSelector"));
    }

    @Test
    public void addToCart() {

        // log-in
        _.maximize();
        _.url(URL2);
        _.write("[placeholder='Username']", userName2);
        _.write("[placeholder='Password']", password2);
        _.click(".login-box .submit-button", "cssSelector");

        // add to cart
        _.click("add-to-cart-sauce-labs-backpack", "id");
        _.click("add-to-cart-sauce-labs-bike-light", "id");
        _.click("add-to-cart-sauce-labs-bolt-t-shirt", "id");

        // validate cart icon count
        String cartCount = _.read(".shopping_cart_badge");
        Assert.assertEquals("3", cartCount);

        // go to cart
        _.click(".shopping_cart_badge", "cssSelector");
        _.on("https://www.saucedemo.com/cart.html");

        // validate items selected count
        Assert.assertEquals(3, _.count(".cart_item"));
    }

    @Test
    public void checkOut() throws InterruptedException {

        // log-in
        _.maximize();
        _.url(URL2);
        _.write("[placeholder='Username']", userName2);
        _.write("[placeholder='Password']", password2);
        _.click(".login-box .submit-button", "cssSelector");

        // add to cart
        _.click("add-to-cart-sauce-labs-backpack", "id");

        // go to cart
        _.click(".shopping_cart_badge", "cssSelector");

        // checkout
        _.click("checkout", "id");

        // validate checkout: screen 1 (information)
        _.on("https://www.saucedemo.com/checkout-step-one.html");

        // add info
        _.write("[placeholder='First Name']", "Jake");
        _.write("[placeholder='Last Name']", "Johnson");
        _.write("[placeholder='Zip/Postal Code']", "12345-7890");
        _.click("continue", "id");

        // validate checkout: screen 2 (overview)
        _.on("https://www.saucedemo.com/checkout-step-two.html");
        _.displayed("//div[contains(text(),'Payment Information:')]", "xpath");
        _.displayed("//div[contains(text(),'Shipping Information:')]", "xpath");
        _.click("finish", "id");

        // validate last screen
        _.displayed("//span[contains(text(),'Checkout: Complete!')]", "xpath");
        _.displayed("//h2[contains(text(),'THANK YOU FOR YOUR ORDER')]", "xpath");

        // log out
        _.click(".bm-burger-button", "cssSelector");
        Thread.sleep(1500);
        Assert.assertTrue(_.displayed("//*[contains(text(),'Logout')]", "xpath"));
        _.click("//*[contains(text(),'Logout')]", "xpath");

        // validate back to home
        _.on("https://www.saucedemo.com/");
    }

}