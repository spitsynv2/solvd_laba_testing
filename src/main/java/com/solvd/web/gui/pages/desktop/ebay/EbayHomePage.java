package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.CategoryPageBase;
import com.solvd.web.gui.pages.common.ebay.EbayHomePageBase;
import com.solvd.web.gui.pages.common.ebay.SearchResultPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.Context;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-20
 */
@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = EbayHomePageBase.class)
public class EbayHomePage extends EbayHomePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(id = "gdpr-banner")
    private ExtendedWebElement cookiesBanner;

    @FindBy(id = "gdpr-banner-accept")
    private ExtendedWebElement acceptCookies;

    @FindBy (xpath = "//*[@id=\"vl-flyout-nav\"]/ul/li")
    private List<ExtendedWebElement> categoryMenu;

    @FindBy(xpath = "//*[@id=\"gh-search-box\"]/div")
    private ExtendedWebElement searchBlock;

    @Context(dependsOn = "searchBlock")
    @FindBy(tagName = "input")
    private ExtendedWebElement searchInputField;

    @Context(dependsOn = "searchBlock")
    @FindBy(tagName = "select")
    private ExtendedWebElement selectMenu;

    @FindBy(xpath = "//*[@id=\"gh-search-btn\"]/span")
    private ExtendedWebElement searchButton;

    public EbayHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void open() {
        super.open();
        waitForJSToLoad(10);
        if (acceptCookies.isElementPresent(5)){
            waitUntil(ExpectedConditions.visibilityOf(acceptCookies),5);
            acceptCookies.sendKeys(Keys.ENTER);
        }
    }

    public CategoryPageBase selectCategory(String categoryName) {
        CategoryPageBase categoryPage = null;
        for (ExtendedWebElement category : categoryMenu) {
            if(category.getText().equals(categoryName)){
                category.click();
                categoryPage = initPage(getDriver(), CategoryPageBase.class);
                return categoryPage;
            }
        }
        LOGGER.error("Category: {} is not found", categoryName);
        return categoryPage;
    }

    public SearchResultPageBase searchForItem(String searchText,String itemEbayType) {
        searchInputField.type(searchText);
        selectMenu.select(itemEbayType);
        searchButton.click(5);
        return initPage(getDriver(), SearchResultPageBase.class);
    }
}
