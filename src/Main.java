import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Main {
	public static String image_directory = "/Users/fnc/Pictures/";
	public static String image_directory_proccesed = image_directory + "processed/";
	public static String csv_file = "/Users/fnc/Pictures/csv_file.csv";

	public static List<String> listFilesInDir(String dirname) {
		File dir = new File(dirname);
		List<String> result = new ArrayList<String>();
		for(String s : dir.list()) {
			s = s.toLowerCase();
			if(s.endsWith("jpg")) {
				result.add(s);
			}
		}
		return result;
	}

	public static boolean doesExist(String filename) {
		File file=new File(filename);
		return file.exists();
	}

	public static void moveFile(String from_file, String to, String new_file_name) {
		File file = new File(from_file);
		File dir = new File(to);
		boolean success = file.renameTo(new File(dir, new_file_name));
	}

	public static void processFiles() throws IOException, SQLException {
		DB db = new DB();


		for(String s : Main.listFilesInDir(image_directory)) {
			String file_name = s.split("\\.")[0];
			String file_extension = s.split("\\.")[1];


			try {


				Statement select = db.conn.createStatement();
				ResultSet result = select.executeQuery("SELECT * FROM employee WHERE objectid="+file_name);

				while(result.next()) {
					try {
						String pnr = result.getString("personnelnr");


						writeToCSV(pnr, image_directory_proccesed+pnr+"."+file_extension);

						System.out.println(image_directory+file_name+"."+file_extension);
						System.out.println(image_directory_proccesed+ pnr+".jpg");
						
						moveFile(image_directory+file_name+"."+file_extension, image_directory_proccesed, pnr+".jpg");	
					
					
					} catch (Exception e) {
						System.out.println(e);
					}
				}		
			} catch(Exception e) {
				System.out.println(e);
			}
		}	

		db.conn.close();

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
	}

	public static void main(String[] args) throws SQLException, IOException {


		if(!doesExist(csv_file)) {
			Main.processFiles()	;
			System.out.println("Processing files");
		}
		else {
			System.out.println("Nedap not done");
		}
	}
}
