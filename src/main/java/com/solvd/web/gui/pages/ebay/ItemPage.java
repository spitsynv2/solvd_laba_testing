package com.solvd.web.gui.pages.ebay;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-18
 */
public class ItemPage extends AbstractPage {

    @FindBy(xpath = "//*[@id=\"mainContent\"]/div[1]/div[1]/h1/span")
    private ExtendedWebElement itemTitle;

    public ItemPage(WebDriver driver) {
        super(driver);
        waitForJSToLoad();
        setUiLoadedMarker(itemTitle);
    }

    public String getItemTitleText(){
        return itemTitle.getText();
    }
}
