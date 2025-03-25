package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.FinancePageBase;
import com.solvd.web.gui.pages.common.yahoo.WeatherPageBase;
import com.solvd.web.gui.pages.common.yahoo.YahooHomePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = YahooHomePageBase.class)
public class YahooHomePage extends YahooHomePageBase {

    @FindBy(xpath = "//*[@id=\"consent-page\"]/div/div/div/form/div[2]/div[2]/button[1]")
    private ExtendedWebElement acceptCookiesButton;

    @FindBy(xpath = "//*[@id=\"grid-layout\"]/div[2]/a")
    private ExtendedWebElement seeMoreWeatherLink;

    @FindBy(xpath = "//*[@id=\"ybar-l1-nav\"]/li[2]/a")
    private ExtendedWebElement financeLink;

    public YahooHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void open() {
        super.open();
        if (acceptCookiesButton.isElementPresent(5)){
            waitUntil(ExpectedConditions.visibilityOf(acceptCookiesButton),5);
            acceptCookiesButton.clickIfPresent(5);
        }
    }

    public WeatherPageBase goToWeatherPage() {
        seeMoreWeatherLink.click(5);
        return initPage(getDriver(), WeatherPageBase.class);
    }

    @Override
    public FinancePageBase goToFinancePage() {
        financeLink.click(5);
        return initPage(getDriver(), FinancePageBase.class);
    }
}
