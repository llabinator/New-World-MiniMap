import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;


public class Main {
	
	// SOME STATIC VARIABLES USED THROUGHOUT
	static JLabel img = new JLabel();
	static Robot robot = null;
	static boolean calibrating = true;
	static ArrayList<Integer> calibratedValues = new ArrayList<>(4);
	
	//Finds position of 'Position [...]' text relative to screen size
			//	Default Values
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int x = screenSize.width - 268;
	static int y = 19;
	static int width = 269;
	static int height = 20;
	static int period = 1;

	public static void main(String[] args) throws IOException, InterruptedException {
		
		// LOAD CALIBRATION IF IT EXISTS, OTHERWISE RESULT TO 'DEFAULT' SETTINGS
		if(loadCalibration()) {
			x = calibratedValues.get(0);
			y = calibratedValues.get(1);
			width = calibratedValues.get(2);
			height = calibratedValues.get(3);
		} else {
			if(screenSize.width > 2600) {
				x = screenSize.width - 270;
			} else if(screenSize.width > 2000) {
				x = screenSize.width - 268;
			} else if(screenSize.width > 1300) {
				x = screenSize.width - 265;
			} else if(screenSize.width > 800) {
				x = screenSize.width - 263;
			}
		}
		
		BufferedImage screenShot;
		
		try {
			// INITIALIZE SCREENSHOTTER
			robot = new Robot();
			
			// START GUI CONFIG
				// FRAME SETTINGS AND HEADER SETTINGS
			JFrame frame = new JFrame();
			
			frame.setPreferredSize(new Dimension(1000, 1000));
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("LLab's NewW Minimap v1.3");
			BufferedImage logo = ImageIO.read(new URL("https://images.ctfassets.net/j95d1p8hsuun/4N9cVzWDzqiBiCSlxhziuM/831a668b08e28a898e8319f162f5632b/map-for-web-2.png"));
			frame.setIconImage(logo);
			
			JPanel header = new JPanel();

			header.setPreferredSize(new Dimension(920, 100));
			header.setLayout(new GridLayout(8, 4));
			header.setBorder(new EmptyBorder(10, 40, 10, 40));
			
			// DISPLAY CURRENTLY WHAT IS BEING SCREENSHOTTED
			JLabel headerTxt = new JLabel("This is what the application is currently screenshotting: ");
			img = new JLabel();
			
			// DISPLAY EXPECTED SCREENSHOT
			JLabel headerTxtShould = new JLabel("This is what it should look like: ");
			JLabel imgShould = new JLabel();
			BufferedImage im = ImageIO.read(new URL("https://raw.githubusercontent.com/llabinator/New-World-MiniMap/main/screenShot.png"));
			imgShould.setIcon(new ImageIcon(im));
			
			// LABELS FOR SLIDERS
			JLabel startX = new JLabel("Starting X Position: ");
			JLabel startY = new JLabel("Starting Y Position: ");
			JLabel widthL = new JLabel("Width of Screenshot: ");
			JLabel heightL = new JLabel("Height of Screenshot: ");
			
			// SUBPANEL WITH PERIOD COMPONENTS
			JPanel options = new JPanel();
			JLabel periodL = new JLabel("How fast would you like to update the tracker (In Seconds)?");			
			JLabel periodL2 = new JLabel("Select an option from the dropdown: ");
			JComboBox<String> periodChoices = new JComboBox<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" });
			periodChoices.setSelectedIndex(period - 1 );
			
			// SLIDER COMPONENTS
			JSlider xslider = new JSlider(0, screenSize.width, x);
			JSlider yslider = new JSlider(0, screenSize.height/4, y);
			JSlider widthslider = new JSlider(0, screenSize.width/4, width);
			JSlider heightslider = new JSlider(0, screenSize.height/8, height);
			JSlider[] sliders = { xslider, yslider, widthslider, heightslider };
			
			// CALIBRATED BUTTON
			JButton button = new JButton("Calibrated");
			
			// ADD ALL COMPONENTS TO PANEL
			header.add(headerTxt);
			header.add(img);
			header.add(headerTxtShould);
			header.add(imgShould);
			header.add(startX);
			header.add(xslider);
			header.add(startY);
			header.add(yslider);
			header.add(widthL);
			header.add(widthslider);
			header.add(heightL);
			header.add(heightslider);
			header.add(periodL);
			header.add(options);
			options.add(periodL);
			options.add(periodL2);
			options.add(periodChoices);
			header.add(button);
			
			//------- SETTINGS FOR SLIDERS -------
			xslider.setMajorTickSpacing(256);
			xslider.setMinorTickSpacing(128);
			xslider.setPaintTicks(true);
			xslider.setPaintLabels(true);
			
			yslider.setMajorTickSpacing(100);
			yslider.setMinorTickSpacing(50);
			yslider.setPaintTicks(true);
			yslider.setPaintLabels(true);
			
			widthslider.setMajorTickSpacing(128);
			widthslider.setMinorTickSpacing(64);
			widthslider.setPaintTicks(true);
			widthslider.setPaintLabels(true);
			
			heightslider.setMajorTickSpacing(32);
			heightslider.setMinorTickSpacing(16);
			heightslider.setPaintTicks(true);
			heightslider.setPaintLabels(true);
			//------------------------------------
			
			// When value sliders are changes/moved
				// Update the preview screenshot
			for(JSlider slider : sliders) {
				slider.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						updateScreenshot(xslider.getValue(), yslider.getValue(), widthslider.getValue(), heightslider.getValue());
					}
				});
			}
			
			// When 'Calibrated' button is clicked
				// Stop update loop and stop displaying GUI
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					calibrating = false;
					frame.setVisible(false);
					
				}
			});
			
			frame.add(header);
			frame.pack();
			
			while(calibrating) {
				TimeUnit.MILLISECONDS.sleep(400);
				updateScreenshot(x, y, width, height);
			}
			
			// Sets period from dropdown menu
			period = Integer.valueOf((String) periodChoices.getSelectedItem());
			
			// Resets the calibration settings if changes were made
			calibratedValues.clear();
			calibratedValues.add(x);
			calibratedValues.add(y);
			calibratedValues.add(width);
			calibratedValues.add(height);
			
			// Then saves the calibration
			saveCalibration();
			
			//END OF GUI CONFIG
		
		} catch(Exception e) {
			System.out.print("Failed to create Robot object: ");
			e.printStackTrace();
			System.out.println("\nContact creator of program @MalBall_");
			System.exit(1);
		}
		
		try {
			
			boolean loop = true;
			boolean debug = true;
	
			System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
			WebDriver driver = new FirefoxDriver();
			
			//Implement in future?
			//driver.manage().window().setSize(new org.openqa.selenium.Dimension(500, 500));
			//driver.manage().window().setPosition(new Point(screenSize.width-500, (screenSize.height / 2) - 250));
			
			while (loop) {
				
				screenShot = robot.createScreenCapture(new Rectangle(x, y, width, height));
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
						
						//driver.get("https://www.newworld-map.com");
						
						
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

		} catch (Exception e) {
			System.out.print("Program failed... ");
			e.printStackTrace();
			System.out.println("\nContact creator of program @MalBall_");
		}

	}
	
	// UPDATES SCREENSHOT IN STARTING GUI
	public static void updateScreenshot(int newx, int newy, int newwidth, int newheight) {
		if(width != 0 || height != 0) {
			x = newx;
			y = newy;
			width = newwidth;
			height = newheight;
			img.setIcon(new ImageIcon(robot.createScreenCapture(new Rectangle(x, y, width, height))));
		}
	}
	// ----------------------------------
	
	// LOAD AND SAVING CALIBRATION SETTINGS
	public static boolean loadCalibration() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("calibration.txt"));
			calibratedValues = (ArrayList<Integer>) ois.readObject();
			ois.close();
		} catch(Exception e) {
			return false;
		}

		return true;
	}
	
	public static void saveCalibration() {
		try (
			FileOutputStream fos = new FileOutputStream("calibration.txt"); 
		    ObjectOutputStream oos = new ObjectOutputStream(fos)) {
		    oos.writeObject(calibratedValues);
		    fos.close();
		    oos.close();
		} catch(Exception e) {
			System.out.println("Error when saving calibration: " + e);	
		}
	}
	//--------------------------------------
	
	
}
