/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author USER
 */
public class koneksi {
    private Connection koneksi;
    private String driverName = "com.mysql.cj.jdbc.Driver";
    private String jdbc = "jdbc:mysql://";
    private String host = "localhost:";
    private String port = "3306/";
    private String database = "arsip_perpustakaan";
    private String url = jdbc+host+port+database;
    private String username = "root";
    private String password = "";

    public Connection getKoneksi(){
        if(koneksi == null){
            try{
                Class.forName(driverName);
                System.out.println("driver ditemukan");
                try{
                    koneksi = DriverManager.getConnection(url,username,password);
                    System.out.println("koneksi database berhasil");

                }catch(SQLException se){
                System.out.println("koneksi gagal");
                System.exit(0);
                }
            }catch(ClassNotFoundException se){
                System.out.println("driver tidak ditemukan");
                System.exit(0);
            }
        }
        return koneksi;
    }
}