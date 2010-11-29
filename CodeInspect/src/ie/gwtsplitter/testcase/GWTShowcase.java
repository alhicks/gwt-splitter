package ie.gwtsplitter.testcase;

import com.thoughtworks.selenium.SeleneseTestCase;

public class GWTShowcase extends SeleneseTestCase {
	public void setUp() throws Exception {
		// /Applications/ChromeWithSpeedTracer
		setUp("http://gwt.google.com/samples/Showcase/Showcase.html", "*chrome");
	}
	public void testGWTShowcase() throws Exception {
		selenium.open("/samples/Showcase/Showcase.html");
		selenium.click("//div[2]/table/tbody/tr/td[1]/img");
		selenium.click("//div[2]/table/tbody/tr/td[1]/img");
		selenium.click("link=Checkbox");
		selenium.click("link=Radio Button");
		selenium.click("link=Basic Button");
		selenium.click("link=Custom Button");
		selenium.click("link=File Upload");
		selenium.click("link=Date Picker");
		selenium.click("link=Hyperlink");
		selenium.click("//div[2]/table/tbody/tr/td[1]/img");
		selenium.click("//div[3]/table/tbody/tr/td[1]/img");
		selenium.click("link=List Box");
		selenium.click("link=Suggest Box");
		selenium.click("link=Tree");
		selenium.click("link=Menu Bar");
		selenium.click("link=Stack Panel");
		selenium.click("//div[3]/table/tbody/tr/td[1]/img");
	}
}
