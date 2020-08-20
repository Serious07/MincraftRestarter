# Mincraft Restarter
Programm to restart minecraft servers

Allowed to check if server online localy on server and globaly on internet, by domain for example

If server offline restart it the best way possible, by killing process or sending command on server screen

Works only on linux OS

# How to use?

- Download last jar and configs from Build folder
- Put it in minecraft location
- Allow jar run as executble
- Tweak you configs in MineRestarter/MincraftRestarter.ini
- Run screen on server for example screen -S MinecraftRestarter
- cd to programm location for example cd /home/username/minecraftServer/
- run command "java -Xmx124M -Xms124M -jar MinecraftRestarter.jar" also you can create .sh file to run it instead
- Press Ctrl + A + D to exit screen

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
- serverStopServerCmd - server stop command to send on running screen 
- serverKillServerCmd - kill server command to OS
- serverRunCmd - server run command
