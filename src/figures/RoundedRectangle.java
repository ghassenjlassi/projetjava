package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

import figures.enums.FigureType;

/**
 * Classe de Rectangle pour les {@link Figure}
 *
 * @author davidroussel
 */
public class RoundedRectangle extends Figure
{
	/**
	 * Le compteur d'instance des cercles.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;

	/**
	 * Création d'un rectangle avec les points en haut à gauche et en bas à
	 * droite
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 * @param topLeft le point en haut à gauche
	 * @param bottomRight le point en bas à droite
	 */
	public RoundedRectangle(BasicStroke stroke, Paint edge, Paint fill, Point2D topLeft,
			Point2D bottomRight)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double x = topLeft.getX();
		double y = topLeft.getY();
		double w = (bottomRight.getX() - x);
		double h = (bottomRight.getY() - y);
		shape = new RoundRectangle2D.Double(x, y, w, h, 50, 50);

		// System.out.println("Rectangle created");
	}

	/**
	 * Constructeur de copie assurant une copie distincte du rectangle
	 * @param rect le rectangle à copier
	 */
	public RoundedRectangle(RoundedRectangle rect)
	{
		super(rect.stroke, rect.edge, rect.fill);
		double x = rect.getBounds2D().getX();
		double y = rect.getBounds2D().getY();
		double w = rect.getBounds2D().getWidth();
		double h = rect.getBounds2D().getHeight();
		shape = new RoundRectangle2D.Double(x, y, w, h, 50, 50);
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		return new RoundedRectangle(this);
	}

	/**
	 * Création d'un rectangle sans points (utilisé dans les classes filles
	 * pour initialiser seulement les couleur et le style de trait sans
	 * initialiser {@link #shape}.
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 */
	protected RoundedRectangle(BasicStroke stroke, Paint edge, Paint fill)
	{
		super(stroke, edge, fill);

		shape = null;
	}

	/**
	 * Déplacement du point en bas à droite du rectangle à la position
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
			RoundRectangle2D.Double rect = (RoundRectangle2D.Double) shape;
			double newWidth = p.getX() - rect.x;
			double newHeight = p.getY() - rect.y;
			rect.width = newWidth;
			rect.height = newHeight;
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
		RectangularShape rect = (RectangularShape) shape;

		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
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
		double cx = center.getX();
		double cy = center.getY();
		RectangularShape rectangle = (RectangularShape) shape;
		translation.translate(cx, cy);
		rectangle.setFrame(rectangle.getX() - cx,
		                   rectangle.getY() - cy,
		                   rectangle.getWidth(),
		                   rectangle.getHeight());
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		return FigureType.ROUNDED_RECTANGLE;
 	}
}
