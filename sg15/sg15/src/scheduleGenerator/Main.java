package scheduleGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles the interaction of one frame to another as well as
 * handling initialization.
 * 
 * @author Mason Schneider and Orion Martin. Created Oct 8, 2012.
 */
public class Main {

	// SWAP 1, TEAM 06
	// Smell: Divergent Change
	// Main really shouldn't have getter and setter methods, something is wrong here.
	// Could be moved into more appropriate classes or split into it's own.
	
	private static ArrayList<Day> days;
	private static ArrayList<Worker> workers;
	private static File standardPath = new File("schedule_data.ser");
	private static File path;
	/**
	 * Configures days.
	 */
	static Config config;
	/**
	 * Configures workers.
	 */
	static WorkerSetup wSet;
	/**
	 * Displays schedule.
	 */
	static CalendarGUI cal;
	private static Schedule schedule;

	/**
	 * Program starts here.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//path = new File("schedule_data.ser");
        // SWAP 1, TEAM 06
        // ADDITIONAL FEATURE
        // Now have the option to load a schedule from file via the command line
        // Before this was a hard coded value, which helped lead to code smell.
		config = new Config();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter schedule file name:");
		String input = scanner.next();
		scanner.close();
		if(input.length() >=5){
		System.out.println(input.substring(input.length()-5, input.length()));
		}
		if (input == null || input.isEmpty()){
			path = standardPath;
		}
		else if(input.length() >=5){
			if(input.substring(input.length()-5, input.length()).equals(".ser")){
				path = new File(input);
			}
			else{
				path = new File(input + ".ser");
			}
		}
		else{
			path = new File(input + ".ser");
		}
		// Code to open the config file.

		try {
			recallConfigFile();
			if (getSchedule() != (null)) {
				cal = new CalendarGUI(getSchedule());
				// config.setVisible(true);
				cal.setVisible(true);
			} else {
				config.setVisible(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Changes visible of config.
	 * 
	 */
	public static void toggleConfig() {
		config.setVisible(!config.isVisible());
	}

	/**
	 * Changes visible of calendar.
	 * 
	 */
	public static void toggleCalendar() {
		cal.setVisible(!cal.isVisible());
	}

	/**
	 * Changes visible of worker setup.
	 * 
	 */
	public static void toggleWorkerSetup() {
		if (wSet != null) {
			wSet.setVisible(!wSet.isVisible());
		}
	}

	/**
	 * Returns the value of the field called 'schedule'.
	 * 
	 * @return Returns the schedule.
	 */
	public static Schedule getSchedule() {
		return Main.schedule;
	}

	/**
	 * Sets the field called 'schedule' to the given value.
	 * 
	 * @param schedule
	 *            The schedule to set.
	 */
	public static void setSchedule(Schedule schedule) {
		Main.schedule = schedule;
	}

	/**
	 * Sets the value of workers.
	 * 
	 * @return workers
	 */
	public static ArrayList<Worker> getWorkers() {
		return workers;
	}

	/**
	 * Sets workers.
	 * 
	 * @param w
	 */
	public static void setWorkers(ArrayList<Worker> w) {
		workers = w;
	}

	/**
	 * Returns the value of the field called 'days'.
	 * 
	 * @return Returns the days.
	 */
	public static ArrayList<Day> getDays() {
		return days;
	}

	/**
	 * Sets the field called 'days' to the given value.
	 * 
	 * @param d
	 */
	public static void setDays(ArrayList<Day> d) {
		days = d;
	}

	/**
	 * Dumps data to the file schedule_data.ser.
	 * 
	 */
	public static void dumpConfigFile() {

		try {
			path.delete();
			path.createNewFile();
			FileOutputStream dumpConfig = new FileOutputStream(path);
			ObjectOutputStream fileStore = new ObjectOutputStream(dumpConfig);
			fileStore.writeObject(days);
			fileStore.writeObject(workers);
			fileStore.writeObject(schedule);
			fileStore.writeObject(HTMLGenerator.getTables());
			fileStore.close();
			dumpConfig.close();

			System.out.println("Stored");

		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Recalls data from schedule_data.ser.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void recallConfigFile() throws ClassNotFoundException,
			IOException {
		if (path.exists()) {
			FileInputStream recallConfig = new FileInputStream(path);
			ObjectInputStream fileRecall = new ObjectInputStream(recallConfig);
			days = (ArrayList<Day>) fileRecall.readObject();
			workers = (ArrayList<Worker>) fileRecall.readObject();
			schedule = (Schedule) fileRecall.readObject();
			HTMLGenerator.setTables((String) fileRecall.readObject());

			fileRecall.close();
			recallConfig.close();
		}
	}
}
