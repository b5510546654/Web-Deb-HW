import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hw5_7 {

	static Map<Integer, String> map;	
	static List<Integer> list;
	static Map<Integer,String> map2;
	public static void main(String[] args) throws IOException {
				map = new HashMap<>();
				map2 = new HashMap<>();
				list = new ArrayList<>();
				PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

				BufferedReader br = new BufferedReader(new FileReader("web-Google.txt"));
				String line = br.readLine();
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
					int number = list.get(i);
					String[] incomming_node = map.get(number).split(",");
					for(int j = 0;j<incomming_node.length;j++){
						int number2 = Integer.parseInt(incomming_node[j]);
						System.out.println(number2+" "+map.get(number2));
						if(map.get(number2) != null){
							String[] incomming_node2 = map.get(number2).split(",");
							for(int k = 0;k<incomming_node2.length;k++){
								mapkey2(number,Integer.parseInt(incomming_node2[k]));
							}						
						}
					}
				}
				
				for(int i = 0;i<list.size();i++){
					if(map2.containsKey(list.get(i)))
						writer.println(list.get(i)+":"+map2.get(list.get(i)).split(",").length);
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
	
	public static void mapkey2(int key,int value){
		if(map2.containsKey(key)){
			String[] s = map.get(key).split(",");
			boolean iscontain = false;
			for(int i = 0;i<s.length;i++){
				if(s[i].equals(value)){
					iscontain = true;
				}
			}
			if(!iscontain)
				map2.put(key, map.get(key)+","+value);
		}
		else{
			map2.put(key, value+"");
		}
	}
}
