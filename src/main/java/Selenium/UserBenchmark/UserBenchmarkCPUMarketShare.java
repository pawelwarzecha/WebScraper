package Selenium.UserBenchmark;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UserBenchmarkCPUMarketShare {
    public static void main(String[] args) throws InterruptedException, IOException {

        // Add options to use Chrome for automated testing
        ChromeOptions co = new ChromeOptions();
        co.setBinary("C:\\Chrome Driver\\chrome-win64\\chrome.exe");
        WebDriver driver = new ChromeDriver(co);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://cpu.userbenchmark.com/");

        // Accept cookies
        driver.findElement(By.cssSelector("[class='fc-button-label']")).click();

        // Scroll down and click on the market share column
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)");
        driver.findElement(By.xpath("//th[@data-mhth='MC_MKTSHARE']")).click();
        Thread.sleep(1000);

        ArrayList<String> cpuNames = new ArrayList<>();
        ArrayList<String> marketShares = new ArrayList<>();

        // Loop through pages and collect data
        while (true) {
            for (int i = 1; i <= 50; i++) {
                try {
                    String cpuName = driver.findElement(By.xpath(
                            "//table[@class='table mh-td table-v-center table-h-center']/tbody/tr["
                                    + i + "]/td/div/div/span")).getText();
                    // String from the element contains the word "Compare" as well as the CPU name, it needs to be cut
                    cpuName = cpuName.replace("Compare", "").trim();
                    String marketShare = driver.findElement(By.xpath(
                            "//table[@class='table mh-td table-v-center table-h-center']/tbody/tr["
                                    + i + "]/td[8]/div")).getText() + "%";
                    cpuNames.add(cpuName);
                    marketShares.add(marketShare);
                } catch (Exception e) {
                    break;
                }
            }
            // id of the "next" button often changes on the site, might need updating
            WebElement nextButton;
            try {
                nextButton = driver.findElement(By.cssSelector("a[id='tableDataForm:j_idt283']"));
                if (!nextButton.isDisplayed() || nextButton.getAttribute("class").contains("ui-state-disabled")) {
                    break;
                }
                nextButton.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                break;
            }
        }

        // Write data to Excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("CPU Market Share");
        for (int i = 0; i < cpuNames.size(); i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(cpuNames.get(i));
            row.createCell(1).setCellValue(marketShares.get(i));
        }

        try (FileOutputStream fileOut = new FileOutputStream("CPU_Market_Share.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
        driver.quit();
    }
}