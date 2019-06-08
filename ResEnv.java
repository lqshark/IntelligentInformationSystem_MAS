import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;
import java.applet.*;
import javax.swing.*;

public class ResEnv extends Environment {

    public int Restaurant_Space = 17;
    public static final int Food_n  = 16;
	public static final int wall_n  = 15;
	public int table_n = 1;
	public int s1 = 8;
	public int s2 = 1;
	public int s3 = 2;
	public int s4 = 3;
	public int t_i = 1;
    public int t_n = 0;
	public int kitchen_x = 8;
	public int kitchen_y = 8;
	public int kitchen_i = 1;
	public int food_w = 10;

    public static final Term    move_n = Literal.parseLiteral("move(table)");
	public static final Term    stop_n = Literal.parseLiteral("stop(table)");
    public static final Term    drop_n = Literal.parseLiteral("delivery(food)");
    public static final Term    give_n = Literal.parseLiteral("get_next(food)");
    public static final Term    give_f_A = Literal.parseLiteral("giveA(food)");
    public static final Literal f_A = Literal.parseLiteral("food(waiter)");
    public static final Literal f_K = Literal.parseLiteral("food(kitchen)");
	public static final Literal f_W = Literal.parseLiteral("food(wall)");
	public static final Literal f_T = Literal.parseLiteral("parreals(table)");
    static Logger waiter_name = Logger.getLogger(ResEnv.class.getName());
    private AK_Model model;
    private RestaurantView  view;

    public void init(String[] args) {
        model = new AK_Model();
        view  = new RestaurantView(model);
        model.setView(view);
        updatePercepts();
    }

    public boolean executeAction(String ag, Structure action) {
        waiter_name.info(ag+"'s work: "+ action);
        try {
            if (action.equals(move_n)) {
                model.nextgrid();
            } else if(action.equals(stop_n)){
				model.stop_open();
			} else if (action.getFunctor().equals("return_kitchen")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.return_kk(x,y);
            } else if (action.equals(drop_n)) {
                model.meal_delivery();
				
            } else if (action.equals(give_n)) {
                model.givePosition();	
				model.settable();
            } else if (action.equals(give_f_A)) {
                model.givefoodF();
				
            } 
			else if (action.equals(f_T)) {
                model.parrelw();
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatePercepts();

        try {
            Thread.sleep(200);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }

    /** creates the agents perception based on the AK_Model */
      void updatePercepts() {
        clearPercepts();

        Location waiterLoc  = model.getAgPos(0);
        Location kitchenLoc = model.getAgPos(1);
		Location wallLoc    = model.getAgPos(2);

        Literal pos1 = Literal.parseLiteral("pos(waiter,"  + waiterLoc.x +  "," + waiterLoc.y  + ")");
        Literal pos2 = Literal.parseLiteral("pos(kitchen," + kitchenLoc.x + "," + kitchenLoc.y + ")");
		//Literal pos3 = Literal.parseLiteral("pos(wall," + wallLoc.x + "," + wallLoc.y + ")");
        addPercept(pos1);
        addPercept(pos2);
		//addPercept(pos3);
        if (model.hasObject(Food_n, waiterLoc)) {
            addPercept(f_A);
        }
        if (model.hasObject(Food_n, kitchenLoc)) {
            addPercept(f_K);
        }
		//if (model.hasObject(Food_n, wallLoc)) {
        //    addPercept(f_W);
        //}
    }

    class AK_Model extends GridWorldModel {
        boolean waiter_with_food = false; 
        private AK_Model() {
            super(Restaurant_Space, Restaurant_Space, 4);           
			int center = (Restaurant_Space-1)/2;
            setAgPos(0, center, center);
            setAgPos(1, center, center);
            // randomly set the table
            add(Food_n, s1, s2);
			add(2, 5 , 5);
			add(2, 6 , 5);
			add(2, 7 , 5);
			add(2, 8 , 5);
			add(2, 9 , 5);
			add(2, 10 , 5);
			add(2, 11 , 5);
			
        }

        void nextgrid() throws Exception {
           Location waiter = getAgPos(0);
		   //System.out.println(move_n);
           if ((waiter.x<=8)&&(waiter.x>=4)&&(waiter.y==6))
		   {
			   waiter.x--;
			   //System.out.println(waiter.x);
		   }
		   else if((waiter.x<=12)&&(waiter.x>=9)&&(waiter.y==6)){
			    waiter.x++;
		   }
           else {
		    if(waiter.x < s1)
				{
				waiter.x++;
				}
			else if (waiter.x > s1)
				{
				waiter.x--;
				}	
            if (waiter.y < s2)
				{ waiter.y++;}
            else if (waiter.y > s2)
				{waiter.y--;}
		   }
		   	
            
			if(food_w<=0){
				
				model.stop_open();
				return;
			
			}else{
				setAgPos(0, waiter);
            setAgPos(1, kitchen_x,kitchen_y); 
			//setAgPos(2, 7,7); 
			add(2, 5 , 5);
			add(2, 6 , 5);
			add(2, 7 , 5);
			add(2, 8 , 5);
			add(2, 9 , 5);
			add(2, 10 , 5);
			add(2, 11 , 5);}
        }
		
		void stop_open(){
            System.out.println("The chef does not have food material!");
		}
		void parrelw() throws Exception {
            System.out.println("The chef does not have food material!");
		}
		
		void settable() throws Exception {
			int max=16;
			int min=0;
			Random random = new Random();
			s1 = random.nextInt(max)%(max-min) + min;
			s2 = random.nextInt(max)%(max-min) + min;
			s3 = random.nextInt(max)%(max-min) + min;
			s4 = random.nextInt(max)%(max-min) + min;
			if((s1==8)&&(s2==8)){
				s1=3;s2=3;
			}
			if(s2==5||s2==6||s2==4){
			s2=1;
			}
			if((s3==8)&&(s4==8)){
				s3=3;s4=3;
			}
			if(s4==5){
			s4=1;
			}
			kitchen_x = random.nextInt(10)%(10-6)+6;
			//s1 = random.nextInt(max)%(max-min) + min;
			add(Food_n, s1 , s2);
			//add(Food_n, s3 , s4);

		}

        void return_kk(int x, int y) throws Exception {
            Location waiter = getAgPos(0);
if ((waiter.x<=8)&&(waiter.x>=4)&&(waiter.y==4))
		   {
			   waiter.x--;
			   //System.out.println(waiter.x);
		   }
		   else if((waiter.x<=12)&&(waiter.x>=9)&&(waiter.y==4)){
			    waiter.x++;
		   }
           else {
		    if(waiter.x < x)
				{
				waiter.x++;
				}
			else if (waiter.x > x)
				{
				waiter.x--;
				}	
            if (waiter.y < y)
				{ waiter.y++;}
            else if (waiter.y > y)
				{waiter.y--;}
		   }
            setAgPos(0, waiter);
            setAgPos(1, kitchen_x,kitchen_y); // just to draw it in the view
        }

        void meal_delivery() {
                   remove(Food_n, getAgPos(0));
				   waiter_with_food = true;
				   
        }
        void givePosition() {
            if (waiter_with_food) {
                waiter_with_food = false;
				//model.settable();
                add(Food_n, getAgPos(0));
            }
        }
        void givefoodF() {
            // waiter has food
            if (model.hasObject(Food_n, getAgPos(1))) {
                remove(Food_n, getAgPos(1));
            }
			food_w--;
			if (food_w<0){
			model.stop_open();
			}
        }
    }

    class RestaurantView extends GridWorldView {

        public RestaurantView(AK_Model model) {
            super(model, "Restaurant", 800);
            defaultFont = new Font("Microsoft Yahei", Font.BOLD, 16); 
			//defaultFont_number = new Font("Calibri", Font.PLAIN, 14); 
            setVisible(true);
            repaint();
        }

        public void draw(Graphics g, int x, int y, int object) {
            switch (object) {
            case ResEnv.Food_n:
                drawTable_name(g, x, y);
                break;
			case ResEnv.wall_n:
			 	draw_wall(g, x, y);
                break;
            }
			drawKitchenArea(g,x,y);
        }
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
            String label1 = "W";
            c = Color.black;
			if(t_n==11){
				c = Color.black;
			}
            if (id == 0) {
                c = Color.orange;
				
				if (t_n > 10){
                        c = Color.gray;
                        label1 = "K-NF"; 
                    }
					else{
					label1 = "A-F";
					}
                if (((AK_Model)model).waiter_with_food) {
                    c = Color.white;
					label1 = "A";
                }
            }
			
		    if (id == 1) {
                c = Color.green;
				label1 = "K";
                if (((AK_Model)model).waiter_with_food) {
                    //c = Color.green;
                    if (t_n == 10){
                        c = Color.gray;
                        label1 = "K-NF"; 
                    }
                    else{
                        c = Color.green;
                        label1 = "K-F"; 
                    }
                    
                }
            }
			
            super.drawAgent(g, x, y, c, -2);
            if (id == 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }
            super.drawString(g, x, y, defaultFont, label1);
        }
        public void drawTable_name(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
			String label_t = "T";
			t_i++;
			if(t_i%3==0){
			  t_n++;
            }

			//defaultFont1 = new Font("Calibri", Font.PLAIN, 12); 
            drawString(g, x, y, defaultFont, "T"+t_n+"");
        }
		public void draw_wall(Graphics g, int x, int y){
		    super.drawObstacle(g, x, y);
			g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "W");
		}
		
		public void drawKitchenArea(Graphics g, int x, int y) {
		   g.setColor(Color.pink);
		   g.fillRect( 1*276, 8 * 44, 231, 44);
		}
	

    }
}
