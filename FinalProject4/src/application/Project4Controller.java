package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator.Id;

import java.util.stream.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ProgressIndicator;

import application.DBClass;
import application.twinsTable;

public class Project4Controller {
	
	Connection con;
	
    @FXML
    private TableView<twinsTable> twinsRosterdb;

    @FXML
    private TextField playerNameSearchTxt;
    
    /**
     * searches and modifies table based on search field text
     * @param event keys typed
     */
    @FXML
    void filterTableResult(KeyEvent event) {
    	filteredData.clear();

        for (twinsTable p : playerInfo) {
            if (matchesFilter(p)) {
                filteredData.add(p);
            }
        }

        // Must re-sort table after items changed
        reapplyTableSortOrder();
    }
    
    private boolean matchesFilter(twinsTable player) {
        String filterString = playerNameSearchTxt.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        if (player.getPlayer().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false; // Does not match
    }
    
    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<twinsTable, ?>> sortOrder = new ArrayList<>(twinsRosterdb.getSortOrder());
        twinsRosterdb.getSortOrder().clear();
        twinsRosterdb.getSortOrder().addAll(sortOrder);
    }
    
    public void initialize(URL url, ResourceBundle rb) {
    	
    }
    
    ObservableList<twinsTable> playerInfo = FXCollections.observableArrayList();
    ObservableList<twinsTable> filteredData = FXCollections.observableArrayList();
    
    /**
     * this queries all data from db and sets in observable list
     * @return observable list
     */
    public ObservableList<twinsTable> getData(){
		
		Connection conn1;
        
        try{
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	conn1 = DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/project4", "mikerolfe", "#OnTrack1!");
        	System.out.println("Connected");
            String SQL = "Select * from twinsmn";            
            ResultSet rs = conn1.createStatement().executeQuery(SQL);
            System.out.println("Query Ran");
            while(rs.next()){              
                playerInfo.add( new twinsTable(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
                System.out.println("inserted row");
                System.out.println("The player id: " + rs.getInt(1) + " The Jersey Number: " +rs.getInt(2) + " The Player: " + rs.getString(3) + " The BT: "+ rs.getString(4) + " The Height: "+ rs.getString(5));
            }
            filteredData.addAll(playerInfo);
        }
        catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");            
        }
        return playerInfo;
    }
    
    /**
     * this scrapes the data from website
     * @throws SQLException
     */
    public void scrapTwins() throws SQLException {
    	
		// DECLARATIONS
		int x = 0;
		String num;
		String name;
		String bt;
		String ht;
		String wt;
		String dob;

		System.out.println("Scraps roster and displays info");
		
		Connection conn = null;
		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/project4", "mikerolfe", "#OnTrack1!");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connection successful");
		
		try {
			
			// Clears existing data for new scrap
			String clearTable = "TRUNCATE TABLE project4.twinsmn";
			PreparedStatement clearForScrap;
			try {
				clearForScrap = conn.prepareStatement(clearTable);
				clearForScrap.execute();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			System.out.println("Table cleared");
			
			Document doc = Jsoup.connect("http://m.twins.mlb.com/min/roster/40-man/").get();
			
			String title = doc.title(); // title 
			
			System.out.println(title);
			
			while (x <= 49) {
				
				Elements jersey = doc.getElementsByClass("dg-jersey_number");
				num = jersey.get(x).text();
				
				Elements person = doc.getElementsByClass("dg-name_display_first_last");
				name = person.get(x).text();
					
				Elements batsAndThrows = doc.getElementsByClass("dg-bats_throws");
				bt = batsAndThrows.get(x).text();
					
				Elements tall = doc.getElementsByClass("dg-height");
				ht = tall.get(x).text();
					
				Elements fat = doc.getElementsByClass("dg-weight");
				wt = fat.get(x).text();
					
				Elements birth = doc.getElementsByClass("dg-date_of_birth");
				dob = birth.get(x).text();

				
			    try {
					
				    // the mysql insert statement
				    String query = "INSERT into twinsmn (JerseyNumber, Player, BT, Height, Weight, DOB)" + " values (?, ?, ?, ?, ?, ?)";
				    
				    if (!num.equals("#") && !num.equals("")) {
				    	
				    	// create the mysql insert preparedstatement
					    PreparedStatement preparedStmt = conn.prepareStatement(query);
					    preparedStmt.setString (1, num);
					    preparedStmt.setString (2, name);
					    preparedStmt.setString (3, bt);
					    preparedStmt.setString (4, ht);
					    preparedStmt.setString (5, wt);
					    preparedStmt.setString (6, dob);
					    
					    // execute the preparedstatement
					    preparedStmt.execute();
				    }
				    
				    
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				x++;
			}
			conn.close();		
		}
		catch (IOException e) {
			
			// TODO Auto-generated catch block
			System.out.println(e);
			
		}
		System.out.println("done");
		return;
	}
    
    /**
     * This creates and sets data into columns in table
     * @param event clicks button to run
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked" })
	@FXML
    void onClickTwinsRoasterLoader(ActionEvent event) throws SQLException {
    	
    	scrapTwins();
    	getData();
		
		//player id
		TableColumn<twinsTable, SimpleIntegerProperty> playerIDColumn = new TableColumn<>("Player ID");
		playerIDColumn.setCellValueFactory(new PropertyValueFactory<>("playerID"));

		//jersey number
		TableColumn<twinsTable, SimpleIntegerProperty> jerseyNumberColumn = new TableColumn<>("Jersey #");
		jerseyNumberColumn.setCellValueFactory(new PropertyValueFactory<>("jerseyNumber"));

		//player
		TableColumn<twinsTable, SimpleStringProperty> playerColumn = new TableColumn<>("Player");
		playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));

		//bt
		TableColumn<twinsTable, SimpleStringProperty> btColumn = new TableColumn<>("BT");
		btColumn.setCellValueFactory(new PropertyValueFactory<>("BT"));
		
		//height
		TableColumn<twinsTable, SimpleStringProperty> heightColumn = new TableColumn<>("Height");
		heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));

		//weight
		TableColumn<twinsTable, SimpleStringProperty> weightColumn = new TableColumn<>("Weight");
		weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

		//dob
		TableColumn<twinsTable, SimpleStringProperty> dobColumn = new TableColumn<>("DOB");
		dobColumn.setCellValueFactory(new PropertyValueFactory<>("DOB"));
		
		twinsRosterdb.setItems(filteredData);
		twinsRosterdb.getColumns().addAll(playerIDColumn, jerseyNumberColumn, playerColumn, btColumn, heightColumn, weightColumn, dobColumn);
		
    }

}
