
package corejava;

import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_HotelManagement {
    private static final String URL = "jdbc:postgresql://localhost:5432/mydb6";
    private static final String USER = "postgres";
    private static final String PASSWD = "12345";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWD);
    }

    public static void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "room_no INT UNIQUE NOT NULL, " +
                "days_stayed INT NOT NULL, " +
                "bill_amount DOUBLE PRECISION NOT NULL" +
                ")";
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table '" + tableName + "' created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertGuest(String tableName, String name, int roomNo, int daysStayed, double billAmount) {
        String sql = "INSERT INTO " + tableName + " (name, room_no, days_stayed, bill_amount) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, roomNo);
            pstmt.setInt(3, daysStayed);
            pstmt.setDouble(4, billAmount);
            pstmt.executeUpdate();
            System.out.println("Guest inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGuest(String tableName, int id, String name, int roomNo, int daysStayed, double billAmount) {
        String sql = "UPDATE " + tableName + " SET name=?, room_no=?, days_stayed=?, bill_amount=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, roomNo);
            pstmt.setInt(3, daysStayed);
            pstmt.setDouble(4, billAmount);
            pstmt.setInt(5, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Guest record updated successfully.");
            } else {
                System.out.println("Guest with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGuest(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Guest deleted successfully.");
            } else {
                System.out.println("Guest with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showGuests(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID | Name | Room No | Days Stayed | Bill Amount");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getInt("room_no") + " | " +
                        rs.getInt("days_stayed") + " | " +
                        rs.getDouble("bill_amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String tableName = "guestdetails";
        int choice;

        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("1. Create Guest Table");
            System.out.println("2. Insert Guest");
            System.out.println("3. Update Guest");
            System.out.println("4. Show All Guests");
            System.out.println("5. Delete Guest");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createTable(tableName);
                    break;
                case 2:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Room No: ");
                    int roomNo = scanner.nextInt();
                    System.out.print("Enter Days Stayed: ");
                    int daysStayed = scanner.nextInt();
                    System.out.print("Enter Bill Amount: ");
                    double billAmount = scanner.nextDouble();
                    insertGuest(tableName, name, roomNo, daysStayed, billAmount);
                    break;
                case 3:
                    System.out.print("Enter Guest ID to update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter New Room No: ");
                    int newRoomNo = scanner.nextInt();
                    System.out.print("Enter New Days Stayed: ");
                    int newDays = scanner.nextInt();
                    System.out.print("Enter New Bill Amount: ");
                    double newBill = scanner.nextDouble();
                    updateGuest(tableName, id, newName, newRoomNo, newDays, newBill);
                    break;
                case 4:
                    showGuests(tableName);
                    break;
                case 5:
                    System.out.print("Enter Guest ID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteGuest(tableName, deleteId);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
            }
        }
    }
}
