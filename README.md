# Pasta-Music
A [material design](https://material.google.com/) music player for Android using google's [ExoPlayer](https://github.com/google/ExoPlayer) library, based off [Pasta for Spotify](https://github.com/TheAndroidMaster/Pasta-for-Spotify).

## About

Pasta Music is a material design music player for android that attempts to create a better user experience than the standard music players which can have too many features or be generally confusing to users. It was created to show an improvement in design and to allow older and slower devices to have quicker access to local music files. Some examples of this are as follows:
### Design:
- Touch areas are increased for small devices to be able to open things like menus and playlists more easily than the layout in the official app
- A lot of access relies on swipe navigation within the app to speed up general user experience
- The shuffle and repeat buttons have been removed from the now playing screen and moved into an "order tracks" dialog that is accessible from the menu of playlists and albums, and shows up in the settings menu. This provides greater consistency: in most music players you can order tracks by name, date, etc when viewing a list, but this all becomes obsolete once shuffle is enabled. Moving all order-related options to the same place makes more sense from the perspective of both new and existing users.
- The backgrounds of different elements on the screen change color according to the album arts, allowing the user to quickly identify items based on color as well as text and image, and blend the image with the rest of the app so that it doesn't seem out of place.
- Most parts of the app can be customized from the settings menu to allow the user to change their experience, including the main color scheme of the app and whether to display items as cards, tiles, or lists.

### Performance:
- The app uses Aidan Follestad's [Async](https://github.com/afollestad/async) library to load content separately from the UI thread, which allows the user to navigate the app while content is loading, for example: navigating back to the previous task while content is loading will cancel the download.
- [Butterknife](http://jakewharton.github.io/butterknife/), by Jake Wharton, is used to bind views instead of the standard view binding method. Truthfully I have no idea what this means but it saves time so just go with it. ;)
- [Glide](https://github.com/bumptech/glide) is used to load image urls provided by the spotify api. This saves a lot of loading time by asynchronously loading an image while scrolling as well as compressing it to speed up the download as much as possible.

## Features
- Shows recently added music and featured playlists
- A favorites section for playlists, albums, songs, and artists
- Search through all of Spotify's database
- View different categories of music
- Dynamic backgrounds that adapt to the album art
- A light/dark theme in the settings menu
- Options to change the global color scheme of the app
- Change the ordering of songs in playlists and albums

### Screenshots

Home Screen | Now Playing
----------- | -----------
![](http://theandroidmaster.github.io/images/screenshots/image4155.png) | ![](http://theandroidmaster.github.io/images/screenshots/image4646.png)

## Contributing
### Issues
Okay, there aren't really any guidelines over issue formatting provided that you don't create duplicate issues and test the app throughly before creating an issue (ex: try clearing the app data).

### Pull Requests
I usually don't have any organization over how I handle issues and what I commit at any given time. If I'm interrupted in the middle of a session, I might commit a half-finished class that causes an error before the project even compiles. To prevent good work going to waste or having to be copied and pasted a lot to prevent merge conflicts, please contact me before you start working on any changes. This way we can decide who will work on the project when, and exactly what changes they will be making.

## Links

- [Google Plus Community](https://plus.google.com/communities/101536497390778012419)
- [Website](http://theandroidmaster.github.io/apps/pasta/)

#### Contributors:
- [James Fenn](http://theandroidmaster.github.io/)
- [Jan-Lk Else](https://plus.google.com/+JanLkElse)
- [Patrick J](http://pddstudio.com/)
- [Vukašin Anđelković](https://plus.google.com/+Vuka%C5%A1inAn%C4%91elkovi%C4%87zavukodlak)
- [Kevin Aguilar](https://plus.google.com/+KevinAguilarC)

## License

```
Copyright 2016 James Fenn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
