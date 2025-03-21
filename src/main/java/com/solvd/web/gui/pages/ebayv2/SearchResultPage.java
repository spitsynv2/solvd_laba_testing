package com.solvd.web.gui.pages.ebayv2;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
public class SearchResultPage extends AbstractPage {

    @FindBy(xpath = "/html/body/div[5]/div[4]/div[3]/div[1]/div[3]/ul/li[1]/div/div[2]/a")
    private ExtendedWebElement itemLink;

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    public ItemPageV2 selectFirstResultItem(){
        openURL(itemLink.getAttribute("href"));
        return new ItemPageV2(getDriver());
    }
}
