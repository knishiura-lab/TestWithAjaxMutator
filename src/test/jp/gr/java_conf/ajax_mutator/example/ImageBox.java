package jp.gr.java_conf.ajax_mutator.example;

import org.openqa.selenium.WebElement;

public class ImageBox {
	private WebElement _webElement;
	private String _imageUrl;
	
	public ImageBox(WebElement webElement, String imageUrl) { 
		_webElement = webElement;
		_imageUrl = imageUrl;
	}
	
	public WebElement getWebElement() {
		return _webElement;
	}
	
	public String getImageUrl() {
		return _imageUrl;
	}
	
	public void setWebElement(WebElement webElement) {
		_webElement = webElement;
	}
	
	public void setImageUrl(String imageUrl) {
		_imageUrl = imageUrl;
	}
}
