package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.ComputersTabletsNetworkPageBase;
import com.solvd.web.gui.pages.common.ebay.ItemPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = ComputersTabletsNetworkPageBase.class)
public class ComputersTabletsNetworkPage extends ComputersTabletsNetworkPageBase {

    @FindBy(xpath = "/html/body/div[2]/div[2]/section[3]/section[3]/div[2]/div/div")
    private ExtendedWebElement limitedTimeDealsBlock;

    @Context(dependsOn = "searchBlock")
    @FindBy(className = "bsig__title")
    private List<ExtendedWebElement> limitedTimeDealsItems;

    public ComputersTabletsNetworkPage(WebDriver driver) {
        super(driver);
    }

    public String getLimitedTimeDealsItemName(int position) {
        return limitedTimeDealsItems.get(position).getText();
    }

    public ItemPageBase selectLimitedTimeDealsItem(int position) {
        limitedTimeDealsItems.get(position).click();
        return initPage(getDriver(), ItemPageBase.class);
    }
}
