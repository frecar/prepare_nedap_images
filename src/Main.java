import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import sun.security.provider.MD5;

public class Main {
	public static String image_directory = "";
	public static String csv_file = "";
	public static String log_file = "";
	public static String history_file = "";

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

	public static String getHashOfFile(String file_path) throws NoSuchAlgorithmException, IOException  {

		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(file_path);

		byte[] dataBytes = new byte[1024];

		int nread = 0; 
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		};
		byte[] mdbytes = md.digest();

		//convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();

	}

	public static void processFiles() throws IOException, SQLException, NoSuchAlgorithmException {

		for(String s : Main.listFilesInDir(image_directory)) {

			String file_name = s.split("\\.")[0];
			String file_extension = s.split("\\.")[1];

			String file_path = image_directory+file_name+"."+file_extension;
			String file_hash = getHashOfFile(file_path);
			
			List<String> hashes = getImageHashesForPersonellnr(file_name);
						
			boolean old_file = false;
			
			for(String hash : hashes) {				
				if(hash.equals(file_hash)) {
					old_file = true;
				}
			}
			
			if(! old_file) {
				writeToCSV(file_name, file_path);		
			}
			
		}
	}

	public static void writeToCSV(String personellnr, String image) throws IOException, NoSuchAlgorithmException {

		File f = new File(csv_file);

		if(!f.exists()) { 
			f.createNewFile();	
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(csv_file, true)); 
		String s = "23,1,,,,,,"+personellnr+",,,,,,,,,,,,,,,,"+image+",,,,,,,,,,,,,,,,,,,,,,,,,,\n";
		out.write(s); 
		out.close(); 	

		Main.writeToLog(s);
		
		Main.writeToHistory(personellnr, getHashOfFile(image));
	}

	public static List<String> getImageHashesForPersonellnr(String personellnr) throws IOException {
		
		List<String> list = new ArrayList<String>();
		
		File file = new File(history_file);
		 
		if(!file.exists()) { 
			file.createNewFile();	
		}
		
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;

		while((line = bufRdr.readLine()) != null)
		{
			
			String[] values = line.split(",");
		
			if(values[0].equals(personellnr))
				list.add(values[1]);
		
		}
		 
		bufRdr.close();
		
		return list;
		
	}
	
	public static void writeToHistory(String personellnr, String hash) throws IOException {

		File f = new File(history_file);

		if(!f.exists()) { 
			f.createNewFile();	
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(history_file, true)); 
		String s = personellnr+","+hash+"\n";
		out.write(s); 
		out.close(); 	

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

	public static void main(String[] args) throws SQLException, IOException, NoSuchAlgorithmException {

		String settingsLocation = "settings.xml";
		try {
			settingsLocation = args[0];
		}
		catch(Exception e) {}

		Settings settings = new Settings(settingsLocation);

		image_directory = settings.settings.get("image_directory");
		csv_file = settings.settings.get("csv_file");
		log_file = settings.settings.get("log_file");
		history_file = settings.settings.get("history_file");

		if(!doesExist(csv_file)) {
			Main.processFiles()	;
		} else {
			Main.writeToLog("CSV-file still there, Nedap not done");
		}
	}
}
