# New-World-MiniMap
A custom minimap that displays resources all around you while you adventure through Aeternum!

# Download
[Download Page](https://github.com/llabinator/New-World-MiniMap/releases/tag/Minimap)
- Minimap.rar

# The Program
An enhanced minimap for New World. The application takes screenshots of your Primary Monitor Screen using Optical Character Recognition (OCR) to detect your position on the world map while the 'Show FPS' menu option is enabled. The application DOES NOT interact with the game itself. It DOES NOT inject itself into memory. It DOES NOT read game memory. It DOES NOT contact with the game whatsoever. The program simply takes the information being displayed on your screen. It is no different than recording/streaming your screen, therefore, it applys to the terms and service.

# Using Newworld-map.com
Thanks to the already brilliant website [newworld-map.com](https://newworld-map.com) for there easy to use map and database. This made it easy for the program to open up the website using Selenium and manipulate the website into showing the player position.

# Must Haves
You must have Java installed on your PC. I am not entirely sure what version is necessary, but most should work.
You must also have Firefox installed on your PC. This is because I am using Seleniums firefox drivers.

# What to do
- Download the .rar from the [Download Page](https://github.com/llabinator/New-World-MiniMap/releases/tag/Minimap)
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

# Licensing
This program is fully open-source under the MIT license. Thanks to Selenium and ITesseract for allowing me to create this application, the licenses for the methods used are in the Licenses folder. 
