package com.solvd.web.gui.pages.common.ebay;

import com.solvd.web.gui.forms.CheckoutForm;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class CheckoutPageBase extends AbstractPage {

    public CheckoutPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract void checkout(CheckoutForm checkoutForm);

    public abstract void selectCountryCode(String countryCode);
}
