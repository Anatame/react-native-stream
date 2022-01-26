import HeadlessWebView from './HeadlessWebView'

import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

export default function LoadVideoData(props) {
  return <View>
    <HeadlessWebView 

      onVideoLoaded={(event)=>{
        props.onVideoLoaded(event.message)
      }}

      width="100%" height="100%" 

      url={props.src}/>
  </View>;
}
