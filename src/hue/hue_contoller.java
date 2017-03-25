package hue;
import org.json.simple.JSONObject;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


public class hue_contoller {
	static String bridge_addr=null;
	static HttpConnection bridge_con;
	static String bridge_control_addr=null;
	static HttpConnection bridge_control_con;
	public static void main(String [] args) throws Exception{
	//	String username=get_username();
	//	bridge_control_addr=bridge_addr+'/'+username;
		
		bridge_control_addr="http://192.168.219.152/api/v7NniwM9vf0Cabgoi-ZWdbCoUk3ox6USuOl9FhWK/lights/2/state";
		bridge_control_con=new HttpConnection(bridge_control_addr);
		//param="{"+'"'+"on"+'"'+':'+"true"+", "+'"'+"sat"+'"'+':'+"254"+", "+'"'+"bri"+'"'+':'+"20"+","+'"'
		//				+"hue"+'"'+':'+"10000"+"}";
		//set_color(false,0,0,0); //turn off
		//set_color(true,0,200,0);//turn on
		//set_color(true,62535,200,200); // red
		//set_color(true,23500,200,200); // green
	}
	static void set_color(boolean on, int hue, int bri, int sat) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("on", on);
		obj.put("hue",hue);
		obj.put("bri",bri);
		obj.put("sat", sat);
		System.out.println(obj);
		bridge_control_con.sendPut(obj.toJSONString());
	}
	static String get_username() throws Exception{
		Scanner scanner = new Scanner(System.in);
		String ip;
		String username;
		ip=get_ip();
		System.out.println(ip);
		bridge_addr="http://"+ip+"/api";
		System.out.println(bridge_addr);
		bridge_con=new HttpConnection(bridge_addr);
		username=pushed();
		while(username==null){
			System.out.println("1. Press the button on the hue bridge");
			System.out.println("2. Press enter");
			String readString = scanner.nextLine();
			username=pushed();
		}
		System.out.println(username);
		return username;
	}
	static String get_ip(){
		String ip = null,s;
		JSONParser parser = new JSONParser();
		HttpConnection http=new HttpConnection("https://www.meethue.com/api/nupnp");
		try {
			s=http.sendGet();
	        ip=ip_parser(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
	static String pushed() throws Exception{
		String result;
		String param="{" + '"' + "devicetype"+'"'+':'+'"'+"my_hue_app#iphone peter"+'"'+'}';
		System.out.println(param);
		result=bridge_con.sendPost(param);
		System.out.println(result);
		if(result.contains("error")) return null;
		return username_parser(result);
	}
	static String ip_parser(String input) throws ParseException{
		String result;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(input);
        JSONArray array = (JSONArray)obj;
        JSONObject obj2 = (JSONObject)array.get(0);
        result=(String) obj2.get("internalipaddress");
        return result;
	}
	static String username_parser(String input) throws ParseException{
		String result;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(input);
        JSONArray array = (JSONArray)obj;
        JSONObject obj2 = (JSONObject)array.get(0);
        JSONObject obj3 = (JSONObject)obj2.get("success");
        result=(String)obj3.get("username");
        return result;
	}
	
}
