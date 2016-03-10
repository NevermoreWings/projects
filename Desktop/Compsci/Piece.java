public class Piece
{	
	private boolean isHot;
	private Board realm;
	private int xPos;
	private int yPos;
	private String race;
	private int captured;
	private boolean isBoss;

	public Piece(boolean isFire, Board b, int x, int y, String type)
	{
		isHot=isFire;
		realm= b;
		xPos=x;
		yPos=y;
		race=type;
		isBoss=false;
	}

	public boolean isFire()
	{
		if(isHot)
		{
			return true;
		}
		else
			return false;
	}
	public int side()
	{
		if(isHot)
		{
			return 0;
		}
		else
			return 1;
	}
	public boolean isKing()
	{
		return isBoss;
	}
	public boolean isBomb()
	{
		return race.equals("Bomb");
	}
	public boolean isShield()
	{	
		return race.equals("Shield");
	}
	public void move(int x, int y)
	{
		if(!isBomb())
		{
			if(Math.abs(x-xPos)==2 && Math.abs(y-yPos)==2)
			{


				Piece a= realm.remove((x+xPos)/2,(y+yPos)/2);

				captured+=1;

				
				
			}
			realm.place(realm.remove(xPos, yPos), x, y);
			xPos=x;
			yPos=y;
			if(isFire())
			{
				if(yPos==7)
					isBoss=true;
			}
			else
			{
				if(yPos==0)
					isBoss=true;
			}
		}
		else
		{
			if(Math.abs(x-xPos)==2 && Math.abs(y-yPos)==2)
			{


				realm.place(realm.remove(xPos, yPos), x, y);
				xPos=x;
				yPos=y;
				
				Piece b;
				
				for(int i=xPos-1; i<xPos+2; i++)
				{
					for(int j=yPos-1; j<yPos+2; j++)
					{
						if(realm.pieceAt(i,j)!=null && realm.pieceAt(i,j).isShield()!=true)
						{
							b=realm.remove(i,j);
							b=null;
						}

					}
				}
				
				

				
				
			}
			else
			{
				
				realm.place(realm.remove(xPos, yPos), x, y);
				xPos=x;
				yPos=y;
				if(isFire())
				{
					if(yPos==7)
						isBoss=true;
				}
				else
				{
					if(yPos==0)
						isBoss=true;
				}
			}
		}
		
	}
	public boolean hasCaptured()
	{

		if(captured>0)
			return true;
		else
			return false;
	}
	public void doneCapturing()
	{
		captured=0;
	}
}