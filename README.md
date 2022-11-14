# HNG-Mobile-Task-3
In fulfilment of HNG Mobile Track Task 3. An android app that consumes an API to display details of all countries of the world.

## Features
- Multi-language support (English, German, French, Italian, Spanish)
- Explore all the countries of the world
- Details including flags and coats or arm
- Searchable content
- Filterable content
- Dark mode


## Codebase Description

A simple android app built natively with Kotlin in android studio.
It is a one-activity, one-fragment app.

The viewmodel subserves smooth event handling and maintenance of state across configuration changes.

The network data is retrieved via a one-time call done in the init block of the viewmodel. This is parsed into a custom country class and created instances are stored in a list. Storing in a list allows the full use of the powerful helper functionalities provided in the kotlin.collections package, which is what powers the search and filter functions.

Simple nav_graph from the Jetpack Navigation component subserves navigation.

The network call is wrapped in a tyr-catch block. Multiple states were implemented for the ui and are responsive to the outcome of the network call. The result is a graceul handling of all network errors.


## Libraries
### Retrofit. 
Used to for https network calls

### Coil. 
Used to load images to imageviews by simply supplying the URL's


## Future Features
If I had more time, I'd implement
1. Support for more languages
2. More options for the filter
3. More decorations in form of animations.

## Challenges
- Collapsing menu. This proved difficult but eventually solved thanks to some constraintLayout features.
- Multi language. 5 lanugages are supported. It was tedious getting the translations of all strings used in the project in all 5 languages.
- API response. The documentaiton was not very helpful. To properly parse the response, I had to study the plain text response painstaikingly

## Appetize.io
[Play](https://appetize.io/app/75dco5hoq3rwdbi7nwqvxqlhn4)
