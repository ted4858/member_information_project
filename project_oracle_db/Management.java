package project_oracle_db;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Management {
	
	private CustomerOracle customerOracle = new CustomerOracle();
	private CustomerData customerData = new CustomerData();
	private static Scanner scanner = new Scanner(System.in);
	private static boolean run = true;
	private String userID;
	private String userPassWord;
	private String changePassword1;
	private String changePassword2;
	private int selectNo;
	private int checkError;
	private String selectID;
	private String selectCellPhoneNumber;
	private boolean id_Or_CellPhoneNumber;
	private String selectYesOrNo;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPassWord() {
		return userPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}

	//생성자
	public Management() {
		this.userID = "소대웅";
		this.userPassWord = "1234";
		this.changePassword1 = null;
		this.changePassword2 = null;
		this.selectNo = 0;
		this.checkError = 1;
		this.selectID = null;
		this.selectCellPhoneNumber = null;
		this.id_Or_CellPhoneNumber = false;
		this.selectYesOrNo = new String();
	}
	
	//회원 정보를 검색하기 위한 메소드
	public void dataSearch() {
		while(run) {
			try {
				System.out.println(">> 1.회원들의 정보 검색");
				System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("\t1.전체 데이터 출력  |  2.아이디로 검색  |  3.휴대폰 번호로 검색  |  4.메인 메뉴");
				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.print("\n# 번호 입력 : ");
	 
	            selectNo = scanner.nextInt();
	            
	            System.out.println();
	 
	            if(selectNo == 1) {
	            	System.out.println(">> 1-(1).전체 데이터 출력\n");
	            	customerOracle.run_sql();
		        } else if(selectNo == 2) {
		        	System.out.println(">> 1-(2).아이디로 찾기\n");
	            	id_Or_CellPhoneNumber = true;
	            	searchForMemberInformation(id_Or_CellPhoneNumber);
		        } else if(selectNo == 3) {
		        	System.out.println(">> 1-(3).휴대폰 번호로 찾기\n");
		        	id_Or_CellPhoneNumber = false;
		        	searchForMemberInformation(id_Or_CellPhoneNumber);
		        } else if(selectNo == 4) {
		        	System.out.println(">> 1-(4).메인 메뉴");
		        	run = false;
		        } else {
		        	System.out.println("!! 잘못 입력하셨습니다. 다시 입력하세요! (입력값 : 1, 2)\n");
		        }
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못 입력하셨습니다. (정수만 입력 가능)\n");
			}
		}
		
		run = true;
	}
	
	//회원 정보를 아이디나 휴대폰 번호로 검색한 후 해당 데이터를 출력하는 메소드
	public void searchForMemberInformation(boolean id_Or_CellPhoneNumber) {
		int checkOverlap = 0;
		
		while(run) {
			try {
				switch(checkError) {
				case 1:
					if(id_Or_CellPhoneNumber == true) {
						System.out.print(">> 아이디 : ");
						this.selectID = scanner.next();
						checkOverlap = 1;
						if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.selectID)) {
							checkError = 2;
						} else {
							System.out.println("\n!! DB에 존재하지 않는 아이디입니다.");
							System.out.println("!! 다시 입력해 주십시오.\n");
							break;
						}
					} else {
						System.out.print(">> 핸드폰 번호 : ");
						this.selectCellPhoneNumber = scanner.next();
						checkOverlap = 8;
						if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.selectCellPhoneNumber)) {
							checkError = 2;
						} else {
							System.out.println("\n!! DB에 존재하지 않는 핸드폰 번호입니다.");
							System.out.println("!! 다시 입력해 주십시오.\n");
							break;
						}
					}
				case 2:
					if(id_Or_CellPhoneNumber == true) {
						System.out.println("\n\t<< 아이디 " + this.selectID + "님의 회원 정보 출력 >>\n");
						this.customerOracle.search_sql(true, this.selectID);
					} else {
						System.out.println("\n\t<< 핸드폰 번호 " + this.selectCellPhoneNumber + "님의 회원 정보 출력 >>\n");
						this.customerOracle.search_sql(true, this.selectCellPhoneNumber);
					}
					checkError = 3;
				case 3:
					System.out.println("\nㅇ 현재 총 회원 정보 수는 " + this.customerOracle.oracleDataNum() + "개 입니다.");
					System.out.print("ㅇ 회원 정보 검색을 더 하시겠습니까?(Y/N) ");
					this.selectYesOrNo = scanner.next();
					System.out.println();
					if(this.selectYesOrNo.equals("Y")) {
						checkError = 1;
						break;
					} else if(this.selectYesOrNo.equals("N")) {
						System.out.println("ㅇ 회원 정보 검색을 종료합니다.");
						System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
						run = false;
					} else {
						System.out.println("!! 잘못된 입력입니다. (입력값 : Y / N)\n");
						break;
					}
					checkError = 4;
				default:
					checkError = 1;
					run = false;
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
				e.printStackTrace();
			}
		}
	}
	
	//회원 정보를 데이터베이스에 입력하기 위한 메소드
	public void addMember() {
		System.out.println(">> 2.회원 추가\n");
		
		String unique_idPattern = "\\w{4,8}";
		String person_namePattern = "^[ㄱ-ㅎ가-힣]{1,8}$";
		String date_of_birthPattern = "\\d{4}-\\d{2}-\\d{2}";
		String phone_numberPattern = "\\d{2,3}\\)\\d{3}-\\d{4}";
		String cell_phone_numberPattern = "010-\\d{4}-\\d{4}";
		String e_mailPattern = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
		String niknamePattern = "\\w{1,8}";
		String wedding_anniversaryPattern = "\\d{4}-\\d{2}-\\d{2}";
		int checkOverlap = 0;
		
		while(run) {
			try {
				System.out.print("ㅇ 회원 정보를 추가하시겠습니까?(Y/N) ");
				this.changePassword1 = scanner.next();
				if(this.changePassword1.equals("Y")) {
					break;
				} else if(this.changePassword1.equals("N")) {
					System.out.println("\nㅇ 회원 추가를 종료합니다.");
					System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
					run = false;
				} else {
					System.out.println("\n!! 잘못된 입력입니다. (입력값 : Y / N)\n");
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
			}
		}
		
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\tㅇ " + (this.customerOracle.oracleDataNum() + 1) + "번 회원 정보 추가");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
		
		while(run) {
			try {
				switch(checkError) {
				case 1:
					System.out.print(">> 아이디 [형식(영문, 숫자), 필수] : ");
					this.customerData.unique_id = scanner.next();
					checkOverlap = 1;
					if(Pattern.matches(unique_idPattern, this.customerData.unique_id) && 
						this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.unique_id)) {
						checkError = 2;
					} else if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.unique_id)) {
						System.out.println("\n!! 이미 등록된 아이디입니다. 다시 확인해주세요.\n");
						break;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 4~8자리 영문, 숫자)\n");
						break;
					}
				case 2:
					System.out.print(">> 비밀번호 [형식(4자리 숫자), 필수] : ");	// 1000 이상
					this.customerData.passWord = scanner.nextInt();
					if(1000 < this.customerData.passWord && this.customerData.passWord < 10000) {
						checkError = 3;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 4자리 숫자)\n");
						break;
					}
				case 3:
					System.out.print(">> 성명 [형식(최대 8글자 한글), 필수] : ");
					this.customerData.person_name = scanner.next();
					if(Pattern.matches(person_namePattern, this.customerData.person_name)) {
						checkError = 4;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 최대 8글자 한글)\n");
						break;
					}
				case 4:
					System.out.print(">> 성별 [형식(남/여), 필수] : ");
					this.customerData.gender = scanner.next();
					if(this.customerData.gender.equals("남") || 
							this.customerData.gender.equals("여")) {
						checkError = 5;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (입력값 : 남 / 여)\n");
						break;
					}
				case 5:
					System.out.print(">> 생년월일 [형식(2000-01-01), 필수] : ");
					this.customerData.date_of_birth = scanner.next();
					if(Pattern.matches(date_of_birthPattern, this.customerData.date_of_birth)) {
						checkError = 6;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 2000-01-01)\n");
						break;
					}
				case 6:
					System.out.print(">> 전화번호 [형식(000)000-0000), 선택(.)] : ");
					this.customerData.phone_number = scanner.next();
					if(Pattern.matches(phone_numberPattern, this.customerData.phone_number)) {
						checkError = 7;
					} else if(this.customerData.phone_number.equals(".")) {
						checkError = 7;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : (000)000-0000, 미입력 시 . 입력)\n");
						break;
					}
				case 7:
					System.out.println(">> 주소 [형식(5글자 이상), 필수] : ");
					scanner.nextLine();
					this.customerData.address = scanner.nextLine();
					if(this.customerData.address.length() > 5) {
						checkError = 8;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 5글자 이상)\n");
						break;
					}
				case 8:
					System.out.print(">> 핸드폰 번호 [형식(010-0000-0000), 필수] : ");
					this.customerData.cell_phone_number = scanner.next();
					checkOverlap = 8;
					if(Pattern.matches(cell_phone_numberPattern, this.customerData.cell_phone_number) && 
						this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.cell_phone_number)) {
						checkError = 9;
					} else if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.cell_phone_number)) {
						System.out.println("\n!! 이미 등록된 핸드폰 번호입니다. 다시 확인해주세요.\n");
						break;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 010-0000-0000)\n");
						break;
					}
				case 9:
					System.out.println(">> 이메일 [형식(@, . 을 포함하는 숫자, 영문 30글자 이내), 필수] : ");
					this.customerData.e_mail = scanner.next();
					checkOverlap = 9;
					if(Pattern.matches(e_mailPattern, this.customerData.e_mail) && 
						this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.e_mail)) {
						checkError = 10;
					} else if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.e_mail)) {
						System.out.println("\n!! 이미 등록된 이메일입니다. 다시 확인해주세요.\n");
						break;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : @, . 을 포함하는 숫자, 영문 30글자 이내)\n");
						break;
					}
				case 10:
					System.out.print(">> 닉네임 [형식(최대 8글자 영문, 숫자), 선택(.)] : ");
					this.customerData.nikname = scanner.next();
					if(Pattern.matches(niknamePattern, this.customerData.nikname)) {
						checkError = 11;
					} else if(this.customerData.nikname.equals(".")) {
						checkError = 11;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 최대 8글자 영문, 숫자, 미입력 시 . 입력)\n");
						break;
					}
				case 11:
					System.out.print(">> 결혼기념일 [형식(2000-01-01), 선택(.)] : ");
					this.customerData.wedding_anniversary = scanner.next();
					if(Pattern.matches(wedding_anniversaryPattern, this.customerData.wedding_anniversary)) {
						checkError = 12;
					} else if(this.customerData.wedding_anniversary.equals(".")) {
						checkError = 12;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 2000-01-01, 미입력 시 . 입력)\n");
						break;
					}
				case 12:
					this.customerOracle.insert_sql(this.customerData.unique_id, this.customerData.passWord, this.customerData.person_name, this.customerData.gender, this.customerData.date_of_birth, this.customerData.phone_number, this.customerData.address, this.customerData.cell_phone_number, this.customerData.e_mail, this.customerData.nikname, this.customerData.wedding_anniversary);
					checkError = 13;
				case 13:
					System.out.println("\nㅇ 회원 정보 입력이 완료되었습니다.");
					System.out.print("ㅇ 회원 정보를 추가로 입력하시겠습니까?(Y/N) ");
					this.selectYesOrNo = scanner.next();
					if(this.selectYesOrNo.equals("Y")) {
						System.out.println();
						checkError = 1;
						break;
					} else if(this.selectYesOrNo.equals("N")) {
						System.out.println("\nㅇ 회원 정보 입력을 종료합니다.");
						System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
					} else {
						System.out.println("\n잘못된 입력입니다. (입력값 : Y / N)\n");
						break;
					}
				default:
					checkError = 1;
					run = false;
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
				e.printStackTrace();
			}
		}
		run = true;
	}
	
	//회원 정보를 삭제하는 방법을 선택하는 메소드
	public void deleteMember() {
		System.out.println(">> 3.회원 삭제\n");
		
		while(run) {
			try {
				System.out.print("ㅇ 삭제하시겠습니까?(Y/N) ");
				this.changePassword1 = scanner.next();
				if(this.changePassword1.equals("Y")) {
					break;
				} else if(this.changePassword1.equals("N")) {
					run = false;
				} else {
					System.out.println("\n!! 잘못된 입력입니다. (입력값 : Y / N)\n");
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
			}
		}
		
		while(run) {
			try {
				System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("\t1.아이디로 삭제하기  |  2.핸드폰 번호로 삭제하기  |  3.메인 메뉴");
				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.print("\n# 번호 입력 : ");
	 
	            selectNo = scanner.nextInt();
	            
	            System.out.println();
	 
	            if(selectNo == 1) {
	            	System.out.println(">> 3-(1).아이디로 삭제하기\n");
	            	id_Or_CellPhoneNumber = true;
	            	deleteMemberToID_Or_CellPhoneNumber(id_Or_CellPhoneNumber);
		        } else if(selectNo == 2) {
		        	System.out.println(">> 3-(2).핸드폰 번호로 삭제하기\n");
		        	id_Or_CellPhoneNumber = false;
		        	deleteMemberToID_Or_CellPhoneNumber(id_Or_CellPhoneNumber);
		        } else if(selectNo == 3) {
		        	System.out.println(">> 3-(3).메인 메뉴");
		        	break;
		        } else {
		        	System.out.println("!! 잘못 입력하셨습니다. 다시 입력하세요! (입력값 : 1, 2)\n");
		        }
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못 입력하셨습니다. (정수만 입력 가능)\n");
			}
		}
		
		run = true;
	}
		
	//입력받은 아이디나 핸드폰 번호에 해당하는 회원 정보를 삭제하기 위한 메소드
	public void deleteMemberToID_Or_CellPhoneNumber(boolean id_Or_CellPhoneNumber) {
		int checkOverlap = 0;
		
		while(run) {
			try {
				switch(checkError) {
				case 1:
					if(id_Or_CellPhoneNumber == true) {
						System.out.print(">> 아이디 : ");
						this.selectID = scanner.next();
						checkOverlap = 1;
						if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.selectID)) {
							checkError = 2;
						} else {
							System.out.println("\n!! DB에 존재하지 않는 아이디입니다.");
							System.out.println("!! 다시 입력해 주십시오.\n");
							break;
						}
					} else {
						System.out.print(">> 핸드폰 번호 : ");
						this.selectCellPhoneNumber = scanner.next();
						checkOverlap = 8;
						if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.selectCellPhoneNumber)) {
							checkError = 2;
						} else {
							System.out.println("\n!! DB에 존재하지 않는 핸드폰 번호입니다.");
							System.out.println("!! 다시 입력해 주십시오.\n");
							break;
						}
					}
				case 2:
					System.out.print("\nㅇ 회원 정보를 삭제하시겠습니까?(Y/N) ");
					this.selectYesOrNo = scanner.next();
					if(this.selectYesOrNo.equals("Y")) {
						checkError = 3;
					} else if(this.selectYesOrNo.equals("N")) {
						System.out.println("\nㅇ 회원 정보 삭제를 취소합니다.");
						System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
						run = false;
						break;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (입력값 : Y / N)\n");
						break;
					}
				case 3:
					if(id_Or_CellPhoneNumber == true) {
						this.customerOracle.delete_sql(true, this.selectID);
					} else {
						this.customerOracle.delete_sql(false, this.selectCellPhoneNumber);
					}
					System.out.println("\nㅇ 해당 데이터 삭제가 완료되었습니다.");
					checkError = 4;
				case 4:
					System.out.print("ㅇ 회원 정보를 더 삭제하시겠습니까?(Y/N) ");
					this.selectYesOrNo = scanner.next();
					System.out.println();
					if(this.selectYesOrNo.equals("Y")) {
						checkError = 1;
						break;
					} else if(this.selectYesOrNo.equals("N")) {
						System.out.println("ㅇ 회원 정보 삭제를 종료합니다.");
						System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
						run = false;
					} else {
						System.out.println("!! 잘못된 입력입니다. (입력값 : Y / N)\n");
						break;
					}
					checkError = 5;
				default:
					checkError = 1;
					run = false;
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
				e.printStackTrace();
			}
		}
	}
	
	//회원 정보를 수정하기 위한 메소드
	public void correctionMember() {
		System.out.println(">> 4.회원정보 수정\n");
		
		while(run) {
			try {
				System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("\t1.아이디로 수정하기  |  2.핸드폰 번호로 수정하기  |  3.메인 메뉴");
				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.print("\n# 번호 입력 : ");
	 
	            selectNo = scanner.nextInt();
	            
	            System.out.println();
	 
	            if(selectNo == 1) {
	            	System.out.println(">> 4-(1).아이디로 수정하기\n");
	            	id_Or_CellPhoneNumber = true;
	            	correctionMemberToID_Or_CellPhoneNumber(id_Or_CellPhoneNumber);
		        } else if(selectNo == 2) {
		        	System.out.println(">> 4-(2).핸드폰 번호로 수정하기\n");
		        	id_Or_CellPhoneNumber = false;
		        	correctionMemberToID_Or_CellPhoneNumber(id_Or_CellPhoneNumber);
		        } else if(selectNo == 3) {
		        	System.out.println(">> 4-(3).메인 메뉴");
		        	break;
		        } else {
		        	System.out.println("!! 잘못 입력하셨습니다. 다시 입력하세요! (입력값 : 1, 2)\n");
		        }
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못 입력하셨습니다. (정수만 입력 가능)\n");
			}
		}
		
		run = true;
	}
	
	public void correctionMemberToID_Or_CellPhoneNumber(boolean id_Or_CellPhoneNumber) {
		int checkOverlap = 0;
		String unique_idPattern = "\\w{4,8}";
		
		while(run) {
			try {
				switch(checkError) {
				case 1:
					if(id_Or_CellPhoneNumber == true) {
						System.out.print(">> 아이디 : ");
						this.selectID = scanner.next();
						checkOverlap = 1;
						if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.selectID)) {
							checkError = 2;
						} else {
							System.out.println("\n!! DB에 존재하지 않는 아이디입니다.");
							System.out.println("!! 다시 입력해 주십시오.\n");
							break;
						}
					} else {
						System.out.print(">> 핸드폰 번호 : ");
						this.selectCellPhoneNumber = scanner.next();
						checkOverlap = 8;
						if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.selectCellPhoneNumber)) {
							checkError = 2;
						} else {
							System.out.println("\n!! DB에 존재하지 않는 핸드폰 번호입니다.");
							System.out.println("!! 다시 입력해 주십시오.\n");
							break;
						}
					}
				case 2:
					System.out.print("\nㅇ 회원 정보(ID, PW)를 수정하시겠습니까?(Y/N) ");
					this.selectYesOrNo = scanner.next();
					if(this.selectYesOrNo.equals("Y")) {
						checkError = 3;
					} else if(this.selectYesOrNo.equals("N")) {
						System.out.println("\nㅇ 회원 정보 수정을 취소합니다.");
						System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
						run = false;
						break;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (입력값 : Y / N)\n");
						break;
					}
				case 3:
					System.out.print(">> 아이디 [형식(영문, 숫자), 필수] : ");
					this.customerData.unique_id = scanner.next();
					checkOverlap = 1;
					if(Pattern.matches(unique_idPattern, this.customerData.unique_id) && 
						this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.unique_id)) {
						checkError = 4;
					} else if(!this.customerOracle.oracleCheckOverlap(checkOverlap, this.customerData.unique_id)) {
						System.out.println("\n!! 이미 등록된 아이디입니다. 다시 확인해주세요.\n");
						break;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 4~8자리 영문, 숫자)\n");
						break;
					}
				case 4:
					System.out.print(">> 비밀번호 [형식(4자리 숫자), 필수] : ");	// 1000 이상
					this.customerData.passWord = scanner.nextInt();
					if(1000 < this.customerData.passWord && this.customerData.passWord < 10000) {
						checkError = 5;
					} else {
						System.out.println("\n!! 잘못된 입력입니다. (형식 : 4자리 숫자)\n");
						break;
					}
				case 5:
					if(id_Or_CellPhoneNumber == true) {
						this.customerOracle.update_sql(true, this.selectID, this.customerData.unique_id, this.customerData.passWord);
					} else {
						this.customerOracle.update_sql(false, this.selectCellPhoneNumber, this.customerData.unique_id, this.customerData.passWord);
					}
					System.out.println("\nㅇ 해당 데이터 수정이 완료되었습니다.");
					checkError = 6;
				case 6:
					System.out.print("ㅇ 회원 정보를 더 수정하시겠습니까?(Y/N) ");
					this.selectYesOrNo = scanner.next();
					System.out.println();
					if(this.selectYesOrNo.equals("Y")) {
						checkError = 1;
						break;
					} else if(this.selectYesOrNo.equals("N")) {
						System.out.println("ㅇ 회원 정보 수정을 종료합니다.");
						System.out.println("ㅇ 메인 메뉴로 돌아갑니다.");
						run = false;
					} else {
						System.out.println("!! 잘못된 입력입니다. (입력값 : Y / N)\n");
						break;
					}
					checkError = 7;
				default:
					checkError = 1;
					run = false;
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
				e.printStackTrace();
			}
		}
	}
	
	//로그인 되었는지 확인하기 위한 메소드
	public boolean checkLogin(String userName, String passWord) {
		if(this.userID.equals(userName) && this.userPassWord.equals(passWord)) {
			System.out.println("\nㅇ 로그인 되었습니다!");
			return true;
		} else if(this.userID.equals(userName)) {
			System.out.println("\n!! 패스워드가 잘못 입력되었습니다. 다시 입력하십시오.");
			return false;
		} else if(this.userPassWord.equals(passWord)) {
			System.out.println("\n!! 아이디가 잘못 입력되었습니다. 다시 입력하십시오.");
			return false;
		} else {
			System.out.println("\n!! 아이디와 패스워드가 잘못 입력되었습니다.\nㅇ 다시 입력하십시오.");
			return false;
		}
	}

	//프로그램 동작 시간을 계산하기 위한 메소드
	public void operatingTime(long startTime) {
		long endTime = System.currentTimeMillis();
        
        //근무 시간(MilliSecond)을 시, 분, 초로 저장
        int second = (int) ((endTime - startTime) / 1000) % 60;
        int minute = (int) ((endTime - startTime) / 1000 / 60) % 60;
        int hour = (int) ((endTime - startTime) / 1000 / 60 / 60);
        
        System.out.println("ㅇ 프로그램 이용 시간 : " + hour + "시간 " + minute + "분 " + second + "초");
	}

	//PassWord를 변경하기 위한 메소드
	public void changePassword() {
		while(run) {
			try {
				System.out.println("ㅇ 기존의 패스워드를 입력해 주십시오.");
				System.out.print("PassWord : ");
				this.changePassword1 = scanner.next();
				if(this.userPassWord.equals(this.changePassword1)) {
					break;
				} else {
					System.out.println("\n!! 잘못된 패스워드입니다. 다시 입력해 주십시오.\n");
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
			}
		}
		while(run) {
			try {
				System.out.println("\nㅇ 변경하실 패스워드를 입력해 주십시오.");
				System.out.print("PassWord1 : ");
				this.changePassword1 = scanner.next();
				System.out.println("\nㅇ 한번 더 입력해 주십시오.");
				System.out.print("PassWord2 : ");
				this.changePassword2 = scanner.next();
				if(this.changePassword1.equals(this.changePassword2)) {
					this.userPassWord = this.changePassword1;
					this.changePassword1 = null;
					this.changePassword2 = null;
					System.out.println("\nㅇ 암호 변경이 완료되었습니다!\n");
					break;
				} else {
					System.out.println("\n!! 입력하신 패스워드가 동일하지 않습니다.");
					System.out.println("!! 다시 입력해주십시오.\n");
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
			}
		}
	}
	
	//사용자를 변경하기 위한 메소드
	public void changeUserName() {
		while(run) {
			try {
				System.out.println("ㅇ 현재 사용자는 " + this.userID + "님 입니다.");
				System.out.print("ㅇ 사용자를 변경하시겠습니까?(Y/N) ");
				this.changePassword1 = scanner.next();
				if(this.changePassword1.equals("Y")) {
					break;
				} else if(this.changePassword1.equals("N")) {
					System.out.println();
					run = false;
				} else {
					System.out.println("\n!! 잘못된 입력입니다. (입력값 : Y / N)\n");
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
			}
		}
		while(run) {
			try {
				System.out.println("\nㅇ 변경하실 아이디를 입력해 주십시오.");
				System.out.print("ID : ");
				this.changePassword1 = scanner.next();
				System.out.println("\nㅇ 한번 더 입력해 주십시오.");
				System.out.print("ID : ");
				this.changePassword2 = scanner.next();
				if(this.changePassword1.equals(this.changePassword2)) {
					this.userID = this.changePassword1;
					this.changePassword1 = null;
					this.changePassword2 = null;
					System.out.println("\nㅇ 아이디 변경이 완료되었습니다!\n");
					run = false;
				} else {
					System.out.println("\n!! 입력하신 아이디가 동일하지 않습니다.");
					System.out.println("!! 다시 입력해주십시오.\n");
				}
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못된 입력입니다. 다시 입력해 주십시오.\n");
			}
		}
		run = true;
	}

	//5번 암호 변경 메뉴에서 사용자와 PassWord 중 변경할 것을 선택하기 위한 메소드
	public void selectChangePasswordOrUserID() {
		System.out.println(">> 5.암호 변경\n");
		while(run) {
			try {
				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("\t1.아이디 변경  |  2.암호 변경  |  3.메인 메뉴");
				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.print("\n# 번호 입력 : ");
	 
	            selectNo = scanner.nextInt();
	            
	            System.out.println();
	 
	            if(selectNo == 1) {
	            	System.out.println(">> 5-(1).아이디 변경\n");
	            	changeUserName();
		        } else if(selectNo == 2) {
		        	System.out.println(">> 5-(2).암호 변경\n");
		        	changePassword();
		        } else if(selectNo == 3) {
		        	System.out.println(">> 5-(3).메인 메뉴");
		        	break;
		        } else {
		        	System.out.println("!! 잘못 입력하셨습니다. 다시 입력하세요! (입력값 : 1, 2, 3)\n");
		        }
			} catch (Exception e) {
				scanner = new Scanner(System.in);
				System.out.println("\n!! 잘못 입력하셨습니다. (정수만 입력 가능)\n");
			}
		}
	}

}
