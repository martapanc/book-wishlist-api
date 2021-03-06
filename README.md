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
- Destroy reviews endpoint not working: https://www.goodreads.com/topic/show/19383522-using-review-destroy-to-delete-a-review-of-a-book
- OAuth example in Java: https://github.com/davecahill/goodreads-oauth-sample/blob/master/src/main/java/oauth/GoodreadsOAuthSample.java
- Sample API client: https://github.com/rharter/goodreads-api
- GR API from Android: https://medium.com/@intmainreturn00/goodreads-api-from-android-with-kotlin-fd8c2c7338d8
