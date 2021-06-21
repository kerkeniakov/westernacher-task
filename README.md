# Westernacher task
task list:
```
gradle downloadChromeDriver
```

## Installation
If using my selenium hub :
1. Populate framework.properties with
 * seleniumHub=http://78.130....
 * use_local_chrome_driver=false
2. execute 
```
gradle clean test
```

If using self-ran selenium grid 
1. Populate framework.properties with  
 * seleniumHub=http://localhost:4445/wd/hub  
 * use_local_chrome_driver=false 
tasks for hub:
```
gradle startWebDriverGridHub
```
```
gradle startWebDriverGridNode
```
```
gradle shutDownNodeAndHub
```


2. npm install  
3. npm run tests:chrome -> runs the whole test suite on chrome
 * npm run tests:firefox -> runs the whole test suite on firefox
 * for specific test cases you can use the manual command instead of the scripted one. For example:
>  node node_modules/protractor/bin/protractor ./conf.js --suite clickAndProtocols --params.browser=firefox  
>  node node_modules/protractor/bin/protractor ./conf.js --suite searchWidget --params.browser=chrome
