package com.solvd.web.gui.pages.desktop.ebay;

import com.solvd.web.gui.forms.CheckoutForm;
import com.solvd.web.gui.pages.common.ebay.CheckoutPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
@DeviceType(pageType = DeviceType.Type.DESKTOP, parentClass = CheckoutPageBase.class)
public class CheckoutPage extends CheckoutPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@id=\"country\"]")
    private ExtendedWebElement countrySelect;

    @FindBy(xpath = "//*[@id=\"firstName\"]")
    private ExtendedWebElement firstNameInput;

    @FindBy(xpath = "//*[@id=\"lastName\"]")
    private ExtendedWebElement lastNameInput;

    @FindBy(xpath = "//*[@id=\"city\"]")
    private ExtendedWebElement cityInput;

    @FindBy(xpath = "//*[@id=\"email\"]")
    private ExtendedWebElement emailInput;

    @FindBy(xpath = "//*[@id=\"emailConfirm\"]")
    private ExtendedWebElement emailConfirmInput;

    @FindBy(xpath = "//*[@id=\"phoneNumber\"]")
    private ExtendedWebElement phoneInput;

    @FindBy(xpath = "//*[@id=\"phoneCountryCode\"]")
    private ExtendedWebElement countryCodeSelect;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void selectCountryCode(String countryCode) {
        countryCodeSelect.click();
        List<ExtendedWebElement> elementList = findExtendedWebElements(By.className("custom-phone-field__dial-code"));
        boolean isCountryFound = false;
        for (ExtendedWebElement countryCodeElement : elementList) {
            if (countryCodeElement.getText().equals(countryCode)){
                countryCodeElement.setName("countryCodeElement");
                countryCodeElement.click();
                isCountryFound = true;
                break;
            }
        }
        Assert.assertTrue(isCountryFound,"Cannot find such country code");
    }

    public void checkout(CheckoutForm checkoutForm){
        countrySelect.select(checkoutForm.getcountry());
        firstNameInput.type(checkoutForm.getFirstName());
        lastNameInput.type(checkoutForm.getLastName());
        cityInput.type(checkoutForm.getCity());
        emailInput.type(checkoutForm.getEmail());
        emailConfirmInput.type(checkoutForm.getEmail());
        phoneInput.type(checkoutForm.getPhone());
        selectCountryCode(checkoutForm.getcountryCode());
    }

}
