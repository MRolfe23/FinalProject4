/**
 * 
 */
package application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

/**
 * @author rolmicw
 *
 */
public class twinsTable {
	private final SimpleIntegerProperty playerID;
    private final SimpleIntegerProperty jerseyNumber;
    private final SimpleStringProperty player;
    private final SimpleStringProperty bt;
    private final SimpleStringProperty height;
    private final SimpleStringProperty weight;
    private final SimpleStringProperty dob;
    
    twinsTable(int playerID, int jerseyNumber, String player, String bt, String height, String weight, String dob) {
        this.playerID = new SimpleIntegerProperty(playerID); 
        this.jerseyNumber = new SimpleIntegerProperty(jerseyNumber);
        this.player = new SimpleStringProperty(player);
        this.bt = new SimpleStringProperty(bt);
        this.height = new SimpleStringProperty(height);
        this.weight = new SimpleStringProperty(weight);
        this.dob = new SimpleStringProperty(dob);
    }
    

	/**
	 * gets and sets player id from db
	 * @param id
	 */
    public void setPlayerID(int id) {
		playerID.set(id);
	}
    public int getPlayerID() {
		return (int) playerID.get();
	}

    
    /**
     * gets and sets jersey number from db
     * @param jersey
     */
    public void setJerseyNumber(int jersey) {
		jerseyNumber.set(jersey);
	}
    public int getJerseyNumber() {
		return (int) jerseyNumber.get();
	}

    
    /**
     * gets and sets name from db
     * @param name
     */
    public  void setPlayer(String name) {
		player.set(name);
	}
    public String getPlayer() {
		return player.get();
	}

    

    /**
     * gets and sets Bats and Throws from db
     * @param BT
     */
    public void setBT(String BT) {
		bt.set(BT);
	}
    public String getBT() {
		return bt.get();
	}

    
    /**
     * gets and sets height from db
     * @param HT
     */
    public void setHeight(String HT) {
		height.set(HT);
	}
    public String getHeight() {
		return height.get();
	}

    
    /**
     * gets and sets weight from db
     * @param WT
     */
    public void setWeight(String WT) {
		weight.set(WT);
	}
    public String getWeight() {
		return weight.get();
	}

    
    /**
     * gets and sets date of birth from db
     * @param DOB
     */
    public void setDOB(String DOB) {
		dob.set(DOB);
	}
    public String getDOB() {
		return dob.get();
	}

}
