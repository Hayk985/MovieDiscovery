## Movie Discovery

An application where you can ***discover popular, top-rated, and upcoming*** movies.
The application has search functionality. You can use it and find your desired movie.
By clicking on your desired movie you'll get detailed information.

### Disclaimer
In order to use the application you need to sign up and generate an ***API key*** following
the instructions <a href="https://developers.themoviedb.org/3/getting-started/introduction">here</a>
To use the application with production build, you need to generate a keystore file "keystore.jks"
in the "key" folder and add the corresponding values in "keystore.properties" file.

### Available screen in the app

There are the following screens in the app:
- PopularMovies
- UpcomingMovies
- TopRatedMovies
- SearchMovies
- MovieDetails

### Screenshots
<img alt="Popular" title="Popular" width="216" height="427" src="/screenshots/popular_movies_screen.jpg"/> <img alt="Popular" title="Top Rated" width="216" height="427" src="/screenshots/top_rated_movies_screen.jpg"/>
<img alt="Popular" title="Upcoming" width="216" height="427" src="/screenshots/upcoming_movies_screen.jpg"/> <img alt="Popular" title="Search" width="216" height="427" src="/screenshots/search_screen.jpg"/>
<img alt="Popular" title="Search Not Found" width="216" height="427" src="/screenshots/search_not_found_screen.jpg"/> <img alt="Popular" title="Detail" width="216" height="427" src="/screenshots/detail_screen.jpg"/>

### The architecture and technical documentation

TMDB API is used for getting the movies.
After opening the application user will see popular movies in the screen.
Using the bottom navigation view, user can navigate between ***popular -> upcoming -> top_rated -> search screens.***
Every time we fetch movies, the first page needs to be saved in the memory for the offline view.
On clicking every movie corresponding movie details should be shown to the user.
If some error will occur corresponding error message will be shown to the user.

The application architecture is MVVM with some MVI style (**MVVMI**).
The following libraries and components were used in the app:

- Dagger Hilt for the dependency injection
- Retrofit and Coroutines for the network calls
- Room with Coroutines for the offline caching
- Glide for the image loading

#### Thank you

Thank you for your provided time if you read until this section. Special thanks to the creators of the app, I mean me :)
If you'll have some questions, don't hesitate and ask in StackOverFlow.
All rights reserved... Maybe :D 