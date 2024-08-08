package keyy1315;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class hr_question {
    hr_data_manage hrManage = new hr_data_manage();
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    public void hr_question_start() throws IOException {
        bw.write("==== 인적 자원 관리 시스템 ====");
        bw.newLine();

        bw.write("1. 근태 관리 *\n" +
                "0. 종료 *\n" +
                "선택하세요 : ");
        bw.flush();

        int selectNum = Integer.parseInt(bf.readLine());
        if(selectNum==1) hr_question_manage();
        else if(selectNum==0) hr_question_end();
        else {
            bw.write("잘못입력하셨습니다. 다시 입력하세요");
            bw.flush();
            hr_question_start();
        }
    }
    public void hr_question_end() throws IOException{
        bw.write("...");
        bw.flush();
    }

    public void hr_question_manage() throws IOException {
            bw.write("==== 근태 관리 ====");
            bw.newLine();

            bw.write("1. 근태 입력 (option)\n" +
                    "2. 근태 수정 *\n" +
                    "3. 근태 삭제 *\n" +
                    "4. 부서별 월별 근태 현황 보기 *\n" +
                    "0. 메인 메뉴로 돌아가기 *\n" +
                    "\n" +
                    "선택하세요: \n");
            bw.flush();
            hr_question_select(Integer.parseInt(bf.readLine()));
    }

    public void hr_question_select(int s) throws IOException {
        switch (s) {
            case 1:
                hr_insert_data();
                break;
            case 2:
                hr_modify_data();
                break;
            case 3:
                hr_delete_data();
                break;
            case 4:
                hr_show_data();
                break;
            case 0:
                hr_question_start();
                break;
        }
    }

    private void hr_show_data() throws IOException {
        bw.write("==== 부서별 월별 근태 현황 ====");
        bw.newLine();
        Map<Integer, String> deptMap = hrManage.findAllDept();
        for (Integer i : deptMap.keySet()) {
            bw.write("부서: "+deptMap.get(i)+"\n");
            bw.write("2024년 8월 근태 현황:");

        }

    }

    private void hr_delete_data() throws IOException{
        List<String> deleteList = new ArrayList<>();
        bw.write("==== 근태 삭제====");

        bw.write("직원 ID 입력: [EX: 12345]");
        bw.flush();
        deleteList.add(bf.readLine());

        bw.write("날짜 입력 (YYYY-MM-DD): [EX: 2024-08-01]");
        bw.flush();
        deleteList.add(bf.readLine());

        hrManage.deleteData(deleteList);
    }

    private void hr_modify_data() throws IOException{
        List<String> modifyList = new ArrayList<>();
        bw.write("==== 근태 입력 ====");

        bw.write("직원 ID 입력 : [EX: 12345]");
        bw.flush();
        modifyList.add(bf.readLine());

        bw.write("날짜 입력 (YYYY-MM-DD): [EX: 2024-08-01]");
        bw.flush();
        modifyList.add(bf.readLine());

        bw.write("근무 상태 입력 (출근/퇴근/휴가 등): [EX: 출근]");
        bw.flush();
        modifyList.add(bf.readLine());

        hrManage.modifyData(modifyList);
    }

    private void hr_insert_data() throws IOException{
        List<String> insertList = new ArrayList<>();
        bw.write("==== 근태 입력 ====");

        bw.write("직원 ID 입력 : [EX: 12345]");
        bw.flush();
        insertList.add(bf.readLine());

        bw.write("날짜 입력 (YYYY-MM-DD): [EX: 2024-08-01]");
        bw.flush();
        insertList.add(bf.readLine());

        bw.write("근무 상태 입력 (출근/퇴근/휴가 등): [EX: 출근]");
        bw.flush();
        insertList.add(bf.readLine());
        
        if(hrManage.insertData(insertList)==1) {
            bw.write("데이터 입력 완료");
            bw.flush();
        } else {
            bw.write("데이터 입력 실패 종료합니다.");
            bw.flush();
            hr_question_end();
        }
    }
}
