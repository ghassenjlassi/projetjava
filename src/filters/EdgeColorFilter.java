/**
 * 
 */
package filters;

import java.awt.Paint;

import figures.Figure;

/**
 * Filtre filtrant les figures possédant une certaine couleur de trait
 * @author davidroussel
 */
public class EdgeColorFilter extends FigureFilter<Paint>
{
	public EdgeColorFilter(Paint e) {
		super(e);
	}
	
	public boolean test(Figure f) {
		if(element.equals(f.getEdgePaint())) return true;
		return false;
	}
}
