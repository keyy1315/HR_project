package keyy1315;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class hr_question {
    hr_data_manage hrManage = new hr_data_manage();
    connectionData connectionData = new connectionData();
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    public void hr_question_start() throws IOException {
        bw.write("==== 인적 자원 관리 시스템 ====");

        bw.write("1. 근태 관리 *\n" +
                "0. 종료 *\n" +
                "선택하세요 : ");

        int selectNum = Integer.parseInt(bf.readLine());
        if(selectNum==1) hr_question_manage();
        else if(selectNum==0) hr_question_end();
        else {
            bw.write("잘못입력하셨습니다. 다시 입력하세요");
            hr_question_start();
        }
        bw.flush();
    }
    public void hr_question_end() throws IOException{
        bw.write("...");
        bw.flush();
    }

    public void hr_question_manage() throws IOException {
            bw.write("==== 근태 관리 ====");

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

    private void hr_insert_data() throws IOException{
        List<String> insertList = new ArrayList<>();
        bw.write("==== 근태 입력 ====");

        bw.write("직원 ID 입력 : [EX: 12345]");
        insertList.add(bf.readLine());

        bw.write("날짜 입력 (YYYY-MM-DD): [EX: 2024-08-01]");
        insertList.add(bf.readLine());

        bw.write("근무 상태 입력 (출근/퇴근/휴가 등): [EX: 출근]");
        insertList.add(bf.readLine());
    }
}
