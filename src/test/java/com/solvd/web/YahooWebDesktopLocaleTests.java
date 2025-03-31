package com.solvd.web;

import com.solvd.web.gui.pages.common.yahoo.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.utils.resources.L10N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class YahooWebDesktopLocaleTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void localeTest() {
        YahooHomePageBase yahooHomePageBase = initPage(getDriver(),YahooHomePageBase.class);
        yahooHomePageBase.open();

        YahooHomePageLocalBase yahooHomePageLocalBase = yahooHomePageBase.goToLocalPage();

        SoftAssert softAssert = new SoftAssert();
        String actualTrendingTitleText = yahooHomePageLocalBase.getTrendingTitleText();
        String expectedTrendingTitleText = L10N.getText("YahooHomePageLocal.trendingTitle");
        softAssert.assertEquals(actualTrendingTitleText,expectedTrendingTitleText, "Text not the same");
        softAssert.assertAll();
    }
}
