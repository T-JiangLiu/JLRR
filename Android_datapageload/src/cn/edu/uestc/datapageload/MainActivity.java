package cn.edu.uestc.datapageload;

import java.util.ArrayList;
import java.util.List;

import cn.edu.uestc.service.DataService;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView listView;
	private List<String> data = new ArrayList<String>();//保存该listView中所有的数据
	private ArrayAdapter adapter;
	private View footer;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		footer = getLayoutInflater().inflate(R.layout.footer, null);
		data.addAll(DataService.getData());
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnScrollListener(new ScrollListener());
		adapter = new ArrayAdapter(this, R.layout.listview_item, R.id.textView, data);
		listView.addFooterView(footer);//添加页脚(放在ListView最后)
		listView.setAdapter(adapter);
		listView.removeFooterView(footer);
	}
	
	private int number = 20;//每次获取多少条数据
	private int maxpage = 5;//总共有多少页
	private boolean loadfinish = true;//标记此次数据是否已经加载完毕
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			data.addAll((List<String>)msg.obj);
			adapter.notifyDataSetChanged();//告诉ListView数据已经发生改变，要求ListView更新界面显示
			if(listView.getFooterViewsCount() > 0){
				listView.removeFooterView(footer);
			}
			loadfinish = true;
		}
	};
	
	private final class ScrollListener implements OnScrollListener{
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			int lastitemid = listView.getLastVisiblePosition();//获取当前屏幕最后Item的ID
			if((lastitemid+1) == totalItemCount){//达到数据的最后一条记录
				if(totalItemCount > 0 ){
					//当前页
					int currentpage = totalItemCount%number==0 ? totalItemCount/number : totalItemCount/number+1;
					int nextpage = currentpage + 1;//下一页
					if(nextpage<=maxpage && loadfinish){
						listView.addFooterView(footer);
						loadfinish = false;
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								List<String> result = DataService.getData();
								handler.sendMessage(handler.obtainMessage(1, result));
							}
						}).start();
					}
				}
			}
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}
}
