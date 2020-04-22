import java.awt.Color;


public class Grid {

	space[] space = new space[90];
	
	public void setupSpaces(){
		for (int i = 0; i < 90; i++){
			space[i] = new space();
			space[i].setIndex(i);
			space[i].setNextRed(15);
			space[i].setNextGreen(15);
			space[i].setNextBlue(15);
		}
	
		double g = 6.25;
		int x = 32;
		space[0].setup(Color.WHITE, Color.GRAY, x+50, 50, 2, -1, true);
		space[1].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*1, g*10, 2, -1, true);
		space[2].setup(Color.WHITE, Color.GRAY, x+g*2, g*10, 2, -1, false);
		space[3].setup(Color.WHITE, Color.GRAY, x+g*3, g*10, 2, -1, false);
		space[4].setup(Color.WHITE, Color.GRAY, x+g*4, g*10, 2, -1, false);
		space[5].setup(Color.WHITE, Color.GRAY, x+g*5, g*10, 2, -1, false);
		space[6].setup(Color.WHITE, Color.WHITE, x+g*6, g*10, 2, -1, true);
		space[7].setup(Color.WHITE, Color.GRAY, x+g*6, g*11, 2, -1, false);
		space[8].setup(Color.WHITE, Color.GRAY, x+g*6, g*12, 2, -1, false);
		space[9].setup(Color.WHITE, Color.GRAY, x+g*6, g*13, 2, -1, false);
		space[10].setup(Color.WHITE, Color.GRAY, x+g*6, g*14, 2, -1, false);
		space[11].setup(Color.WHITE, Color.GRAY, x+g*6, g*15, 2, -1, false);
		space[12].setup(Color.WHITE, Color.GRAY, x+g*7, g*15, 2, -1, false);
		space[13].setup(Color.WHITE, Color.GRAY, x+g*8, g*15, 2, -1, false);
		space[14].setup(Color.WHITE, Color.GRAY, x+g*9, g*15, 2, -1, false);
		space[15].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*10, g*15, 2, -1, true);
		space[16].setup(Color.WHITE, Color.GRAY, x+g*10, g*14, 2, -1, false);
		space[17].setup(Color.WHITE, Color.GRAY, x+g*10, g*13, 2, -1, false);
		space[18].setup(Color.WHITE, Color.GRAY, x+g*10, g*12, 2, -1, false);
		space[19].setup(Color.WHITE, Color.GRAY, x+g*10, g*11, 2, -1, false);
		space[20].setup(Color.WHITE, Color.GRAY, x+g*10, g*10, 2, -1, true);
		space[21].setup(Color.WHITE, Color.GRAY, x+g*11, g*10, 2, -1, false);
		space[22].setup(Color.WHITE, Color.GRAY, x+g*12, g*10, 2, -1, false);
		space[23].setup(Color.WHITE, Color.GRAY, x+g*13, g*10, 2, -1, false);
		space[24].setup(Color.WHITE, Color.GRAY, x+g*14, g*10, 2, -1, false);
		space[25].setup(Color.WHITE, Color.GRAY, x+g*15, g*10, 2, -1, false);
		space[26].setup(Color.WHITE, Color.GRAY, x+g*15, g*9, 2, -1, false);
		space[27].setup(Color.WHITE, Color.GRAY, x+g*15, g*8, 2, -1, false);
		space[28].setup(Color.WHITE, Color.GRAY, x+g*15, g*7, 2, -1, false);
		space[29].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*15, g*6, 2, -1, true);
		space[30].setup(Color.WHITE, Color.GRAY, x+g*14, g*6, 2, -1, false);
		space[31].setup(Color.WHITE, Color.GRAY, x+g*13, g*6, 2, -1, false);
		space[32].setup(Color.WHITE, Color.GRAY, x+g*12, g*6, 2, -1, false);
		space[33].setup(Color.WHITE, Color.GRAY, x+g*11, g*6, 2, -1, false);
		space[34].setup(Color.WHITE, Color.GRAY, x+g*10, g*6, 2, -1, true);
		space[35].setup(Color.WHITE, Color.GRAY, x+g*10, g*5, 2, -1, false);
		space[36].setup(Color.WHITE, Color.GRAY, x+g*10, g*4, 2, -1, false);
		space[37].setup(Color.WHITE, Color.GRAY, x+g*10, g*3, 2, -1, false);
		space[38].setup(Color.WHITE, Color.GRAY, x+g*10, g*2, 2, -1, false);
		space[39].setup(Color.WHITE, Color.GRAY, x+g*10, g*1, 2, -1, false);
		space[40].setup(Color.WHITE, Color.GRAY, x+g*9, g*1, 2, -1, false);
		space[41].setup(Color.WHITE, Color.GRAY, x+g*8, g*1, 2, -1, false);
		space[42].setup(Color.WHITE, Color.GRAY, x+g*7, g*1, 2, -1, false);
		space[43].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*6, g*1, 2, -1, true);
		space[44].setup(Color.WHITE, Color.GRAY, x+g*6, g*2, 2, -1, false);
		space[45].setup(Color.WHITE, Color.GRAY, x+g*6, g*3, 2, -1, false);
		space[46].setup(Color.WHITE, Color.GRAY, x+g*6, g*4, 2, -1, false);
		space[47].setup(Color.WHITE, Color.GRAY, x+g*6, g*5, 2, -1, false);
		space[48].setup(Color.WHITE, Color.GRAY, x+g*6, g*6, 2, -1, true);
		space[49].setup(Color.WHITE, Color.GRAY, x+g*5, g*6, 2, -1, false);
		space[50].setup(Color.WHITE, Color.GRAY, x+g*4, g*6, 2, -1, false);
		space[51].setup(Color.WHITE, Color.GRAY, x+g*3, g*6, 2, -1, false);
		space[52].setup(Color.WHITE, Color.GRAY, x+g*2, g*6, 2, -1, false);
		space[53].setup(Color.WHITE, Color.GRAY, x+g*1, g*6, 2, -1, false);
		space[54].setup(Color.WHITE, Color.GRAY, x+g*1, g*7, 2, -1, false);
		space[55].setup(Color.WHITE, Color.GRAY, x+g*1, g*8, 2, -1, false);
		space[56].setup(Color.WHITE, Color.GRAY, x+g*1, g*9, 2, -1, false);

		space[57].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*2, g*8, 2, -1, true);
		space[58].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*3, g*8, 2, -1, true);
		space[59].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*4, g*8, 2, -1, true);
		space[60].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*5, g*8, 2, -1, true);
		space[61].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*8, g*14, 2, -1, true);
		space[62].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*8, g*13, 2, -1, true);
		space[63].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*8, g*12, 2, -1, true);
		space[64].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*8, g*11, 2, -1, true);
		space[65].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*14, g*8, 2, -1, true);
		space[66].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*13, g*8, 2, -1, true);
		space[67].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*12, g*8, 2, -1, true);
		space[68].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*11, g*8, 2, -1, true);
		space[69].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*8, g*2, 2, -1, true);
		space[70].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*8, g*3, 2, -1, true);
		space[71].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*8, g*4, 2, -1, true);
		space[72].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*8, g*5, 2, -1, true);
		
		space[73].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*4, g*4, 2, 0, true);
		space[74].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*3, g*3, 2, 0, true);
		space[75].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*2, g*2, 2, 0, true);
		space[76].setup(Color.WHITE, aggravation_Main.player[0].getColor(), x+g*1, g*1, 2, 0, true);
		space[77].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*4, g*12, 2, 1, true);
		space[78].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*3, g*13, 2, 1, true);
		space[79].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*2, g*14, 2, 1, true);
		space[80].setup(Color.WHITE, aggravation_Main.player[1].getColor(), x+g*1, g*15, 2, 1, true);
		space[81].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*12, g*12, 2, 2, true);
		space[82].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*13, g*13, 2, 2, true);
		space[83].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*14, g*14, 2, 2, true);
		space[84].setup(Color.WHITE, aggravation_Main.player[2].getColor(), x+g*15, g*15, 2, 2, true);
		space[85].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*12, g*4, 2, 3, true);
		space[86].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*13, g*3, 2, 3, true);
		space[87].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*14, g*2, 2, 3, true);
		space[88].setup(Color.WHITE, aggravation_Main.player[3].getColor(), x+g*15, g*1, 2, 3, true);
		
		space[0].setNextRed(0);
		space[0].setNextGreen(255);
		space[0].setNextBlue(255);
		
		space[6].setNextRed(0);
		space[6].setNextGreen(255);
		space[6].setNextBlue(255);
		space[20].setNextRed(0);
		space[20].setNextGreen(255);
		space[20].setNextBlue(255);
		space[34].setNextRed(0);
		space[34].setNextGreen(255);
		space[34].setNextBlue(255);
		space[48].setNextRed(0);
		space[48].setNextGreen(255);
		space[48].setNextBlue(255);
		
		space[57].setNextRed(aggravation_Main.player[0].getR());
		space[57].setNextGreen(aggravation_Main.player[0].getG());
		space[57].setNextBlue(aggravation_Main.player[0].getB());
		space[58].setNextRed(aggravation_Main.player[0].getR());
		space[58].setNextGreen(aggravation_Main.player[0].getG());
		space[58].setNextBlue(aggravation_Main.player[0].getB());
		space[59].setNextRed(aggravation_Main.player[0].getR());
		space[59].setNextGreen(aggravation_Main.player[0].getG());
		space[59].setNextBlue(aggravation_Main.player[0].getB());
		space[60].setNextRed(aggravation_Main.player[0].getR());
		space[60].setNextGreen(aggravation_Main.player[0].getG());
		space[60].setNextBlue(aggravation_Main.player[0].getB());
		space[61].setNextRed(aggravation_Main.player[1].getR());
		space[61].setNextGreen(aggravation_Main.player[1].getG());
		space[61].setNextBlue(aggravation_Main.player[1].getB());
		space[62].setNextRed(aggravation_Main.player[1].getR());
		space[62].setNextGreen(aggravation_Main.player[1].getG());
		space[62].setNextBlue(aggravation_Main.player[1].getB());
		space[63].setNextRed(aggravation_Main.player[1].getR());
		space[63].setNextGreen(aggravation_Main.player[1].getG());
		space[63].setNextBlue(aggravation_Main.player[1].getB());
		space[64].setNextRed(aggravation_Main.player[1].getR());
		space[64].setNextGreen(aggravation_Main.player[1].getG());
		space[64].setNextBlue(aggravation_Main.player[1].getB());
		space[65].setNextRed(aggravation_Main.player[2].getR());
		space[65].setNextGreen(aggravation_Main.player[2].getG());
		space[65].setNextBlue(aggravation_Main.player[2].getB());
		space[66].setNextRed(aggravation_Main.player[2].getR());
		space[66].setNextGreen(aggravation_Main.player[2].getG());
		space[66].setNextBlue(aggravation_Main.player[2].getB());
		space[67].setNextRed(aggravation_Main.player[2].getR());
		space[67].setNextGreen(aggravation_Main.player[2].getG());
		space[67].setNextBlue(aggravation_Main.player[2].getB());
		space[68].setNextRed(aggravation_Main.player[2].getR());
		space[68].setNextGreen(aggravation_Main.player[2].getG());
		space[68].setNextBlue(aggravation_Main.player[2].getB());
		space[69].setNextRed(aggravation_Main.player[3].getR());
		space[69].setNextGreen(aggravation_Main.player[3].getG());
		space[69].setNextBlue(aggravation_Main.player[3].getB());
		space[70].setNextRed(aggravation_Main.player[3].getR());
		space[70].setNextGreen(aggravation_Main.player[3].getG());
		space[70].setNextBlue(aggravation_Main.player[3].getB());
		space[71].setNextRed(aggravation_Main.player[3].getR());
		space[71].setNextGreen(aggravation_Main.player[3].getG());
		space[71].setNextBlue(aggravation_Main.player[3].getB());
		space[72].setNextRed(aggravation_Main.player[3].getR());
		space[72].setNextGreen(aggravation_Main.player[3].getG());
		space[72].setNextBlue(aggravation_Main.player[3].getB());
	}
	
	public void draw() {
		double rot = 0;
		int x = 0;
		int y = 0;
		//StdDraw.picture(80, 52.5, "wood.png", 160, 105);
		
		//StdDraw.setPenRadius(.015);
		//StdDraw.setPenColor(aggravation_Main.player[aggravation_Main.playerTurn].getSpaceColor());
		//StdDraw.rectangle(79.7, 52.75, 76.3, 49);
		
		if (aggravation_Main.resume){
			
			for (int i = 0; i < 4; i++){
				StdDraw.setPenColor(aggravation_Main.player[i].getColor());
				
				if (i == 0){
					rot = 2.36;
					x = -3;
					y = -3;
				} 
				else if (i == 1){
					rot = 0.79;
					x = -3;
					y = +3;
				}
				else if (i == 2){
					rot = 2.36;
					x = +3;
					y = +3;
				}
				else if (i == 3){
					rot = 0.79;
					x = +3;
					y = -3;
				}
				
				StdDraw.drawBase(aggravation_Main.grid.space[aggravation_Main.player[i].path[61]].getX()+x, aggravation_Main.grid.space[aggravation_Main.player[i].path[61]].getY()+y, 30, aggravation_Main.player[i].getColor(), rot);
				
				if (aggravation_Main.playerTurn == i)
					aggravation_Main.player[i].draw();
				
				StdDraw.drawLogo(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), 20, aggravation_Main.player[i].getColor(), aggravation_Main.player[i].getRotation());

				StdDraw.setPenColor(Color.BLACK);
				
				StdDraw.filledRoundRectangle(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), 12, 13, 0.5f, 25, 25);
				
				StdDraw.setPenRadius(.02);
				StdDraw.setPenColor(Color.DARK_GRAY);
				StdDraw.roundRectangle(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), 12, 13, 25, 25);
				StdDraw.setPenRadius(.012);
				StdDraw.setPenColor(Color.GRAY);
				StdDraw.roundRectangle(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), 12, 13, 25, 25);
				StdDraw.setPenRadius(.006);
				StdDraw.setPenColor(Color.WHITE);
				StdDraw.roundRectangle(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), 12, 13, 25, 25);
				StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY()+10, aggravation_Main.player[i].getName());
				StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY()+7, "Aggravations: "+aggravation_Main.player[i].getAggravations());
				StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY()+4, "Aggravated: "+aggravation_Main.player[i].getAggravated());
				
				if (aggravation_Main.player[i].hasWon()){
					if (aggravation_Main.player[i].getWonPlace() == 1)
						StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), "1st");
					else if (aggravation_Main.player[i].getWonPlace() == 2)
						StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), "2nd");
					else if (aggravation_Main.player[i].getWonPlace() == 3)
						StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), "3rd");
					else if (aggravation_Main.player[i].getWonPlace() == 4)
						StdDraw.text(aggravation_Main.player[i].getX(), aggravation_Main.player[i].getY(), "4th");
				}
			}
		}
		
    	for (int i = 0; i <= 88; i++){
    		space[i].draw();
    		if ((i == 6) || (i == 20) || (i == 34) || (i == 48))
    			space[i].drawStar();
    		
    		//StdDraw.text(space[i].getX(), space[i].getY(), ""+i);
    	}
    	
    	aggravation_Main.dice.draw(aggravation_Main.playerTurn, aggravation_Main.dice.getTempVal());
    	aggravation_Main.endTurn.draw();
    }
}
