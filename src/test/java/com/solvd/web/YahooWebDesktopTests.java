package com.solvd.web;

import com.solvd.web.gui.pages.common.yahoo.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class YahooWebDesktopTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void weatherModulesLoadTest() {
        YahooHomePageBase yahooHomePageBase = initPage(getDriver(),YahooHomePageBase.class);
        yahooHomePageBase.open();

        WeatherPageBase weatherPageBase = yahooHomePageBase.goToWeatherPage();

        List<String> expectedModules = List.of("Forecast","Details","Sun & Moon","Precipitation","Wind & Pressure");

        Assert.assertEquals(weatherPageBase.getModuleLabels(),expectedModules,"Page not contains all weather modules");
    }

    @Test
    public void teslaChartTest() {
        YahooHomePageBase yahooHomePageBase = initPage(getDriver(),YahooHomePageBase.class);
        yahooHomePageBase.open();

        FinancePageBase financePageBase = yahooHomePageBase.goToFinancePage();
        MostStockActivePageBase mostStockActivePageBase = financePageBase.goToMostStockActivePage();
        TeslaStockChartPageBase teslaStockChartPageBase = mostStockActivePageBase.goToTeslaStockChartPage();
        List<String> openValues = teslaStockChartPageBase.getLastAndPreviousOpenValues();
        LOGGER.info("Tesla charts open values {}", openValues);
        Assert.assertNotNull(openValues);
    }
}
