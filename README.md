# Mincraft Restarter
Programm to restart minecraft servers

Allowed to check if server online localy on server and globaly on internet, by domain for example

If server offline restart it the best way possible, by killing process or sending command on server screen

Works only on linux OS

# Options description

- checkIntervalInSconds - interval to check server online or not
- serverStopTimeInSeconds - time to stop server correctly after stop command
- serverStartTimeInSeconds - time to run server correctly after start
- serverLocalIp - 0.0.0.0 or localhost for local server ip adress
- serverGlobalIp - global ip adress or domain for example mc1.gregtechrus.ru or 345.23.345.67
- serverGlobalPort - global port of server 25565 by default
- serverLocalPort - loсal port of server 25565 by default
- screenTag - string replace for commands
- screenName - screen name in linux OS to use for server
- serverStopServerCmd - server stop command to send to running screen 
- serverKillServerCmd - kill server command to OS
- serverRunCmd - server run command
