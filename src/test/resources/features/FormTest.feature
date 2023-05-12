@RunWith(Cucumber.class)
Feature: Form test
  Scenario: Submit form
    Given Open "https://templates.jinshuju.net/detail/Dv9JPD"
    Then Switch iframe "//iframe[@title='template-preview']" by xpath
    Then TakeScreenShot as "0_Page1"
    Then Scroll down half page
    Then Scroll "//*[@class='published-form__body']" into view by xpath
    Then Click "//*[text()='连续生产/开工类企事业单位 ']" by xpath
    Then TakeScreenShot as "1_Page1"
    Then Click "//span[text()='下一页']/parent::button" by xpath

    Then Sleep for 10000 ms
    Then Assert "//*[text()='申请日期']/ancestor::div[contains(@class,'ant-form-item-row')]//input[@class='ant-input']" is presence by xpath
    And TakeScreenShot as "0_Page2"
    Then Scroll "//*[text()='申请日期']/ancestor::div[contains(@class,'ant-form-item-row')]//input[@class='ant-input']" into view by xpath
    Then Input date into "//*[text()='申请日期']/ancestor::div[contains(@class,'ant-form-item-row')]//input[@class='ant-input']" by xpath
    And Input "自动化" into "//*[text()='申请人']/ancestor::div[contains(@class,'ant-form-item-row')]//input[@class='ant-input']" by xpath
    And Input "13161005893" into "//*[text()='联系方式']/ancestor::div[contains(@class,'ant-form-item-row')]//input[@class='ant-input']" by xpath
    Then TakeScreenShot as "1_Page2"
    Then Click "//span[text()='下一页']/parent::button" by xpath

    Then Sleep for 10000 ms
    And TakeScreenShot as "0_Page3"
    Then Scroll "//*[text()='报备单位']/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" into view by xpath
    Then Input "测试公司" into "//*[text()='报备单位']/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" by xpath
    Then Input "99" into "//*[text()='在岗人数']/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" by xpath
    Then Input today into "//*[text()='报备日期']/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" by xpath
    Then Input "0" into "//*[contains(text(),'密切接触')]/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" by xpath
    Then Input "zyue" into "//*[text()='单位负责人']/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" by xpath
    Then Input "13161005893" into "//*[text()='联系方式']/ancestor::div[contains(@class,'ant-form-item-row')]//input[contains(@class,'ant-input')]" by xpath
    Then Input "测试内容" into "//*[text()='疫情防控方案']/ancestor::div[contains(@class,'ant-form-item-row')]//textarea[contains(@class,'ant-input')]" by xpath
    Then TakeScreenShot as "1_Page3"
    Then Click "//*[text()='提 交']/parent::button" by xpath

    Then Sleep for 5000 ms
    And Assert "//*[contains(text(),'提交成功')]" is presence by xpath
    Then TakeScreenShot as "result_Page"