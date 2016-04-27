package cn.edu.uestc.service;

import java.util.ArrayList;
import java.util.List;

public class DataService {
	public static List<String> getData(){
		List<String> data = new ArrayList<String>();
		for(int i=0; i<20; i++){
			data.add("ListView数据的分批加载"+ i);
		}
		return data;
	}
}
