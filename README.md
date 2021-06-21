# Westernacher task

## Installation
### If using my selenium hub (just copy paste the framework.properties from email) :
1. Populate framework.properties with
 * seleniumHub=http://78.130....
 * use_local_chrome_driver=false
2. Execute 

```
gradle clean test
```
> executes all test cases.

### If using self-ran selenium grid 
1. Populate framework.properties with  
 * seleniumHub=http://localhost:4445/wd/hub   
 * use_local_chrome_driver=false 
  tasks related to spawning or killing the selenium hub + node:  

```
gradle downloadSeleniumHub
```
> downloads selenium jar file in root dir.

```
gradle startWebDriverGridHub
```
> starts webdriver grid @ 4445.

```
gradle startWebDriverGridNode
```
> starts a webdriver node @5555 and registers it to the grid.

```
gradle shutDownNodeAndHub
```
> curls shutdown endpoints of both grid and node in order.

### If you want to run a local chromewebdriver
1. Populate framework.properties with  
 * seleniumHub=<can be blank>
 * use_local_chrome_driver=true 
2. Execute 

  ```
gradle setupChromeDriver
```
> checks OS and downloads corresponding webdriver version.  

### Reporting framework
  Reporting framework used is Allure. After each execution it should generate output in \build\allure-results.
### What I'm not happy about and what I really need to refactor but couldn't given that this is my first time time coding in Java and also time-limits:
  - Abstract away all the @BeforeEach and @After each etc. I should probably create an abstract class and let every test extend it?
  - Create a DriverFactory for spawning local browsers
  - Implement parralel test run
  - Better logging
  - attach screenshots/videos of each test steps to allure report
  
 
