## Movie Discovery

An application where you can ***discover popular, top-rated, and upcoming*** movies.
The application has search functionality. You can use it and find your desired movie.
By clicking on your desired movie you'll get detailed information.

### Available screen in the app

There are the following screens in the app:
- PopularMovies
- UpcomingMovies
- TopRatedMovies
- SearchMovies
- MovieDetails

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