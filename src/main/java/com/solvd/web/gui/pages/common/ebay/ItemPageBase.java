package com.solvd.web.gui.pages.common.ebay;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class ItemPageBase extends AbstractPage {

    public ItemPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract String getItemName();

    public abstract CheckoutPageBase goToCheckOutPage();

}
