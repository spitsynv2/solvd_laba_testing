package com.solvd.web;

import com.zebrunner.agent.core.webdriver.RemoteWebDriverFactory;
import com.zebrunner.carina.core.AbstractTest;
import com.solvd.web.gui.pages.PexelsMainPage;
import com.zebrunner.carina.utils.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.decorators.Decorated;
import org.testng.annotations.Test;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncognitoDownloadTest extends AbstractTest {

    private static final Logger LOGGER = LogManager.getLogger(IncognitoDownloadTest.class);

    @Test
    public void testPexelsDownloadInIncognito() {
        String downloadPath = prepareDownloadDirectory();
        ChromeOptions options = getIncognitoChromeOptions();

        WebDriver webDriver = getDriver("Chrome Browser", options);
        RemoteWebDriver driver = unwrapRemoteDriver(webDriver);

        String seleniumUrl = getSeleniumUrl().toString();
        String sessionId = driver.getSessionId().toString();

        setDownloadBehavior(seleniumUrl, sessionId, downloadPath);

        driver.get("https://www.pexels.com/");
        PexelsMainPage pexelsPage = new PexelsMainPage(driver);
        pexelsPage.openPage();
        pexelsPage.acceptCookiesIfPresent();
        pexelsPage.clickDownload();

        //LOGGER.info("Waiting for downloads to complete...");
        //waitForDownloadsToFinish(seleniumUrl, sessionId);

        LOGGER.info("Downloading all files from container...");
        downloadAllFilesFromContainer(seleniumUrl, sessionId);

        pause(1500);

        driver.quit();
    }

    private RemoteWebDriver unwrapRemoteDriver(WebDriver webDriver) {
        if (webDriver instanceof Decorated<?>) {
            return (RemoteWebDriver) ((Decorated<?>) webDriver).getOriginal();
        }
        return (RemoteWebDriver) webDriver;
    }

    private String prepareDownloadDirectory() {
        String downloadPath = "/home/selenium/Downloads";
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

    private void setDownloadBehavior(String seleniumUrl, String sessionId, String downloadPath) {
        String cmd = "Page.setDownloadBehavior";
        String paramsJson = String.format(
                "{\"behavior\":\"allow\",\"downloadPath\":\"%s\",\"eventsEnabled\":true}", downloadPath
        );

        try {
            sendCDPCommand(seleniumUrl, sessionId, cmd, paramsJson);
            LOGGER.info("✅ Download behavior configured for: " + downloadPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set download behavior", e);
        }
    }

    /*
    private void waitForDownloadsToFinish(String seleniumUrl, String sessionId) {
        String routerUrl = seleniumUrl.replace("/wd/hub", "");
        String listUrl = String.format("%s/download/%s/tmp/downloads/", routerUrl, sessionId);
        long timeout = System.currentTimeMillis() + 60_000; // 1 minute timeout

        while (System.currentTimeMillis() < timeout) {
            try {
                String html = sendSimpleGet(listUrl);
                if (!html.contains(".crdownload")) {
                    LOGGER.info("Downloads completed in container.");
                    return;
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                LOGGER.warn("Waiting for downloads...", e);
            }
        }
        LOGGER.warn("⚠️ Timeout waiting for downloads to complete.");
    }
    /*

     */
    /**
     * Parse the /tmp/downloads/ directory exposed by Selenoid and download each file to target/downloads.
     */
    private void downloadAllFilesFromContainer(String seleniumUrl, String sessionId) {
        try {
            String routerUrl = seleniumUrl.replace("/wd/hub", "");
            String listUrl = String.format("%s/download/%s/", routerUrl, sessionId);
            LOGGER.info("Listing files from: " + listUrl);

            String html = sendSimpleGet(listUrl);

            Pattern pattern = Pattern.compile("href=\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(html);

            Path localDownloadDir = Paths.get("/home/selenium/Downloads");
            Files.createDirectories(localDownloadDir);

            int fileCount = 0;
            while (matcher.find()) {
                String fileName = matcher.group(1);
                if (fileName.equals("../") || fileName.endsWith(".crdownload")) continue;

                String fileUrl = listUrl + fileName;
                Path filePath = localDownloadDir.resolve(fileName);

                LOGGER.info("Downloading: " + fileUrl);

                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    try (InputStream inputStream = conn.getInputStream();
                         FileOutputStream fos = new FileOutputStream(filePath.toFile())) {

                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        long totalBytes = 0;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                            totalBytes += bytesRead;
                        }

                        LOGGER.info("Saved file: {} ({} bytes)", fileName, totalBytes);
                        fileCount++;
                    }
                } else {
                    LOGGER.warn("Failed to download {}, HTTP {}", fileName, conn.getResponseCode());
                }
            }

            LOGGER.info("Total files downloaded: {}", fileCount);

        } catch (Exception e) {
            LOGGER.error("Error downloading files from container", e);
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

    private String sendSimpleGet(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            LOGGER.info(sb.toString());
            return sb.toString();
        }
    }
}
