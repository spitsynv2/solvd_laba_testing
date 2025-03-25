package com.solvd.web.gui.pages.common.yahoo;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class MostStockActivePageBase extends AbstractPage {
    public MostStockActivePageBase(WebDriver driver) {
        super(driver);
    }

    public abstract TeslaStockChartPageBase goToTeslaStockChartPage();
}
