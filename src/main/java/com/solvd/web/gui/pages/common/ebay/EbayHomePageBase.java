package com.solvd.web.gui.pages.common.ebay;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class EbayHomePageBase extends AbstractPage {

    public EbayHomePageBase(WebDriver driver) {
        super(driver);
    }

    public abstract CategoryPageBase selectCategory(String categoryName);

    public abstract SearchResultPageBase searchForItem(String searchText, String itemEbayType);
}
