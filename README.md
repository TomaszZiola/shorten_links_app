# shorten-links

To make any link shorter, please send a post request under: localhost:8080/shorten. Request should be json looking like this:

    {
      "link": "https://www.baeldung.com/rest-with-spring-series"
    }

Shorten link is valid only for 24 hours. After this time it gets deleted from database and cannot by accesed any longer.

After sending such a request user will receive response which looks like this:

     {
       "link": "PH23JY",
       "password": "XUF"
     }

This link can be used to navigate to original site. Simple send a get request to localhost:8080/{link} 

Password is used to delete link manually from database.

To use this link, user may send delete request to localhost:8080/{password}
