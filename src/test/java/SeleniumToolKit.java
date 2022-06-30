
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SeleniumToolKit {

    WebDriver driver;

    public SeleniumToolKit(WebDriver driver) {
        this.driver = driver;
    }

    public void click(String locator, String locatorType) {

        switch(locatorType) {
            case "cssSelector":
                driver.findElement(By.cssSelector(locator)).click();
                break;
            case "id":
                driver.findElement(By.id(locator)).click();
                break;
            case "xpath":
                driver.findElement(By.xpath(locator)).click();
                break;
            case "classname":
                driver.findElement(By.className(locator)).click();
                break;
            default:
            case "link":
                driver.findElement(By.linkText(locator)).click();
                break;
        }
    }

    public void write(String selector, String input) {

        driver.findElement(By.cssSelector(selector)).sendKeys(input);
    }

    public boolean displayed(String locator, String locatorType) {

        switch(locatorType) {
            case "cssSelector":
                return driver.findElement(By.cssSelector(locator)).isDisplayed();
            case "id":
                return driver.findElement(By.id(locator)).isDisplayed();
            case "xpath":
                return driver.findElement(By.xpath(locator)).isDisplayed();
            default:
            case "link":
                return driver.findElement(By.linkText(locator)).isDisplayed();
        }
    }

    public void selected(String selector) {

        driver.findElement(By.cssSelector(selector)).isSelected();
    }

    public void enabled(String selector) {

        driver.findElement(By.cssSelector(selector)).isEnabled();
    }

    public void on(String url) {

        Assert.assertEquals(url, driver.getCurrentUrl());
    }

    public void quit() {

        driver.quit();
    }

    public void url(String url) {

        driver.get(url);
    }

    public void maximize() {

        driver.manage().window().maximize();
    }

    public String read(String selector) {

        return driver.findElement(By.cssSelector(selector)).getText();
    }

    public int count(String selector) {

        return driver.findElements(By.cssSelector(selector)).size();
    }

    public void count(String selector, int expectedCount) {

        int actualCount = driver.findElements(By.cssSelector(selector)).size();
        Assert.assertEquals(expectedCount, actualCount);
    }

}
