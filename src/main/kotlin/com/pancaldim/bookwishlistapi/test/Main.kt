package com.pancaldim.bookwishlistapi.test

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import java.lang.Exception
import java.util.concurrent.TimeUnit

fun main() {
    System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
    val driver: WebDriver = ChromeDriver()
    driver.manage().window().maximize()
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS)

    driver.get("https://www.amazon.co.uk/hz/wishlist/genericItemsPage/1OK2Q8NZ7P6D3")
    val jse = driver as JavascriptExecutor
    var lastHeight: Int = Integer.parseInt(jse.executeScript("return document.body.scrollHeight").toString())
    var newHeight: Int
    while (elementExists(By.id("endOfListMarker"), driver) == null) {
        jse.executeScript("window.scrollBy(0, 1000)")
        Thread.sleep(500)
        newHeight = Integer.parseInt(jse.executeScript("return document.body.scrollHeight").toString())
//            if (newHeight == lastHeight) {
//                break
//            }
//            lastHeight = newHeight
    }

    println("")
}

fun elementExists(by: By, driver: WebDriver): WebElement? {
    var attempts = 0;
    while (attempts < 5)
        try {
            return driver.findElement(by);
        } catch (e: Exception) {
            Thread.sleep(10);
            attempts++;
        }
    return null
}
