package textanalyzer;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.*;
import java.net.MalformedURLException;


public class TextAnalyzer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		runTextAnalyzer();
		

	}
	
	public static void runTextAnalyzer() throws FileNotFoundException, IOException {
		printToFile(createFile(), sortHashMap(countWords(extractPoem(getURL()))));
	}
	
	
	public static Scanner getURL() throws IOException {
		
		System.out.println("Enter URL: ");
		String URLString = new Scanner(System.in).next();
		java.net.URL url = null;
		try {
			url = new java.net.URL(URLString);
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		Scanner input = new Scanner(url.openStream());
		
		return input;
	}

	public static File createFile() {
		System.out.println("Enter name of file you wish to create: ");
		String fileName = new Scanner(System.in).next();
		File file = new File(fileName);
		
		if(file.exists()) {
			System.out.println("File already exists");
			System.exit(1);
		}
		
		return file;
	}
	
	public static ArrayList<String> extractPoem(Scanner input) {
		
		Boolean isInChapter = false;
		ArrayList<String> nextLineArray = new ArrayList<>();
		ArrayList<String> poemArray = new ArrayList<>();
		
		while(input.hasNext()) {
			nextLineArray.add(input.nextLine());
		}
		
		for(int i = 0; i < nextLineArray.size(); i++) {
			if(nextLineArray.get(i).contains("<h1>")) {
				isInChapter = true;
			}
			if(nextLineArray.get(i).contains("</div>")) {
				isInChapter = false;
			}
			if(isInChapter) {
				
			poemArray.add(nextLineArray.get(i).replaceAll("(<[^>]*>)|([;?!@#$%^&*:.,\"“”’])", " "));
				
			}
		}
		
		return poemArray;
	}
	
	public static HashMap<String, Integer> countWords(ArrayList<String> poemArray) {
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();

		for(int j = 0; j < poemArray.size(); j++) {
				String[] words = poemArray.get(j).toLowerCase().split(" ");
				
				for(String word: words) {
				
				if(wordCount.containsKey(word)) {
					wordCount.put(word, wordCount.get(word)+1);
				} else {
					wordCount.put(word, 1);
				}	
			
			}	
		}
		return wordCount;
	}
	
	public static Map<String, Integer> sortHashMap(HashMap<String, Integer> wordCount) {
		Map<String, Integer> sorted = wordCount
		        .entrySet()
		        .stream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		
		return sorted;
	}
	


	public static void printToFile(File file, Map<String, Integer> wordCount) throws FileNotFoundException {
		PrintWriter output = new PrintWriter(file);
		
		for(String key: wordCount.keySet()) {
			output.print(key + " " + wordCount.get(key) + "\n");
		}
		
		output.close();
	}
	
}
