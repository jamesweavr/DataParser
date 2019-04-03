# screenData
This code allows me to parse and interpret data that I am able to collect from my phone.

Using the app "Tasker" I am able to record the date and time each time the screen is either turned on or turned off.
Saving this is JSON format allows me to more easily parse the collected data.

Upon loading the JSON formated screen data I can check whether the screen was turned on or off. If the screen was turned on then I store 
the corresponding time and check the next time the screen was turned off. Subtracting these times gives me the time that the screen was 
on. Finally I sort the amount of time the screen was on both by the date and the hour of the day. I can average these lists to give me an 
average 'screen on time' per date and per hour of day. These averages are exported as a CSV file so that the data can be interpreted by 
excel and a chart can me made to better make sense of the data.

Some of this data skews my averages. For example, while driving to a new location I used Waze which prevents the screen from sleeping. 
This in turn resulted in one data point of over 1 hour of 'screen on time', which skewed the data for both that date and time of day. 
