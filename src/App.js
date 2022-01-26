import React, {PureComponent, useState} from 'react';
import {InputAccessoryView, StyleSheet, Text, View} from 'react-native';
import HeadlessWebView from './component/HeadlessWebView' 
import LoadVideoData from './component/LoadVideoData' 

const cheerio = require('react-native-cheerio')
let $;

const App = () => {

  const [streamUrl, setStreamUrl] = useState("")
  const [videoSrc, setVideoSrc] = useState("https://fmoviesto.cc/watch-tv/snowpiercer-2020-full-61226.5445217")
  const [contentSrc, setContentSrc] = useState("https://fmoviesto.cc/watch-tv/snowpiercer-2020-full-61226.5445217")

  const getMoviesFromApi = () => {
    return fetch("https://fmoviesto.cc/home")
      .then((response) => {
        return response.text();
      })
      .then(function (html) {
        // This is the HTML from our response as a text string
        $ = cheerio.load(html)
        console.log($('film_list-wrap'))
        // console.log($.html())
      }).catch((error) => {
        console.error(error);
      });
  };


  return (
    <View>
      <Text>Bruh</Text>
      <Text onPress={() => {
        getMoviesFromApi()
        setVideoSrc("https://fmoviesto.cc/watch-movie/tom-and-jerry-cowboy-up-2022-full-76657.8043097")
      }}>{streamUrl}</Text>
      <LoadVideoData src={videoSrc} 
      onVideoLoaded={setStreamUrl}/>

    </View>
  );
};

const styles = StyleSheet.create({

});

export default App;
