package Selenium.Otodom_scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PropertyType {

    public static void selectType(WebDriver driver, String type){

        driver.findElement(By.cssSelector("div[data-cy='search-form--field--estate']")).click();
        driver.findElement(By.id("react-select-estate-option-" + type)).click();
    }

}
