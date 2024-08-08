package keyy1315;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class hr_data_manage {
    connectionData connectionData = new connectionData();

    public int insertData(List<String> insertList) {
        int r = 0;
        String SQl = "insert into Attend(User_id, Date, status) values (?,?,?)";
        try {
            Connection conn = connectionData.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQl);

            pstmt.setString(1, insertList.get(0));
            pstmt.setString(2, insertList.get(1));
            pstmt.setString(3, insertList.get(2));

            r = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("sql err : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("jdbc err : " + e.getMessage());
        }
        return r;
    }

    public void modifyData(List<String> modifyList) {

        String sql = "UPDATE Attend SET status = ? WHERE User_id = ? AND Date = ?";

        try (Connection conn = connectionData.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, modifyList.get(2));
            pstmt.setString(2, modifyList.get(0));
            pstmt.setString(3, modifyList.get(1));


            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("데이터 수정 완료.");
            } else {
                System.out.println("수정할 데이터가 없습니다.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("데이터 수정 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteData(List<String> deleteList) {

        String sql = "DELETE FROM Attend WHERE User_id = ? AND Date = ?";

        try (Connection conn = connectionData.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, deleteList.get(0));
            pstmt.setString(2, deleteList.get(1));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("근태 정보가 삭제되었습니다.");
            } else {
                System.out.println("삭제할 근태 정보가 없습니다.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("데이터 삭제 오류: " + e.getMessage());
        }
    }

    public Map<Integer, String> findAllDept() {
        try {
            Connection conn = connectionData.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select User_id from Attend");

            while (rs.next()) {
                System.out.println(rs.getString("User_id"));
            }


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}