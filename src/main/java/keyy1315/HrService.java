package keyy1315;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HrService {
    HrRepository hrRepository = new HrRepository();
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    //시스템 시작
    public void start() throws IOException {
        bw.write("==== 인적 자원 관리 시스템 ====\n");

        bw.write("1. 근태 관리 \n" +
                "0. 종료 \n\n" +
                "** 선택하세요 : ");
        bw.flush();
        bw.newLine();

        int input = Integer.parseInt(bf.readLine());
        switch (input) {
            case 1:
                manage();
                break;
            case 0:
                end();
                break;
            default:
                bw.write("** 잘못된 입력입니다. 다시 입력하세요\n");
                bw.flush();
                start();
                break;
        }
    }

    //시스템 종료
    public void end() throws IOException{
        bw.write("...");
        bw.flush();
    }

    //근태 관리
    public void manage() throws IOException {
        bw.write("==== 근태 관리 ====\n");

        bw.write("1. 근태 입력\n" +
                "2. 근태 수정 \n" +
                "3. 근태 삭제 \n" +
                "4. 부서별 월별 근태 현황 보기 \n" +
                "0. 메인 메뉴로 돌아가기 \n" +
                "\n" +
                "** 선택하세요: ");
        bw.flush();
        bw.newLine();
        selectManage(Integer.parseInt(bf.readLine()));

    }

    public void selectManage(int s) throws IOException {
        switch (s) {
            case 1:
                insert();
                break;
            case 2:
                modify();
                break;
            case 3:
                delete();
                break;
            case 4:
                show();
                break;
            case 0:
                start();
                break;

            default:
                bw.write("** 잘못된 입력입니다. 다시 입력하세요.\n");
                manage();
        }
    }

    // 근태 입력
    private void insert() throws IOException{
        List<String> insertList = new ArrayList<>();
        bw.write("==== 근태 입력 ====\n" +
                "직원 ID 입력 [EX: 1~4] : ");
        bw.flush();

        insertList.add(bf.readLine());

        bw.write("날짜 입력 (YYYY-MM-DD) [EX: 2024-08-01] : ");
        bw.flush();
        insertList.add(bf.readLine());

        bw.write("근무 상태 입력 (출근/결근/휴가) [EX: 출근] : ");
        bw.flush();
        insertList.add(bf.readLine());

        if(hrRepository.insertData(insertList)==1) {
            bw.write("** 데이터 입력 완료\n\n");
            bw.flush();
            manage();

        } else {
            bw.write("** 데이터 입력 실패 종료합니다.\n\n");
            bw.flush();
            end();
        }
    }

    // 근태 수정
    private void modify() throws IOException{
        List<String> modifyList = new ArrayList<>();
        bw.write("==== 근태 수정 ====\n");

        bw.write("직원 ID 입력 [EX: 1~4] : ");
        bw.flush();
        modifyList.add(bf.readLine());

        bw.write("날짜 입력 (YYYY-MM-DD) [EX: 2024-08-01] : ");
        bw.flush();
        modifyList.add(bf.readLine());

        bw.write("근무 상태 입력 (출근/결근/휴가) [EX: 출근] : ");
        bw.flush();
        modifyList.add(bf.readLine());

        hrRepository.modifyData(modifyList);

        manage();
    }

    // 근태 삭제
    private void delete() throws IOException{
        List<String> deleteList = new ArrayList<>();
        bw.write("==== 근태 삭제 ====\n" +
                "직원 ID 입력 [EX: 1~4] : ");
        bw.flush();

        deleteList.add(bf.readLine());
        bw.write("날짜 입력 (YYYY-MM-DD) [EX: 2024-08-01] : ");
        bw.flush();
        deleteList.add(bf.readLine());

        hrRepository.deleteData(deleteList);

        manage();
    }

    // 근태 정보 출력
    private void show() throws IOException {
        bw.write("==== 부서별 월별 근태 현황 ====\n");
        bw.flush();

        hrRepository.printAttendByDept();
        manage();
    }
}