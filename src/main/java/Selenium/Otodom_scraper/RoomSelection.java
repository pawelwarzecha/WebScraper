package Selenium.Otodom_scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

//class for selecting the number of rooms filter, can apply more than one filter or none
public class RoomSelection {

    public static void selectRooms(WebDriver driver, String numberOfRooms) {

        String[] rooms = numberOfRooms.split(",");

        for (String room : rooms) {
            switch (room.trim()) {
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
        }
    }
}