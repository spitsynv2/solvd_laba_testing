package com.solvd.web;

import com.zebrunner.agent.core.webdriver.RemoteWebDriverFactory;
import com.zebrunner.carina.core.AbstractTest;
import com.solvd.web.gui.pages.PexelsMainPage;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.report.ReportContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.decorators.Decorated;
import org.testng.annotations.Test;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class IncognitoDownloadTest extends AbstractTest {
    private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void testPexelsDownloadInIncognito() {
        ChromeOptions options = getIncognitoChromeOptions();
        WebDriver webDriver = getDriver("Chrome", options);
        RemoteWebDriver driver = unwrapRemoteDriver(webDriver);

        String downloadPath = prepareDownloadDirectory();
        String seleniumUrl = getSeleniumUrl().toString();
        String sessionId = driver.getSessionId().toString();

        setDownloadBehavior(seleniumUrl, sessionId, downloadPath);

        driver.get("https://www.pexels.com/");
        PexelsMainPage pexelsPage = new PexelsMainPage(driver);
        pexelsPage.openPage();
        pexelsPage.acceptCookiesIfPresent();
        pexelsPage.clickDownload();

        LOGGER.info("Waiting for downloads to complete...");
        pause(5);

        String artifactFileName = "pexels-david-wilkowski-432252441-29278193.jpg";
        getSessionArtifact(driver, artifactFileName);
        saveFileToReportContext(downloadPath, artifactFileName);
    }

    private void getSessionArtifact(RemoteWebDriver driver, String artifactFileName) {
        String seleniumUrl = R.CONFIG.get("selenium_url");

        if (seleniumUrl.contains("localhost")) {
            LOGGER.debug("Skipping artifact retrieval - running on localhost (URL: {})", seleniumUrl);
            return;
        }

        try {
            File file = ReportContext.getArtifact(driver, artifactFileName);
            LOGGER.info("Remote run detected — artifact '{}' downloaded and attached to session in reporting portal successfully.", file.getName());
            LOGGER.info("File full path - {}", file.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve artifact '{}' for driver: {}", artifactFileName, driver, e);
        }
    }

    private static void saveFileToReportContext(String downloadPath, String artifactFileName) {
        String seleniumUrl = R.CONFIG.get("selenium_url");

        if (seleniumUrl.contains("localhost")) {
            String filePath = downloadPath + "/" + artifactFileName;
            try {
                ReportContext.saveArtifact(new File(filePath));
                LOGGER.info("Local artifact '{}' saved to report context successfully.", artifactFileName);
            } catch (IOException e) {
                LOGGER.error("Failed to save local artifact '{}' to report context.", artifactFileName, e);
            }
        } else {
            LOGGER.debug("Skipping artifact save - remote run detected (selenium_url: {})", seleniumUrl);
        }
    }

    private String prepareDownloadDirectory() {
        String downloadPath;
        if (R.CONFIG.get("selenium_url").contains("localhost")) {
            downloadPath = "/Users/vadymspitsyn/IdeaProjects/solvd_laba_testing/src/test/resources/downloadsV2";
            LOGGER.info("Using local download path: " + downloadPath);
        } else {
            downloadPath = "/home/selenium/Downloads";
            LOGGER.info("Using remote download path: " + downloadPath);
        }
        return downloadPath;
    }

    private RemoteWebDriver unwrapRemoteDriver(WebDriver webDriver) {
        if (webDriver instanceof Decorated<?>) {
            return (RemoteWebDriver) ((Decorated<?>) webDriver).getOriginal();
        }
        return (RemoteWebDriver) webDriver;
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

    private void setDownloadBehavior(String seleniumUrl, String sessionId, String downloadPath) {
        String cmd = "Page.setDownloadBehavior";
        String paramsJson = String.format(
                "{\"behavior\":\"allow\",\"downloadPath\":\"%s\",\"eventsEnabled\":true}", downloadPath
        );

        try {
            sendCDPCommand(seleniumUrl, sessionId, cmd, paramsJson);
            LOGGER.info("Download behavior configured for: " + downloadPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set download behavior", e);
        }
    }

    public String sendCDPCommand(String selenoidHost, String sessionId, String cmd, String paramsJson) throws Exception {
        String url = String.format("%s/session/%s/goog/cdp/execute", selenoidHost, sessionId);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        String body = String.format("{\"cmd\":\"%s\",\"params\":%s}", cmd, paramsJson);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        InputStream is = conn.getResponseCode() >= 200 && conn.getResponseCode() < 300 ?
                conn.getInputStream() : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append('\n');
            }
        }

        LOGGER.debug("CDP response: {}", response);
        return response.toString();
    }
}