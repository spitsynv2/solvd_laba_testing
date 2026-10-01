package com.solvd.web;

import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class WebDriverBaselineTests implements IAbstractTest {
    private static final String WEB_FORM_URL = "https://www.selenium.dev/selenium/web/web-form.html";
    private static final String EXAMPLE_URL = "https://example.com/";

    @Test
    @MethodOwner(owner = "VS")
    public void webDriverBasicCommandsTest() {
        WebDriver driver = getDriver();
        Assert.assertNotNull(driver, "WebDriver session was not created");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().setSize(new Dimension(1280, 720));
        driver.get(WEB_FORM_URL);

        Assert.assertEquals(driver.getTitle(), "Web form");
        Assert.assertEquals(driver.getCurrentUrl(), WEB_FORM_URL);

        WebElement textInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("my-text")));
        textInput.clear();
        textInput.sendKeys("baseline text");

        WebElement passwordInput = driver.findElement(By.name("my-password"));
        passwordInput.clear();
        passwordInput.sendKeys("baseline-password");

        WebElement textarea = driver.findElement(By.name("my-textarea"));
        textarea.clear();
        textarea.sendKeys("Browser form interaction is stable.");

        new Select(driver.findElement(By.name("my-select"))).selectByVisibleText("Two");
        driver.findElement(By.name("my-datalist")).sendKeys("Seattle");

        WebElement defaultCheckbox = driver.findElement(By.id("my-check-2"));
        if (!defaultCheckbox.isSelected()) {
            defaultCheckbox.click();
        }
        Assert.assertTrue(defaultCheckbox.isSelected(), "Default checkbox was not selected");

        WebElement defaultRadio = driver.findElement(By.id("my-radio-2"));
        defaultRadio.click();
        Assert.assertTrue(defaultRadio.isSelected(), "Default radio was not selected");

        Object readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState");
        Assert.assertEquals(readyState, "complete");
        Assert.assertTrue(driver.getPageSource().contains("Web form"), "Page source is not available");

        String originalWindow = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(EXAMPLE_URL);
        Assert.assertEquals(driver.getTitle(), "Example Domain");
        driver.close();
        driver.switchTo().window(originalWindow);
        Assert.assertEquals(driver.getTitle(), "Web form");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        Assert.assertEquals(message.getText(), "Received!");

        driver.navigate().back();
        wait.until(ExpectedConditions.titleIs("Web form"));
        Assert.assertEquals(driver.findElement(By.name("my-text")).getDomProperty("value"), "baseline text");

        driver.navigate().refresh();
        wait.until(ExpectedConditions.titleIs("Web form"));

        driver.navigate().forward();
        wait.until(ExpectedConditions.urlContains("submitted-form.html"));
        Assert.assertEquals(driver.findElement(By.id("message")).getText(), "Received!");
    }
}
