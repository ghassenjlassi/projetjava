package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Point;
import java.awt.geom.Point2D;

import figures.enums.FigureType;

/**
 * Une classe représentant les ligne polygonales composées de 2 ou + de points
 * @author davidroussel
 */
public class Polygon extends Figure
{
	/**
	 * Le compteur d'instance des polygones.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;

	/**
	 * Constructeur valué d'une ligne polyonale à partir d'un style de ligne,
	 * d'une couleur et des deux premiers point de la ligne
	 * @param stroke le style de la ligne
	 * @param edgeColor la couleur de la ligne
	 * @param fillColor la couleur de remplissage
	 * @param point1 le premier point de la ligne
	 * @param point2 le second point de la ligne
	 */
	public Polygon(BasicStroke stroke, Paint edgeColor, Paint fillColor,
		Point point1, Point point2)
	{
		super(stroke, edgeColor, fillColor);
		
		instanceNumber = ++counter;
		
		java.awt.Polygon poly = new java.awt.Polygon();
		
		poly.addPoint(point1.x, point1.y);
		
		poly.addPoint(point2.x, point2.y);
		
		shape = poly;
	}

	/**
	 * Constructeur de copie assurant une copie distincte du polygone
	 * @param poly le polygone à copier
	 */
	public Polygon(Polygon poly)
	{
		super(poly);
		java.awt.Polygon oldPoly = (java.awt.Polygon) poly.shape;
		int npoints = oldPoly.npoints;
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];

		for (int i = 0; i < npoints; i++)
		{
			xpoints[i] = oldPoly.xpoints[i];
			ypoints[i] = oldPoly.ypoints[i];
		}

		shape = new java.awt.Polygon(xpoints, ypoints, npoints);
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{	
		//System.out.println("cccccc");
		return new Polygon(this);
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
		
		//System.out.println("aaaaaddddd");
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
		java.awt.Polygon poly = (java.awt.Polygon) shape;
		
		int lastIndex = poly.npoints-1;
		
		if (lastIndex >= 0)
		{
		
			poly.xpoints[lastIndex] = Double.valueOf(p.getX()).intValue();
			
			poly.ypoints[lastIndex] = Double.valueOf(p.getY()).intValue();
		}
	}

	/**
	 * Calcule le barycentre des points du polygone (sans transformations)
	 * @return le barycentre des points du polygone
	 */
	protected Point2D computeCenter()
	{
		java.awt.Polygon poly = (java.awt.Polygon) shape;

		double[] center = {0.0, 0.0};

		if (poly.npoints > 0)
		{
			for (int i = 0; i < poly.npoints; i++)
			{
				center[0] += poly.xpoints[i];
			
				center[1] += poly.ypoints[i];
			}

				center[0] /= poly.npoints;
				
				center[1] /= poly.npoints;
		}

		return new Point2D.Double(center[0], center[1]);
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter()
	{
		Point2D center = computeCenter();
	
		Point2D tCenter = new Point2D.Double();

		getTransform().transform(center,tCenter);

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
		System.out.println("normalize poly");
		
		Point2D center = computeCenter();
		
		double cx = center.getX();
		
		double cy = center.getY();
		
		translation.setToTranslation(cx, cy);
		
		java.awt.Polygon poly = (java.awt.Polygon) shape;
		
		if (poly.npoints > 0)
		{
			int[] newX = new int[poly.npoints];
			
			int[] newY = new int[poly.npoints];

			for (int i = 0; i < poly.npoints; i++)
			{
				newX[i] = poly.xpoints[i] - Double.valueOf(cx).intValue();
			
				newY[i] = poly.ypoints[i] - Double.valueOf(cy).intValue();
			}

			poly.reset();

			for (int i = 0; i < newX.length; i++)
			{
				poly.addPoint(newX[i], newY[i]);
			}
		}
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	//retour du type poly
 	public FigureType getType()
 	{
 		
 		return FigureType.POLYGON;
 	}
}
