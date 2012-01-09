import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
	public static String image_directory = "";
	public static String csv_file = "";
	public static String log_file = "";

	public static List<String> listFilesInDir(String dirname) {
		File dir = new File(dirname);
		List<String> result = new ArrayList<String>();
		for(String s : dir.list()) {
			s = s.toLowerCase();
			if(s.endsWith("jpg") || s.endsWith("jpeg")) {
				result.add(s);
			}
		}
		return result;
	}

	public static String basePath() {
		return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())+"/";
	}

	public static boolean doesExist(String filename) {
		File file=new File(filename);
		return file.exists();
	}

	public static void processFiles() throws IOException, SQLException {
		for(String s : Main.listFilesInDir(image_directory)) {
			String file_name = s.split("\\.")[0];
			String file_extension = s.split("\\.")[1];
			writeToCSV(file_name, image_directory+file_name+"."+file_extension);						
		}	
	}

	public static void writeToCSV(String personellnr, String image) throws IOException {

		File f = new File(csv_file);

		if(!f.exists()) { 
			f.createNewFile();	
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(csv_file, true)); 
		String s = "23,1,,,,,,"+personellnr+",,,,,,,,,,,,,,,,"+image+",,,,,,,,,,,,,,,,,,,,,,,,,,\n";
		out.write(s); 
		out.close(); 	

		Main.writeToLog(s);
		
	}

	public static void writeToLog(String msg) throws IOException {
		File f = new File(log_file);

		if(!f.exists()) { 
			f.createNewFile();	
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(log_file, true)); 
		Date date = new Date();
		out.write(date + " | " + msg + "\n"); 
		out.close(); 

	}

	public static void main(String[] args) throws SQLException, IOException {

		String settingsLocation = "settings.xml";
		try {
			settingsLocation = args[0];
		}
		catch(Exception e) {}
		
		Settings settings = new Settings(settingsLocation);

		image_directory = settings.settings.get("image_directory");
		csv_file = settings.settings.get("csv_file");
		log_file = settings.settings.get("log_file");

		if(!doesExist(csv_file)) {
			Main.processFiles()	;
		} else {
			Main.writeToLog("CSV-file still there, Nedap not done");
		}
	}
}
