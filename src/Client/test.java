package Client;

public class test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String msg = "/b dfdfdf";
        
        String comand = msg.substring(0, 3);
        
        if(comand.equalsIgnoreCase("/b ")) {
            System.out.println("성공");
        }else {
            System.out.println("실패");
        }
        
    }

}
