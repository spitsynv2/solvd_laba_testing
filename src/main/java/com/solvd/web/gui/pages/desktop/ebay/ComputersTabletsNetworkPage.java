package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.ComputersTabletsNetworkPageBase;
import com.solvd.web.gui.pages.common.ebay.ItemPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = ComputersTabletsNetworkPageBase.class)
public class ComputersTabletsNetworkPage extends ComputersTabletsNetworkPageBase {

    private static final By LIMITED_TIME_DEALS_ITEM_TITLES = By.xpath(
            "//h2[normalize-space()='Limited time deals']" +
                    "/ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' dp-browse-destinations-module ')][1]" +
                    "//a[contains(concat(' ', normalize-space(@class), ' '), ' su-item-card__title ') and contains(@href, '/itm/')]"
    );

    @FindBy(xpath = "//h2[normalize-space()='Limited time deals']")
    private ExtendedWebElement limitedTimeDealsTitle;

    public ComputersTabletsNetworkPage(WebDriver driver) {
        super(driver);
    }

    public String getLimitedTimeDealsItemName(int position) {
        return getLimitedTimeDealsItem(position).getText();
    }

    public ItemPageBase selectLimitedTimeDealsItem(int position) {
        ExtendedWebElement item = getLimitedTimeDealsItem(position);

        item.scrollTo();
        item.click();

        return initPage(getDriver(), ItemPageBase.class);
    }

    private ExtendedWebElement getLimitedTimeDealsItem(int position) {
        Assert.assertTrue(position >= 0, "Limited time deals item position must be zero or greater");

        limitedTimeDealsTitle.scrollTo();
        waitUntil(ExpectedConditions.numberOfElementsToBeMoreThan(LIMITED_TIME_DEALS_ITEM_TITLES, position), 10);

        List<ExtendedWebElement> items = findExtendedWebElements(LIMITED_TIME_DEALS_ITEM_TITLES);
        Assert.assertTrue(items.size() > position,
                String.format("Limited time deals item at position %d is not found. Found %d items.", position, items.size()));
        return items.get(position);
    }
}
