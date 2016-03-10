public class monsterChessBoard
{
	private Monster[][] pieces;
	private boolean turn; // if true player 1 if false player 2
	private int currentPlayerResource; // should increment by 1 every turn 
	private Monster selected;
	private int selectedX;
	private int selectedY; 
	//not like chess more like war on an 8x8 board. 
	public static void main(String[] args)
	{
		monsterChessBoard b= new monsterChessBoard(false);
		b.turn=true;
		b.currentPlayerResource=1; 
		while(!b.gameOver())
		{
			StdDrawPlus.show(15);
			b.drawBoard();
			
		}
	}
	public monsterChessBoard(boolean shouldBeEmpty)
	{
		pieces=new Monster[8][8];
	}
	//shoudl draw board with terrain with different attributes
	// ie ocean should be red
	private void drawBoard()
	{

	}
	public Monster pieceAt(int x, int y)
	{

	}
	private boolean validMove(int xi, int yi, int xf, int yf)
	{
		
	}

}