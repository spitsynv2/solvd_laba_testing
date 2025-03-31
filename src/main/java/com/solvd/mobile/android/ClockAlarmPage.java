package com.solvd.mobile.android;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ClockAlarmPage extends AbstractPage {

    @FindBy(id = "com.google.android.deskclock:id/fab")
    private ExtendedWebElement addAlarmButton;

    @FindBy(id = "com.google.android.deskclock:id/material_timepicker_ok_button")
    private ExtendedWebElement confirmationButton;

    @FindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"com.google.android.deskclock:id/alarm_card_layout\"])[1]")
    private ExtendedWebElement firstAlarm;

    @FindBy(id = "com.google.android.deskclock:id/delete")
    private ExtendedWebElement deleteAlarmButton;

    public ClockAlarmPage(WebDriver driver) {
        super(driver);
    }

    public void createAlarm(){
        addAlarmButton.click(3);
        confirmationButton.click(3);
    }

    public void deleteFirstAlarm(){
        firstAlarm.click(3);
        deleteAlarmButton.click(3);
    }
}
