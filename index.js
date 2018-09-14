import { NativeModules, DeviceEventEmitter } from 'react-native'
import { EventEmitter } from 'events'

const {RNDomalityNetworkScanner} = NativeModules

export default class DomalityNetworkScanner extends EventEmitter {

  constructor (props) {
    super(props)
    this._devices = {}
    this._listeners = {}
    this.addDeviceListeners()
  }

  addDeviceListeners () {
    this._listeners.start = DeviceEventEmitter.addListener('RNDomStart',
      () => this.emit('start'))
    this._listeners.resolved = DeviceEventEmitter.addListener('RNDomResolved',
      devices => {
        this.emit('resolved', devices)
      })
    this._listeners.resolved = DeviceEventEmitter.addListener('RNDomUpdated',
      device => {
        this.emit('updated', device)
        this.emit('resolved', this._devices);
      })
  }

  scan () {
    this._devices = {}
    RNDomalityNetworkScanner.scan()
  }

}
