import java.util.Random;
import api.XSTR;

public class randtest{
    public static void main(String args[])
    {
        XSTR aRandom = new XSTR(-1);
        
        for( int i = 0; i < 20; i++){
          System.out.println(
            "RND = " + (2 + aRandom.nextInt(2))
            );
        }
    }
};