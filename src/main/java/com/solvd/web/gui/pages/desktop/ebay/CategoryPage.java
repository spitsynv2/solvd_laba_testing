package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.pages.common.ebay.CategoryPageBase;
import com.solvd.web.gui.pages.common.ebay.ComputersTabletsNetworkPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = CategoryPageBase.class)
public class CategoryPage extends CategoryPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CategoryPage(WebDriver driver) {
        super(driver);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
    }

    public ComputersTabletsNetworkPageBase openComputersTabletsNetworkPage() {
        ExtendedWebElement electronicsPageTitle = findExtendedWebElement(By.className("page-title"));
        Assert.assertEquals(electronicsPageTitle.getText(), "Electronics", "Electronics category page is not opened");

        ExtendedWebElement computersTabletsNetworkLink = findExtendedWebElement(By.
                xpath("/html/body/div[2]/div[2]/section[3]/section[1]/div[2]/ul/li[1]/div/a/div[2]/span"));
        computersTabletsNetworkLink.click(5);
        return initPage(getDriver(), ComputersTabletsNetworkPageBase.class);
    }

}
