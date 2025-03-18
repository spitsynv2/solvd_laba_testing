package com.solvd.web.gui.pages.ebay;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-18
 */
public class LaptopsNetbooksPage extends AbstractPage {

    @FindBy(xpath = "/html/body/div[2]/div[2]/section[1]/h1")
    private ExtendedWebElement laptopsNetbooksPageTitle;

    @FindBy(xpath = "/html/body/div[2]/div[2]/section[3]/section[4]/ul/li[1]/div/div/div[2]/div[1]/div[1]/span/a/h3")
    private ExtendedWebElement firstItemTitle;

    @FindBy(xpath = "//*[@id=\"s0-1-0-15-4-49[3]-18[19]-0-1-2-1[0]-6-2-list\"]/li[1]/a/img")
    private ExtendedWebElement firstItemImg;

    public LaptopsNetbooksPage(WebDriver driver) {
        super(driver);
        waitForJSToLoad();
        setPageURL("b/PC-Laptops-Netbooks/177/bn_317584");
        if (laptopsNetbooksPageTitle.getText().equals("PC Laptops & Netbooks")){
            setUiLoadedMarker(laptopsNetbooksPageTitle);
        }
    }

    public ItemPage openFirstItemPage(){
        firstItemTitle.hover();
        firstItemTitle.clickIfPresent(3);
        return new ItemPage(driver);
    }

    public String getFirstItemTitleText(){
        return firstItemTitle.getText();
    }

}
