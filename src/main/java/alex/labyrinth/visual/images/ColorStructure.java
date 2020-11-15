package alex.labyrinth.visual.images;

import java.util.HashMap;
import java.util.Map;

import alex.buffer.render.Color;
import alex.labyrinth.blueprints.CellStructure;
import alex.labyrinth.blueprints.Side;

public class ColorStructure {
	private Map<Side, Color> colors = new HashMap<Side, Color>();
	
	
	public ColorStructure(CellStructure struct){
		//System.out.println("use "+sides.size()+" Sides");
		for(Side side : struct.getSides().values()){
			colors.put(side, Color.random());
			//System.out.println(side);
		}
	}
	
	public Color getColor(Side side){
		Color color = this.colors.get(side);
		if(color == null){
			return Color.BLACK;
		}
		return color;
	}
}
