# Parlor Beacon ( Work in Progress)

## Project overview

As the name suggests, it is a beacon (search) service for parlors and salons of all types. The main
motive of this project is to increase convenience among the masses for a fluid service. They do
not have to come to a particular parlor and wait in line for their turn; that’s a hassle. Instead
through this Android-based application, we intend to aid them in simplifying this experience.
Registered customers can book an appointment beforehand and get their time slot fixed for their
particular needed service. This will not only save time for the customers but will also help in the
smooth working of salons.

## Features of the Application

The main features of this app are:
- Keep a record of information of Salons and Customers.
- Allow Shopkeepers to add employee details and salon services.
- Allow Shopkeepers to maintain history of their customers and reviews.
- Allow Shopkeepers to accept or reject customer bookings according to their shop schedules.
- Allow Customers to search and book services according to their needs.
- Allow Customers to give rating and reviews to the shop services provided to them.

## Intended Users

- Shopkeepers who run parlors or Salons
- People / Customers who want to use salon services.

## Key Considerations

### How will the app handle data persistence?

App will store data in
- PostrgreSQL Database of server.
- Local SQlite Database using Content Provider.
- Shopkeeper Application regularly pulls or sends data to and from a web service, so app updates data in its cache at regular intervals using a SyncAdapter.
- Customer Application performs short duration, on-demand requests(such as search) which uses AsyncTask.

### Any corner cases in the UX?

- No Employee data added : Show message
- No Service added : Show Message
- No Picture added : Show Message
- No internet connection : Show Message
- No Bookmarks : Show Message
- No Favourites : Show Message

### Libraries Used?

The libraries which will be used are
- Glide to handle the loading and caching of images.
- Butterknife to reduce the boilerplate code required for binding views.
- Android Design Support to include material design.
- Volley to fetch and send data to server.

## Contributing

You can contribute to the project by either finding out bugs or by requesting new features.

## License

MIT License

Copyright (c) 2017 Mayank Agarwal

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
