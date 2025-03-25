package com.solvd.web.gui.pages.desktop.yahoo;

import com.solvd.web.gui.pages.common.yahoo.WeatherPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = WeatherPageBase.class)
public class WeatherPage extends WeatherPageBase {

    @FindBy(xpath = "//*[@id=\"module-weather-forecast\"]/h3")
    private ExtendedWebElement forecastModuleLabel;

    @FindBy(xpath = "//*[@id=\"module-weather-details\"]/h3")
    private ExtendedWebElement detailsModuleLabel;

    @FindBy(xpath = "//*[@id=\"module-weather-sun-moon\"]/h3")
    private ExtendedWebElement sunMoodModuleLabel;

    @FindBy(xpath = "//*[@id=\"module-weather-precipitation\"]/h3")
    private ExtendedWebElement precipitationModuleLabel;

    @FindBy(xpath = "//*[@id=\"module-weather-wind-pressure\"]/h3")
    private ExtendedWebElement pressureModuleLabel;

    public WeatherPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getModuleLabels() {
        List<String> moduleLabelsList = new ArrayList<>();
        moduleLabelsList.add(forecastModuleLabel.getText());
        moduleLabelsList.add(detailsModuleLabel.getText());
        moduleLabelsList.add(sunMoodModuleLabel.getText());
        moduleLabelsList.add(precipitationModuleLabel.getText());
        moduleLabelsList.add(pressureModuleLabel.getText());
        return moduleLabelsList;
    }
}
