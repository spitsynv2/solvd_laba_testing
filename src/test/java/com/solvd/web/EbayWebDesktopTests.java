package com.solvd.web;

import com.solvd.web.gui.pages.common.ebay.*;
import com.zebrunner.agent.core.webdriver.RemoteWebDriverFactory;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.utils.common.CommonUtils;
import com.zebrunner.carina.webdriver.CarinaDriver;
import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.TestPhase;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.decorators.Decorated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

        URL seleniumUrl = RemoteWebDriverFactory.getSeleniumHubUrl();

        RemoteWebDriver remoteDriver = new RemoteWebDriver(seleniumUrl, options);



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

        logCurrentDriverInfoUnwrapped();

        EbayHomePageBase ebayHomePage = initPage(getDriver(),EbayHomePageBase.class);
        ebayHomePage.open();

        String sessionId = remoteDriver.getSessionId().toString();
        String cmd = "Page.setDownloadBehavior";
        String paramsJson = "{\"behavior\":\"allow\",\"downloadPath\":\"/tmp/downloads\"}";
        try {
            sendCDPCommand(RemoteWebDriverFactory.getSeleniumHubUrl().toString(),sessionId,cmd,paramsJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CategoryPageBase electronicsPage = ebayHomePage.selectCategory("Electronics");

        ComputersTabletsNetworkPageBase computersTabletsNetworkPage = electronicsPage.openComputersTabletsNetworkPage();
        String limitedTimeDealItemName = computersTabletsNetworkPage.getLimitedTimeDealsItemName(position);

        ItemPageBase itemPageBase = computersTabletsNetworkPage.selectLimitedTimeDealsItem(position);
        String expectedItemName = itemPageBase.getItemName();

        Assert.assertEquals(limitedTimeDealItemName,expectedItemName);
    }

    public void logCurrentDriverInfoUnwrapped() {
        WebDriver driver = getDriver();  // get default driver

        // Find matching CarinaDriver by comparing WebDriver references
        Map<String, CarinaDriver> drivers = IDriverPool.getDrivers();
        CarinaDriver foundCarinaDriver = null;

        for (CarinaDriver carinaDriver : drivers.values()) {
            if (carinaDriver.getDriver().equals(driver)) {
                foundCarinaDriver = carinaDriver;
                break;
            }
        }

        if (foundCarinaDriver != null) {
            Capabilities originalCapabilities = foundCarinaDriver.getOriginalCapabilities();

            LOGGER.warn("Original Capabilities: " + originalCapabilities);

            // Unwrap driver if decorated
            WebDriver unwrappedDriver = driver;
            if (driver instanceof Decorated<?>) {
                unwrappedDriver = (WebDriver) ((Decorated<?>) driver).getOriginal();
            }

            if (unwrappedDriver instanceof RemoteWebDriver) {
                RemoteWebDriver remoteDriver = (RemoteWebDriver) unwrappedDriver;
                SessionId sessionId = remoteDriver.getSessionId();
                LOGGER.warn("Session ID: " + sessionId);

                Capabilities actualCaps = remoteDriver.getCapabilities();
                LOGGER.warn("Actual Capabilities: " + actualCaps);
            } else {
                LOGGER.warn("Unwrapped driver is not a RemoteWebDriver instance");
            }
        } else {
            LOGGER.warn("Could not find CarinaDriver associated with current WebDriver instance");
        }
    }

    @DataProvider(name = "DP1")
    public Object[][] dataprovider() {
        return new Object[][]{
                {"TUID: Test position0",0},
                {"TUID: Test position1",1},
                {"TUID: Test position2",2}
        };
    }

    public void sendCDPCommand(String selenoidHost, String sessionId, String cmd, String paramsJson) throws Exception {
        String url = String.format("http://%s/wd/hub/session/%s/goog/cdp/execute", selenoidHost, sessionId);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String body = String.format("{\"cmd\":\"%s\",\"params\":%s}", cmd, paramsJson);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to execute CDP command, HTTP code: " + responseCode);
        }
    }
}
