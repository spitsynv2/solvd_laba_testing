package com.solvd.web.gui.util;

import org.openqa.selenium.WebDriver;

public final class BrowserWindowsUtil {
    public static void switchToLastWindow(WebDriver driver){
        String lastWindowHandle = "";
        for (String handle : driver.getWindowHandles()) {
            lastWindowHandle = handle;
        }
        driver.switchTo().window(lastWindowHandle);
    }
}
