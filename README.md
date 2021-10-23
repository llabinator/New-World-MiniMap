# New-World-MiniMap
A custom minimap that displays resources all around you while you adventure through Aeternum!

# Download
[Download Page](https://github.com/llabinator/New-World-MiniMap/releases/latest)
- Minimap.rar

# Other Project I am Working On
[Github](https://github.com/llabinator/New-World-CreatureInfo)

# The Program
An enhanced minimap for New World. The application takes screenshots of your Primary Monitor Screen using Optical Character Recognition (OCR) to detect your position on the world map while the 'Show FPS' menu option is enabled. The application DOES NOT interact with the game itself. It DOES NOT inject itself into memory. It DOES NOT read game memory. It DOES NOT contact with the game whatsoever. The program simply takes the information being displayed on your screen. It is no different than recording/streaming your screen, therefore, it applys to the terms and service.

# Using Newworld-map.com
Thanks to the already brilliant website [newworld-map.com](https://newworld-map.com) for there easy to use map and database. This made it easy for the program to open up the website using Selenium and manipulate the website into showing the player position.

# Must Haves
You must have Java installed on your PC. Java version 1.8+. 
You must also have Firefox installed on your PC. This is because I am using Seleniums firefox drivers.

# What to do
- Download the .rar from the [Download Page](https://github.com/llabinator/New-World-MiniMap/releases/latest)
- Extract the .rar
- To Run with Non-Console mode for the application just open the .jar with "Java(TM) Platform SE Binary"
  - If a firefox window does not pop up in a few seconds, try running in Console mode and check for errors

- To Run with Console mode, open cmd and change directory into the un-zipped folder
  - Then type: java -jar Minimap.jar

- If errors persist, please contact me at my [discord](https://discord.gg/HxsTVM3wB2)

- A firefox window should pop up
  - if window just shows 'new tab' continously, move your player around in game to make sure OCR can pick up position
- Otherwise newworld-map.com should pop up with your position.

- You can zoom in more onto your position in order to display the resources around you more clearly.

- If you want the website to smoothly follow your pointer (im not sure why this is not on by default), click one of the search buttons (like 'NPC') and click the search icon for a random item. This will move the pointer temporarly, however, moving in game will set your position back and auto centering will be enabled.

The map should up every 5 secs (DEFAULT) (+ a few seconds if OCR is having trouble). Amount of time is adjustable now.

NOTE THIS WARNING IS NORMAL: Warning: Invalid resolution 0 dpi. Using 70 instead. I can't seem to find a way around it not yelling this constantly.

# Calibration
In the GUI when you run the .jar, You are able to use the sliders in order to find the position in the top right of your screen. Since resolutions effect each the position, and width of the screenshot, I have made it possible to calibrate this process. You want to calibrate it so the position in brackets are only showing. Like this:
![screenShot](https://user-images.githubusercontent.com/78010038/137380363-a362f094-b83d-4217-b97d-024b10a5ab19.png)
If there is any sliver of text on the left most side, the OCR will pick that up and the program will not work.

# Licensing
This program is fully open-source under the MIT license. Thanks to Selenium and ITesseract for allowing me to create this application, the licenses for the methods used are in the Licenses folder. 
