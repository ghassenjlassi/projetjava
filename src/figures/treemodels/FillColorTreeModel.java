package figures.treemodels;

import java.awt.Paint;

import javax.swing.JTree;

import figures.Drawing;
import figures.Figure;
import filters.FigureFilter;
import filters.FillColorFilter;
import utils.CColor;

public class FillColorTreeModel extends AbstractTypedFigureTreeModel<CColor> {

	public FillColorTreeModel( Drawing drawing, JTree tree)
			throws NullPointerException {
		super(CColor.class, drawing, tree,"fill color");
		// TODO Auto-generated constructor stub
	}

	@Override
	public CColor getValueFrom(Figure f) {
		// TODO Auto-generated method stub
		
		return f.getFillCColor();
	}

	@Override
	public FigureFilter<CColor> getFilter(CColor element) {
		// TODO Auto-generated method stub
		return null;
	}

}
