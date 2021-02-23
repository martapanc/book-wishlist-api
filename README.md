# Book Wishlist Scraper

Request / scrape Amazon's and Goodreads's wish lists.

## Setup
- Rename `application.properties.local` to `application.properties`
- Add Goodreads API key to the file and the Chromedriver local path

When deploying to Heroku, the following variables also need to be set:

| key | value |
| --- | ----- |
| `CHROMEDRIVER_PATH` | `/app/.chromedriver/bin/chromedriver` |
| `GOOGLE_CHROME_BIN` | `/app/.apt/usr/bin/google_chrome` |
| `GOODREADS_KEY` | `your-api-key` |

Commands to install required buildpacks:
```
heroku buildpacks:add --index 1 https://github.com/heroku-buildpack-chromedriver 
heroku buildpacks:add --index 2 https://github.com/heroku/heroku-buildpack-google-chrome
```

Start dyno:
```
heroku ps:scale web=1
```

## Resources
- Heroku and Chromedriver: https://medium.com/@mikelcbrowne/running-chromedriver-with-python-selenium-on-heroku-acc1566d161c
- Sample Amazon Wishlist scraper with Selenium: https://github.com/miguelvazsilva/selenium-amazon
- Amazon Wish lister: https://github.com/doitlikejustin/amazon-wish-lister  
- Scraping Amazon wishlists: https://www.reddit.com/r/amazon/comments/7paenb/amazon_wishlist_web_scraper/

Docker:
- https://spring.io/guides/gs/spring-boot-docker/
- https://stackoverflow.com/questions/58527079/headless-chrome-with-selenium-and-java-in-a-docker-container
- https://github.com/Hronom/chromedriver-docker-example
- https://stackoverflow.com/questions/62167572/cannot-run-program-chromedriver-from-a-docker-container
- https://stackoverflow.com/questions/41144934/cannot-find-chrome-binary-when-executing-a-selenium-testng-test-in-jenkins-on
- https://linuxize.com/post/how-to-install-google-chrome-web-browser-on-ubuntu-18-04/
