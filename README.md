# SmallSlackBot
A small bot in slack with 2 web endpoints.

This is a simple bot in slack that does the following:
1. Sending the average number of all the user's previous messages whenever he sends a new number.
2. sends the total average of all the users to all the public channels unless it didn't changed since the last time.
The project have 2 endpoints:
1. localhost:8081/average will show the current total average.
2. localhost:8081/average/{slack_user_name} will show the slack user's specific average. 
