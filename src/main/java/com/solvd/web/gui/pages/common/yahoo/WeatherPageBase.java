package com.solvd.web.gui.pages.common.yahoo;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class WeatherPageBase extends AbstractPage {

    public WeatherPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract List<String> getModuleLabels();
}
