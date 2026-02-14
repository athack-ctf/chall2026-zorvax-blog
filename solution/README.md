# How to Solve the Challenge?

The URL has a route called `/blog`, (eg. `http://localhost:8080/blog/:PostId`) which directly executes PostId within an sql query to return a blog post without validation or cleaning.

The payload `-1%20UNION%20SELECT%20*%20FROM%20users` should be typed in place of PostId (eg. `http://localhost:8080/blog/-1%20UNION%20SELECT%20*%20FROM%20users`) to union nothing from the blogposts table with everything from the users table. The view of this route returns only the first result which will return a username in place of the blog header, and a password in place of the blog body (The username is `zorvax` and Password is `Z0rVaxL0v3sK1tt3ns`).

Navigate to the login page (eg.`http://localhost:8080/login`) and type in the found credentials, once validated by the login a message will pop-up showing the flag.
