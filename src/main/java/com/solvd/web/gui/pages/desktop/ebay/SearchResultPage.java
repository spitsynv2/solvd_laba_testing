package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.ItemPageBase;
import com.solvd.web.gui.pages.common.ebay.SearchResultPageBase;
import com.solvd.web.gui.util.BrowserWindowsUtil;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = SearchResultPageBase.class)
public class SearchResultPage extends SearchResultPageBase {

    @FindBy(xpath = "//*[@id=\"srp-river-results\"]/ul/li[1]/div[1]/div[1]/div[1]")
    private ExtendedWebElement itemLink;

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    public ItemPageBase selectFirstResultItem(){
        itemLink.click(5);
        BrowserWindowsUtil.switchToLastWindow(getDriver());
        return initPage(getDriver(), ItemPageBase.class);
    }
}
