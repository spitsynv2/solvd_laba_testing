package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.TeslaStockChartPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = TeslaStockChartPageBase.class)
public class TeslaStockChartPage extends TeslaStockChartPageBase {

    @FindBy(id = "chart-beacon")
    private ExtendedWebElement lastChartPosition;

    @FindBy(xpath = "//*[@id=\"nimbus-app\"]/section/section/section/article/div[2]/div/section/div[3]/section/cq-context/div/div/div/table/tbody")
    private ExtendedWebElement chartPositionDataTable;

    @Context(dependsOn = "chartPositionDataTable")
    @FindBy(xpath = ".//tr[3]")
    private ExtendedWebElement openDataElement;

    @Context(dependsOn = "openDataElement")
    @FindBy(className = "hu-tooltip-value")
    private ExtendedWebElement openDataValuePlaceholder;

    @FindBy(xpath = "//*[@id=\"nimbus-app\"]/section/section/section/article/div[3]/ul/li[2]/span[2]/fin-streamer")
    private ExtendedWebElement previousOpenValuePlaceholder;

    public TeslaStockChartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getLastAndPreviousOpenValues() {
        lastChartPosition.hover();
        return List.of(openDataValuePlaceholder.getText(), previousOpenValuePlaceholder.getText());
    }
}
