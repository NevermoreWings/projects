import java.util.HashMap;
import java.util.HashSet;
public class Instance implements java.io.Serializable
{
	private Integer commitCounter;//update at commit
	private HashSet<String> stagedFiles;//add and remove and also remember to clear at commit
	private HashSet<String> removedFiles;// ^^
	private String currentBranch; // modified by checkout
	private HashMap<String, HashSet<Integer>> mToC; //modify at commit, add the commit message and the id to this map
	private HashMap<String, Integer> branchHead; //maps string branch name to the commit id it's at


	public Instance()
	{
		commitCounter=1;//gives the id for the next commit
		stagedFiles=new HashSet<String>();// upon creation shouldnt have anything
		removedFiles= new HashSet<String>();//^^
		branchHead= new HashMap<String, Integer>();// should have master pointing at zero
		mToC=new HashMap<String, HashSet<Integer>>();// initial commit and an zero in a hashset
		HashSet<Integer> a= new HashSet<Integer>();
		a.add(0);
		mToC.put("initial commit", a);
		currentBranch="Master";

		branchHead.put(currentBranch, 0);


	}
	public HashMap<String, HashSet<Integer>> getCommitMap()
	{
		return mToC;
	}
	public void addToMToC(String s, Integer a)
	{
		if(mToC.containsKey(s))
		{
			mToC.get(s).add(a);
		}
		else
		{
			HashSet<Integer> n= new HashSet<Integer>();
			n.add(a);
			mToC.put(s, n);
		}
	}
	public String getCurrentBranch()
	{
		return currentBranch;
	}
	public void augmentCounter()
	{
		commitCounter+=1;
	}
	public static void main(String[] args)
	{

	}
	public void updateHead(String s, Integer c)
	{
		branchHead.put(s, c);
	}
	public HashSet<String> getStagedFiles()
	{
		return stagedFiles;
	}
	public void addToStaged(String s)
	{
		stagedFiles.add(s);
	}
	public void removeFromStaged(String s)
	{
		stagedFiles.remove(s);
	}
	public void clearStaged()
	{
		stagedFiles.clear();
	}
	public HashSet<String> getRemovedFiles()
	{
		return removedFiles;
	}
	public void removeFromRemove(String s)
	{
		removedFiles.remove(s);
	}
	public void addtoRemove(String s)
	{
		removedFiles.add(s);
	}
	public void clearRemove()
	{
		removedFiles.clear();
	}
	public HashMap<String, Integer> getBranches()
	{
		return branchHead;
	}
	public void deletePointerBranch(String s)
	{
		branchHead.remove(s);
	}
	public Integer getCounter()
	{
		return commitCounter; 
	}
	public void changeCurrentBranch(String s)
	{
		currentBranch=s;
	}
	public void addNewBranch(String s)
	{
		branchHead.put(s, branchHead.get(currentBranch));
	}

}

