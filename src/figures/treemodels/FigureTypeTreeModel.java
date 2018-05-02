package figures.treemodels;

import javax.swing.JTree;

import figures.Drawing;
import figures.Figure;
import figures.enums.FigureType;
import filters.FigureFilter;

public class FigureTypeTreeModel extends AbstractTypedFigureTreeModel<FigureType> {

	public FigureTypeTreeModel(Drawing drawing, JTree tree)
			throws NullPointerException {
		super(FigureType.class, drawing, tree, "FigureType");
		// TODO Auto-generated constructor stub
	}

	@Override
	public FigureType getValueFrom(Figure f) {
		// TODO Auto-generated method stub
		
		return f.getType();
	}

	@Override
	public FigureFilter<FigureType> getFilter(FigureType element) {
		// TODO Auto-generated method stub
		return new FigureFilter<FigureType>(element) {
			
			@Override
			public boolean test(Figure f) {
				// TODO Auto-generated method stub
				return element.equals(f.getType());
				
			}
		};
	}

}
