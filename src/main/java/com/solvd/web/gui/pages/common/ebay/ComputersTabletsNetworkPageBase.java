package com.solvd.web.gui.pages.common.ebay;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class ComputersTabletsNetworkPageBase extends AbstractPage {

    public ComputersTabletsNetworkPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract ItemPageBase selectLimitedTimeDealsItem(int position);

    public abstract String getLimitedTimeDealsItemName(int position);
}
