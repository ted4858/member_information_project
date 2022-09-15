package project_oracle_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class CustomerOracle {
	private Connection connection;
	private String oracle_url;
	private String user_id;
	private String user_password;
	
	//오라클에 접속하기 위한 생성자
	public CustomerOracle() {
		
		this.oracle_url = "jdbc:oracle:thin:@localhost:1521:xe";
		this.user_id = "madang";
		this.user_password = "madang";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("JDBC 드라이버가 없습니다.");
			e.printStackTrace();
		}
		
		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("DB 연결이 안되었습니다.");
			e.printStackTrace();
		}
	}
	
	//데이터베이스 전체 목록을 출력하는 메소드
	void run_sql() {
		String query = "SELECT * FROM CUSTOMER_DB";
		int data_num = 0;
		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			Statement statement = this.connection.createStatement();
			ResultSet result_set = statement.executeQuery(query);
			System.out.println("===================================================================================================================================================");
			System.out.println("번호|    아이디   |비밀번호|  성명   |성별 |   생년월일   |    전화번호    |      주소      |   핸드폰 번호   |         이메일         |     닉네임     | 결혼기념일");
			System.out.println("===================================================================================================================================================");
			while(result_set.next()) {
				data_num++;
				System.out.print(" " + data_num + " | ");
				System.out.print(result_set.getString(1) + "\t| ");
				System.out.print(result_set.getInt(2) + " | ");
				System.out.print(result_set.getString(3) + "\t| ");
				System.out.print(result_set.getString(4) + " | ");
				System.out.print(result_set.getString(5) + " | ");
				if(result_set.getString(6).equals(".")) {
					System.out.print(result_set.getString(6) + "\t\t| ");
				} else {
					System.out.print(result_set.getString(6) + "\t| ");
				}
				System.out.print(result_set.getString(7) + "\t| ");
				System.out.print(result_set.getString(8) + " | ");
				System.out.print(result_set.getString(9) + "\t| ");
				if(result_set.getString(10).length() > 5) {
					System.out.print(result_set.getString(10) + "\t|");
				} else {
					System.out.print(result_set.getString(10) + "\t\t|");
				}
				System.out.println(result_set.getString(11));
			}
			System.out.println("===================================================================================================================================================");
			result_set.close();
			statement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("쿼리문 오류발생");
			e.printStackTrace();
		}
	}
	
	//데이터베이스에서 입력한 ID나 핸드폰 번호에 해당하는 회원 정보를 출력해주는 메소드
	void search_sql(boolean checkId_Or_CellPhoneNumber, String Id_Or_CellPhoneNumber) {
		Calendar current = Calendar.getInstance();
		
		int currentMonth = current.get(Calendar.MONTH) + 1;
		int currentDay = current.get(Calendar.DAY_OF_MONTH);
		
		String query = null;
		PreparedStatement preparedStatement = null;
		
		if(checkId_Or_CellPhoneNumber) { //true면 ID
			query = "SELECT PERSON_NAME, ADDRESS, CELL_PHONE_NUMBER, E_MAIL, DATE_OF_BIRTH, WEDDING_ANNIVERSARY FROM CUSTOMER_DB WHERE UNIQUE_ID = ?";
		} else { //false면 휴대폰 번호
			query = "SELECT PERSON_NAME, ADDRESS, CELL_PHONE_NUMBER, E_MAIL, DATE_OF_BIRTH, WEDDING_ANNIVERSARY FROM FROM CUSTOMER_DB WHERE CELL_PHONE_NUMBER = ?";
		}

		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			preparedStatement = this.connection.prepareStatement(query);
			preparedStatement.setString(1, Id_Or_CellPhoneNumber);
			ResultSet result_set = preparedStatement.executeQuery();
			
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
			while(result_set.next()) {
				System.out.print("\tㅇ 성명 : ");
				System.out.println(result_set.getString(1));
				System.out.print("\tㅇ 주소 : ");
				System.out.println(result_set.getString(2));
				System.out.print("\tㅇ 핸드폰 번호 : ");
				System.out.println(result_set.getString(3));
				System.out.print("\tㅇ 이메일 : ");
				System.out.println(result_set.getString(4));
				String birthMonth = result_set.getString(5).substring(5, 7);
				String birthDay = result_set.getString(5).substring(8, 10);
				if(Integer.parseInt(birthMonth) == currentMonth && Integer.parseInt(birthDay) == currentDay) {
					System.out.println("\n\tㅇ 생일 축하드립니다~!! \\ ^_^ /");
				}
				String weddingMonth = result_set.getString(6).substring(5, 7);
				String weddingDay = result_set.getString(6).substring(8, 10);
				if(Integer.parseInt(weddingMonth) == currentMonth && Integer.parseInt(weddingDay) == currentDay) {
					System.out.println("\n\tㅇ 결혼기념일 이벤트 대상자입니다.");
				}
			}
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
			
			preparedStatement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("delete_sql Error");
			e.printStackTrace();
		}
	}
	
	//회원 정보를 데이터베이스에 저장하기 위한 메소드
	void insert_sql(String unique_id, int pass_word, String person_name, String gender, String date_of_birth, String phone_number, String address, String cell_phone_number, String e_mail, String nikname, String wedding_anniversary) {
		String query = "INSERT INTO CUSTOMER_DB(UNIQUE_ID, PW, PERSON_NAME, GENDER, DATE_OF_BIRTH, PHONE_NUMBER, ADDRESS, CELL_PHONE_NUMBER, E_MAIL, NICKNAME, WEDDING_ANNIVERSARY) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			preparedStatement = this.connection.prepareStatement(query);
			// Oracle 준비되었음.
			preparedStatement.setString(1, unique_id);
			preparedStatement.setInt(2, pass_word);
			preparedStatement.setString(3, person_name);
			preparedStatement.setString(4, gender);
			preparedStatement.setString(5, date_of_birth);
			preparedStatement.setString(6, phone_number);
			preparedStatement.setString(7, address);
			preparedStatement.setString(8, cell_phone_number);
			preparedStatement.setString(9, e_mail);
			preparedStatement.setString(10, nikname);
			preparedStatement.setString(11, wedding_anniversary);
			preparedStatement.executeLargeUpdate(); // 완료 되었음.
			preparedStatement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Primary Key는 중복이 안됩니다.");
			e.printStackTrace();
		}
	}
	
	//데이터베이스에 있는 회원 정보 중에서 입력받은 ID나 핸드폰 번호에 해당하는 데이터를 삭제하기 위한 메소드
	void delete_sql(boolean checkId_Or_CellPhoneNumber, String Id_Or_CellPhoneNumber) {
		String query = null;
		PreparedStatement preparedStatement = null;
		
		if(checkId_Or_CellPhoneNumber) { //true면 ID
			query = "DELETE FROM CUSTOMER_DB WHERE UNIQUE_ID = ?";
		} else { //false면 휴대폰 번호
			query = "DELETE FROM CUSTOMER_DB WHERE CELL_PHONE_NUMBER = ?";
		}

		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			preparedStatement = this.connection.prepareStatement(query);
			preparedStatement.setString(1, Id_Or_CellPhoneNumber);
			preparedStatement.executeLargeUpdate(); // 완료 되었음.
			preparedStatement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("delete_sql Error");
			e.printStackTrace();
		}
	}
	
	//데이터베이스에 있는 회원 정보 중에서 입력받은 ID나 핸드폰 번호에 해당하는 데이터를 수정하기 위한 메소드
	void update_sql(boolean checkId_Or_CellPhoneNumber, String Id_Or_CellPhoneNumber, String unique_id, int pass_word) {
		String query = null;
		PreparedStatement preparedStatement = null;
		
		if(checkId_Or_CellPhoneNumber) { //true면 ID
			query = "UPDATE CUSTOMER_DB SET UNIQUE_ID = ?, PW = ? WHERE UNIQUE_ID = ?";
		} else { //false면 휴대폰 번호
			query = "UPDATE CUSTOMER_DB SET UNIQUE_ID = ?, PW = ? WHERE CELL_PHONE_NUMBER = ?";
		}

		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			preparedStatement = this.connection.prepareStatement(query);
			preparedStatement.setString(1, unique_id);
			preparedStatement.setInt(2, pass_word);
			preparedStatement.setString(3, Id_Or_CellPhoneNumber);
			preparedStatement.executeLargeUpdate(); // 완료 되었음.
			preparedStatement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("update_sql Error");
			e.printStackTrace();
		}
	}
	
	//데이터베이스에 중복된 데이터가 있는지확인하기 위한 메소드
	boolean oracleCheckOverlap(int checkData, String Data) {
		String query = "SELECT * FROM CUSTOMER_DB";
		boolean overlapCheck = true;
		
		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			Statement statement = this.connection.createStatement();
			ResultSet result_set = statement.executeQuery(query);
			while(result_set.next()) {
				if(result_set.getString(checkData).equals(Data)) {
					overlapCheck = false;
					break;
				}
			}
			result_set.close();
			statement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("oracleCheckOverlap Error");
			//e.printStackTrace();
		}
		
		return overlapCheck;
	}
	
	//저장된 회원 수를 파악하는 메소드
	public int oracleDataNum() {
		int dataCount = 0;
		String query = "SELECT * FROM CUSTOMER_DB";
		try {
			this.connection = DriverManager.getConnection(this.oracle_url, this.user_id, this.user_password);
			Statement statement = this.connection.createStatement();
			ResultSet result_set = statement.executeQuery(query);
			while(result_set.next()) {
				dataCount++;
			}
			result_set.close();
			statement.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("oracleDataNum Error");
			e.printStackTrace();
		}
		return dataCount;
	}
}
