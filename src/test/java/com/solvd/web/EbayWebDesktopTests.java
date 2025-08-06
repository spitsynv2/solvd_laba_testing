package com.solvd.web;

import com.solvd.web.gui.pages.common.ebay.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.utils.common.CommonUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

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
        int maxAttempts = 20;
        int pauseSeconds = 5;
        ChromeDriver driver = null;
        int attempt = 0;

        while (attempt <= maxAttempts) {
            try {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--incognito");

                driver = new ChromeDriver(options);

                // Set download behavior using CDP
                Map<String, Object> params = new HashMap<>();
                params.put("behavior", "allow");
                params.put("downloadPath", "/downloads");

                driver.executeCdpCommand("Page.setDownloadBehavior", params);

                // ✅ Success! Break the retry loop
                break;

            } catch (Exception e) {
                if (driver != null) {
                    try {
                        driver.quit(); // 🧹 Clean up failed browser
                    } catch (Exception ignored) {}
                }

                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to create WebDriver after retries", e);
                }

                LOGGER.warn("Attempt {} failed: {}. Retrying in {}s...", attempt, e.getMessage(), pauseSeconds);
                CommonUtils.pause(pauseSeconds);
                attempt++;
            }
        }

        EbayHomePageBase ebayHomePage = initPage(driver,EbayHomePageBase.class);
        ebayHomePage.open();

        CategoryPageBase electronicsPage = ebayHomePage.selectCategory("Electronics");

        ComputersTabletsNetworkPageBase computersTabletsNetworkPage = electronicsPage.openComputersTabletsNetworkPage();
        String limitedTimeDealItemName = computersTabletsNetworkPage.getLimitedTimeDealsItemName(position);

        ItemPageBase itemPageBase = computersTabletsNetworkPage.selectLimitedTimeDealsItem(position);
        String expectedItemName = itemPageBase.getItemName();

        Assert.assertEquals(limitedTimeDealItemName,expectedItemName);
        driver.quit();
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
