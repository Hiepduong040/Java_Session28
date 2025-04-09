package Ex06;

import java.sql.*;

public class Ex06 {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/portal_db";
        String USER = "root";
        String PASS = "12345678";
        Connection conn = null;
        PreparedStatement deptStmt = null;
        PreparedStatement empStmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket noi thanh cong");
            conn.setAutoCommit(false);
            String insertDeptSQL = "insert into departments(name) values (?)";
            deptStmt = conn.prepareStatement(insertDeptSQL, Statement.RETURN_GENERATED_KEYS);
            deptStmt.setString(1, "Phong Nhan su");
            deptStmt.executeUpdate();
            ResultSet rs = deptStmt.getGeneratedKeys();
            int deptId = 0;
            if (rs.next()) deptId = rs.getInt(1);
            System.out.println("Da them phong ban voi ID: " + deptId);
            String insertEmpSQL = "insert into employees(name, department_id) values (?, ?)";
            empStmt = conn.prepareStatement(insertEmpSQL);
            empStmt.setString(1, "Nguyen Van C");
            empStmt.setInt(2, deptId);
            empStmt.executeUpdate();
            System.out.println("Da them nhan vien 1: Nguyen Van C");
            empStmt.setString(1, "Tran Van D");
            empStmt.setInt(2, 999);
            empStmt.executeUpdate();
            System.out.println("Da them nhan vien 2: Tran Van D");
            conn.commit();
            System.out.println("Da them phong ban va nhan vien");
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            try {
                if (conn != null) {
                    System.out.println("Loi xay ra. Rollback");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Loi khi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (deptStmt != null) deptStmt.close();
                if (empStmt != null) empStmt.close();
                if (conn != null) conn.close();
                System.out.println("Da dong ket noi");
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }
}