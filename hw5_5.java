import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hw5_5 {

	static Map<Integer, String> map;	
	static List<Integer> list;
	public static void main(String[] args) throws IOException {
				map = new HashMap<>();
				list = new ArrayList<>();
				PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

				BufferedReader br = new BufferedReader(new FileReader("web-Google.txt"));
				String line = br.readLine();
				for (String string : args) {
					
				}
				while(line != null){							
					String[] temp = line.split("\\s+");
					if(temp.length == 2){
						Integer i1,i2;
						try{
							i1 = Integer.parseInt(temp[0]);
							i2 = Integer.parseInt(temp[1]);
							if(!list.contains(i2))
								list.add(i2);
							mapkey(i2,i1);
						}
						catch(NumberFormatException e){
							System.out.println("error:"+line);
						}
					}
					line = br.readLine();
				}
				
				Collections.sort(list);
				for(int i = 0;i<list.size();i++){
					writer.println(list.get(i)+":"+map.get(list.get(i)).split(",").length);
				}
				br.close();
				writer.close();
	}

	public static void mapkey(int key,int value){
		if(map.containsKey(key)){
			String[] s = map.get(key).split(",");
			boolean iscontain = false;
			for(int i = 0;i<s.length;i++){
				if(s[i].equals(value)){
					iscontain = true;
				}
			}
			if(!iscontain)
				map.put(key, map.get(key)+","+value);
		}
		else{
			map.put(key, value+"");
		}
	}
}
