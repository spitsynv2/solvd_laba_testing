package com.solvd.web.gui.pages.common.yahoo;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class YahooHomePageBase extends AbstractPage {

    public YahooHomePageBase(WebDriver driver) {
        super(driver);
    }

    public abstract WeatherPageBase goToWeatherPage();

    public abstract FinancePageBase goToFinancePage();

    public abstract YahooHomePageLocalBase goToLocalPage();

}
