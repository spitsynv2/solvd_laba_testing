package com.solvd.web;

import com.solvd.web.gui.pages.common.ebay.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.utils.common.CommonUtils;
import com.zebrunner.carina.webdriver.CarinaDriver;
import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.TestPhase;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EbayWebDesktopTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /*
    @Test(dataProvider = "DataProvider")
    @MethodOwner(owner = "Laba")
    @XlsDataSourceParameters(path = "data_source/testData.xlsx", sheet = "Page1", dsUid = "TUID")
    public void checkoutItemWithEncryptedDataTest(Map<String, String> args) {
        EbayHomePageBase ebayHomePage = initPage(getDriver(),EbayHomePageBase.class);
        ebayHomePage.open();

        SearchResultPageBase searchResultPage = ebayHomePage.searchForItem(args.get("searchText"),args.get("category"));
        ItemPageBase itemPage = searchResultPage.selectFirstResultItem();
        CheckoutPageBase checkoutPage = itemPage.goToCheckOutPage();
        CheckoutForm checkoutForm = new CheckoutForm(
                args.get("country"),
                args.get("firstName"),
                args.get("lastName"),
                args.get("city"),
                args.get("email"),
                args.get("countryCode"),
                args.get("phone"));
        checkoutPage.checkout(checkoutForm);
    }
    */

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest(String TUID, int position) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        RemoteWebDriver remoteDriver = new RemoteWebDriver(options);

        // Register it in the DRIVERS_POOL
        CarinaDriver carinaDriver = new CarinaDriver(
                IDriverPool.DEFAULT,
                remoteDriver,
                IDriverPool.getNullDevice(), // or your Device
                TestPhase.getActivePhase(),
                Thread.currentThread().getId(),
                options
        );

        IDriverPool.DRIVERS_POOL
                .computeIfAbsent(Thread.currentThread().getId(), k -> new ConcurrentHashMap<>())
                .put(IDriverPool.DEFAULT, carinaDriver);

        EbayHomePageBase ebayHomePage = initPage(getDriver(),EbayHomePageBase.class);
        ebayHomePage.open();

        CategoryPageBase electronicsPage = ebayHomePage.selectCategory("Electronics");

        ComputersTabletsNetworkPageBase computersTabletsNetworkPage = electronicsPage.openComputersTabletsNetworkPage();
        String limitedTimeDealItemName = computersTabletsNetworkPage.getLimitedTimeDealsItemName(position);

        ItemPageBase itemPageBase = computersTabletsNetworkPage.selectLimitedTimeDealsItem(position);
        String expectedItemName = itemPageBase.getItemName();

        Assert.assertEquals(limitedTimeDealItemName,expectedItemName);
    }

    @DataProvider(name = "DP1")
    public Object[][] dataprovider() {
        return new Object[][]{
                {"TUID: Test position0",0},
                {"TUID: Test position1",1},
                {"TUID: Test position2",2}
        };
    }
}
