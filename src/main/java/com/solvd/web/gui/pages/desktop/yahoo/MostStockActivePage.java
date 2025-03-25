package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.MostStockActivePageBase;
import com.solvd.web.gui.pages.common.yahoo.TeslaStockChartPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = MostStockActivePageBase.class)
public class MostStockActivePage extends MostStockActivePageBase {

    @FindBy(xpath = "//*[@id=\"2\"]/td[1]/span/div/a/div/span")
    private ExtendedWebElement teslaLabel;

    public MostStockActivePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public TeslaStockChartPageBase goToTeslaStockChartPage() {
        teslaLabel.click(5);
        return initPage(getDriver(), TeslaStockChartPageBase.class);
    }

}
