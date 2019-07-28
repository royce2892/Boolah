## Testing Instructions

*Open any city page, swipe left to save place locally, swipe right if you don't wish to save
*Check lowest fares to the destination from your home location
*See most busiest periods in the destination
*Check cheapest round trip dates from your home location to this destination
*See most traveled and most booked dates for your journey from home location to destination in flight fares list page
*Track flight fares and view them locally in your my world page
*Based on saved places itinerary can be found in the today tab 
*Navigate to the itinerary place or remove it
*Check my world page to see list of all saved translations, tracked flight prices and saved wishlist of places


## Inspiration

When you land in Fiji and are first welcomed with the greeting "Bula!" (pronounced boo-lah!), you will certainly feel you have arrived somewhere special. The literal meaning of this word is **Life**, and Boolah aims to do exactly that, **"Say Hello to the Life of Travel"** encompassing your interests and empowered by local language support.

## What it does

Boolah does all the **travel planning for you**. It optimizes the quality and number of places covered a day at your destination based on your **interests** and your **distance** from the places. Boolah exists for the adventure seekers who live in the moment and make plans in a jiffy. Reports suggest that as much as 85% of today's vibrant young travel community never look for activities before they arrive at a place. What if we could simplify their itinerary after they arrive at a place? So in place of travelers browsing through a not-so-short list of recommendations and reviews to create their own itinerary, Boolah **builds the itinerary** for them.

## How Boolah picks places for you

Open Boolah and you see a page to explore 8 cities we've hand picked for you. Each city has several points of interests with an image and description. The user can **swipe left or right** the point. Just swipe left if you like the place or wish to pass the same, right if you don't. It's as simple as that, and unlike those numerous dating apps, our places don't swipe right on you, you can always visit every place that you've swiped left. 

Boolah learns your travel interests based on your selections and we build the itinerary based exactly on your choice styles. Say you prefer natural parks over skyscrapers, we make sure you get to see Central Park or the Hudson river in NYC before we take you to the towering skyscrapers. The more you swipe, the better we get to understand your favorites and the better we build your itinerary. Every point of interest that you've liked gets saved to your liked places list in *My world* page.

*So swipe your way to a better, personalized AI powered itinerary tailor-made for you.*

## Fares and cheapest round trip dates

If you thought itinerary was just what you needed, here's more...

You also have the option to see **lowest fares** for the dates you've in mind for that dream destination in your bucket list!
Or if you're flexible on your dates and are looking for the cheapest round trip dates that can help your pockets fuel 2 or 3 air fare tickets at the normal cost of a single trip, we have that too! 
That's how Boolah simplifies your travel options considering your timeline and pockets. Boolah also gives you the option to track the fares you've shortlisted from your *My world* page.

## Follow the herd or stay distinct

Maybe you're a people person or maybe you wish to travel in solitude. Boolah lets you do both.
For every flight fare you browse, we let you know what were the most traveled months in terms of people visiting from your hometown to your tourist destination. Maybe you wish to visit when the city is full of people buzzing or maybe you wish to visit an isolated place where you're in perfect harmony with the serenity of nature. 

That's not all. 
Boolah also lets you know what were the most booked and most traveled destinations from your hometown, helping you build your shortlist of dream destinations curated based on travel preferences of people near you.

## Lit your trip with local language

Ability to learn and save local language translations of common phrases is the icing on the cake. Knowing a bit of the local language can go a long way to improving the quality of your vacation. Fly anywhere in the world and you'll see that the locals will appreciate that you took the time to learn about their culture. Boolah let's you learn common phrases and save the translations locally just in case you forget and need a quick reminder once you enter the land of your dreams. You can take this translations offline as these are saved in your local app database that works with or without an internet connection

**In short, Boolah has all the tips that you ever thought you needed for traveling... and more.**

## How I built it

Places were fetched by the **point of interest** API. Tags, categories and sub categories were used in classifying points and then matched up to generate the daily itinerary. Several flights APIs were used to better plan travel which are listed below </br>
 **Flight Cheapest Date Search** - To display cheapest upcoming round trip dates </br>
 **Flight Low-fare Search**  - Display prices for selected dates </br>
 **Airport & City Search** - Find user's current location and nearby airport </br>
 **Flight Most Booked Destinations** - Show most booked destinations in fare and cheapest dates list </br>
 **Flight Mose Traveled destinations** - Show most traveled destinations in flight lists </br>
 **Most busy travel period** - Show most busy months in a particular destination </br>

Here Maps SDK was integrated for the maps part of today's itinerary generator and Yandex Translate API were used to provide local language support.

## Challenges I ran into

Generating a interest based itinerary was the most complicated part of the app as it involved considering several factors like user interests , distance to the place, managing times and preferences to suggest restaurants.

## Accomplishments that I'm proud of

I have packed quite a lot of features in what looks and feels like a seemingly light app with few screens and well managed space, easy to use UI and local database support for tracked flight prices, dates,saved local translations and shortlisted / liked point of interests in a place.

## What I learned

Amadeus Java API sdk, here maps and Microsoft translate APIs are something that I integrated into an app for the first time.

## What's next for Boolah

I wish to improve the itinerary generator by taking into account the anticipated weather data for the dates of travel and shooting suggestions based on behavior of like minded travelers. Options of itinerary export, sharing and user sign in and cloud saving are on the cards too
