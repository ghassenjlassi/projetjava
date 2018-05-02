package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

/**
 * Une classe représentant les ligne polygonales composées de 2 ou + de points
 * @author davidroussel
 */
public class Star extends Figure
{
	/**
	 * Le compteur d'instance des cercles.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;
	private static final double	HALF_PI = 0.5 * Math.PI; //changer le nom de la var half_pi
	public final static float DEFAULT_RAYON = 20.0f; // chnager 20
	private Point2D initialPoint;
	private int nb = 5; //chnger le nom de la var nb en nbpts
	private int step2Y;
	private double innerRadius;
	private float rayon;

	/**
	 * Constructeur valué d'une ligne polyonale à partir d'un style de ligne,
	 * d'une couleur et des deux premiers point de la ligne
	 * @param stroke le style de la ligne
	 * @param edgeColor la couleur de la ligne
	 * @param fillColor la couleur de remplissage
	 * @param point1 le premier point de la ligne
	 * @param point2 le second point de la ligne
	 */
	public Star(BasicStroke stroke, Paint edgeColor, Paint fillColor,
		Point2D point,float rayon)
	{
		super(stroke, edgeColor, fillColor);
		instanceNumber = ++counter;
		initialPoint = point;
		this.rayon = rayon;
		makestar(point, rayon);
		
	}

	public Point2D getInitialPoint() {
		return initialPoint;
	}

	public void setInitialPoint(Point2D initialPoint) {
		this.initialPoint = initialPoint;
	}

	/**
	 * Ajout d'un point au polygone
	 * @param x l'abcisse du point à ajouter
	 * @param y l'ordonnée du point à ajouter
	 */
	public void addPoint(int x, int y)
	{
		java.awt.Polygon poly = (java.awt.Polygon) shape;
		poly.addPoint(x, y);
	}
	
	public void makestar(Point2D p,float rayon){
		Polygon	star = new Polygon();
		this.rayon = rayon;
		double 	piN = Math.PI / nb,angle = HALF_PI, ax, ay, bx, by;
		for ( int i = 0; i < nb; i++ ) {
		    ax = p.getX() + rayon * Math.cos( angle );
		    ay = p.getY() - rayon * Math.sin( angle );
		    star.addPoint( ( int )ax, ( int )ay );
		    angle += piN;
		    bx = p.getX() + innerRadius * Math.cos( angle );
		    by = p.getY() - innerRadius * Math.sin( angle );
		    star.addPoint( ( int )bx, ( int )by );
		    angle += piN;
		}
		shape = star;
	}

	/**
	 * Suppression du dernier point du polygone.
	 * Uniquement s'il y en a plus d'un
	 */
	public void removeLastPoint()
	{
		java.awt.Polygon poly = (java.awt.Polygon) shape;

		if (poly.npoints > 1)
		{
			// Sauvegarde des coords des points - le dernier
			int[] xs = new int[poly.npoints-1];
			int[] ys = new int[poly.npoints-1];
			for (int i = 0; i < xs.length; i++)
			{
				xs[i] = poly.xpoints[i];
				ys[i] = poly.ypoints[i];
			}

			// reset poly
			poly.reset();

			// Reajout des points sauvegardés
			for (int i = 0; i < xs.length; i++)
			{
				poly.addPoint(xs[i], ys[i]);
			}
		}
	}

	/**
	 * Déplacement du dernier point du polygone
	 * @param p la position du dernier point
	 * @see lines.AbstractLine#setLastPoint(Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p)
	{
		float r = Math.abs((float)(initialPoint.getX() - p.getX()));
		makestar(initialPoint, r);
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter()
	{
		java.awt.Polygon poly = (java.awt.Polygon) shape;

		float xm = 0.0f;
		float ym = 0.0f;

		if (poly.npoints > 0)
		{
			for (int i = 0; i < poly.npoints; i++)
			{
				xm += poly.xpoints[i];
				ym += poly.ypoints[i];
			}

			xm /= poly.npoints;
			ym /= poly.npoints;
		}

		return new Point2D.Float(xm, ym);
	}
	
 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		return FigureType.STAR;
 	}

	public void editNB(Point point) {
		System.out.println(step2Y);
		
		nb = Math.abs((int)(step2Y - point.getY()))/10;
		nb = nb>5?nb:5;
		setLastPoint(point);
	}

	public int getStep2Y() {
		return step2Y;
	}

	public void setStep2Y(int step2y) {
		step2Y = step2y;
	}

	public double getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(double innerRadius) {
		this.innerRadius = innerRadius;
	}
	public float getRayon() {
		return rayon;
	}

	public void setRayon(float rayon) {
		this.rayon = rayon;
	}

	@Override
	public Figure clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub
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

	
}
