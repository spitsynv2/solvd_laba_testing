package com.solvd.web.gui.pages.ebay;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-17
 */
public class ComputerTabletsNetworkPage extends AbstractPage {

    @FindBy(xpath = "/html/body/div[2]/div[2]/section[3]/section[1]/div[2]/ul/li[1]/div/a/div[1]/img")
    private ExtendedWebElement laptopsNetbooksImage;

    public ComputerTabletsNetworkPage(WebDriver driver) {
        super(driver);
    }

    public LaptopsNetbooksPage openLaptopsNetbooksPage(){
        laptopsNetbooksImage.clickIfPresent(10);
        return new LaptopsNetbooksPage(getDriver());
    }

}
