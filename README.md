# Westernacher task

## Installation
### If using my selenium hub :
1. Populate framework.properties with
 * seleniumHub=http://78.130....
 * use_local_chrome_driver=false
2. Execute 
```
gradle clean test
```
executes all test cases.

### If using self-ran selenium grid 
1. Populate framework.properties with  
 * seleniumHub=http://localhost:4445/wd/hub   
 * use_local_chrome_driver=false 
  tasks related to spawning or killing the selenium hub + node:  
```
gradle downloadSeleniumHub
```
downloads selenium jar file in root dir.
```
gradle startWebDriverGridHub
```
starts webdriver grid @ 4445.
```
gradle startWebDriverGridNode
```
starts a webdriver node @5555 and registers it to the grid.
```
gradle shutDownNodeAndHub
```
makes http requests to stop grid and node

### If you want to run a local chromewebdriver
1. Populate framework.properties with  
 * seleniumHub=<can be blank>
 * use_local_chrome_driver=true 
2. Execute 
```
gradle setupChromeDriver
```
checks OS and downloads corresponding webdriver version.  

