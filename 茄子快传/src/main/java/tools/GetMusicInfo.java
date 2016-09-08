package tools;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bean.Music1;
/*
�õ���������
 */
public class GetMusicInfo {
	private static List<Music1> musics=new ArrayList<Music1>();
	private Context context;
	public GetMusicInfo(Context context){
		this.context = context;
		getMusic();
	}
	
	public void getMusic(){
		if(isSdCardExist()){
			Cursor cur=context.getContentResolver().query
			(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
			readMusic(cur);
		}
		else{
			Toast.makeText(context, "sd��������", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * �ж�sd���Ƿ����
	 */
	private boolean isSdCardExist(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * ��ȡ������Ϣ
	 */
	private void readMusic(Cursor cur){
		musics.clear();
		while(cur.moveToNext()){
			Music1 music=new Music1();
			music.id=cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
			music.title=cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
			music.album=cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
			music.artist=cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
			music.duration=cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
			music.size=cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
			music.duration=cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
			music.path=cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			musics.add(music);
		}
	}
	/**
	 * ����������Ϣ
	 */
	
	public static List<Music1> getAllMusic(){
		return musics;
	}
}
