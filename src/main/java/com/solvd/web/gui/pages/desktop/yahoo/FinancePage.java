package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.FinancePageBase;
import com.solvd.web.gui.pages.common.yahoo.MostStockActivePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = FinancePageBase.class)
public class FinancePage extends FinancePageBase {


    @FindBy(xpath = "//*[@id=\"ybar-navigation\"]/div/ul/li[3]/a/span")
    private ExtendedWebElement marketsLabel;

    @FindBy(xpath = "//*[@id=\"ybar-navigation\"]/div/ul/li[3]/div/ul/li[1]/a/div")
    private ExtendedWebElement mostStockActiveSubLabel;

    public FinancePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public MostStockActivePageBase goToMostStockActivePage() {
        marketsLabel.hover();
        mostStockActiveSubLabel.click();
        return initPage(getDriver(), MostStockActivePageBase.class);
    }

}
