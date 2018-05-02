package figures.treemodels;

import javax.swing.JTree;

import figures.Drawing;
import figures.Figure;
import figures.enums.LineType;
import filters.FigureFilter;
import filters.LineFilter;

public class EdgeTypeTreeModel extends AbstractTypedFigureTreeModel<LineType> {

	public EdgeTypeTreeModel( Drawing drawing, JTree tree)
			throws NullPointerException {
		super( LineType.class,drawing, tree,"edge type");
		// TODO Auto-generated constructor stub
	}

	@Override
	public LineType getValueFrom(Figure f) {
		// TODO Auto-generated method stub
		return f.getLineType();
	}

	@Override
	public FigureFilter<LineType> getFilter(LineType element) {
		// TODO Auto-generated method stub
		
		return new LineFilter(element);
	}

}
