package project_oracle_db;

import java.util.Scanner;

public class Customer_Main {
	private static Scanner scanner = new Scanner(System.in);
    private static Management management = new Management();
    private static boolean run = true;
    private static boolean login_state = false;
    private static long startTime;
 
    //회원 가입 시스템 메인 코드
    public static void main(String[] args) {
    	
        int selectNo = 0;
        String userID = null;
    	String passWord = null;
        
        System.out.println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< [ 회원 가입 시스템입니다. ] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        
        //회원 가입 시스템 로그인 및 메뉴 선택 무한 반복문
        while(run) {
        	try {
        		if(login_state == false) {
        			System.out.println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ( 관리자 아이디와 패스워드를 입력하십시오. ) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        			System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
        			System.out.println("\t\t\t\t\t\t\t\t< 로그인(ID, PW 입력) >");
        			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------\n");
        			System.out.print("\t\t\t\t\t\t\tㅇ ID : ");
        			userID = scanner.next();
        			System.out.print("\t\t\t\t\t\t\tㅇ PW : ");
        			passWord = scanner.next();
        			System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
        			login_state = management.checkLogin(userID, passWord);
        			// 로그인 후에 프로그램 시작 시간(MilliSecond) 저장
        			startTime = System.currentTimeMillis();
        		} else if (login_state == true) {
        			try {
        				System.out.println("\n------------------------------------------------------------------ < 메뉴 선택 > --------------------------------------------------------------------");
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("\t1.회원들의 정보 검색  |  2.회원 추가  |  3.회원 삭제  |  4.회원정보 수정(ID, PW)  |  5.암호 변경  |  6.로그아웃  |  7.종료");
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.print("\n# 번호 입력 : ");
             
                        selectNo = scanner.nextInt();
                        
                        System.out.println();
             
                        if(selectNo == 1) {                        	
                        	management.dataSearch();
                        } else if(selectNo == 2) {                        	
                        	management.addMember();
                        } else if(selectNo == 3) {                       	
                        	management.deleteMember();
                        } else if(selectNo == 4) {                        	
                        	management.correctionMember();
                        } else if(selectNo == 5) {                        	
                        	management.selectChangePasswordOrUserID();
                        } else if(selectNo == 6) {
                        	logOut();
                        } else if(selectNo == 7) {
                        	exit();
                        } else {
                        	System.out.println("!! 잘못 입력하셨습니다. 다시 입력하세요! (입력값 : 1 ~ 7)\n");
                        }
					} catch (Exception e) {
						scanner = new Scanner(System.in);
						System.out.println("\n!! 잘못 입력하셨습니다. (정수만 입력 가능)");
					}
        		} else {
        			System.out.println("\nㅇ 관리자 아이디와 패스워드를 입력하십시오.\n");
        		}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 로그인 해주십시오.");
				//e.printStackTrace();
			}
        }
        
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
        management.operatingTime(startTime);
        System.out.println("ㅇ 회원 가입 시스템을 이용해주셔서 감사합니다.");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
    }
    
    //로그아웃 메소드
    private static void logOut() {
    	System.out.println(">> 6.로그아웃\n");
    	login_state = false;
		System.out.println("ㅇ 로그아웃 되었습니다!");
    }
    
    //프로그램 종료 메소드
    private static void exit() {
    	System.out.println(">> 7.종료\n");
        run = false;
    }
}
