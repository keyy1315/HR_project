package keyy1315;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
                System.out.println("수정되었습니다.");
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
        Map<Integer, String> map = new HashMap<>();
        try {
            Connection conn = connectionData.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from Dept ");

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                map.put(rs.getInt("Dept_id"),rs.getString("dept_Name"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public Map<Integer, String> findUserByDeptId(String deptID) {
        Map<Integer, String> map = new HashMap<>();
        try {
            Connection conn = connectionData.getConnection();
            String SQL = "select User_id, user_Name from User where Dept_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, deptID);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                map.put(rs.getInt("User_id"),rs.getString("user_Name"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public Map<String, Integer> setWorkMap(String s) {
//        출근율, 출근, 결근, 휴가
        Map<String, Integer> map = new HashMap<>();

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



    // 모든 부서명을 가져오는 메서드
    private List<String> getAllDeptName() {
        String sql = "SELECT dept_Name FROM Dept";
        List<String> deptNames = new ArrayList<>();

        try (Connection conn = connectionData.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deptNames.add(rs.getString("dept_Name"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return deptNames;
    }

    // 부서별로 근태 정보를 조회하고 출력하는 메서드
    public void printAttendByDept() {
        List<String> deptNames = getAllDeptName();

        for (String department : deptNames) {
            getAttendByDept(department);
        }
    }

    // 특정 부서의 근태 정보를 출력하는 메서드
    private void getAttendByDept(String department) {
        String sql = "SELECT u.user_id AS UserID, u.user_Name AS Employee, " +
                "COUNT(CASE WHEN a.status = '출근' THEN 1 END) AS DaysPresent, " +
                "COUNT(CASE WHEN a.status = '결근' THEN 1 END) AS DaysAbsent, " +
                "COUNT(CASE WHEN a.status = '휴가' THEN 1 END) AS DaysOnLeave " +
                "FROM Attend a " +
                "JOIN User u ON a.User_id = u.User_id " +
                "JOIN Dept d ON u.Dept_id = d.Dept_id " +
                "WHERE d.dept_Name = ? AND MONTH(a.Date) = 8 AND YEAR(a.Date) = 2024 " +
                "GROUP BY u.user_id, u.user_Name " +
                "ORDER BY u.user_id";

        try (Connection conn = connectionData.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department); // 부서명

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("부서: " + department);
                System.out.println("2024년 8월 근태 현황:");

                while (rs.next()) {
                    int userId = rs.getInt("UserID");
                    String employee = rs.getString("Employee");
                    int daysPresent = rs.getInt("DaysPresent");
                    int daysAbsent = rs.getInt("DaysAbsent");
                    int daysOnLeave = rs.getInt("DaysOnLeave");

                    // 출석률 계산
                    double attendanceRate = (double) daysPresent / 24 * 100;

                    System.out.printf("- 직원 ID: %d, 이름: %s\n", userId, employee);
                    System.out.printf("  - 출근율 : %.2f%%\n", attendanceRate);
                    System.out.printf("  - 출근: %d일, 결근: %d일, 휴가: %d일\n",
                            daysPresent, daysAbsent, daysOnLeave);
                    System.out.println();
                }

                System.out.println("------------------------");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
