import React, {useEffect} from 'react';
import { requireNativeComponent, Platform, View, DeviceEventEmitter  } from 'react-native';

const HWebView = Platform.select({
  // sorry iOS :(
  ios: View, 
  
  // name from overriden `getName` function
  android: requireNativeComponent('HeadlessWebView'),
});


export default class HeadlessWebView extends React.PureComponent {

    constructor(props) {
      super(props);
  }

  componentWillMount() {
      DeviceEventEmitter.addListener('loaded', this.props.onVideoLoaded);
  }

  componentWillUnmount() {
      DeviceEventEmitter.removeListener('loaded', this.props.onVideoLoaded);
  }


  render() {
    const { width = '100%', height = 200 } = this.props;
    return <HWebView {...this.props} style={{ width, height }} />;
  }
}