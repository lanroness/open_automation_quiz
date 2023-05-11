package scb.open.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.junit.Cucumber;
import io.cucumber.testng.CucumberOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        glue = "scb.open.cucumber",
        plugin = {"pretty", "html:target/cucumber"})
public class FormSubTest {
    public WebDriver driver;


    @Given("Open {string}")
    public void open(String url) throws InterruptedException {

        WebDriverManager webM;
        webM = WebDriverManager.chromedriver();
        webM.setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");//解决 403 出错问题
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.get(url);
    }


    @Then("Click {string} by xpath")
    public void clickByXpath(String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        try{
            driver.findElement(By.xpath(xpath)).click();
        }catch (Exception e){
            ((JavascriptExecutor) driver).executeScript("arguments[0].click;", driver.findElement(By.xpath(xpath)));
        }

    }


    @Then("Switch iframe {string} by xpath")
    public void switchIframeByXpath(String xpath) {
        driver.switchTo().frame(driver.findElement(By.xpath(xpath)));
    }



    public void screenshotForElement(WebDriver driver, WebElement element, long ms){
        try {
//            WebElement element = driver.findElement(By.xpath("//*[@class='published-form__body']"));
            File source = element.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File("./ScreenShots/"+ms+"test.png"));
            System.out.println("ScreenShot Taken");
        } catch (Exception e) {
            System.out.println("Exception while taking ScreenShot "+e.getMessage());
        }
    }

    @Then("Scroll down half page")
    public void scrollDownHalfPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    @Then("Scroll {string} into view by xpath")
    public void scrollElementIntoViewByXpath(String xpath) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(xpath)));
    }

    @Then("TakeScreenShot as {string}")
    public void screenshot(String name){
        try {
//            Long width = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollWidth");
//            Long height = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollHeight");
//            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File("./ScreenShots/"+ name +"test.png"));
            System.out.println("ScreenShot Taken");
        } catch (Exception e) {
            System.out.println("Exception while taking ScreenShot "+e.getMessage());
        }
    }

    @Then("Input date into {string} by xpath")
    public void inputDateIntoByXpath(String xpath) {
        LocalDate now = LocalDate.now();
        //某年的第一天
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfYear());
        driver.findElement(By.xpath(xpath)).sendKeys(String.valueOf(firstDay));
    }

    @And("Input {string} into {string} by xpath")
    public void inputIntoByXpath(String content, String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        element.sendKeys(content);
    }

    @Then("Input today into {string} by xpath")
    public void inputTodayIntoByXpath(String xpath) {
        LocalDate now = LocalDate.now();
        driver.findElement(By.xpath(xpath)).sendKeys(String.valueOf(now));
    }

    @And("Assert {string} is presence by xpath")
    public void assertIsPresence(String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        Assert.assertTrue(driver.findElements(By.xpath(xpath)).size()>0);
    }

    @Then("Sleep for {int} ms")
    public void sleepForSeconds(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after(Scenario scenario){
        final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);;
        scenario.attach(screenshot, "image/png", "name");
        driver.quit();
    }

}
