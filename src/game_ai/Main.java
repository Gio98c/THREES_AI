package game_ai;

import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;



public class Main{	
		
	private static Handler handler;
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		JFrame game = new JFrame();
	    game.setTitle("2048 Game");
	    game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    game.setSize(340, 400);
	    game.setResizable(false);	
	    
	    Game_Ai gameAi = new Game_Ai();
	    
	    
	    game.add(gameAi);
	    		
	    game.setLocationRelativeTo(null);
	    game.setVisible(true);
	    
	    boolean haiPerso = false;
	    
	    Thread t;
	    t = new Thread(new Runnable() {
	    	
			@Override	
			public void run() {
			
				while(!haiPerso) {		
					try {	
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
						
					 handler = new DesktopHandler(new DLVDesktopService("lib/dlv2.x64_5 -n 0 --filter scelta/1"));
					 InputProgram facts = new ASPInputProgram();
				    	
					    
					    String id = "id";
					    	
					    int cont = 0;
					    for(int i = 0; i < 4; i++) {
					    	for(int j = 0; j < 4; j++) {
					    		try {
					    			System.out.print(
					    								gameAi.tileAt(j, i).value + " ");
									facts.addObjectInput(new InMatrice(id+String.valueOf(cont++),i,j,gameAi.tileAt(j, i).value));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					    	} 
					    	System.out.println();
					    }
					    
					    handler.addProgram(facts);
					    
					    InputProgram program = new ASPInputProgram();
					    program.addFilesPath("dlv/2048.txt");
					    
					    handler.addProgram(program);
					    
							try {
								ASPMapper.getInstance().registerClass(Scelta.class);
							} catch (ObjectNotValidException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAnnotationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	
					    Output o = handler.startSync();
					    
					    AnswerSets answers = (AnswerSets) o;
	   
					    String scelta = "";
					    for(AnswerSet a : answers.getAnswersets()) {					 
					    	try {
								for(Object obj:a.getAtoms()) {
									if(obj instanceof Scelta) {
										Scelta s = (Scelta) obj;
										scelta = s.getScelta();
										System.out.println(s);
									}
								}						
							}catch(Exception e1) {
								e1.printStackTrace();
							}
					    
					    }
					    
					    switch(scelta) {
					    case "destra":
					    	gameAi.right();
					    	break;
					    case "sinistra":
					    	gameAi.left();
					    	break;
					    case "giu":
					    	gameAi.down();
					    	break;
					    case "su":
					    	gameAi.up();
					    	break;
					    default: 
					    	gameAi.myLose = true;					    
					    	return;  
					   	
				}
					    
					    gameAi.repaint();
			}
			}

			    			
	    });	

	
	   t.start();
	   
	   
	
	}
}
