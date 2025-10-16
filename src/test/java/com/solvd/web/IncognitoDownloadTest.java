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
        downloadFileFromContainer(seleniumUrl, sessionId, "pexels");

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

    private void downloadFileFromContainer(URL seleniumUrl, String sessionId, String filenamePrefix) {
        try {
            // Get the router URL from selenium URL (replace /wd/hub with empty string)
            String routerUrl = seleniumUrl.toString();

            // Try different possible download paths in the container
            String[] possiblePaths = {
                    "tmp/downloads/"
            };

            for (String path : possiblePaths) {
                String downloadUrl = String.format("%s/download/%s/%s", routerUrl, sessionId, path);
                LOGGER.info("Attempting to access: " + downloadUrl);

                if (downloadDirectoryFromContainer(downloadUrl, filenamePrefix)) {
                    LOGGER.info("Successfully downloaded files from path: " + path);
                    return;
                }
            }

            LOGGER.warn("No files found in any of the attempted container paths");

        } catch (Exception e) {
            LOGGER.error("Failed to download files from container", e);
        }
    }

    private boolean downloadDirectoryFromContainer(String downloadUrl, String filenamePrefix) {
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            LOGGER.info("Response code for " + downloadUrl + ": " + responseCode);

            if (responseCode == 200) {
                // Create local download directory
                Path localDownloadDir = Paths.get("tmp/downloads");
                Files.createDirectories(localDownloadDir);

                // Read the response (could be a file or directory listing)
                try (InputStream inputStream = conn.getInputStream()) {
                    // For now, just log the content type and size
                    String contentType = conn.getContentType();
                    long contentLength = conn.getContentLengthLong();

                    LOGGER.info("Content-Type: " + contentType + ", Content-Length: " + contentLength);

                    if (contentType != null && !contentType.startsWith("text/html")) {
                        // It's likely a file, save it
                        String fileName = filenamePrefix + "_downloaded_file";
                        Path filePath = localDownloadDir.resolve(fileName);

                        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }

                        LOGGER.info("File saved to: " + filePath.toAbsolutePath());
                        return true;
                    } else {
                        // It's likely an HTML directory listing, log it
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            StringBuilder content = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                content.append(line).append("\n");
                            }
                            LOGGER.info("Directory listing: " + content.toString());
                        }
                    }
                }
                return true;
            } else {
                LOGGER.warn("Failed to access " + downloadUrl + " with response code: " + responseCode);
            }

        } catch (Exception e) {
            LOGGER.warn("Error accessing " + downloadUrl + ": " + e.getMessage());
        }

        return false;
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
