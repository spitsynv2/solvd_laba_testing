package com.solvd.web.gui.pages.common.ebay;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class CategoryPageBase extends AbstractPage {

    public CategoryPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract ComputersTabletsNetworkPageBase openComputersTabletsNetworkPage();

}
