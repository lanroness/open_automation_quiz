package scb.open.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.xml.xpath.XPath;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Test
public class SearchTest {

    public WebDriver driver;
    public String filePath = "./searchResult.txt";


    @BeforeClass
    public void beforeClass() throws IOException {
//        System.setProperty("webdriver.chrome.driver","/Users/neilliu/99Lanroness/chromedriver");
//        自适配浏览器
        WebDriverManager webM;
        webM = WebDriverManager.chromedriver();
        webM.setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");//解决 403 出错问题

        emptyFile(filePath);
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.get("https://www.ianzhang.cn/bing/");
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='bing']")));
    }

    @DataProvider(name = "keyWords")
    public Object[][] inputData(){
        return new Object[][]{
                {"Ian"},
                {"Selenium"}
        };
    }

    public String getTopDomain(String url){
        try {
            URL u = new URL(url);
            String host = u.getHost();
            String topLevelDomain = host.substring(host.indexOf(".") + 1);
            return topLevelDomain;
        } catch (MalformedURLException e) {
            System.out.println("Error getting domain: " + e.getMessage());
        }
        return null;
    }


    @Test(dataProvider = "keyWords")
    public void searchKeyWords(String keyWords) throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By searchBy = By.xpath("//input[@id='sb_form_q']");
        wait.until(ExpectedConditions.presenceOfElementLocated(searchBy));
        WebElement searchBox = driver.findElement(searchBy);
        searchBox.clear();
        searchBox.sendKeys(keyWords);
        searchBox.submit();
        By pageTwoBy = By.xpath("//a[@aria-label='Page 2']");
        wait.until(ExpectedConditions.presenceOfElementLocated(searchBy));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement pageTwo = driver.findElement(pageTwoBy);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click;", pageTwo);
        List<WebElement> resultLinks = driver.findElements(By.xpath("//ol[@id='b_results']//li[contains(@class, 'b_algo')]//h2/a"));
//        getResult(elementsTitle,elementsLink);

        ArrayList<String> topDomains = new ArrayList<String>();
        Map<String, Long> resultMap = new HashMap<>();
        writeToFile("结果列表",filePath);
        for (int i = 0; i < resultLinks.size(); i++) {
            String link = resultLinks.get(i).getAttribute("href");
            topDomains.add(getTopDomain(link));
            writeToFile(resultLinks.get(i).getText() + "  --> " + resultLinks.get(i).getAttribute("href"),filePath);
        }


        writeToFile("结果统计",filePath);
        resultMap = topDomains.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        for (String i : resultMap.keySet()) {
            writeToFile(i + "  --> " + resultMap.get(i),filePath);
        }
    }


    // 将统计结果写入到文件中
    private void writeToFile(String content, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
        writer.write(content);
        writer.newLine();
        writer.close();
    }

//    清空文件夹
    private void emptyFile(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("");
        writer.flush();
        writer.close();
    }



    @AfterClass
    public void afterClass(){
        driver.quit();

    }

}
