package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

/**
 * Classe de Rectangle pour les {@link Figure}
 *
 * @author davidroussel
 */
public class Ellipse extends Figure
{
	/**
	 * Le compteur d'instance des cercles.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;

	/**
	 * Création d'un elip avec les points en haut à gauche et en bas à
	 * droite
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 * @param topLeft le point en haut à gauche
	 * @param bottomRight le point en bas à droite
	 */
	public Ellipse(BasicStroke stroke, Paint edge, Paint fill, Point2D topLeft,
			Point2D bottomRight)
	{
		super(stroke, edge, fill);
		
		instanceNumber = ++counter;
		//cordonn�es
		double x = topLeft.getX();
		
		double y = topLeft.getY();
		
		double w = (bottomRight.getX() - x);
		
		double h = (bottomRight.getY() - y);

		shape = new Ellipse2D.Double(x, y, w, h);

		

		// System.out.println("elip created");
	}

	/**
	 * Constructeur de copie assurant une copie distincte du elip
	 * @param elipse le elip à copier
	 */
	public Ellipse(Ellipse elipse)
	{
		super(elipse.stroke, elipse.edge, elipse.fill);
		
		double x = elipse.getBounds2D().getX();
		
		double y = elipse.getBounds2D().getY();
		
		double w = elipse.getBounds2D().getWidth();
		
		double h = elipse.getBounds2D().getHeight();

		shape = new Ellipse2D.Double(x, y, w, h);
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		//clonage
		return new Ellipse(this);
	}

	/**
	 * Création d'un elip sans points (utilisé dans les classes filles
	 * pour initialiser seulement les couleur et le style de trait sans
	 * initialiser {@link #shape}.
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 */
	protected Ellipse(BasicStroke stroke, Paint edge, Paint fill)
	{
		super(stroke, edge, fill);

		shape = null;
	}

	/**
	 * Déplacement du point en bas à droite du elip à la position
	 * du point p
	 *
	 * @param p la nouvelle position du dernier point
	 * @see figures.Figure#setLastPoint(Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p)
	{
		if (shape != null)
		{
			Ellipse2D.Double elipse = (Ellipse2D.Double) shape;
			
			double newWidth = p.getX() - elipse.x;
			
			double newHeight = p.getY() - elipse.y;
			
			elipse.width = newWidth;
			
			elipse.height = newHeight;
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter()
	{
		RectangularShape elipse = (RectangularShape) shape;

		Point2D center = new Point2D.Double(elipse.getCenterX(), elipse.getCenterY());
		
		Point2D tCenter = new Point2D.Double();
		
		getTransform().transform(center, tCenter);
		
		return tCenter;
	}

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize()
	{
		Point2D center = getCenter();
		//System.out.println("ssss");
		double cx = center.getX();
		
		double cy = center.getY();
		
		RectangularShape elip = (RectangularShape) shape;
		
		translation.translate(cx, cy);
		
		elip.setFrame(elip.getX() - cx,
		
				elip.getY() - cy,
		        
				elip.getWidth(),
		        
				elip.getHeight());
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		//retour
 		return FigureType.ELLIPSE;
 	}
}
