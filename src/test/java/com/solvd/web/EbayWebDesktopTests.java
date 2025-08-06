package com.solvd.web;

import com.solvd.web.gui.pages.common.ebay.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.webdriver.CarinaDriver;
import com.zebrunner.carina.webdriver.IDriverPool;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
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

public class EbayWebDesktopTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String EXECUTOR_URL = "http://localhost:4444";  // hardcoded ChromeDriver URL & port

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest(String TUID, int position) throws Exception {
        WebDriver driver = getDriver();

        // unwrap decorated driver
        while (driver instanceof Decorated<?>) {
            driver = (WebDriver) ((Decorated<?>) driver).getOriginal();
        }

        // augment to RemoteWebDriver if needed
        driver = new Augmenter().augment(driver);

        // Cast to RemoteWebDriver for session info
        RemoteWebDriver remoteDriver = (RemoteWebDriver) driver;

        // Raw CDP command using manual HTTP call
        String networkEnableResponse = RawCDPCommandSender.sendCommand(remoteDriver, "Network.enable", "{}");
        LOGGER.info("Network.enable CDP response: " + networkEnableResponse);

        // Your normal test flow below

        EbayHomePageBase ebayHomePage = initPage(getDriver(), EbayHomePageBase.class);
        ebayHomePage.open();

        CategoryPageBase electronicsPage = ebayHomePage.selectCategory("Electronics");
        ComputersTabletsNetworkPageBase computersTabletsNetworkPage = electronicsPage.openComputersTabletsNetworkPage();
        String limitedTimeDealItemName = computersTabletsNetworkPage.getLimitedTimeDealsItemName(position);

        ItemPageBase itemPageBase = computersTabletsNetworkPage.selectLimitedTimeDealsItem(position);
        String expectedItemName = itemPageBase.getItemName();

        Assert.assertEquals(limitedTimeDealItemName, expectedItemName);
    }

    // unchanged logCurrentDriverInfoUnwrapped() here ...

    @DataProvider(name = "DP1")
    public Object[][] dataprovider() {
        return new Object[][]{
                {"TUID: Test position0", 0},
                {"TUID: Test position1", 1},
                {"TUID: Test position2", 2}
        };
    }

    // Static helper class for raw CDP commands
    public static class RawCDPCommandSender {
        public static String sendCommand(RemoteWebDriver driver, String cmd, String paramsJson) throws Exception {
            String sessionId = driver.getSessionId().toString();
            String resource = EXECUTOR_URL + "/session/" + sessionId + "/chromium/send_command_and_get_result";

            URL url = new URL(resource);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonBody = String.format("{\"cmd\":\"%s\",\"params\":%s}", cmd, paramsJson);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + code);
            }

            try (var br = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        }
    }
}
