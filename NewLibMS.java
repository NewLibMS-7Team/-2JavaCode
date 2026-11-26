
/**
 * New 도서관관리시스템
 *
 * @author (Team 7)
 * @version Iteration 1 (2026-11-26)
 */
public class NewLibMS {
    private String name;

    /**
     * 새로운 이용자를 등록한다
     *
     * @param  userID  이용자의 학번 
     * @param  name    이용자의 이름
     */
    public void registerNewBorrower(String userID, String name) {
        User result = UserDB.searchID(userID);
        
        if(result != null){
            System.println("이미 등록되어있는 이용자입니다.");
            return;
        }else{
            User user = new User(userID, name);
            UserDB.save(user);
            user.display();
        }
    }
 
    /**
     * 새로운 책을 등록한다
     *
     * @param  bookID  책의 등록번호
     * @param  title   책의 제목
     * @param  author  책의 저자
     */
    public void registerNewBook(String bookID, String title, String author) {
        Book result = BookDB.searchID(bookID);
        if (result != null){
            System.out.println("이미 등록된 책입니다");
            return;
        }else {
            Book book = new Book(bookID, title, author);
            BookDB.save(book);
            book.display();
        }
    }
    
    /**
     * 책을 빌리는 메서드
     *
     * @param  bookID  책의 등록번호
     * @param  userID  이용자 학번
     */
    public void borrowBook(String bookID, String userID) {
        Book book = BookDB.searchID(bookID);
        User user = UserDB.searchID(userID);
        
        boolean bookResult = book.checkBook();
        boolean userResult = user.checkUser();
        if (book != null && user != null) {
            book.display();
            user.display();
        }else{
            System.out.println("책 또는 학생증 이 등록되어있지않습니다");
        }

        if (bookResult && !userResult) {
            System.out.println("책 대출불가능. & 이용자 대출불가능.");
            return;
        } else if (bookResult && !userResult) {
            System.out.println("책 대출가능. & 이용자 대출불가능.");
            return;
        } else if (!bookResult && userResult) {
            System.out.println("책 대출불가능. & 이용자 대출가능.");
            return;
        }
        Loan loan = new Loan(user, book);
        loan.display();
        LoanDB.save(loan);
    }
    
    /**
     * 책을 반납하는 메서드
     *
     * @param  bookID  책의 등록번호
     */    
    public void returnBook(String bookID) {
        Book resultBook = BookDB.searchID(bookID);
        Loan resultLoan = LoanDB.searchID(bookID);
        if (resultLoan == null) {
            System.out.println("대출 중이 아님");
            return;
        }

        boolean result = resultLoan.checkDueDate();
        if (result == false) {
            processOverdue(resultLoan);   // 연체처리
        }
        User user = resultLoan.getUser();
        resultLoan.unlink();
        user.unlink(resultLoan);
        resultBook.display();
    }
}
 