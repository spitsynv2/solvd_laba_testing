package com.solvd.web.gui.pages.ebay;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


/**
 * @author Vadym Spitsyn
 * @created 2025-03-17
 */
public class EbayHomePage extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(id = "gdpr-banner-accept")
    private ExtendedWebElement acceptCookies;

    @FindBy(xpath = "//*[@id=\"gh-logo\"]")
    private ExtendedWebElement ebayLogo;

    @FindBy(xpath = "//*[@id=\"vl-flyout-nav\"]/ul/li[4]/a")
    private ExtendedWebElement electronicsElement;

    @FindBy(xpath = "//*[@id=\"vl-flyout-nav\"]/ul/li[4]/div[2]/div[1]/nav[1]/ul/li[1]/a")
    private ExtendedWebElement computerTabletsNetworkElement;

    public EbayHomePage(WebDriver driver) {
        super(driver);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(ebayLogo);
    }

    @Override
    public void open() {
        super.open();
        waitForJSToLoad();
        waitUntil(ExpectedConditions.visibilityOfElementLocated(acceptCookies.getBy()),5);
        acceptCookies.clickIfPresent(5);
    }

    public ComputerTabletsNetworkPage openComputerTabletsNetworkPage(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        electronicsElement.hover();
        computerTabletsNetworkElement.clickIfPresent(10);
        return new ComputerTabletsNetworkPage(getDriver());
    }
}
