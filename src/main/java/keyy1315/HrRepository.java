package keyy1315;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HrRepository {
    private final ConnectionData connectionData = new ConnectionData();

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        return connectionData.getConnection();
    }


    // 근태 입력
    public int insertData(List<String> insertList) {
        String sql = "INSERT INTO Attend(User_id, Date, status) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(insertList.get(0)));
            pstmt.setDate(2, Date.valueOf(insertList.get(1)));
            pstmt.setString(3, insertList.get(2));

            return pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("근태 입력 오류: " + e.getMessage()+"\n");
            return 0;
        }
    }

    // 근태 수정
    public void modifyData(List<String> modifyList) {
        String sql = "UPDATE Attend SET status = ? WHERE User_id = ? AND Date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, modifyList.get(2));
            pstmt.setInt(2, Integer.parseInt(modifyList.get(0)));
            pstmt.setDate(3, Date.valueOf(modifyList.get(1)));

            int result = pstmt.executeUpdate();
            System.out.println(result > 0 ? "** 수정되었습니다.\n" : "** 수정할 데이터가 없습니다.\n");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("** 데이터 수정 오류: " + e.getMessage()+"\n");
        }
    }

    // 근태 삭제
    public void deleteData(List<String> deleteList) {
        String sql = "DELETE FROM Attend WHERE User_id = ? AND Date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(deleteList.get(0)));
            pstmt.setDate(2, Date.valueOf(deleteList.get(1)));

            int result = pstmt.executeUpdate();
            System.out.println(result > 0 ? "** 삭제되었습니다.\n" : "** 삭제할 근태 정보가 없습니다.\n");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("** 데이터 삭제 오류: " + e.getMessage()+"\n");
        }
    }

    // 모든 부서명 가져오기
    private List<String> getAllDeptNames() {
        String sql = "SELECT dept_Name FROM Dept";
        List<String> deptNames = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deptNames.add(rs.getString("dept_Name"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("** 부서명 조회 오류: " + e.getMessage()+"\n");
        }

        return deptNames;
    }

    // 부서별 근태 정보 출력
    public void printAttendByDept() {
        List<String> deptNames = getAllDeptNames();

        for (String department : deptNames) {
            getAttendByDept(department);
        }
    }

    // 특정 부서의 근태 정보 출력
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

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("부서: " + department);
                System.out.println("2024년 8월 근태 현황:");

                while (rs.next()) {
                    int userId = rs.getInt("UserID");
                    String employee = rs.getString("Employee");
                    int daysPresent = rs.getInt("DaysPresent");
                    int daysAbsent = rs.getInt("DaysAbsent");
                    int daysOnLeave = rs.getInt("DaysOnLeave");

                    double attendRate = (double) daysPresent / 24 * 100;

                    System.out.printf("  - 직원 ID: %d\n", userId);
                    System.out.printf("  - 이름: %s\n", employee);
                    System.out.printf("  - 출근율: %.2f%%\n", attendRate);
                    System.out.printf("  - 출근: %d일, 결근: %d일, 휴가: %d일\n\n",
                            daysPresent, daysAbsent, daysOnLeave);
                }

                System.out.println("------------------------");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("** 부서별 근태 조회 오류: " + e.getMessage()+"\n");
        }
    }
}
