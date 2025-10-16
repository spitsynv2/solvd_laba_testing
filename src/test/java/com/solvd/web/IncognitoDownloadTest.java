package com.solvd.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.decorators.Decorated;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.zebrunner.agent.core.webdriver.RemoteWebDriverFactory;
import com.solvd.web.gui.pages.PexelsMainPage;
import com.zebrunner.carina.core.AbstractTest;


import com.zebrunner.carina.utils.R;

public class IncognitoDownloadTest extends AbstractTest {

    private static final Logger LOGGER = LogManager.getLogger(IncognitoDownloadTest.class);

    @Test
    public void testPexelsDownloadInIncognito() {
        String downloadPath = prepareDownloadDirectory();

        ChromeOptions options = getIncognitoChromeOptions();

        URL seleniumUrl = getSeleniumUrl();

        WebDriver webDriver = getDriver("Chrome Browser", options, seleniumUrl.toString());
        RemoteWebDriver driver = unwrapRemoteDriver(webDriver);

        setDownloadBehavior(seleniumUrl, driver, downloadPath);

        driver.get("https://www.pexels.com/");

        PexelsMainPage pexelsPage = new PexelsMainPage(driver);
        pexelsPage.openPage();
        pexelsPage.acceptCookiesIfPresent();
        pexelsPage.clickDownload();

        pause(10);

        verifyDownloadedFile(downloadPath, "pexels");

        pause(10);
    }

    private RemoteWebDriver unwrapRemoteDriver(WebDriver webDriver) {
        if (webDriver instanceof Decorated<?>) {
            return (RemoteWebDriver) ((Decorated<?>) webDriver).getOriginal();
        }
        return (RemoteWebDriver) webDriver;
    }
    private String prepareDownloadDirectory() {
        String downloadPath = "/tmp/downloads";
        new File(downloadPath).mkdirs();
        return downloadPath;
    }
    private ChromeOptions getIncognitoChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        return options;
    }

    private URL getSeleniumUrl() {
        URL seleniumUrl = RemoteWebDriverFactory.getSeleniumHubUrl();
        if (seleniumUrl == null) {
            try {
                seleniumUrl = new URL(R.CONFIG.get("selenium_url"));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Selenium URL", e);
            }
        }
        return seleniumUrl;
    }

    private void setDownloadBehavior(URL seleniumUrl, RemoteWebDriver driver, String downloadPath) {
        String sessionId = driver.getSessionId().toString();
        String cmd = "Page.setDownloadBehavior";
        String paramsJson = String.format(
                "{\"behavior\":\"allow\",\"downloadPath\":\"%s\",\"eventsEnabled\":true}", downloadPath
        );

        try {
            sendCDPCommand(seleniumUrl.toString(), sessionId, cmd, paramsJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set download behavior", e);
        }
    }

    private void verifyDownloadedFile(String downloadPath, String filenamePrefix) {
        File folder = new File(downloadPath);
        File[] matchingFiles = folder.listFiles((dir, name) -> name.startsWith(filenamePrefix));

        if (matchingFiles != null && matchingFiles.length > 0) {
            LOGGER.info("Found file: " + matchingFiles[0].getName());
        } else {
            LOGGER.info("No file starting with '" + filenamePrefix + "' found.");
        }
    }

    public void sendCDPCommand(String selenoidHost, String sessionId, String cmd, String paramsJson) throws Exception {
        String url = String.format("%s/session/%s/goog/cdp/execute", selenoidHost, sessionId);
        LOGGER.warn(url);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        String body = String.format("{\"cmd\":\"%s\",\"params\":%s}", cmd, paramsJson);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();

        InputStream is;
        if (responseCode >= 200 && responseCode < 300) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append("\n");
            }
        }

        LOGGER.info("CDP command response: " + response);

        if (responseCode != 200) {
            LOGGER.warn("Failed to execute CDP command, HTTP code: " + responseCode + ", response: " + response);
        }
    }
}
