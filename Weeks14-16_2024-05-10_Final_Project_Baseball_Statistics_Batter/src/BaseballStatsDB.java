/*
    Created by:  xxx
    Created on:  05/10/2024
    Purpose:     Baseball Batter Stats DB method
    Description: This method contains all the access for the Baseball Batter Stats database.
*/

import java.util.*;
import java.sql.*;

public class BaseballStatsDB {

    private static String dbUrl = "jdbc:sqlite:baseball_batter_stats.sqlite";
    private static Connection connection;
//    private static int PENDING = 1;
//    private static int COMPLETED = 0;
    
    private List<Batter> batters = null;
    private List<Player> players = null;

    public void openConnection() {
        try {       
            // sqlite will create database if it does not already exist
            connection = DriverManager.getConnection(dbUrl);
            try (Statement stmt = connection.createStatement()) {
                // for new database, create the tables
                stmt.execute("CREATE TABLE IF NOT EXISTS Baseball_Player (" +
                        " Player_Number INTEGER PRIMARY KEY NOT NULL DEFAULT 0," +
                        " Player_Name TEXT NOT NULL DEFAULT ''," +
                        " Player_Active BOOLEAN NOT NULL DEFAULT 1)");
                stmt.execute("CREATE TABLE IF NOT EXISTS Baseball_Game_Player_Stat (" +
                        " Game_Number INTEGER NOT NULL DEFAULT 0," +
                        " Player_Number INTEGER NOT NULL DEFAULT 0," +
                        " Batter_Order_Number INTEGER NOT NULL DEFAULT 0," + 
                        " Batter_AB   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_Runs INTEGER NOT NULL DEFAULT 0," +
                        " Batter_1B   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_2B   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_3B   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_HR   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_BB   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_HP   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_RBI  INTEGER NOT NULL DEFAULT 0," +
                        " Batter_SO   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_GDP  INTEGER NOT NULL DEFAULT 0," +
                        " Batter_SBA  INTEGER NOT NULL DEFAULT 0," +
                        " Batter_SB   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_SF   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_SH   INTEGER NOT NULL DEFAULT 0," +
                        " Batter_LOB  INTEGER NOT NULL DEFAULT 0," +        
                        " PRIMARY KEY (Game_Number, Player_Number))" );
                //                " batter_positions TEXT NOT NULL" +
//                stmt.execute("CREATE TABLE IF NOT EXISTS Baseball_Team_Schedule (" +
//                " season_year INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                " game_number INTEGER SECONDARY KEY AUTOINCREMENT NOT NULL," +
//                " game_date INTEGER NOT NULL," + // fix date format
//                " game_time INTEGER NOT NULL," + // fix time format
//                " game_opponent_number SECONDARY INTEGER NOT NULL," + 
//                " game_location TEXT NOT NULL" +
//                " game_location_name TEXT NOT NULL"); 
//                stmt.execute("CREATE TABLE IF NOT EXISTS Baseball_Team_Opponent (" +
//                " opponent_number INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                " opponent_team_name TEXT PRIMARY KEY AUTOINCREMENT NOT NULL");

            } catch (SQLException e) {
                Console.printlnerr("openConnection create table failed: " + e); 
            }
        } catch (SQLException e) {
            Console.printlnerr("openConnection connection failed: " + e); 
        }    
    }
    
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Console.printlnerr("closeConnection failed: " + e);          
        }
    }
   
    public void addPlayer(String player_firstName, String player_lastName, int player_number) {
        openConnection(); 
        String insertPlayer =
                "INSERT INTO Baseball_Player (Player_Number, Player_Name) " +
                  "VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertPlayer)) {
            ps.setInt(1, player_number);
            ps.setString(2, player_lastName + ", " + player_firstName);
            ps.executeUpdate();
        } catch (SQLException e) {
            Console.printlnerr("addPlayer failed: " + e);
        }
        closeConnection();
    } 
    
    public List<Player> getPlayers() {
        players = new ArrayList<>();
        openConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Baseball_Player")) { 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Player p = new Player(rs.getInt(1), rs.getString(2), rs.getBoolean(3));
                players.add(p);
            }
            rs.close();
        } catch (SQLException e) {
            Console.printlnerr("getPlayers failed: " + e);
        }
        closeConnection();
        return players;
    }
    
    public void addGamePlayerStats(int game_number, int player_number, 
            int batter_order_number, int batter_ab, int batter_runs, int batter_1b, 
            int batter_2b, int batter_3b, int batter_hr, int batter_bb, int batter_rbi,
            int batter_so, int batter_gdp, int batter_sba, int batter_sb, int batter_sh,
            int batter_lob) {
        
        openConnection(); 
        String insertPlayerStat =
                "INSERT INTO Baseball_Game_Player_Stat (Game_Number, Player_Number, " +
                  "Batter_Order_Number, Batter_AB, Batter_Runs, Batter_1B, " +  
                  "Batter_2B, Batter_3B, Batter_HR, Batter_BB, Batter_RBI, " +
                  "Batter_SO, Batter_GDP, Batter_SBA, Batter_SB, Batter_SH, " +
                  "Batter_LOB) " + 
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertPlayerStat)) {
            ps.setInt(1, game_number);
            ps.setInt(2, player_number);
            ps.setInt(3, batter_order_number);
            ps.setInt(4, batter_ab);
            ps.setInt(5, batter_runs);
            ps.setInt(6, batter_1b);
            ps.setInt(7, batter_2b);
            ps.setInt(8, batter_3b);
            ps.setInt(9, batter_hr);
            ps.setInt(10, batter_bb);
            ps.setInt(11, batter_rbi);
            ps.setInt(12, batter_so);
            ps.setInt(13, batter_gdp);
            ps.setInt(14, batter_sba);
            ps.setInt(15, batter_sb);
            ps.setInt(16, batter_sh);
            ps.setInt(17, batter_lob);
            ps.executeUpdate();
        } catch (SQLException e) {
            Console.printlnerr("addGamePlayerStats failed: " + e);
        }
        closeConnection();
    } 
    
    public List<Batter> getGamePlayerStats(int game_number) {
        batters = new ArrayList<>();
        openConnection();
        try (PreparedStatement ps = connection.prepareStatement(
//                "SELECT * FROM Baseball_Game_Player_Stat WHERE Game_Number = ?")) {
                "SELECT Baseball_Player.Player_Name, " +
                       "Baseball_Game_Player_Stat.Player_Number, " +
                       "Baseball_Game_Player_Stat.Batter_Order_Number, " +
                       "Baseball_Game_Player_Stat.Batter_AB, " +
                       "Baseball_Game_Player_Stat.Batter_Runs, " +
                       "Baseball_Game_Player_Stat.Batter_1B, " +
                       "Baseball_Game_Player_Stat.Batter_2B, " +
                       "Baseball_Game_Player_Stat.Batter_3B, " +
                       "Baseball_Game_Player_Stat.Batter_HR, " +
                       "Baseball_Game_Player_Stat.Batter_BB, " +
                       "Baseball_Game_Player_Stat.Batter_HP, " +
                       "Baseball_Game_Player_Stat.Batter_RBI, " +
                       "Baseball_Game_Player_Stat.Batter_SO, " +
                       "Baseball_Game_Player_Stat.Batter_GDP, " +
                       "Baseball_Game_Player_Stat.Batter_SBA, " +
                       "Baseball_Game_Player_Stat.Batter_SB, " +
                       "Baseball_Game_Player_Stat.Batter_SF, " +
                       "Baseball_Game_Player_Stat.Batter_SH, " +
                       "Baseball_Game_Player_Stat.Batter_LOB " +
                       "FROM Baseball_Game_Player_Stat, Baseball_Player " +
                       "WHERE Baseball_Game_Player_Stat.Player_Number=Baseball_Player.Player_Number " + 
                       "AND Baseball_Game_Player_Stat.Game_Number = ? " +
                       "ORDER BY Baseball_Game_Player_Stat.Batter_Order_Number")) {
            
            ps.setString(1, String.valueOf(game_number));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Batter b = new Batter(rs.getString(1), rs.getInt(2), rs.getInt(3),
                                      rs.getInt(4), rs.getInt(5), rs.getInt(6),
                                      rs.getInt(7), rs.getInt(8), rs.getInt(9),
                                      rs.getInt(10), rs.getInt(11), rs.getInt(12),
                                      rs.getInt(13), rs.getInt(14), rs.getInt(15),
                                      rs.getInt(16), rs.getInt(17), rs.getInt(18),
                                      rs.getInt(19));
                batters.add(b);
            }
            rs.close();
        } catch (SQLException e) {
            Console.printlnerr("getGamePlayerStats failed: " + e);
        }
        closeConnection();
        return batters;
    }
    
}
