package com.easemob.chatuidemo.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class KeywordSearchingAdapter extends BaseAdapter{
	private Context mContext;
	private List<EMMessage> msgsList;
	private int type = 1;
	private String keyword;
	private long count;
	public List<Long> counts = new ArrayList<Long>();
	private List<Map.Entry<Pair<String, Long>, EMMessage> > entriesList;
	private EMMessage message;
	
	public KeywordSearchingAdapter(Context context, List<Map.Entry<Pair<String, Long>, EMMessage> > entriesList,String keyword){
		this.mContext = context;
		this.keyword = keyword;
		this.entriesList = entriesList;
		
	}
	public KeywordSearchingAdapter(Context context, List<EMMessage> msgsList,String keyword,int type){
		this.mContext = context;
		this.msgsList = msgsList;
		this.type = type;
		this.keyword = keyword;
	}


	@Override
	public int getCount() {
		if (type == 1) {
			return entriesList.size();
		}
		return msgsList.size();
	}

	@Override
	public Object getItem(int position) {
		if(type == 1){
			return entriesList.get(position);
		}
		return msgsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.content_item, null);
			holder.textNick = (TextView) convertView
					.findViewById(R.id.tv_nick);
			holder.textTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.textContent = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (type == 1) {
			 message = entriesList.get(position).getValue();
		}else {
			 message = msgsList.get(position);
		}
		String messagContent = ((TextMessageBody)(message.getBody())).getMessage();
		int changeTextColor;
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
		SpannableStringBuilder builder = new SpannableStringBuilder(messagContent);
		changeTextColor = messagContent.indexOf(keyword);
		if(changeTextColor != -1){
			builder.setSpan(greenSpan, changeTextColor, changeTextColor+keyword.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if(type == 1){
			holder.textTime.setVisibility(View.GONE);
			count = entriesList.get(position).getKey().second;
			
			if(message.getChatType() == EMMessage.ChatType.Chat){
					holder.textNick.setText(entriesList.get(position).getKey().first);
				if(count == 1){
					holder.textContent.setText(builder);
				}else {
					holder.textContent.setText(count + "条相关的聊天记录");
				}
				
			}else {
				if (count == 1) {
					holder.textContent.setText(builder);
				}else {
					holder.textContent.setText(count + "条相关的聊天记录");
					Log.i("info", "message body:"+message.getBody());
				}
				holder.textNick.setText(entriesList.get(position).getKey().first);
			}
		}else {
			if(message.getChatType() == EMMessage.ChatType.Chat){
				holder.textNick.setText(message.getFrom());
			}else {
				holder.textNick.setText(message.getTo());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			Date date = new Date(message.getMsgTime());
			holder.textTime.setText(String.valueOf(sdf.format(date)));
			holder.textContent.setText(builder);
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView textNick;
		TextView textTime;
		TextView textContent;
		ImageView avatar;
	}


}
