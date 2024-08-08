package keyy1315;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class hr_data_manage {
    connectionData connectionData = new connectionData();
    int Atd_no = 10;

    public int insertData(List<String> insertList) {
        int r = 0;

        String SQl = "insert into Attend(Atd_no, User_id, Date, status) values (?,?,?,?)";
        try{
            Connection conn = connectionData.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQl);

            pstmt.setString(1, String.valueOf(Atd_no));
            pstmt.setString(2,insertList.get(0));
            pstmt.setString(3,insertList.get(1));
            pstmt.setString(4,insertList.get(2));

            r= pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("sql err : "+e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("jdbc err : "+e.getMessage());
        }
        Atd_no++;
        return r;
    }

    public void modifyData(List<String> modifyList) {
    }

    public void deleteData(List<String> deleteList) {
    }

    public Map<Integer, String> findAllDept() {
        return null;
    }
}
