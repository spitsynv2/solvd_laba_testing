package com.solvd.web;

import com.zebrunner.agent.core.webdriver.RemoteWebDriverFactory;
import com.zebrunner.carina.core.AbstractTest;
import com.solvd.web.gui.pages.PexelsMainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.decorators.Decorated;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        String sessionId = driver.getSessionId().toString();

        // Get actual downloaded file information from browser
        String downloadedFileName = getDownloadedFileNameFromBrowser(seleniumUrl, sessionId);

        if (downloadedFileName != null) {
            LOGGER.info("Found downloaded file: " + downloadedFileName);
            downloadSpecificFileFromContainer(seleniumUrl, sessionId, downloadedFileName);
        } else {
            LOGGER.warn("Could not determine downloaded file name");
        }

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



    private String getDownloadedFileNameFromBrowser(URL seleniumUrl, String sessionId) {
        try {
            // Try to get download information using CDP
            String cmd = "Browser.getDownloadCommands";
            String params = "{}";

            String response = sendCDPCommand(seleniumUrl.toString(), sessionId, cmd, params);
            LOGGER.info("Download info response: " + response);

            // Parse response to extract filename
            if (response != null && response.contains("\"filename\"")) {
                int start = response.indexOf("\"filename\":\"") + 12;
                int end = response.indexOf("\"", start);
                if (end > start) {
                    return response.substring(start, end);
                }
            }

        } catch (Exception e) {
            LOGGER.warn("Could not get download info from CDP: " + e.getMessage());
        }

        return null;
    }

    private void downloadSpecificFileFromContainer(URL seleniumUrl, String sessionId, String fileName) {
        try {
            String routerUrl = seleniumUrl.toString();
            String fileUrl = String.format("%s/download/%s/tmp/downloads/%s", routerUrl, sessionId, fileName);

            LOGGER.info("Attempting to download specific file: " + fileUrl);

            Path localDownloadDir = Paths.get("target/downloads");
            Files.createDirectories(localDownloadDir);

            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            LOGGER.info("Response code: " + responseCode);

            if (responseCode == 200) {
                Path filePath = localDownloadDir.resolve(fileName);

                try (InputStream inputStream = conn.getInputStream();
                     FileOutputStream fos = new FileOutputStream(filePath.toFile())) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    long totalBytes = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                    }

                    LOGGER.info("Successfully downloaded file: " + fileName + " (" + totalBytes + " bytes) to " + filePath.toAbsolutePath());
                }
            } else {
                LOGGER.warn("Failed to download file with response code: " + responseCode);
            }

        } catch (Exception e) {
            LOGGER.error("Error downloading specific file: " + fileName, e);
        }
    }

    public String sendCDPCommand(String selenoidHost, String sessionId, String cmd, String paramsJson) throws Exception {
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

        return response.toString();
    }
}
