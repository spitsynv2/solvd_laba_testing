package com.solvd.web.gui.pages.ebay;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-17
 */
public class EbayHomePage extends AbstractPage {

    @FindBy(xpath = "//*[@id=\"vl-flyout-nav\"]/ul/li[4]/div[2]/div[1]/nav[1]/ul/li[1]/a")
    private ExtendedWebElement acceptCookies;

    @FindBy(xpath = "//*[@id=\"gh-logo\"]")
    private ExtendedWebElement ebayLogo;

    @FindBy(xpath = "//*[@id=\"vl-flyout-nav\"]/ul/li[4]/a")
    private ExtendedWebElement electronicsElement;

    @FindBy(xpath = "//*[@id=\"vl-flyout-nav\"]/ul/li[4]/div[2]/div[1]/nav[1]/ul/li[1]/a")
    private ExtendedWebElement computerTabletsNetworkElement;

    public EbayHomePage(WebDriver driver) {
        super(driver);
        waitForJSToLoad();
        setUiLoadedMarker(ebayLogo);
    }

    @Override
    public void open() {
        super.open();
        acceptCookies.clickIfPresent(3);
    }

    public ComputerTabletsNetworkPage openComputerTabletsNetworkPage(){
        electronicsElement.hover();
        computerTabletsNetworkElement.clickIfPresent(3);
        return new ComputerTabletsNetworkPage(driver);
    }
}
