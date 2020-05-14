import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestCase {

    public WebDriver driver;

    @BeforeTest
    public void verifyDrySkyLink()
    {
        System.setProperty("webdriver.chrome.driver", "src\\main\\chromedriver.exe");
        driver = new ChromeDriver();
        String baseURL = "https://darksky.net/";
        driver.get(baseURL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        String ActualURL = driver.getCurrentUrl();
        String ExpectedURL = "https://darksky.net/forecast/40.7127,-74.0059/us12/en";
        Assert.assertEquals(ActualURL,ExpectedURL);

        //ClearSearchBox
        driver.findElement(By.xpath("//*[@id=\"searchForm\"]/input")).clear();
        //SendInput
        driver.findElement(By.xpath("//*[@id=\"searchForm\"]/input")).sendKeys("10001");
    }


    @Test
    public void TestCase1() {
        String currentTemp = driver.findElement(By.cssSelector(".summary.swap")).getText();
        //System.out.println("Current Temp:" + currentTemp);
        List<WebElement> tempsInTimeLine = driver.findElements(By.cssSelector(".temps span:last-child"));
        int temp = Integer.parseInt(currentTemp.substring(0, 2));
        int highestInTimeLine = temp;
        int lowestInTimeLine = temp;
        for (WebElement tempInTime : tempsInTimeLine) {
            String sLIneTemp = tempInTime.getText();
            int lineTemp = Integer.parseInt(sLIneTemp.substring(0, 2));
            if (lineTemp > highestInTimeLine) {
                highestInTimeLine = lineTemp;
            }
            if (lineTemp < lowestInTimeLine) {
                lowestInTimeLine = lineTemp;
            }

        }
        //System.out.println("Highest Temp:" + Integer.toString(highestInTimeLine));
        //System.out.println("Lowest Temp:" + Integer.toString(lowestInTimeLine ));
        Assert.assertTrue(highestInTimeLine > temp, "above highest");
        Assert.assertTrue(lowestInTimeLine < temp, "below lowest");
        driver.quit();
    }

    @Test
    public void TestCase2() throws ParseException {
        DateFormat dateFormat2 = new SimpleDateFormat("hhaa");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String dateString2 = dateFormat2.format(new Date()).toLowerCase();
        DateFormat inFormat = new SimpleDateFormat("hhaa");
       //DateFormat outFormat = new SimpleDateFormat("HH");
        String t = dateFormat2.format(new Date()).toLowerCase();
        Date t1 = inFormat.parse(t);

        List<WebElement> hours = driver.findElements(By.cssSelector(".hours"));
        List<String> texts = Arrays.asList(hours.stream().map(WebElement::getText).collect(Collectors.toList()).get(0).split("\n"));
        //List<Integer> res_hour = new ArrayList<Integer>();
        texts.set(0, dateString2);

        for (int i = 1; i <= texts.size() - 1; i++)
        {
            Date date = inFormat.parse(texts.get(i));
            Date date_prev = inFormat.parse(texts.get(i-1));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date_prev);
            cal.add(Calendar.HOUR, 2);
            date_prev = cal.getTime();
            assert date.compareTo(date_prev) == 0;
        }
    }

    @AfterTest
    public void closeBrowser() throws Exception{
        driver.quit();
    }
}



