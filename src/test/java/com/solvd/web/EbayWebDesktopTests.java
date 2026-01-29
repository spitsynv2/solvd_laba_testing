package com.solvd.web;

import com.solvd.web.gui.pages.common.ebay.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.webdriver.CarinaDriver;
import com.zebrunner.carina.webdriver.IDriverPool;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.decorators.Decorated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EbayWebDesktopTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Track last test end per thread so we can measure gaps between tests on the same worker
    private static final ConcurrentHashMap<Long, Long> LAST_TEST_END_MS = new ConcurrentHashMap<>();

    // Keep per-test start time (to compute durations)
    private final ThreadLocal<Long> testStartMs = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void logStart(Method method, Object[] params) {
        long now = System.currentTimeMillis();
        long tid = Thread.currentThread().getId();

        Long prevEnd = LAST_TEST_END_MS.get(tid);
        long idleGap = (prevEnd == null) ? -1 : (now - prevEnd);

        LOGGER.info("▶ START: {}{} | thread={}{}",
                method.getName(),
                params == null ? "()" : Arrays.toString(params),
                tid,
                (idleGap >= 0 ? String.format(" | idle gap since previous test on this thread=%dms", idleGap) : "")
        );
        testStartMs.set(now);
    }

    @AfterMethod(alwaysRun = true)
    public void logFinish(ITestResult result) {
        long endMs = System.currentTimeMillis();
        Long startMs = testStartMs.get();
        long duration = (startMs == null) ? -1 : (endMs - startMs);

        String status;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS: status = "SUCCESS"; break;
            case ITestResult.FAILURE: status = "FAILURE"; break;
            case ITestResult.SKIP:    status = "SKIPPED"; break;
            default:                  status = "UNKNOWN";
        }

        LOGGER.info("■ END(test body): {} | status={} | duration={}ms | thread={}",
                result.getMethod().getMethodName(), status, duration, Thread.currentThread().getId());

        LAST_TEST_END_MS.put(Thread.currentThread().getId(), System.currentTimeMillis());
    }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest1(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest2(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest3(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest4(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest5(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest6(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest7(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTest8(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTestSingle1(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTestSingle2(String TUID, int position) { runSameFlow(position); }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "VS")
    public void itemTitleEqualsTestSingle3(String TUID, int position) { runSameFlow(position); }

    private void runSameFlow(int position) {
        EbayHomePageBase ebayHomePage = initPage(getDriver(), EbayHomePageBase.class);
        getDriver().manage().window().setSize(new Dimension(1920,1080));
        ebayHomePage.open();
        logCurrentDriverInfoUnwrapped();

        CategoryPageBase electronicsPage = ebayHomePage.selectCategory("Electronics");
        ComputersTabletsNetworkPageBase computersTabletsNetworkPage = electronicsPage.openComputersTabletsNetworkPage();

        String limitedTimeDealItemName = computersTabletsNetworkPage.getLimitedTimeDealsItemName(position);
        ItemPageBase itemPageBase = computersTabletsNetworkPage.selectLimitedTimeDealsItem(position);
        String expectedItemName = itemPageBase.getItemName();

        Assert.assertEquals(limitedTimeDealItemName, expectedItemName);
    }

    @DataProvider(name = "DP1", parallel = true) // keep parallel to observe scheduling/idle gaps
    public Object[][] dataprovider() {
        return new Object[][]{
                {"TUID: Test position0", 0},
                {"TUID: Test position1", 1},
                {"TUID: Test position2", 2}
        };
    }

    public void logCurrentDriverInfoUnwrapped() {
        WebDriver driver = getDriver();  // default driver

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
            LOGGER.warn("Original Capabilities: {}", originalCapabilities);

            // Unwrap driver if decorated
            WebDriver unwrappedDriver = driver;
            if (driver instanceof Decorated<?>) {
                unwrappedDriver = (WebDriver) ((Decorated<?>) driver).getOriginal();
            }

            if (unwrappedDriver instanceof RemoteWebDriver) {
                RemoteWebDriver remoteDriver = (RemoteWebDriver) unwrappedDriver;
                SessionId sessionId = remoteDriver.getSessionId();
                LOGGER.warn("Session ID: {}", sessionId);

                Capabilities actualCaps = remoteDriver.getCapabilities();
                LOGGER.warn("Actual Capabilities: {}", actualCaps);
            } else {
                LOGGER.warn("Unwrapped driver is not a RemoteWebDriver instance");
            }
        } else {
            LOGGER.warn("Could not find CarinaDriver associated with current WebDriver instance");
        }
    }
}
