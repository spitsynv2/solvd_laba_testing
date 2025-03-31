package com.solvd.mobile.android;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ClockHomePage extends AbstractPage {

    @FindBy(id = "com.google.android.deskclock:id/tab_menu_alarm")
    private ExtendedWebElement alarmMenuBlock;

    public ClockHomePage(WebDriver driver) {
        super(driver);
    }

    public ClockAlarmPage goToAlarmPage(){
        alarmMenuBlock.click();
        return new ClockAlarmPage(getDriver());
    }
}
