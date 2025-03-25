/*******************************************************************************
 * Copyright 2020-2023 Zebrunner Inc (https://www.zebrunner.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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

/**
 * This sample shows how create Web test.
 *
 * @author Laba
 */
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
