package com.solvd.web.gui.pages.ebayv2;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import com.zebrunner.carina.webdriver.locator.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
public class ItemPageV2 extends AbstractPage {

    @FindBy(className = "x-item-title__mainTitle")
    private ExtendedWebElement itemTitle;

    @FindBy(xpath = "//*[@id=\"binBtn_btn_1\"]")
    private ExtendedWebElement buyButton;

    @FindBy(xpath = "//*[@id=\"s0-1-26-7-15-1-60[4]-12-confirm-dialog-confirm\"]")
    private ExtendedWebElement continueButton;

    @FindBy(className = "ux-bin-nudge__guestCheckOut")
    private ExtendedWebElement butAsGuestButton;

    @Context(dependsOn = "butAsGuestButton")
    @FindBy(tagName = "a")
    private ExtendedWebElement checkOutLink;

    public ItemPageV2(WebDriver driver) {
        super(driver);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(itemTitle);
    }

    public CheckoutPage goToCheckOutPage(){
        buyButton.clickIfPresent(5);
        waitForJSToLoad(5);
        if (continueButton.isClickable(5)){
            continueButton.clickIfPresent(5);
            waitForJSToLoad(5);
        }
        openURL(checkOutLink.getAttribute("href"));
        return new CheckoutPage(getDriver());
    }
}
