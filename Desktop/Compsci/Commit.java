import java.util.HashMap;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Commit implements java.io.Serializable
{
	
	private int parentID;
	private int commitNum;
	private String commitMsg;
	private String date;
	private HashMap<String, String> trackedFiles;//tracked files are the files that we are still tracking remove and add will modify this list. 
	//the tracked files working directory id and the pointer to which commit folder is in 
	public Commit()
	{
		parentID=-1;
		commitNum=0;
		commitMsg="initial commit";
		trackedFiles= new HashMap<String, String>();

		Date derp= new Date();
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		date= formatter.format(derp);
	}	

	public Commit(int rentID, int comNum, String msg)
	{	
		commitMsg=msg;
		trackedFiles=new HashMap<String, String>();
		parentID=rentID;
		commitNum=comNum;
		Date derp= new Date();
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		date= formatter.format(derp);
	}
	public String getCommitMsg()
	{
		return commitMsg;
	}
	public HashMap<String, String> getTracked()
	{
		return trackedFiles;
	}
	public void setTracked(Commit d)
	{

		for(String s: d.getTracked().keySet())
		{
			this.getTracked().put(s, d.getTracked().get(s));
		}
	}
	public void setParentID(int i)
	{
		parentID=i;
	}
	public int getParentID()
	{
		return parentID;
	}
	public String getDate()
	{
		return date;
	}
	public boolean isTracked(String s)
	{
		return trackedFiles.containsKey(s);
	}


	public void add(String s, String s1)
	{
		trackedFiles.put(s, s1);
	}
	public void remove(String s)
	{
		trackedFiles.remove(s);
	}
	public int getCommitNum()
	{
		return commitNum;
	}
	
	public void setCommitMsg(String s)
	{
		commitMsg= s;
	}
	public String getCommitName()
	{
		return "commit"+commitNum;
	}

	public static void main(String[] args)
	{

	}
}