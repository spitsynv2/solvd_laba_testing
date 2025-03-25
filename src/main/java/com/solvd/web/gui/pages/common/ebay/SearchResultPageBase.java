package com.solvd.web.gui.pages.common.ebay;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class SearchResultPageBase extends AbstractPage {

    public SearchResultPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract ItemPageBase selectFirstResultItem();

}
