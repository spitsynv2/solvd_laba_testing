package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.CheckoutPageBase;
import com.solvd.web.gui.pages.common.ebay.ItemPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = ItemPageBase.class)
public class ItemPage extends ItemPageBase {

    @FindBy(className = "x-item-title__mainTitle")
    private ExtendedWebElement itemTitle;

    @FindBy(id = "binBtn_btn_1")
    private ExtendedWebElement buyButton;

    @FindBy(xpath = "//*[@id=\"s0-1-26-7-15-1-60[4]-12-confirm-dialog-confirm\"]")
    private ExtendedWebElement continueButton;

    @FindBy(className = "ux-bin-nudge__guestCheckOut")
    private ExtendedWebElement butAsGuestButton;

    public ItemPage(WebDriver driver) {super(driver);}

    public String getItemName(){
        return itemTitle.getText();
    }

    public CheckoutPageBase goToCheckOutPage() {
        buyButton.click(5);
        continueButton.clickIfPresent(5);
        butAsGuestButton.click(5);
        return initPage(getDriver(), CheckoutPageBase.class);
    }
}
