package com.qaz216.codes.etc;

import java.io.*;
import java.util.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;


public class FileUtil {

	private static Logger log = Logger.getLogger(FileUtil.class);	

	public static void generateFile(String dir, String name, String content) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(dir+"/"+name));
			out.write(content);
			out.flush();
			out.close();
		}
		catch(IOException e) {
			log.warn("could not create file "+name+" "+e.getMessage());
		}
	}

	public static Set<String> getFileSet(String name, boolean toLowerCase) {
		Set<String> set = new HashSet<String>();
		try {
			FileInputStream fis = new FileInputStream(name);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				if(toLowerCase) {
					line = line.toLowerCase();
				}
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				set.add(line);
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.warn("could not open file: "+name+" "+e.getMessage());
		}	
		return set;
	}
	
	public static Set<String> getFileSet(String name) {
		Set<String> set = new HashSet<String>();
		try {
			FileInputStream fis = new FileInputStream(name);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				line = line.toLowerCase();
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				set.add(line);
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.warn("could not open file: "+name+" "+e.getMessage());
		}	
		return set;
	}
	
	public static List<String> getFileList(String name) {
		List<String> list = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(name);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				line = line.toLowerCase();
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				list.add(line);
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.warn("could not open file: "+name+" "+e.getMessage());
		}	
		return list;
	}
	
	public static Map<String, String> getFileMap(String name, String sep) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			FileInputStream fis = new FileInputStream(name);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				line = line.toLowerCase();
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				String[] tokens = line.split(sep);
				if(tokens.length == 1) {
					map.put(tokens[0], null);
				}
				else if(tokens.length == 2) {
					map.put(tokens[0], tokens[1]);
				}
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.warn("could not open file: "+name+" "+e.getMessage());
		}	
		return map;
	}
	
	public static Map<String, String> getFileMap(String name, String sep, boolean toLowerCase) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			FileInputStream fis = new FileInputStream(name);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				
				if(toLowerCase) {
					line = line.toLowerCase();
				}
				
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				String[] tokens = line.split(sep);
				if(tokens.length == 1) {
					map.put(tokens[0], null);
				}
				else if(tokens.length == 2) {
					map.put(tokens[0], tokens[1]);
				}
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.warn("could not open file: "+name+" "+e.getMessage());
		}	
		return map;
	}
	
	public static void deleteDirectoryFiles(String dirName) {
		File dir = new File(dirName);
		if(!dir.exists() || !dir.isDirectory()) {
			return;
		}
		
		for(File file: dir.listFiles()) {
			if(!file.isDirectory()) {
				file.delete();
			}
		}
	}
	
	public static void deleteDirectoryFilesRecurse(String dirName) {
		File dir = new File(dirName);
		if(!dir.exists() || !dir.isDirectory()) {
			return;
		}
		
		for(File file: dir.listFiles()) {
			try {
				if(!file.isDirectory()) {
					file.delete();
				}
				else {
					FileUtils.deleteDirectory(file);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	public static void deleteDirectoryRecurse(File dir) {
		for(File file: dir.listFiles()) {
			if(!file.isDirectory()) {
				file.delete();
			}
			else {
				log.debug("\tgot here 2 ...: "+file);
				if(file.list().length == 0){
					log.debug("\t\tgot here 3 ...: "+file);
					file.delete();
				}
				else {
					deleteDirectoryRecurse(file);
				}
			}
		}
	}
	*/
	
	public static void createDirectoryIfNotExists(String dirName) {
		if(fileExists(dirName)) return;
		
		File dir = new File(dirName);
		if(!dir.mkdir()) {
			log.error("could not create directory: "+dirName);
		}
			
	}

	public static String getFileFromPath(String path, String match, String index) {
		int pos = path.lastIndexOf(index);
        return path.substring(pos+1 , path.length()-1);
	}
	
	public static String getFileFromPath(String path) {
		int pos = path.lastIndexOf("/");
        return path.substring(pos+1 , path.length()-1);
	}

	public static String getFileFromPath(String path, String match) {
		path = path.trim();
		int pos = path.lastIndexOf(match);
        return path.substring(pos+1 , path.length());
	}

	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	
	public static boolean createFile(String fileName) {
		try {
			File file = new File(fileName);
			if(file.createNewFile()) {
				return true;
			}
		}
		catch(IOException e) {
			log.warn("could not create file "+fileName+" "+e.getMessage());
		}
		
		return false;
	}
	
	public static boolean createFileIfNotExists(String fileName) {
		try {
			File file = new File(fileName);
			if(!file.exists()) {
				if(file.createNewFile()) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		catch(IOException e) {
			log.warn("could not create file "+fileName+" "+e.getMessage());
		}
		
		return false;
	}
	
	public static long getFileSizeKb(File file) {
		long size = file.length();
		return size / 1024;
	}
	
	public static long getFileSizeMb(File file) {
		long size = file.length();
		return size / (1024*1024);
	}
	
	public static void writeValue(String fileName, String value) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write(value);
			out.flush();
			out.close();
		}
		catch(IOException e) {
			log.warn("could not create file "+fileName+" "+e.getMessage());
		}
	}
	
	public static String getValue(String fileName) {
		File file = new File(fileName);
		if(!file.exists()) {
			return "";
		}
		
		try {
			FileInputStream fis = new FileInputStream(fileName);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				return line;
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.warn("could not open file: "+fileName+" "+e.getMessage());
		}	
		
		return "";
	}
	
	public static void appendToFile(String fileName, String content) {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		    out.println(content);
		    out.close();
		} catch (IOException e) {
		    log.error("error writing to file: "+fileName);
		}
	}

	public static void appendToFileNoNewLine(String fileName, String content) {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		    out.print(content);
		    out.close();
		} catch (IOException e) {
		    log.error("error writing to file: "+fileName);
		}
	}
	
	public static void appendToFile(String fileName, ArrayList<String> list) {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		    Iterator<String> iter = list.iterator();
		    while(iter.hasNext()) {
		    	String s = iter.next();
		    	out.println(s);
		    }
		    out.close();
		} catch (IOException e) {
		    log.error("error writing to file: "+fileName);
		}
	}
	
	public static boolean clearContent(String fileName) {
		if(!fileExists(fileName)) return false;

		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.print("");
			writer.close();	
			return true;
		}
		catch(FileNotFoundException e) {
			log.error("File not found: "+fileName);
			return false;
		}
	}
	
	public static List<File> getFileList(String dirName, String pattern) {
		ArrayList<File> fileList = new ArrayList<File>();
		File directory = new File(dirName);
		if(!directory.exists()) {
			log.error("directory does not exist: "+dirName);
			return null;
		}
		
		//List<File> files = FileUtil.getFileList(directory, pattern, fileList);
		List<File> files = FileUtil.getFileList(directory, pattern, fileList);
		return fileList;
	}

	public static List<File> getFileList(String dirName, List<String> patternList) {
		List<File> returnList = new ArrayList<File>();
		for(String pattern : patternList) {
			List<File> fileList = getFileList(dirName, pattern);
			if(fileList != null && !fileList.isEmpty()) {
				returnList.addAll(fileList);
			}
		}
		return returnList;

	}
	
	public static List<String> getFileList(String name, boolean toLowerCast) {
		if(toLowerCast) return getFileList(name);

		List<String> list = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(name);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			while((line=br.readLine()) != null) {
				line = line.trim();
				if(line.equals("")) continue;
				if(line.matches("^#.*")) continue;
				list.add(line);
			}
			
			// close streams
			br.close();
			dis.close();
			fis.close();
		}
		catch(IOException e) {
			log.error("could not open file: "+name+" "+e.getMessage());
		}	
		return list;
	}
	
	public static File getMatchingFile(String dirName, String pattern) {
		List<File> fileList = getFileList(dirName, pattern);
		if(fileList == null) return null;
		if(fileList.size() == 0) return null;
		return fileList.get(0);
	}
	
	public static List<File> getFileListPattern(String dirName, String pattern) {
		FileFilter fileFilter = new WildcardFileFilter(pattern);
		
		ArrayList<File> fileList = new ArrayList<File>();
		File directory = new File(dirName);
		if(!directory.exists()) {
			log.error("directory does not exist: "+dirName);
			return null;
		}
		
		List<File> files = FileUtil.getFileListPattern(directory, fileFilter, fileList);
		return fileList;
	}
	
	
	public static List<File> getAllFilesList(String dirName) {
		ArrayList<File> fileList = new ArrayList<File>();
		File directory = new File(dirName);
		if(!directory.exists()) {
			log.error("directory does not exist: "+dirName);
			return null;
		}
		
		List<File> files = FileUtil.getFileList(directory, fileList);
		return fileList;
	}
	
	public static List<File> getFileList(File directory, ArrayList<File> fileList) {
		File[] files = directory.listFiles();
        if (files == null) {
        	log.debug("file list is null");
        	return null;
        }
        
        for (File f : files) {
        	if(f.isDirectory()) {
        		FileUtil.getFileList(f, fileList);
        	}
        	else {
        		String name = f.getName();
        		fileList.add(f);
        		//log.debug("name = "+name);
        	}
        }
		
        //log.debug("fileList size = "+fileList.size());
		return fileList;
	}
	
	public static List<File> getFileList(File directory, String pattern, ArrayList<File> fileList) {
		File[] files = directory.listFiles();
        if (files == null) {
        	log.debug("file list is null");
        	return null;
        }
        
        for (File f : files) {
        	if(f.isDirectory()) {
        		FileUtil.getFileList(f, pattern, fileList);
        	}
        	else {
        		String name = f.getName();
        		if(name.matches(pattern)) {
        			fileList.add(f);
        			//log.debug("name = "+name);
        		}
        	}
        }
		
        //log.debug("fileList size = "+fileList.size());
		return fileList;
	}
	
	public static List<File> getFileListPattern(File directory, FileFilter fileFilter, ArrayList<File> fileList) {
		//File[] directories = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		File[] directories = directory.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
		/*
		String[] directories = directory.list(new FilenameFilter() {
			  public boolean accept(File dir, String name) {
			    return new File(dir, name).isDirectory();
			  }
			});
		log.debug("got here ...");
		*/
		
		File[] files = null;
		//File[] files = directory.listFiles(fileFilter);
		
		if(files != null && files.length > 0) {
			fileList.addAll(Arrays.asList(files));
		}
		
		if(directories != null) {
			for(File dir : directories) {
				log.debug(dir.getAbsolutePath());
				getFileListPattern(dir, fileFilter, fileList);
			}
		}
		
		/*
		File[] files = directory.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".wav");
		    }
		});
		
        if (files == null) {
        	log.debug("file list is null");
        	return null;
        }
        
        for (File f : files) {
        	if(f.isDirectory()) {
        		FileUtil.getFileListPattern(f, pattern, fileList);
        	}
        	else {
        		String name = f.getName();
        		log.debug("name = "+name);
        		if(name.matches(pattern)) {
        			fileList.add(f);
        		}
        	}
        }
		*/
		
        //log.debug("fileList size = "+fileList.size());
		return fileList;
	}

	public static void copyFileToDirectory(String file, String dir) {
	    FileChannel source = null;
	    FileChannel destination = null;
	    
	    File sourceFile = new File(file);
	    String fileName = sourceFile.getName();
	    File destFile = new File(dir+"\\"+fileName);
	    
	    try {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    catch(IOException e) {
	    	log.debug(e.getMessage());
	    }
	    finally {
	    	try {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
	    	}
		    catch(IOException e) {
		    	log.debug(e.getMessage());
		    }
	    }
	}

	public static void copyFile(String sourceFileName, String destFileName) {
	    FileChannel source = null;
	    FileChannel destination = null;
	    
	    File sourceFile = new File(sourceFileName);
	    File destFile = new File(destFileName);
	    
	    try {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    catch(IOException e) {
	    	log.debug(e.getMessage());
	    }
	    finally {
	    	try {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
	    	}
		    catch(IOException e) {
		    	log.debug(e.getMessage());
		    }
	    }
	}

	public static void copyFileNew(File from, File to) {
	    try {
			Files.copy( from.toPath(), to.toPath() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void copyFile(File sourceFile, File destFile) {
	    FileChannel source = null;
	    FileChannel destination = null;
	    
	    try {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    catch(IOException e) {
	    	log.debug(e.getMessage());
	    }
	    finally {
	    	try {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
	    	}
		    catch(IOException e) {
		    	log.debug(e.getMessage());
		    }
	    }
	}
	
	public static void moveFile(String fileName, String targetDir) {
		File file = new File(fileName);
		if(file.renameTo(new File(targetDir + "\\" + file.getName()))){
			log.debug("Moved file: "+fileName+" to directory: "+targetDir);
		}
		else {
			log.error("could not rename file: "+fileName+" to directory: "+targetDir);
		}
	}
	
}
