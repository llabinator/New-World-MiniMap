import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		int period = 5;
		
		String test = JOptionPane.showInputDialog(null, "How fast would you like to update the tracker (Default: 5 seconds, just enter a plain number)");
		try {
			if(test != null)
				period = Integer.parseInt(test);
			
			System.out.println("Tracker Update Every " + period + " Seconds.");
		} catch(Exception e) {
			System.out.println("User entered something else than just a number. Default update timer set.");
		}
		
		try {
			Robot robot = new Robot();
			
			//Finds position of 'Position [...]' text relative to screen size
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = screenSize.width - 269;
			if(screenSize.width > 2600) {
				x = screenSize.width - 273;
			} else if(screenSize.width > 2000) {
				x = screenSize.width - 270;
			} else if(screenSize.width > 1300) {
				x = screenSize.width - 265;
			} else if(screenSize.width > 800) {
				x = screenSize.width - 262;
			}
			int y = 19;
			int width = 269;
			int height = 20;
			
			boolean loop = true;
			boolean debug = false;
			
			if(args.length >= 1)
				if(args[0].toLowerCase() == "debug")
					debug = true;
			
			
			System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
			WebDriver driver = new FirefoxDriver();
			

			while (loop) {
				
				//BufferedImage screenShot = robot.createScreenCapture(new Rectangle(x, y, width, height));
				BufferedImage screenShot = robot.createScreenCapture(new Rectangle(x, y, width, height));
				RescaleOp rescaleOp = new RescaleOp(0.8f, 0f, null);
				rescaleOp.filter(screenShot, screenShot);
				
				if(debug)
					ImageIO.write(screenShot, "png", new File("screenShot.png"));
				
				ITesseract instance = new Tesseract();
				

				try {
					
					//Does OCR on the screenshot
					String result = instance.doOCR(screenShot);				
					
					if (!result.matches(".*[a-zA-Z]+.*")) {
						// Simplifys the string, removing decimals/commas
						char[] resultArr = result.toCharArray();
						String finalResult = "";
						for (int i = 0; i < resultArr.length; i++) {
							if (resultArr[i] == ',' || resultArr[i] == '.' && resultArr[i + 1] != ' ') {
								i += 4;
							}
							finalResult += resultArr[i];

						}
					
						//Extracts pos[0] & pos[1] from the siplified string
						int beginIndex = 1;
						int count = 0;
						int[] pos = new int[2];
						char[] finalCharArr = finalResult.toCharArray();
						if (finalCharArr[0] == '[' || finalCharArr[0] == '(') {
							for (int i = 1; i < finalCharArr.length; i++)
								if (finalCharArr[i] == ' ' || finalCharArr[i] == ',' && count < 2) {
									pos[count++] = Integer.valueOf(finalResult.substring(beginIndex, i));
									i += 2;
									beginIndex = i;
								}
						} else {
							for (int i = 0; i < finalCharArr.length; i++)
								if (finalCharArr[i] == ' ' || finalCharArr[i] == ',' && count < 2) {
									pos[count++] = Integer.valueOf(finalResult.substring(beginIndex - 1, i));
									i += 3;
									beginIndex = i;
								}
						}
						
						// Stops program if window is closed
						try {
					        driver.getTitle();
					        driver.getCurrentUrl();
					    } catch(Exception ubex) {
					        loop = false;
					    }
						
						// Display Results, and validates the position to an actual existing point
						System.out.println("Position Recorded: " + pos[0] + " : " + pos[1]);
						if(pos[1] > 100 && pos[0] > 100 && pos[1] < 15000 && pos[0] < 15000) {
							
							// Updates the website with new coords
							driver.get("https://www.newworld-map.com/#/?lat=" + pos[1] + "&lng=" + pos[0]);
							TimeUnit.SECONDS.sleep(period);
						}
					} else {
						throw new Exception();
					}

				} catch (Exception e) {
					System.err.print("Failed with Tesseract or Finding Position: ");
					e.printStackTrace();
				}

			}

		} catch (AWTException e) {
			System.out.print("Failed to create Robot object: ");
			e.printStackTrace();
			System.out.println("\nContact creator of program @MalBall_");
		}

	}
	
	
}
