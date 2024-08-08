package Selenium.Otodom;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class Otodom {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Chrome Driver\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions co = new ChromeOptions();
        co.setBinary("C:\\Chrome Driver\\chrome-win64\\chrome.exe");

        WebDriver driver = new ChromeDriver(co);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//java//Selenium//Otodom//resources//data.properties");
        prop.load(fis);

        String propertyLocation = prop.getProperty("propLocation");
        String minimumPrice = prop.getProperty("minPrice");
        String maximumPrice = prop.getProperty("maxPrice");
        String minimumSqFootage = prop.getProperty("minSqFootage");
        String maximumSqFootage = prop.getProperty("maxSqFootage");
        String buildYearMin = prop.getProperty("yearMin");
        String buildYearMax = prop.getProperty("yearMax");
        String pricePerMetreMin = prop.getProperty("pricePerMin");
        String pricePerMetreMax = prop.getProperty("pricePerMax");
        String numberOfRooms = prop.getProperty("rooms");

        driver.get("https://www.otodom.pl/");

        Actions a = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();

        WebElement element = driver.findElement(By.cssSelector("div[class='ey2nap44 css-1n1retz']"));
        Thread.sleep(3000);
        a.moveToElement(element).click().sendKeys(propertyLocation).build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='e12tw922 css-1q6iflv']")));
        driver.findElement(By.cssSelector("div[class='e12tw922 css-1q6iflv']")).click();

        driver.findElement(By.id("priceMin")).sendKeys(minimumPrice);
        driver.findElement(By.id("priceMax")).sendKeys(maximumPrice);
        driver.findElement(By.id("areaMin")).sendKeys(minimumSqFootage);
        driver.findElement(By.id("areaMax")).sendKeys(maximumSqFootage);
        driver.findElement(By.id("search-form-submit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[class='css-1c1kq07 epuxlk90']")));
        driver.findElement(By.cssSelector("span[class='css-1c1kq07 epuxlk90']")).click();

        driver.findElement(By.id("pricePerMeterMin")).sendKeys(pricePerMetreMin);
        driver.findElement(By.id("pricePerMeterMax")).sendKeys(pricePerMetreMax);
        driver.findElement(By.id("buildYearMin")).sendKeys(buildYearMin);
        driver.findElement(By.id("buildYearMax")).sendKeys(buildYearMax);
        switch (numberOfRooms) {
            case "1":
                driver.findElement(By.xpath("//div[@id='roomsNumber']//div[1]")).click();
                break;
            case "2":
                driver.findElement(By.xpath("//div[@class='css-1m6zk3n eh39zul0']//div[2]")).click();
                break;
            case "3":
                driver.findElement(By.xpath("//div[@class='css-1m6zk3n eh39zul0']//div[3]")).click();
                break;
            case "4":
                driver.findElement(By.xpath("//div[@class='css-1ynh8jm ejrlokm4']//div[4]")).click();
                break;
            case "5":
                driver.findElement(By.xpath("//div[@class='css-1m6zk3n eh39zul0']//div[5]")).click();
                break;
            case "6":
                driver.findElement(By.xpath("//div[@class='css-1m6zk3n eh39zul0']//div[6]")).click();
                break;
            default:
                break;
        }
        Thread.sleep(2000);
        driver.findElement(By.id("search-form-submit")).click();
        Thread.sleep(2000);

        StringBuilder htmlReport= new StringBuilder();
        htmlReport.append("<html><head><title>Real Estate Data</title></head><body>");
        htmlReport.append("<h1>Real Estate Listings</h1>");
        htmlReport.append("<table border='1'><tr><th>Price</th><th>Location</th><th>Rooms</th><th>Sqr Footage</th><th>Price per sqm</th><th>Floor Nr</th><th>Image</th><th>Link</th></tr>");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Real Estate Data");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Price");
        headerRow.createCell(1).setCellValue("Location");
        headerRow.createCell(2).setCellValue("Rooms");
        headerRow.createCell(3).setCellValue("Square Footage");
        headerRow.createCell(4).setCellValue("Price per sqm");
        headerRow.createCell(5).setCellValue("Floor Number");
        headerRow.createCell(6).setCellValue("Link");

        int rowNum = 1;

        while (true) {
            List<WebElement> listings = driver.findElements(By.cssSelector("article[data-cy='listing-item']"));

            for (WebElement listing : listings) {
                Row row = sheet.createRow(rowNum++);

                String price = listing.findElement(By.cssSelector("span[class='css-2bt9f1 evk7nst0']")).getText();
                row.createCell(0).setCellValue(price);
                htmlReport.append("<td>").append(price).append("</td>");

                String location = listing.findElement(By.cssSelector("p[class='css-42r2ms eejmx80']")).getText();
                row.createCell(1).setCellValue(location);
                htmlReport.append("<td>").append(location).append("</td>");

                String info = listing.findElement(By.cssSelector("dl[class='css-12dsp7a e1clni9t1']")).getText().trim();
                String[] infoParts = info.split("\n");

                String rooms = "N/A";
                String squareFootage = "N/A";
                String pricePerSqm = "N/A";
                String floorNumber = "N/A";

                for (String part : infoParts) {
                    if (part.contains("pokoje")) {
                        rooms = part.trim();
                    } else if (part.contains("m²") && !part.contains("zł/m²")) {
                        squareFootage = part.trim();
                    } else if (part.contains("zł/m²")) {
                        pricePerSqm = part.trim();
                    } else if (part.contains("piętro")) {
                        floorNumber = part;
                    }
                }

                row.createCell(2).setCellValue(rooms);
                row.createCell(3).setCellValue(squareFootage);
                row.createCell(4).setCellValue(pricePerSqm);
                row.createCell(5).setCellValue(floorNumber);

                htmlReport.append("<td>").append(rooms).append("</td>");
                htmlReport.append("<td>").append(squareFootage).append("</td>");
                htmlReport.append("<td>").append(pricePerSqm).append("</td>");
                htmlReport.append("<td>").append(floorNumber).append("</td>");

                try {
                    WebElement imgElement = listing.findElement(By.cssSelector("img[data-cy='listing-item-image-source']"));
                    String imgUrl = imgElement.getAttribute("src");
                    htmlReport.append("<td><img src='").append(imgUrl).append("' width='400' height='230'></td>");
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    htmlReport.append("<td>No Image</td>");
                }

                WebElement linkElement = listing.findElement(By.cssSelector("a[data-cy='listing-item-link']"));
                String link = linkElement.getAttribute("href");
                row.createCell(6).setCellValue(link);
                htmlReport.append("<td><a href='").append(link).append("'>Link</a></td>");

                htmlReport.append("</tr>");
            }

            WebElement nextPageButton = driver.findElement(By.cssSelector("li[aria-label='Go to next Page']"));
            boolean isNextButton;
            isNextButton = nextPageButton.isDisplayed();

            if (!isNextButton) {
                System.out.println("No more pages to navigate. Exiting.");
                break;
            }

            nextPageButton.click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        htmlReport.append("</table></body></html>");

        try (FileOutputStream fileOut = new FileOutputStream("real_estate_data.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("real_estate_data.html"))) {
            writer.write(htmlReport.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.quit();
        System.out.println("Data has been successfully extracted");
    }
}
