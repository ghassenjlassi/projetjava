package figures.treemodels;

import javax.swing.JTree;

import figures.Drawing;
import figures.Figure;
import filters.FigureFilter;
import utils.CColor;

public class EdgeColorTreeModel extends AbstractTypedFigureTreeModel<CColor> {

	public EdgeColorTreeModel( Drawing drawing, JTree tree)
			throws NullPointerException {
		super(CColor.class, drawing,tree, "Edge COLOR");
		// TODO Auto-generated constructor stub
	}

	@Override
	public CColor getValueFrom(Figure f) {
		// TODO Auto-generated method stub
		return f.getEdgeCColor();
	}

	@Override
	public FigureFilter<CColor> getFilter(CColor element) {
		// TODO Auto-generated method stub
		return null;
	}

}
