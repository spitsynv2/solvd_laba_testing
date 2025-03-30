package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.YahooHomePageLocalBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.annotations.Localized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = YahooHomePageLocalBase.class)
public class YahooHomePageLocal extends YahooHomePageLocalBase {

    @Localized
    @FindBy(xpath = "//*[@id=\"module-trending\"]/div/div/div[1]/div/h3")
    private ExtendedWebElement trendingTitle;

    public YahooHomePageLocal(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getTrendingTitleText() {
        return trendingTitle.getText();
    }
}
