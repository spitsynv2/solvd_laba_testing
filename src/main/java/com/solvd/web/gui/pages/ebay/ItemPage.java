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

    @FindBy(className = "x-item-title__mainTitle")
    private ExtendedWebElement itemTitle;

    public ItemPage(WebDriver driver) {
        super(driver);
        waitForJSToLoad(10);
        setUiLoadedMarker(itemTitle);
    }

    public String getItemTitleText(){
        if(itemTitle.isElementPresent(10)){
            return itemTitle.getText();
        }else {
            return "First element title cannot be found";
        }
    }
}
