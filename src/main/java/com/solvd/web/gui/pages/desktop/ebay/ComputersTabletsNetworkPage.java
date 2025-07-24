package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.ComputersTabletsNetworkPageBase;
import com.solvd.web.gui.pages.common.ebay.ItemPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = ComputersTabletsNetworkPageBase.class)
public class ComputersTabletsNetworkPage extends ComputersTabletsNetworkPageBase {

    @FindBy(className = "bsig__title")
    private List<ExtendedWebElement> limitedTimeDealsItems;

    @FindBy(className = "brw-product-card__signals__header")
    private List<ExtendedWebElement> limitedTimeDealsItemsButton;

    public ComputersTabletsNetworkPage(WebDriver driver) {
        super(driver);
    }

    public String getLimitedTimeDealsItemName(int position) {
        return limitedTimeDealsItems.get(position).getText();
    }

    public ItemPageBase selectLimitedTimeDealsItem(int position) {
        ExtendedWebElement item = limitedTimeDealsItemsButton.get(position);

        // Ensure item is scrolled into view
        item.scrollTo();

        // Click
        item.click();

        return initPage(getDriver(), ItemPageBase.class);
    }
}
