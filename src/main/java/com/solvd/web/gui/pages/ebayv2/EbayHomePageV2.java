package com.solvd.web.gui.pages.ebayv2;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import com.zebrunner.carina.webdriver.locator.Context;
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
public class EbayHomePageV2 extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    public EbayHomePageV2(WebDriver driver) {
        super(driver);
    }

    public CategoryPage selectCategory(String categoryName) {
        CategoryPage categoryPage = null;
        for (ExtendedWebElement category : categoryMenu) {
            if(category.getText().equals(categoryName)){
                category.click();
                categoryPage = new CategoryPage(getDriver());
                return categoryPage;
            }
        }
        LOGGER.error("Category: {} is not found", categoryName);
        return categoryPage;
    }

    @Override
    public void open() {
        super.open();
        waitForJSToLoad();
        waitUntil(ExpectedConditions.visibilityOfElementLocated(acceptCookies.getBy()),5);
        acceptCookies.clickIfPresent(5);
    }

    public SearchResultPage searchForItem(String searchText,String itemEbayType) {
        searchInputField.type(searchText);
        selectMenu.select(itemEbayType);
        searchButton.clickIfPresent(5);
        return new SearchResultPage(getDriver());
    }
}
