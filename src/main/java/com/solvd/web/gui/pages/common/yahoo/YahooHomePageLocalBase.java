package com.solvd.web.gui.pages.common.yahoo;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class YahooHomePageLocalBase extends AbstractPage {

    public YahooHomePageLocalBase(WebDriver driver) {
        super(driver);
    }

    public abstract String getTrendingTitleText();

}
