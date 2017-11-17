import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import api.XSTR;
import java.util.ArrayList;

class Chunk {
    public int x;
    public int z;
    
    Chunk( int x, int z) {
        this.x = x;
        this.z = z;
    }
    
    void print(){
        System.out.println(
            "Chunk  X = " + x +
            " Z = " + z                    
        );
    }
};

class testclass{
    public ArrayList<Chunk> list = new ArrayList();
    
    public void execute()
    {
        XSTR rnd = new XSTR(-2);
        
        int cX=0;
        int cZ=0;
    
        int found = 0;
        int iter=10;
        
        for ( int i = 0; i < iter; i ++){
            cX = rnd.nextInt(32)-16;
            cZ = rnd.nextInt(32)-16;
            int wX = cX - 4;
            int eX = cX + 5;
            int nZ = cZ - 4;
            int sZ = cZ + 5;
            System.out.println(
                "\nChunk\n cX = " + cX +
                " cZ = " + cZ);

            for( int x = wX; x < eX; x++){
                for( int z = nZ; z < sZ; z++){
                    if ( ((Math.abs(x)%3) == 1) && ((Math.abs(z)%3) == 1) ) {
                        this.list.add(new Chunk(x,z));
                        found++;
                    }
                }
            }
            
            int listsize=this.list.size();
            System.out.println(
                "Found " + listsize +
                " Neighbor Chunks"
            );
            for(int l = 0; l < listsize; l ++){
                Chunk C = this.list.get(0);
                this.list.remove(0);
                C.print();
            }
            
            
        }
        
        System.out.println(
            "found = " + found +
            " iter = " + iter
        );
    }
};

public class queuetest {
    public static void main(String args[])
    {
        testclass T = new testclass();
        T.execute();
    }
}