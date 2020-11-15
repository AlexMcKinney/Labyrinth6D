package alex.labyrinth.visual.images;

import alex.buffer.Bufferable;

public abstract class WallImage implements Bufferable{
	public abstract OuterWallImage toOuterWall();
}
