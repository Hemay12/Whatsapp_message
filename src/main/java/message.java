import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.io.FileReader;


public class message {

    @Test
    public void testMessage() throws IOException, InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\hemay\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Use an existing Chrome profile to stay logged in
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\hemay\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1");
        options.addArguments("--profile-directory=Profile 1");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("https://web.whatsapp.com/");
        File file = new File("whatsapp_cookies.txt");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ";");
                String name = tokenizer.nextToken();
                String value = tokenizer.nextToken();
                String domain = tokenizer.nextToken();
                String path = tokenizer.nextToken();
                String expiry = tokenizer.nextToken();
                boolean isSecure = Boolean.parseBoolean(tokenizer.nextToken());

                Date expiryDate = null;
                if (!expiry.equals("null")) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");  // Match the date format
                        expiryDate = sdf.parse(expiry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Cookie cookie = new Cookie.Builder(name, value)
                        .domain(domain)
                        .path(path)
                        .expiresOn(expiryDate)  // Use parsed Date object
                        .isSecure(isSecure)
                        .build();
                driver.manage().addCookie(cookie);
            }
            reader.close();
        }

        // Refresh page to apply cookies
        driver.navigate().refresh();
        System.out.println("Cookies loaded! WhatsApp Web should open without QR code.");

        Thread.sleep(10000);

        String contactName = "Krina Gandhi";  // Change this to the contact name you want
        WebElement searchBox = driver.findElement(By.xpath("//div[@contenteditable='true']"));
        searchBox.click();
        searchBox.sendKeys(contactName);
        Thread.sleep(3000);

        WebElement chat = driver.findElement(By.xpath("//span[@title='" + contactName + "']"));
        chat.click();
        System.out.println("Chat opened with " + contactName);
        Thread.sleep(5000);

        // Locate the message input box
        WebElement messageBox = driver.findElement(By.xpath("//div[@aria-label='Type a message']"));
        messageBox.click();

        // Enter message
        messageBox.sendKeys("Hello, this is an automated message!");
        messageBox.sendKeys(Keys.SHIFT, Keys.ENTER);
        messageBox.sendKeys("");

        Thread.sleep(1000);

        // Press Enter to send message
//        messageBox.sendKeys(Keys.ENTER);
        System.out.println("Message sent: " );

        // Keep the browser open for verification
        Thread.sleep(5000);

        driver.quit();

    }
}
