package Selenium.Otodom_scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

//change the number in data.properties to select different type, 0 = apartment, 1 = studio apartment, 2 = house,
// 3 = investments, 4 = rooms, 5 = plot, 6 = business premises, 7 = warehouses, 8 = garages

public class PropertyType {

    public static void selectType(WebDriver driver, String type){

        driver.findElement(By.cssSelector("div[data-cy='search-form--field--estate']")).click();
        driver.findElement(By.id("react-select-estate-option-" + type)).click();
    }

}
