import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import api.XSTR;
import java.util.Hashtable;
import java.util.ArrayList;

// for the sake of testing\
// 0 is air
// 1 is stone
// 2 is netherrack
// 3 is end stone
// 4 is block granites
// 5 is block stones

class CONST {
	public static int AIR = 0;
	public static int STONE = 1;
	public static int NETHERRACK = 2;
	public static int ENDSTONE = 3;
	public static int GRANITE = 4;
	public static int STONY = 5;

	public static int DIM_X = 80;
	public static int DIM_Z = 80;
};

class NearbySeeds {
	public int mX;
	public int mZ;

	public NearbySeeds( int x, int z){
		this.mX = x;
		this.mZ = z;
	}
};

class GT_Worldgen_GT_Ore_Layer
{
	// Fake oregen class
	static int numOremixes;

	int mOremix;

	public GT_Worldgen_GT_Ore_Layer(int mix){
		this.mOremix = mix;
	}
		
};

class testclass
{
	public static int mMaxY = 105;
	public static int mMinY = 15;
	public static int mSize = 16;
	public static int mSecondaryMeta = 11;
	public static int mBetweenMeta = 12;
	public static int mPrimaryMeta = 13;
	public static int mSporadicMeta = 14;
	public static int mDensity = 6;
    public static String BlockName = "Stone is the most common you morons";

	public static final int WRONG_BIOME=0;
	public static final int WRONG_DIMENSION=1;
	public static final int NO_ORE_IN_BOTTOM_LAYER=2;
	public static final int NO_OVERLAP=3;
	public static final int ORE_PLACED=4;
	
	public static int Chunk[][][] = new int[CONST.DIM_X][256][CONST.DIM_Z];
	
	public static int airBlocks = 0;
	public static int deniedBlocks = 0;

	public static GT_Worldgen_GT_Ore_Layer noOresInVein = new GT_Worldgen_GT_Ore_Layer( -1 ); // disabled orevein for all dimensions, used for intentionally empty chunks.
	public static Hashtable<Long, GT_Worldgen_GT_Ore_Layer> hashedChunks = new Hashtable(1024);
	public static ArrayList<NearbySeeds> list = new ArrayList();

	public static void initChunk(int C[][][]){
		int x,y,z;
		for(y = 0; y < 128; y++){
			for (x=0;x<CONST.DIM_X;x++){
				for(z=0;z<CONST.DIM_Z;z++){
					C[x][y][z] = 1; // Bottom layer is stone
				}
			}
		}
		for(y = 128; y < 192; y++){
			for (x=0;x<CONST.DIM_X;x++){
				for(z=0;z<CONST.DIM_Z;z++){
					C[x][y][z] = 0; // Middle layer is air
				}
			}
		}
		for(y = 192; y < 256; y++){
			for (x=0;x<CONST.DIM_X;x++){
				for(z=0;z<CONST.DIM_Z;z++){
					C[x][y][z] = 1; // Top layer is stone
				}
			}
		}
	}

    public static boolean setOreBlock(int aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre) {
		return setOreBlock(aWorld, aX, aY, aZ, aMetaData, isSmallOre, false);
	}
	
    public static boolean setOreBlock(int aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre, boolean air) {
        int tBlock;		
		aX = aX % CONST.DIM_X;
		aZ = aZ % CONST.DIM_Z;

		try {
			tBlock = Chunk[aX][aY][aZ];
		} catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println(
				"Exception Detected!\n" +
				"aX = " + aX +
				" aY = " + aY +
				" aZ = " + aZ +
				"\n");
				throw(e);
		}

		if (!air) {
            aY = Math.min(128, Math.max(aY, 1));
        }

        aMetaData += isSmallOre ? 16000 : 0;
        if ((aMetaData > 0) && ((tBlock != CONST.AIR) || air)) {
			if (tBlock == CONST.STONE){
				/////RAH BUG FIX!  Eject early from if statements, move the three below to after everything else
			} else if (BlockName.equals("tile.igneousStone")) {
                System.out.println("Block changed to UB1");
            } else if (BlockName.equals("tile.metamorphicStone")) {
                System.out.println("Block changed to UB2");
            } else if (BlockName.equals("tile.sedimentaryStone")) {
                System.out.println("Block changed to UB3");
			} else if (tBlock == 2) {
                aMetaData += 1000;
            } else if (tBlock == 3) {
                aMetaData += 2000;
            } else if (tBlock == 4) {
                if (tBlock == 4) {
                    if (aWorld < 8) {
                        aMetaData += 3000;
                    } else {
                        aMetaData += 4000;
                    }
                } else {
                    aMetaData += 3000;
                }
            } else if (tBlock == 5) {
                if (tBlock == 5) {
                    if (aWorld < 8) {
                        aMetaData += 5000;
                    } else {
                        aMetaData += 6000;
                    }
                } else {
                    aMetaData += 5000;
                }
			} else if ((tBlock != 1)) {
				// Block is non-replaceable
				deniedBlocks++;
                return false;
            }
			Chunk[aX][aY][aZ] = aMetaData;
            return true;
        }
		// Block is air
		airBlocks++;
        return false;
    }

	
	public static int abs_int(int i) {
        return i >= 0.0 ? i : -i;
    }

	public static void printChunkSlice( int C[][][], int y){
		int x;
		int z;
		
		System.out.println("\ny="+y);
		
		for( z = 0; z < CONST.DIM_Z; z++){
			for( x = 0; x < CONST.DIM_X; x++){
				System.out.printf(
				    "%02d ", C[x][y][z]
				);
				if (((x+1) %16) == 0){
					System.out.print(" ");
				}
					
			}
			System.out.println();
			if (((z+1) % 16) == 0){
				System.out.println();
			}
		}
	}
	
	public static void executeWorldgenClassic(boolean details, int iterations, int seed, int aChunkX, int aChunkZ)
	{
		Random aRandom = new XSTR(seed);
		int[] placeCount=new int[4];	
		int aWorld = 0;
		int tMinY = 0;
		int wX = 0;
		int eX = 0;
		int nZ = 0;
		int sZ = 0;
		airBlocks=0;
		deniedBlocks=0;

		System.out.println("Classic world gen");
		
		long startTime = System.nanoTime();
		for(long it = 0; it < iterations; it++)
		{
			initChunk(Chunk);
			tMinY = mMinY + aRandom.nextInt(mMaxY - mMinY - 5);

			wX = aChunkX - aRandom.nextInt(mSize); //west side
			eX = aChunkX + 15 + aRandom.nextInt(mSize);   /////RAH BUG FIX!!! Use 15 instead of 16!
			
			nZ = aChunkZ - aRandom.nextInt(mSize);     /////RAH BUG FIX!!! Move Z rands outside of FOR loop.  Otherwise it recalculate N/S every time, but not E/W
			sZ = aChunkZ + 15 + aRandom.nextInt(mSize);
			
			for (int tX = wX; tX <= eX; tX++) {

				for (int tZ = nZ; tZ <= sZ; tZ++) {
					if (mSecondaryMeta > 0) {
						for (int i = tMinY - 1; i < tMinY + 2; i++) {
							if ((aRandom.nextInt(Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)) / (mDensity))) == 0) || (aRandom.nextInt(Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)) / (mDensity))) == 0)) {
								if (setOreBlock(aWorld, tX, i, tZ, mSecondaryMeta, false, false))
									placeCount[1]++;
							}
						}
					} 
					if ((mBetweenMeta > 0) && ((aRandom.nextInt(Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)) / (mDensity))) == 0) || (aRandom.nextInt(Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)) / (mDensity))) == 0))) {
						if (setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, mBetweenMeta, false, false))
							placeCount[2]++;
					} 
					if (mPrimaryMeta > 0) {
						for (int i = tMinY + 3; i < tMinY + 6; i++) {
							if ((aRandom.nextInt(Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)) / (mDensity))) == 0) || (aRandom.nextInt(Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)) / (mDensity))) == 0)) {
								if (setOreBlock(aWorld, tX, i, tZ, mPrimaryMeta, false, false))
									placeCount[0]++;
							}
						}
					} 
					if ((mSporadicMeta > 0) && ((aRandom.nextInt(Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)) / (mDensity))) == 0) || (aRandom.nextInt(Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)) / (mDensity))) == 0))) {
						if (setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, mSporadicMeta, false, false))
							placeCount[3]++;
					}
				}
			}
		}


		long endTime = System.nanoTime();
		long duration = (endTime - startTime);

		if (details){
			for( int depth = (tMinY+6); depth > (tMinY-3); depth--){	
				printChunkSlice(Chunk, depth);
			}
		}
		System.out.println(
			"Oregen took " +
			duration/iterations +
			" nanoseconds to do " + iterations +
			" iterations"			);
        System.out.println(
		    " tMinY=" + tMinY+
            " Density=" + mDensity +
            " Secondary="+placeCount[1]/iterations+
            " Between="+placeCount[2]/iterations+
            " Primary="+placeCount[0]/iterations+
            " Sporadic="+placeCount[3]/iterations+
			" Air blocks="+airBlocks/iterations+
			" Denied blocks="+deniedBlocks/iterations
        );

	}

	public static void executeWorldgenNew(boolean details, int iterations, int seed, int aChunkX, int aChunkZ)
	{
		Random aRandom = new XSTR(seed);
		int[] globalPlaceCount=new int[4];	
		int aWorld = 0;
		int tMinY = 0;
		int wX = 0;
		int eX = 0;
		int nZ = 0;
		int sZ = 0;
		airBlocks=0;
		deniedBlocks=0;
		
		System.out.println("New world gen");
		
		long startTime = System.nanoTime();
		for(long it = 0; it < iterations; it++)
		{
			initChunk(Chunk);
			int[] placeCount=new int[4];
			tMinY = mMinY + aRandom.nextInt(mMaxY - mMinY - 5);

			wX = aChunkX - aRandom.nextInt(mSize); //west side
			eX = aChunkX + 15 + aRandom.nextInt(mSize);   /////RAH BUG FIX!!! Use 15 instead of 16!
			
			nZ = aChunkZ - aRandom.nextInt(mSize);     /////RAH BUG FIX!!! Move Z rands outside of FOR loop.  Otherwise it recalculate N/S every time, but not E/W
			sZ = aChunkZ + 15 + aRandom.nextInt(mSize);

			// To allow for early exit due to no ore placed in the bottom layer (probably because we are in the sky), unroll 1 pass through the loop
			int level = tMinY - 1; //Dunno why, but the first layer is actually played one below tMinY.  Go figure.
				for (int tX = wX; tX <= eX; tX++) {
					int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
					for (int tZ = nZ; tZ <= sZ; tZ++) {
						int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
						if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSecondaryMeta > 0) )
						{
							if (setOreBlock(aWorld, tX, level, tZ, mSecondaryMeta, false, false)) {
								placeCount[1]++;
							}
						}
						else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
								placeCount[3]++;
						}
					}
				}
			if ((placeCount[1]+placeCount[3])==0){
				// In the real code we have to do a 
				// return;
				// In this simulation, just do a continue to re-start the loop.
				continue;
			}
			for (level = tMinY; level < (tMinY-1+3); level++) {  // Now we do level-first oregen
				for (int tX = wX; tX <= eX; tX++) {
					int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
					for (int tZ = nZ; tZ <= sZ; tZ++) {
						int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
						if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSecondaryMeta > 0) )
						{
							if (setOreBlock(aWorld, tX, level, tZ, mSecondaryMeta, false, false)) {
								placeCount[1]++;
							}
						}
						else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
								placeCount[3]++;
						}
					}
				}
			}
			// Low Middle layer is between + sporadic
			// level should be = tMinY-1+3 from end of for loop
				for (int tX = wX; tX <= eX; tX++) {
					int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
					for (int tZ = nZ; tZ <= sZ; tZ++) {
						int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
						if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mBetweenMeta > 0) ) {  // Between are only 1 per vertical column, reduce by 1/2 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mBetweenMeta, false, false)) {
								placeCount[2]++;
							}
						}
						else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
								placeCount[3]++;
						}
					}
				}
			// High Middle layer is between + primary + sporadic
			level++; // Increment level to next layer
				for (int tX = wX; tX <= eX; tX++) {
					int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
					for (int tZ = nZ; tZ <= sZ; tZ++) {
						int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
						if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mBetweenMeta > 0) ) {  // Between are only 1 per vertical column, reduce by 1/2 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mBetweenMeta, false, false)) {
								placeCount[2]++;
							}
						}
						else if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mPrimaryMeta > 0) ) {
							if (setOreBlock(aWorld, tX, level, tZ, mPrimaryMeta, false, false)) {
								placeCount[0]++;
							}
						}
						else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
								placeCount[3]++;
						}
					}
				}
			// Top two layers are primary + sporadic
			level++; // Increment level to next layer
			for( ; level < (tMinY + 6); level++){ // should do two layers
				for (int tX = wX; tX <= eX; tX++) {
					int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
					for (int tZ = nZ; tZ <= sZ; tZ++) {
						int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
						if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mPrimaryMeta > 0) ) {
							if (setOreBlock(aWorld, tX, level, tZ, mPrimaryMeta, false, false)) {
								placeCount[0]++;
							}
						}
						else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
							if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
								placeCount[3]++;
						}
					}
				}
			}	
			globalPlaceCount[0]+=placeCount[0]; // ONLY used for performance monitoring, do not carry over			
			globalPlaceCount[1]+=placeCount[1]; // ONLY used for performance monitoring, do not carry over			
			globalPlaceCount[2]+=placeCount[2]; // ONLY used for performance monitoring, do not carry over			
			globalPlaceCount[3]+=placeCount[3]; // ONLY used for performance monitoring, do not carry over			
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);

		if (details){
			for( int depth = (tMinY+6); depth > (tMinY-3); depth--){	
				printChunkSlice(Chunk, depth);
			}
		}
		System.out.println(
			"Oregen took " +
			duration/iterations +
			" nanoseconds to do " + iterations +
			" iterations"			);
        System.out.println(
		    " tMinY=" + tMinY+
            " Density=" + mDensity +
            " Secondary="+globalPlaceCount[1]/iterations+
            " Between="+globalPlaceCount[2]/iterations+
            " Primary="+globalPlaceCount[0]/iterations+
            " Sporadic="+globalPlaceCount[3]/iterations+
			" Air blocks="+airBlocks/iterations+
			" Denied blocks="+deniedBlocks/iterations
        );
	}

public static int executeWorldgenNewChunkified(boolean details, int seed, int aChunkX, int aChunkZ, int aSeedX, int aSeedZ)
	{
		Random aRandom = new XSTR(seed ^ (((long)aSeedX << 32) | ((long)aSeedZ & 0x00000000ffffffffL) ) + mSecondaryMeta + mPrimaryMeta); //RNG is specific to worldseed and oreseed X/Z and meta values - otherwise two attempts at this chunk would try to put a vein at the same height above the bottom
		int[] globalPlaceCount=new int[4];	
		int aWorld = 0;
		int tMinY = 0;
		int wX = 0;
		int eX = 0;
		int nZ = 0;
		int sZ = 0;
		int level = 0;
		airBlocks=0;
		deniedBlocks=0;
		
		int[] placeCount=new int[4];
		tMinY = mMinY + aRandom.nextInt(mMaxY - mMinY - 5);

		wX = (aSeedX*16) - aRandom.nextInt(mSize); //west side
		eX = (aSeedX*16) + 15 + aRandom.nextInt(mSize);   /////RAH BUG FIX!!! Use 15 instead of 16!
		
		int wXChunk = Math.max(wX, aChunkX);
		int eXChunk = Math.min(eX, aChunkX+15);
		System.out.println(
			" aSeedX=" + aSeedX +
			" aSeedZ=" + aSeedZ +
			" wX=" + wX +
			" eX=" + eX +
			" aChunkX=" + aChunkX +
			" aChunkX+15=" + (aChunkX+15) +
			" wXChunk=" + wXChunk +
			" eXChunk=" + eXChunk
		);		
		if (wXChunk >= eXChunk) {  //No overlap between orevein and this chunk exists in X
			// In the real code we have to do a 
			// return false;
			if (details) {
				System.out.println(
					"No overlap in X dim!"
				);
			}
			return NO_OVERLAP;
		}
		nZ = (aSeedZ*16) - aRandom.nextInt(mSize);     /////RAH BUG FIX!!! Move Z rands outside of FOR loop.  Otherwise it recalculate N/S every time, but not E/W
		sZ = (aSeedZ*16) + 15 + aRandom.nextInt(mSize);
		
		int nZChunk = Math.max(nZ, aChunkZ);
		int sZChunk = Math.min(sZ, aChunkZ+15);
		System.out.println(
			" aSeedZ=" + aSeedZ +
			" nZ=" + nZ +
			" sZ=" + sZ +
			" aChunkZ=" + aChunkZ +
			" aChunkZ+15=" + (aChunkZ+15) +
			" nZChunk=" + nZChunk +
			" sZChunk=" + sZChunk
		);
		if (nZChunk >= sZChunk){
			if (details)
			{
				System.out.println(
					"No overlap in Z dim!"
				);
			}
			return NO_OVERLAP;
		}
		
		// To allow for early exit due to no ore placed in the bottom layer (probably because we are in the sky), unroll 1 pass through the loop
		level = tMinY - 1; //Dunno why, but the first layer is actually placed one below tMinY.  Go figure.
			for (int tX = wXChunk; tX <= (eXChunk); tX++) {
				int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
				for (int tZ = nZChunk; tZ <= (sZChunk); tZ++) {
					int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
					if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSecondaryMeta > 0) )
					{
						if (setOreBlock(aWorld, tX, level, tZ, mSecondaryMeta, false, false)) {
							placeCount[1]++;
						}
					}
					else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
							placeCount[3]++;
					}
				}
			}
		if ((placeCount[1]+placeCount[3])==0){
			return NO_ORE_IN_BOTTOM_LAYER;
		}
		for (level = tMinY; level < (tMinY-1+3); level++) {  // Now we do level-first oregen
			for (int tX = wXChunk; tX <= (eXChunk); tX++) {
				int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
				for (int tZ = nZChunk; tZ <= (sZChunk); tZ++) {
					int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
					if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSecondaryMeta > 0) )
					{
						if (setOreBlock(aWorld, tX, level, tZ, mSecondaryMeta, false, false)) {
							placeCount[1]++;
						}
					}
					else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
							placeCount[3]++;
					}
				}
			}
		}
		// Low Middle layer is between + sporadic
		// level should be = tMinY-1+3 from end of for loop
			for (int tX = wXChunk; tX <= (eXChunk); tX++) {
				int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
				for (int tZ = nZChunk; tZ <= (sZChunk); tZ++) {
					int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
					if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mBetweenMeta > 0) ) {  // Between are only 1 per vertical column, reduce by 1/2 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mBetweenMeta, false, false)) {
							placeCount[2]++;
						}
					}
					else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
							placeCount[3]++;
					}
				}
			}
		// High Middle layer is between + primary + sporadic
		level++; // Increment level to next layer
			for (int tX = wXChunk; tX <= (eXChunk); tX++) {
				int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
				for (int tZ = nZChunk; tZ <= (sZChunk); tZ++) {
					int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
					if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mBetweenMeta > 0) ) {  // Between are only 1 per vertical column, reduce by 1/2 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mBetweenMeta, false, false)) {
							placeCount[2]++;
						}
					}
					else if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mPrimaryMeta > 0) ) {
						if (setOreBlock(aWorld, tX, level, tZ, mPrimaryMeta, false, false)) {
							placeCount[0]++;
						}
					}
					else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
							placeCount[3]++;
					}
				}
			}
		// Top two layers are primary + sporadic
		level++; // Increment level to next layer
		for( ; level < (tMinY + 6); level++){ // should do two layers
			for (int tX = wXChunk; tX <= (eXChunk); tX++) {
				int placeX = Math.max(1, Math.max(abs_int(wX - tX), abs_int(eX - tX)));
				for (int tZ = nZChunk; tZ <= (sZChunk); tZ++) {
					int placeZ = Math.max(1, Math.max(abs_int(sZ - tZ), abs_int(nZ - tZ)));
					if ( ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mPrimaryMeta > 0) ) {
						if (setOreBlock(aWorld, tX, level, tZ, mPrimaryMeta, false, false)) {
							placeCount[0]++;
						}
					}
					else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ/mDensity) == 0) || (aRandom.nextInt(placeX/mDensity) == 0)) && (mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
						if (setOreBlock(aWorld, tX, level, tZ, mSporadicMeta, false, false))
							placeCount[3]++;
					}
				}
			}
		}	
		if (details){
			for( int depth = (tMinY+6); depth > (tMinY-3); depth--){	
				printChunkSlice(Chunk, depth);
			}
		}

        System.out.println(
		    " tMinY=" + tMinY+
            " Density=" + mDensity +
            " Secondary="+placeCount[1]+
            " Between="+placeCount[2]+
            " Primary="+placeCount[0]+
            " Sporadic="+placeCount[3]+
			" Air blocks="+airBlocks+
			" Denied blocks="+deniedBlocks
        );
		return ORE_PLACED;
	}


public static void worldgenChunkified( boolean details, int seed, int aChunkX, int aChunkZ, int oreseedX, int oreseedZ)
{
	long hash = ((long)oreseedX << 32) | ((long)oreseedZ & 0x00000000ffffffffL );

	System.out.println(
		"hash="+hash
	);
	// Check to see if chunk already exists in chunk list
	if (!hashedChunks.containsKey(hash)) {
		int placementResult;
		
		// Chunk not found in hashtable, need to find its oremix and add it to the hash table
		System.out.println(
			"Oreseed X/Z not found in hash table, generating"
		);

		//Do the walk down the oremixes list, find the oremix, try to place it.  If none placed, try again, up to max placement attempts
		//Also take account for intentionally empty chunks
		placementResult = executeWorldgenNewChunkified(details,  seed,  aChunkX,  aChunkZ,  oreseedX,  oreseedZ);
		//If the ore placed, add orelayer to the chunk hash table
		switch(placementResult) {
			case ORE_PLACED:
				System.out.println(
					"Adding oreseed to hash table"
				);
				hashedChunks.put(hash, new GT_Worldgen_GT_Ore_Layer(1));
				break;
			case NO_ORE_IN_BOTTOM_LAYER:
				// SHould do retry in this case until out of chances
				System.out.println(
					"Adding empty oreseed to hash table"
				);
				hashedChunks.put(hash, noOresInVein );
				break;
			case NO_OVERLAP:
				// Orevein didn't reach this chunk, can't add it yet to the hash
				break;
		}
		
	} else {
        System.out.println(
		    "oreseed X/Z found in hashtable"
        );		
	
		GT_Worldgen_GT_Ore_Layer layer = hashedChunks.get(hash);
		executeWorldgenNewChunkified( details,  seed,  aChunkX,  aChunkZ,  oreseedX,  oreseedZ); // Do this on the oremix retried in the step above
	}
	if (details) {
        System.out.println(
		    " Hashtable="+hashedChunks.toString()
        );		
	}
}

public static void run(boolean details, int iterations, int seed, int aChunkX, int aChunkZ)  //sortof the core of the new run function in worldgencontainer
	{
		
		System.out.println("Chunkified world gen run function");
		
		long startTime = System.nanoTime();
		for(long it = 0; it < iterations; it++)
		{
			initChunk(Chunk);

			// So instead of doing stuff here, this function will determine which chunks affect this chunk, and then call a seperate chunkified generator.

			// Determine which oreseeds could potentially affect this chunk.
			int wX = aChunkX/16 - 4;
			int eX = aChunkX/16 + 5;
			int nZ = aChunkZ/16 - 4;
			int sZ = aChunkZ/16 + 5;			
			for( int x = wX; x < eX; x++){
				for( int z = nZ; z < sZ; z++){
					if ( ((Math.abs(x)%3) == 1) && ((Math.abs(z)%3) == 1) ) {
						list.add(new NearbySeeds(x,z));
					}
				}
			}
			//OK, now process each oreseed vs this chunk
			for(int l = 0; list.size()!=0; l++){
				System.out.println(
					list.size() +
					" seeds left"
				);
				NearbySeeds C = list.get(0);
				list.remove(0);
				
				worldgenChunkified(true,seed, aChunkX, aChunkZ, C.mX, C.mZ);
			}
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);

		System.out.println(
			"Oregen took " +
			duration/iterations +
			" nanoseconds to do " + iterations +
			" iterations"			);
	}
};

public class performancetest
{
	public static void main(String args[])
	{
		testclass PT = new testclass();
		
		// testclass.executeWorldgenClassic(true, 1, -10, 16, 16);
//		testclass.executeWorldgenNew(true, 1, -10, 32, 32);
		testclass.run(false, 1, 2, 0, 64);
		testclass.run(false, 1, 2, 16, 64);
	}
}
