package keyy1315;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.LinkedHashMap;
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
    }

    public void deleteData(List<String> deleteList) {
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

    public Map<String, String> findUserByDeptId() {
        return null;
    }

    public Map<String, Integer> setWorkMap(String s) {
//        출근율, 출근, 결근, 휴가
        Map<String, Integer> map = new LinkedHashMap<>();

        map.put("출근", findWorkdayByUserId(s,"출근"));
        map.put("결근", findWorkdayByUserId(s,"결근"));
        map.put("휴가", findWorkdayByUserId(s,"휴가"));

        int per = (map.get("출근") / (map.get("출근") + map.get("결근") + map.get("휴가"))) * 100;

        map.put("출근율", per);

        return map;
    }

    private int findWorkdayByUserId(String userID, String status) {
        int workingDay = 0;
        try {
            Connection conn = connectionData.getConnection();
            String SQL = "select count(*) as cnt from Attend where User_id = ? and status = ?";

            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, userID);
            pstmt.setString(2, status);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                workingDay = rs.getInt("cnt");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return workingDay;
    }
}
