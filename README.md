
# react-native-domality-network-scanner

## Getting started

`$ npm install react-native-domality-network-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-domality-network-scanner`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-domality-network-scanner` and add `RNDomalityNetworkScanner.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNDomalityNetworkScanner.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.domality.RNDomalityNetworkScannerPackage;` to the imports at the top of the file
  - Add `new RNDomalityNetworkScannerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-domality-network-scanner'
  	project(':react-native-domality-network-scanner').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-domality-network-scanner/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-domality-network-scanner')
  	```


## Usage
```javascript
import RNDomalityNetworkScanner from 'react-native-domality-network-scanner';

const scanner = new RNDomalityNetworkScanner();

	scanner.scan();
	
    scanner.on('start',()=>{
      console.log('network scanning start')
	})
	
    scanner.on('resolved',(devices)=> {
      console.log(devices)
	})
	
    scanner.on('updated',(device)=> {
      console.log(device)
    })
```
  