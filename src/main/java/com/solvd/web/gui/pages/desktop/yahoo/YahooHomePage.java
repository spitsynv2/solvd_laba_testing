package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.FinancePageBase;
import com.solvd.web.gui.pages.common.yahoo.WeatherPageBase;
import com.solvd.web.gui.pages.common.yahoo.YahooHomePageBase;
import com.solvd.web.gui.pages.common.yahoo.YahooHomePageLocalBase;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = YahooHomePageBase.class)
public class YahooHomePage extends YahooHomePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<String, String> LOCALE_MAP = new HashMap<>();

    @FindBy(xpath = "//*[@id=\"consent-page\"]/div/div/div/form/div[2]/div[2]/button[1]")
    private ExtendedWebElement acceptCookiesButton;

    @FindBy(xpath = "//*[@id=\"grid-layout\"]/div[2]/a")
    private ExtendedWebElement seeMoreWeatherLink;

    @FindBy(xpath = "//*[@id=\"ybar-l1-nav\"]/li[2]/a")
    private ExtendedWebElement financeLink;

    @FindBy(xpath = "//*[@id=\"ybar-l1-nav\"]/li[4]/button/span")
    private ExtendedWebElement moreMenuPopUp;

    @FindBy(xpath = "//*[@id=\"ybar-l1-more-menu\"]/ul/li[6]/ul/li[2]/button/span/span")
    private ExtendedWebElement changeLocaleButton;

    @FindBy(xpath = "//*[@id=\"ybar-inner-wrap\"]/div[2]/div/div[2]/nav/span/dialog/ul/li/a/span/span")
    private List<ExtendedWebElement> localeButtonsList;

    public YahooHomePage(WebDriver driver) {
        super(driver);
        LOCALE_MAP.put("English", "en_US");
        LOCALE_MAP.put("Deutsch", "de_DE");
        LOCALE_MAP.put("Français", "fr_FR");
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

    @Override
    public YahooHomePageLocalBase goToLocalPage() {
        moreMenuPopUp.hover();
        changeLocaleButton.click(5);
        String localeStr = Configuration.getRequired(WebDriverConfiguration.Parameter.LOCALE);

        for (ExtendedWebElement localeButton : localeButtonsList) {
            String buttonLocaleText = LOCALE_MAP.getOrDefault(localeButton.getText(),"en_US_default");
            if (buttonLocaleText.equals(localeStr)){
                localeButton.click(5);
                return initPage(getDriver(), YahooHomePageLocalBase.class);
            }
        }

        LOGGER.warn("Local page button with text {} not found, opening default - en_US page",localeStr);
        localeButtonsList.get(0).click(5);
        return initPage(getDriver(), YahooHomePageLocalBase.class);
    }
}
